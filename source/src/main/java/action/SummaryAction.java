package action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.MonthlySummaryDTO;
import service.SummaryService;

public class SummaryAction {

    // Controllerから受け取ったrequestを保持する
    private final HttpServletRequest request;

    // CSV出力で使うresponseを保持する
    private final HttpServletResponse response;

    public SummaryAction(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    public String show() {

        // 月次集計画面を今月で初期表示する
        String targetMonth = currentMonth();

        SummaryService service = new SummaryService();
        MonthlySummaryDTO summary = service.getMonthlySummary(targetMonth);

        // 初期表示は予算入力がないため0で反映する
        applyBudgetInput(summary, "0", request.getParameterMap());

        request.setAttribute("summary", summary);

        return "/WEB-INF/jsp/monthlySummary.jsp";
    }

    public String search() {

        // 指定月と画面入力予算で月次集計を再表示する
        String targetMonth = request.getParameter("target_month");
        String monthlyBudgetText = request.getParameter("monthly_budget_manhours");

        SummaryService service = new SummaryService();
        MonthlySummaryDTO summary = service.getMonthlySummary(targetMonth);

        // 予算工数はDB値ではなく画面入力値を反映する
        applyBudgetInput(summary, monthlyBudgetText, request.getParameterMap());

        request.setAttribute("summary", summary);

        return "/WEB-INF/jsp/monthlySummary.jsp";
    }

    public String exportCsv() {

        // 指定月の工数明細をCSVで出力する
        String targetMonth = request.getParameter("target_month");

        SummaryService service = new SummaryService();
        byte[] csv = service.createCsv(targetMonth);

        try {
            // CSVとしてブラウザへ返す
            response.setContentType("text/csv; charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=worklog_" + normalizeTargetMonth(targetMonth) + ".csv");
            response.getOutputStream().write(csv);
            response.getOutputStream().flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Controllerでは遷移しない
        return null;
    }

    private static void applyBudgetInput(MonthlySummaryDTO summary, String monthlyBudgetText,
            Map<String, String[]> parameterMap) {

        // SummaryAction内部だけで使う予算反映処理
        // メソッドをクラス図準拠に保つため内部処理へ分ける
        if (summary == null) {
            return;
        }

        float monthlyBudget = parseFloat(monthlyBudgetText);
        summary.setMonthlyBudgetManhours(monthlyBudget);
        summary.setBudgetAlertCount(summary.getMonthlyTotalManhours() > monthlyBudget && monthlyBudget > 0f ? 1 : 0);
        summary.setMonthlyAchievementRate(calcRate(summary.getMonthlyTotalManhours(), monthlyBudget));

        Map<Integer, Float> projectBudgetMap = createProjectBudgetMap(parameterMap);
        List<MonthlySummaryDTO.ProjectSummaryDTO> projectList = summary.getProjectSummaryList();

        for (MonthlySummaryDTO.ProjectSummaryDTO dto : projectList) {
            float budget = projectBudgetMap.getOrDefault(dto.getProjectId(), 0f);
            dto.setBudgetManhours(budget);
            dto.setAchievementRate(calcRate(dto.getActualManhours(), budget));
        }
    }

    private static Map<Integer, Float> createProjectBudgetMap(Map<String, String[]> parameterMap) {

        // SummaryAction内部だけで使う予算Map作成処理
        // name="budget_案件ID" の入力値を案件IDごとにまとめる
        Map<Integer, Float> budgetMap = new HashMap<>();

        if (parameterMap == null) {
            return budgetMap;
        }

        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String key = entry.getKey();

            if (!key.startsWith("budget_")) {
                continue;
            }

            int projectId = parseInt(key.substring("budget_".length()));
            float budget = parseFloat(firstValue(entry.getValue()));

            if (projectId > 0) {
                budgetMap.put(projectId, budget);
            }
        }

        return budgetMap;
    }

    private static String normalizeTargetMonth(String targetMonth) {

        // SummaryAction内部だけで使う月補正処理
        // 不正な入力なら今月を返す
        if (targetMonth == null || !targetMonth.matches("\\d{4}-\\d{2}")) {
            return currentMonth();
        }

        return targetMonth;
    }

    private static String currentMonth() {

        // SummaryAction内部だけで使う今月取得処理
        // yyyy-MM形式で画面とServiceへ渡す
        return java.time.LocalDate.now().toString().substring(0, 7);
    }

    private static float parseFloat(String value) {

        // SummaryAction内部だけで使うfloat変換処理
        // 数値以外なら0として扱う
        if (value == null || value.trim().isEmpty()) {
            return 0f;
        }

        try {
            return Float.parseFloat(value.trim());
        } catch (NumberFormatException e) {
            return 0f;
        }
    }

    private static int parseInt(String value) {

        // SummaryAction内部だけで使うint変換処理
        // 数値以外なら-1として扱う
        if (value == null || value.trim().isEmpty()) {
            return -1;
        }

        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static int calcRate(float value, float total) {

        // SummaryAction内部だけで使う割合計算処理
        // 0除算を避けるためtotalが0以下なら0を返す
        if (total <= 0f) {
            return 0;
        }

        return Math.round((value / total) * 100f);
    }

    private static String firstValue(String[] values) {

        // private staticなのでSummaryAction内部だけで使う配列取得処理
        // parameterMapの先頭値だけを取り出す
        if (values == null || values.length == 0) {
            return "";
        }

        return values[0];
    }
}