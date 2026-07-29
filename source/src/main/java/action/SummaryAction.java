package action;

import java.io.IOException;
import java.time.YearMonth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.MonthlySummaryDTO;
import service.SummaryService;

/**
 * 月次集計画面とCSV出力を担当するAction.
 */
public class SummaryAction {

    /** 月次集計画面 */
    private static final String JSP_SUMMARY = "/WEB-INF/jsp/monthlySummary.jsp";

    /** request */
    private final HttpServletRequest request;

    /** response */
    private final HttpServletResponse response;

    /**
     * requestとresponseを受け取る.
     *
     * @param request リクエスト.
     * @param response レスポンス.
     */
    public SummaryAction(
            HttpServletRequest request,
            HttpServletResponse response) {

        this.request = request;
        this.response = response;
    }

    /**
     * 月次集計画面を当月で表示する.
     *
     * @return 遷移先JSP.
     */
    public String show() {

        String targetMonth = YearMonth.now().toString();

        SummaryService service = new SummaryService();
        MonthlySummaryDTO summaryDto = service.getMonthlySummary(targetMonth);

        setSummaryAttributes(summaryDto, targetMonth);

        return JSP_SUMMARY;
    }

    /**
     * 対象月を指定して月次集計を表示する.
     *
     * @return 遷移先JSP.
     */
    public String search() {

        String targetMonth =
                normalizeTargetMonth(getParam("target_month", "targetMonth"));

        SummaryService service = new SummaryService();
        MonthlySummaryDTO summaryDto = service.getMonthlySummary(targetMonth);

        setSummaryAttributes(summaryDto, targetMonth);

        return JSP_SUMMARY;
    }

    /**
     * 対象月の工数明細をCSV出力する.
     *
     * @return 遷移なしの場合はnull.
     */
    public String exportCsv() {

        String targetMonth =normalizeTargetMonth(
                        getParam("target_month", "targetMonth"));

        SummaryService service = new SummaryService();
        byte[] csv = service.createCsv(targetMonth);

        try {
            response.setContentType("text/csv; charset=UTF-8");
            response.setHeader(
                    "Content-Disposition",
                    "attachment; filename=\"monthly_summary_"
                            + targetMonth
                            + ".csv\"");

            response.getOutputStream().write(csv);
            response.getOutputStream().flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 月次集計画面で使うrequest属性を設定する.
     *
     * @param summaryDto 月次集計DTO.
     * @param targetMonth 対象月.
     */
    private void setSummaryAttributes(
            MonthlySummaryDTO summaryDto,
            String targetMonth) {

        request.setAttribute("summary", summaryDto);
        request.setAttribute("monthlySummary", summaryDto);
        request.setAttribute("targetMonth", targetMonth);

        request.setAttribute(
                "projectSummaryList",
                summaryDto.getProjectSummaryList());

        request.setAttribute(
                "memberSummaryList",
                summaryDto.getMemberSummaryList());

        request.setAttribute(
                "workLogList",
                summaryDto.getMonthlyWorkLogList());
    }

    /**
     * requestから値を取得する.
     *
     * @param names name候補.
     * @return 取得値.
     */
    private String getParam(String... names) {

        for (String name : names) {
            String value = request.getParameter(name);

            if (value != null) {
                return value.trim();
            }
        }

        return "";
    }

    /**
     * 対象月をyyyy-MM形式へ補正する.
     *
     * @param targetMonth 対象月.
     * @return 補正後対象月.
     */
    private String normalizeTargetMonth(String targetMonth) {

        if (targetMonth == null
                || !targetMonth.matches("\\d{4}-\\d{2}")) {
            return YearMonth.now().toString();
        }

        return targetMonth;
    }
}