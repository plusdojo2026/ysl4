package service;

import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.SummaryDAO;
import model.MonthlySummaryDTO;
import model.WorkLogDTO;

public class SummaryService extends DBAccess {

    public MonthlySummaryDTO getMonthlySummary(String targetMonth) {

        // クラス図準拠のpublicメソッド
        // 指定月の月次集計をまとめて返す
        String safeTargetMonth = normalizeTargetMonth(targetMonth);
        MonthlySummaryDTO dto = new MonthlySummaryDTO();

        try {
            access();

            SummaryDAO dao = new SummaryDAO(conn);

            dto.setTargetMonth(safeTargetMonth);
            dto.setMonthlyTotalManhours(dao.selectMonthlyTotal(safeTargetMonth));
            dto.setMonthlyProjectCount(dao.countMonthlyProjects(safeTargetMonth));
            dto.setMonthlyMemberCount(dao.countMonthlyMembers(safeTargetMonth));
            dto.setProjectSummaryList(dao.selectProjectSummary(safeTargetMonth));
            dto.setMemberSummaryList(dao.selectMemberSummary(safeTargetMonth));
            dto.setMonthlyWorkLogList(dao.selectMonthlyWorkLogs(safeTargetMonth));

            // 予算工数は画面入力で反映するためServiceでは0にする
            dto.setMonthlyBudgetManhours(0f);
            dto.setBudgetAlertCount(0);
            dto.setMonthlyAchievementRate(0);

            // メンバー別割合だけ実績合計から計算する
            applyMemberPercentage(dto.getMemberSummaryList(), dto.getMonthlyTotalManhours());

            commit();

        } catch (SQLException e) {
            rollback();
            e.printStackTrace();

        } finally {
            close();
        }

        return dto;
    }

    public List<MonthlySummaryDTO.ProjectSummaryDTO> getProjectSummary(String targetMonth) {

        // クラス図準拠のpublicメソッド
        // 案件別集計だけが必要な場合に使う
        String safeTargetMonth = normalizeTargetMonth(targetMonth);
        List<MonthlySummaryDTO.ProjectSummaryDTO> list = new ArrayList<>();

        try {
            access();
            SummaryDAO dao = new SummaryDAO(conn);
            list = dao.selectProjectSummary(safeTargetMonth);
            commit();

        } catch (SQLException e) {
            rollback();
            e.printStackTrace();

        } finally {
            close();
        }

        return list;
    }

    public List<MonthlySummaryDTO.MemberSummaryDTO> getMemberSummary(String targetMonth) {

        // クラス図準拠のpublicメソッド
        // メンバー別集計だけが必要な場合に使う
        String safeTargetMonth = normalizeTargetMonth(targetMonth);
        List<MonthlySummaryDTO.MemberSummaryDTO> list = new ArrayList<>();

        try {
            access();
            SummaryDAO dao = new SummaryDAO(conn);
            list = dao.selectMemberSummary(safeTargetMonth);
            float total = dao.selectMonthlyTotal(safeTargetMonth);
            applyMemberPercentage(list, total);
            commit();

        } catch (SQLException e) {
            rollback();
            e.printStackTrace();

        } finally {
            close();
        }

        return list;
    }

    public List<WorkLogDTO> getMonthlyWorkLogs(String targetMonth) {

        // クラス図準拠のpublicメソッド
        // 工数明細だけが必要な場合に使う
        String safeTargetMonth = normalizeTargetMonth(targetMonth);
        List<WorkLogDTO> list = new ArrayList<>();

        try {
            access();
            SummaryDAO dao = new SummaryDAO(conn);
            list = dao.selectMonthlyWorkLogs(safeTargetMonth);
            commit();

        } catch (SQLException e) {
            rollback();
            e.printStackTrace();

        } finally {
            close();
        }

        return list;
    }

    public byte[] createCsv(String targetMonth) {

        // クラス図準拠のpublicメソッド
        // 指定月の工数明細をCSV形式のbyte配列で返す
        String safeTargetMonth = normalizeTargetMonth(targetMonth);
        StringBuilder csv = new StringBuilder();

        try {
            access();

            SummaryDAO dao = new SummaryDAO(conn);
            List<WorkLogDTO> list = dao.selectMonthlyWorkLogs(safeTargetMonth);

            // Excelで文字化けしにくいようにBOMを付ける
            csv.append('\uFEFF');
            csv.append("日付,案件名,タスク名,担当者,工数(h),作業内容\n");

            for (WorkLogDTO dto : list) {
                csv.append(escapeCsv(dto.getWorkDate())).append(",");
                csv.append(escapeCsv(dto.getProjectName())).append(",");
                csv.append(escapeCsv(dto.getTaskName())).append(",");
                csv.append(escapeCsv(dto.getUserName())).append(",");
                csv.append(dto.getManHours()).append(",");
                csv.append(escapeCsv(dto.getJobContents())).append("\n");
            }

            commit();

        } catch (SQLException e) {
            rollback();
            e.printStackTrace();

        } finally {
            close();
        }

        return csv.toString().getBytes(StandardCharsets.UTF_8);
    }

    private static void applyMemberPercentage(List<MonthlySummaryDTO.MemberSummaryDTO> memberList, float total) {

        // private staticなのでSummaryService内部だけで使う割合反映処理
        // DBへ保存せず画面表示用DTOへ計算結果を入れる
        if (memberList == null) {
            return;
        }

        for (MonthlySummaryDTO.MemberSummaryDTO dto : memberList) {
            dto.setPercentage(calcRate(dto.getManhours(), total));
        }
    }

    private static String normalizeTargetMonth(String targetMonth) {

        // private staticなのでSummaryService内部だけで使う月補正処理
        // 不正な入力なら今月を使う
        if (targetMonth == null || !targetMonth.matches("\\d{4}-\\d{2}")) {
            return java.time.LocalDate.now().toString().substring(0, 7);
        }

        return targetMonth;
    }

    private static int calcRate(float value, float total) {

        // private staticなのでSummaryService内部だけで使う割合計算
        // 0除算を避けるためtotalが0以下なら0を返す
        if (total <= 0f) {
            return 0;
        }

        return Math.round((value / total) * 100f);
    }

    private static String escapeCsv(String value) {

        // private staticなのでSummaryService内部だけで使うCSV加工処理
        // カンマや改行がある文字列をCSV用に囲む
        if (value == null) {
            return "";
        }

        String escaped = value.replace("\"", "\"\"");

        if (escaped.contains(",") || escaped.contains("\n") || escaped.contains("\r")) {
            return "\"" + escaped + "\"";
        }

        return escaped;
    }
}