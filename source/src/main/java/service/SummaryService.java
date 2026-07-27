package service;

import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import dao.SummaryDAO;
import model.MemberSummaryDTO;
import model.MonthlySummaryDTO;
import model.ProjectSummaryDTO;
import model.WorkLogDTO;

/**
 * 月次集計の業務処理を担当するService。
 * 集計DTO作成、割合計算、CSV作成を行う。
 */
public class SummaryService extends DBAccess {

    /** CSVヘッダー */
    private static final String CSV_HEADER =
            "日付,案件名,タスク名,担当者,工数(h),作業内容";

    /**
     * 月次集計画面用データを取得する。
     *
     * @param targetMonth 対象月 yyyy-MM
     * @return 月次集計DTO
     */
    public MonthlySummaryDTO getMonthlySummary(String targetMonth) {

        String month = normalizeTargetMonth(targetMonth);
        MonthlySummaryDTO summaryDto = new MonthlySummaryDTO();

        summaryDto.setTargetMonth(month);

        try {
            access();

            SummaryDAO summaryDao = new SummaryDAO(conn);

            Float monthlyTotal = summaryDao.selectMonthlyTotal(month);
            int projectCount = summaryDao.countMonthlyProjects(month);
            int activeMemberCount = summaryDao.countMonthlyMembers(month);
            List<ProjectSummaryDTO> projectSummaryList =
                    summaryDao.selectProjectSummary(month);
            List<MemberSummaryDTO> memberSummaryList =
                    summaryDao.selectMemberSummary(month);
            List<WorkLogDTO> monthlyWorkLogList =
                    summaryDao.selectMonthlyWorkLogs(month);

            for (ProjectSummaryDTO dto : projectSummaryList) {
                dto.setAchievementRate(
                        calcAchievementRate(
                                dto.getActualManhours(),
                                dto.getEstimatedManhours()));
            }

            for (MemberSummaryDTO dto : memberSummaryList) {
                dto.setAchievementRate(
                        calcAchievementRate(
                                dto.getActualManHours(),
                                dto.getEstimatedManhours()));
            }

            summaryDto.setMonthlyTotalManHours(monthlyTotal);
            summaryDto.setTotalManHours(monthlyTotal);
            summaryDto.setProjectCount(projectCount);
            summaryDto.setActiveMemberCount(activeMemberCount);
            summaryDto.setOverrunProjectCount(
                    countOverrunProjects(projectSummaryList));
            summaryDto.setProjectSummaryList(projectSummaryList);
            summaryDto.setMemberSummaryList(memberSummaryList);
            summaryDto.setMonthlyWorkLogList(monthlyWorkLogList);

            commit();

        } catch (SQLException e) {
            rollback();
            e.printStackTrace();

        } finally {
            close();
        }

        return summaryDto;
    }

    /**
     * 指定月の案件別集計を取得する。
     *
     * @param targetMonth 対象月 yyyy-MM
     * @return 案件別集計一覧
     */
    public List<ProjectSummaryDTO> getProjectSummary(String targetMonth) {

        List<ProjectSummaryDTO> projectSummaryList = new ArrayList<>();
        String month = normalizeTargetMonth(targetMonth);

        try {
            access();

            SummaryDAO summaryDao = new SummaryDAO(conn);
            projectSummaryList = summaryDao.selectProjectSummary(month);

            for (ProjectSummaryDTO dto : projectSummaryList) {
                dto.setAchievementRate(
                        calcAchievementRate(
                                dto.getActualManhours(),
                                dto.getEstimatedManhours()));
            }

            commit();

        } catch (SQLException e) {
            rollback();
            e.printStackTrace();

        } finally {
            close();
        }

        return projectSummaryList;
    }

    /**
     * 指定月のメンバー別集計を取得する。
     *
     * @param targetMonth 対象月 yyyy-MM
     * @return メンバー別集計一覧
     */
    public List<MemberSummaryDTO> getMemberSummary(String targetMonth) {

        List<MemberSummaryDTO> memberSummaryList = new ArrayList<>();
        String month = normalizeTargetMonth(targetMonth);

        try {
            access();

            SummaryDAO summaryDao = new SummaryDAO(conn);
            memberSummaryList = summaryDao.selectMemberSummary(month);

            Float monthlyTotal = summaryDao.selectMonthlyTotal(month);

            for (MemberSummaryDTO dto : memberSummaryList) {
                dto.setAchievementRate(
                        calcAchievementRate(
                                dto.getActualManHours(),
                                dto.getEstimatedManhours()));

                dto.setAchivementRate(
                        calcPercentage(
                                dto.getManHours(),
                                monthlyTotal));
            }

            commit();

        } catch (SQLException e) {
            rollback();
            e.printStackTrace();

        } finally {
            close();
        }

        return memberSummaryList;
    }

