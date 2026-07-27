package action;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import model.TaskDTO;
import model.UserDTO;
import model.WorkLogDTO;
import service.WorkLogService;

/**
 * 工数ログ関連の画面処理を担当するAction
 * Controllerから呼ばれてServiceへ処理を依頼
 */
public class WorkLogAction {

    /** 工数登録モーダル */
    private static final String JSP_WORK_LOG_MODAL = "/WEB-INF/jsp/workLogModal.jsp";

    /** タスク一覧へ戻すredirect */
    private static final String REDIRECT_TASK_LIST = "redirect:Controller?page_id=T001";

    /** タスク詳細へ戻すredirect */
    private static final String REDIRECT_TASK_DETAIL = "redirect:Controller?page_id=T002";

    /** request */
    private final HttpServletRequest request;

    /**
     * requestを受け取る。
     *
     * @param request 画面から送られた情報
     */
    public WorkLogAction(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * 工数登録モーダルを表示する。
     *
     * @return 遷移先JSP
     */
    public String showRegist() {

        if (getLoginUser() == null) {
            return REDIRECT_TASK_LIST + "&msg=" + encode("ログインしてください");
        }

        int taskId = parseInt(getParam("task_id", "taskId"));

        if (taskId <= 0) {
            return REDIRECT_TASK_LIST + "&msg=" + encode("タスクIDが不正です");
        }

        WorkLogService service = new WorkLogService();
        TaskDTO taskDto = service.getWorkLogFormData(taskId);

        if (taskDto == null) {
            return REDIRECT_TASK_LIST + "&msg=" + encode("対象タスクが見つかりません");
        }

        WorkLogDTO workLogDto = new WorkLogDTO();
        workLogDto.setTaskId(taskId);
        workLogDto.setWorkDate(LocalDate.now().toString());

        request.setAttribute("task", taskDto);
        request.setAttribute("workLog", workLogDto);

        return JSP_WORK_LOG_MODAL;
    }

    /**
     * 工数ログを登録する。
     *
     * @return 遷移先
     */
    public String regist() {

        UserDTO loginUser = getLoginUser();

        if (loginUser == null) {
            return REDIRECT_TASK_LIST + "&msg=" + encode("ログインしてください");
        }

        WorkLogDTO workLogDto = createWorkLogDtoForRegist(loginUser);
        String errorMessage = validateForRegist(workLogDto);

        if (hasText(errorMessage)) {
            setRegistFormData(workLogDto);
            request.setAttribute("errMsg", errorMessage);
            request.setAttribute("workLog", workLogDto);
            return JSP_WORK_LOG_MODAL;
        }

        WorkLogService service = new WorkLogService();
        int result = service.regist(workLogDto);

        if (result > 0) {
            return createTaskDetailRedirect(
                    workLogDto.getTaskId(),
                    "工数を登録しました");
        }

        setRegistFormData(workLogDto);
        request.setAttribute("errMsg", "工数登録に失敗しました");
        request.setAttribute("workLog", workLogDto);

        return JSP_WORK_LOG_MODAL;
    }

    /**
     * 工数ログを削除する。
     *
     * @return 遷移先
     */
    public String delete() {

        if (getLoginUser() == null) {
            return REDIRECT_TASK_LIST + "&msg=" + encode("ログインしてください");
        }

        int workLogsId = parseInt(
                getParam(
                        "work_logs_id",
                        "workLogsId",
                        "work_log_id",
                        "workLogId"));

        int taskId = parseInt(getParam("task_id", "taskId"));

        if (workLogsId <= 0) {
            return createTaskDetailRedirect(taskId, "工数ログIDが不正です");
        }

        WorkLogService service = new WorkLogService();
        int result = service.delete(workLogsId);

        if (result > 0) {
            return createTaskDetailRedirect(taskId, "工数を削除しました");
        }

        return createTaskDetailRedirect(taskId, "工数削除に失敗しました");
    }

    /**
     * 登録用DTOを作る。
     *
     * @param loginUser ログインユーザー
     * @return 工数ログDTO
     */
    private WorkLogDTO createWorkLogDtoForRegist(UserDTO loginUser) {

        WorkLogDTO workLogDto = new WorkLogDTO();

        workLogDto.setTaskId(parseInt(getParam("task_id", "taskId")));
        workLogDto.setUserId(loginUser.getUserId());
        workLogDto.setWorkDate(getParam("work_date", "workDate"));
        workLogDto.setManHours(parseFloat(getParam("man_hours", "manHours")));
        workLogDto.setJobContents(getParam("job_contents", "jobContents"));

        return workLogDto;
    }

    /**
     * 登録前入力チェックを行う。
     *
     * @param workLogDto 工数ログDTO
     * @return エラーメッセージ
     */
    private String validateForRegist(WorkLogDTO workLogDto) {

        if (workLogDto.getTaskId() <= 0) {
            return "タスクIDが不正です";
        }

        if (workLogDto.getUserId() <= 0) {
            return "ログイン情報が不正です";
        }

        if (!hasText(workLogDto.getWorkDate())) {
            return "作業日を入力してください";
        }

        if (workLogDto.getManHours() < 0.5f
                || workLogDto.getManHours() > 24.0f) {
            return "工数は0.5から24の範囲で入力してください";
        }

        if (workLogDto.getManHours() * 2 != Math.floor(workLogDto.getManHours() * 2)) {
            return "工数は0.5単位で入力してください";
        }

        if (hasText(workLogDto.getJobContents()) && workLogDto.getJobContents().length() > 255) {
            return "作業内容は255文字以内で入力してください";
        }

        return "";
    }

    /**
     * エラー時の再表示情報を設定する。
     *
     * @param workLogDto 工数ログDTO
     */
    private void setRegistFormData(WorkLogDTO workLogDto) {

        if (workLogDto == null || workLogDto.getTaskId() <= 0) {
            return;
        }

        WorkLogService service = new WorkLogService();
        TaskDTO taskDto = service.getWorkLogFormData(workLogDto.getTaskId());

        request.setAttribute("task", taskDto);
    }

    /**
     * タスク詳細へのredirectを作る。
     *
     * @param taskId タスクID
     * @param message メッセージ
     * @return redirect文字列
     */
    private String createTaskDetailRedirect(int taskId, String message) {

        if (taskId <= 0) {
            return REDIRECT_TASK_LIST + "&msg=" + encode(message);
        }

        return REDIRECT_TASK_DETAIL
                + "&task_id=" + taskId
                + "&msg=" + encode(message);
    }

    /**
     * ログインユーザーを取得する。
     *
     * @return ログインユーザー
     */
    private UserDTO getLoginUser() {

        HttpSession session = request.getSession(false);

        if (session == null) {
            return null;
        }

        Object loginUser = session.getAttribute("loginUser");

        if (!(loginUser instanceof UserDTO)) {
            return null;
        }

        return (UserDTO) loginUser;
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
     * 文字列をintに変換する。
     *
     * @param value 変換前文字列
     * @return 変換後数値
     */
    private int parseInt(String value) {

        if (!hasText(value)) {
            return -1;
        }

        try {
            return Integer.parseInt(value);

        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * 文字列をfloatに変換する。
     *
     * @param value 変換前文字列
     * @return 変換後数値
     */
    private float parseFloat(String value) {

        if (!hasText(value)) {
            return -1;
        }

        try {
            return Float.parseFloat(value);

        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * 文字列が入力されているか確認する。
     *
     * @param value 確認する文字列
     * @return 入力ありならtrue
     */
    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }

    /**
     * URL用文字列へ変換する。
     *
     * @param value 変換前文字列
     * @return 変換後文字列
     */
    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}