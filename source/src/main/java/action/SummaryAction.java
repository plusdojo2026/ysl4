package action;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.YearMonth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.MonthlySummaryDTO;
import service.SummaryService;

/**
 * 月次集計の画面処理を担当するAction
 * 集計表示とCSV出力を行う
 */
public class SummaryAction {

	/** 月次集計画面 */
	private static final String JSP_SUMMARY = "/WEB-INF/jsp/monthlySummary.jsp";

	/** request */
	private final HttpServletRequest request;

	/** response */
	private final HttpServletResponse response;

	/**
	 * requestとresponseを受け取る
	 *
	 * @param request 画面から送られた情報
	 * @param response ブラウザへ返す情報
	 */
	public SummaryAction(
			HttpServletRequest request,
			HttpServletResponse response) {

		this.request = request;
		this.response = response;
	}

	/**
	 * 月次集計画面を当月で表示
	 *
	 * @return 遷移先JSP
	 */
	public String show() {

		String targetMonth = YearMonth.now().toString();

		SummaryService service = new SummaryService();
		MonthlySummaryDTO summaryDto = service.getMonthlySummary(targetMonth);

		request.setAttribute("summary", summaryDto);
		request.setAttribute("targetMonth", targetMonth);

		return JSP_SUMMARY;
	}

	/**
	 * 対象月を指定して月次集計を表示する。
	 *
	 * @return 遷移先JSP
	 */
	public String search() {

		String targetMonth = normalizeTargetMonth(
				getParam("target_month", "targetMonth"));

		SummaryService service = new SummaryService();
		MonthlySummaryDTO summaryDto = service.getMonthlySummary(targetMonth);

		request.setAttribute("summary", summaryDto);
		request.setAttribute("targetMonth", targetMonth);

		return JSP_SUMMARY;
	}

	/**
	 * 指定月の工数明細をCSV出力する。
	 *
	 * @return 遷移先
	 */
	public String exportCsv() {

		String targetMonth = normalizeTargetMonth(
				getParam("target_month", "targetMonth"));

		SummaryService service = new SummaryService();
		byte[] csv = service.createCsv(targetMonth);

		try {
			response.setContentType("text/csv; charset=UTF-8");
			response.setHeader(
					"Content-Disposition",
					"attachment; filename="
							+ createCsvFileName(targetMonth));

			response.getOutputStream().write(csv);
			response.getOutputStream().flush();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * requestから値を取得する。
	 *
	 * @param names name候補
	 * @return 取得値
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
	 * 対象月をyyyy-MM形式へ補正
	 *
	 * @param targetMonth 対象月
	 * @return 補正後対象月
	 */
	private String normalizeTargetMonth(String targetMonth) {

		if (targetMonth == null || !targetMonth.matches("\\d{4}-\\d{2}")) {
			return YearMonth.now().toString();
		}

		return targetMonth;
	}

	/**
	 * CSVファイル名を作る
	 *
	 * @param targetMonth 対象月
	 * @return CSVファイル名
	 */
	private String createCsvFileName(String targetMonth) {

		String fileName = "worklog_" + targetMonth + ".csv";

		return URLEncoder.encode(
				fileName,
				StandardCharsets.UTF_8);
	}
}