    /**
     * 指定月の工数明細を取得する。
     *
     * @param targetMonth 対象月 yyyy-MM
     * @return 工数明細一覧
     */
    public List<WorkLogDTO> getMonthlyWorkLogs(String targetMonth) {

        List<WorkLogDTO> workLogList = new ArrayList<>();
        String month = normalizeTargetMonth(targetMonth);

        try {
            access();

            SummaryDAO summaryDao = new SummaryDAO(conn);
            workLogList = summaryDao.selectMonthlyWorkLogs(month);

            commit();

        } catch (SQLException e) {
            rollback();
            e.printStackTrace();

        } finally {
            close();
        }

        return workLogList;
    }

    /**
     * 指定月の工数明細CSVを作成する。
     *
     * @param targetMonth 対象月 yyyy-MM
     * @return CSVデータ
     */
    public byte[] createCsv(String targetMonth) {

        List<WorkLogDTO> workLogList = getMonthlyWorkLogs(targetMonth);
        StringBuilder csv = new StringBuilder();

        csv.append('\uFEFF');
        csv.append(CSV_HEADER).append("\r\n");

        for (WorkLogDTO dto : workLogList) {
            csv.append(escapeCsv(dto.getWorkDate())).append(",");
            csv.append(escapeCsv(dto.getProjectName())).append(",");
            csv.append(escapeCsv(dto.getTaskName())).append(",");
            csv.append(escapeCsv(dto.getUserName())).append(",");
            csv.append(dto.getManHours()).append(",");
            csv.append(escapeCsv(dto.getJobContents())).append("\r\n");
        }

        return csv.toString().getBytes(StandardCharsets.UTF_8);
    }

    /**
     * 予定工数に対する実績割合を計算する。
     *
     * @param actual 実績工数
     * @param estimated 予定工数
     * @return 達成率
     */
    private double calcAchievementRate(Float actual, Float estimated) {

        if (actual == null || estimated == null || estimated <= 0f) {
            return 0;
        }

        return Math.round((actual / estimated) * 1000.0) / 10.0;
    }

    /**
     * 全体に対する割合を計算する。
     *
     * @param value 値
     * @param total 全体
     * @return 割合
     */
    private double calcPercentage(Float value, Float total) {

        if (value == null || total == null || total <= 0f) {
            return 0;
        }

        return Math.round((value / total) * 1000.0) / 10.0;
    }

    /**
     * 超過案件数を数える。
     *
     * @param projectSummaryList 案件別集計一覧
     * @return 超過案件数
     */
    private int countOverrunProjects(
            List<ProjectSummaryDTO> projectSummaryList) {

        if (projectSummaryList == null) {
            return 0;
        }

        int count = 0;

        for (ProjectSummaryDTO dto : projectSummaryList) {
            if (dto.getEstimatedManhours() > 0
                    && dto.getActualManhours()
                    > dto.getEstimatedManhours()) {
                count++;
            }
        }

        return count;
    }

    /**
     * 対象月をyyyy-MM形式へ補正する。
     *
     * @param targetMonth 対象月
     * @return 補正後対象月
     */
    private String normalizeTargetMonth(String targetMonth) {

        if (targetMonth == null
                || !targetMonth.matches("\\d{4}-\\d{2}")) {
            return YearMonth.now().toString();
        }

        return targetMonth;
    }

    /**
     * CSV用文字列へ変換する。
     *
     * @param value 変換前文字列
     * @return CSV用文字列
     */
    private String escapeCsv(String value) {

        if (value == null) {
            return "";
        }

        String escaped = value.replace("\"", "\"\"");

        if (escaped.contains(",")
                || escaped.contains("\"")
                || escaped.contains("\r")
                || escaped.contains("\n")) {
            return "\"" + escaped + "\"";
        }

        return escaped;
    }
}