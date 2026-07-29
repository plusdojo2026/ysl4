package action;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import model.ProjectsDTO;
import model.TaskDTO;
import model.UserDTO;
import service.TaskService;

/**
 * タスク関連の画面処理を担当するAction
 * Controllerから呼ばれてServiceへ処理を依頼する
 */
public class TaskAction {

    /** タスク一覧画面 */
    private static final String JSP_TASK_LIST = "/WEB-INF/jsp/taskList.jsp";

    /** タスク詳細画面 */
    private static final String JSP_TASK_DETAIL = "/WEB-INF/jsp/taskDetail.jsp";

    /** タスク登録画面 */
    private static final String JSP_TASK_FORM = "/WEB-INF/jsp/taskRegist.jsp";
    
    /** タスク編集画面 */
    private static final String JSP_TASK_EDIT = "/WEB-INF/jsp/taskEdit.jsp";

    /** タスク一覧へのリダイレクト */
    private static final String REDIRECT_TASK_LIST = "redirect:Controller?page_id=T001";

    /** request */
    private final HttpServletRequest request;

    /**
     * requestを受け取る
     * @param request 画面から送られた情報
     */
    public TaskAction(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * タスク一覧を表示する
     * @return 遷移先JSP
     */
    public String list() {

        TaskService service = new TaskService();
        List<TaskDTO> taskList = service.selectAll();

        request.setAttribute("taskList", taskList);
        setFormData(service.getTaskFormData(0));
        setMessageFromParameter();

        return JSP_TASK_LIST;
    }

    /**
     * タスク検索を行う
     * @return 遷移先JSP
     */
    public String search() {

        TaskDTO condition = createSearchCondition();

        TaskService service = new TaskService();
        List<TaskDTO> taskList = service.search(condition);

        request.setAttribute("taskList", taskList);
        request.setAttribute("condition", condition);
        request.setAttribute("keyword", condition.getTaskName());

        setFormData(service.getTaskFormData(condition.getProjectId()));

        return JSP_TASK_LIST;
    }

    /**
     * タスク詳細を表示する
     * @return 遷移先JSP
     */
    public String detail() {

        int taskId = parseInt(getParam("task_id", "taskId"));

        if (taskId <= 0) {
            return REDIRECT_TASK_LIST + "&msg=" + encode("タスクIDが不正です");
        }

        TaskService service = new TaskService();
        TaskDTO taskDto = service.findById(taskId);

        if (taskDto == null) {
            return REDIRECT_TASK_LIST + "&msg=" + encode("対象タスクが見つかりません");
        }

        request.setAttribute("task", taskDto);
        request.setAttribute("workLogList", taskDto.getWorkLogList());
        setMessageFromParameter();

        return JSP_TASK_DETAIL;
    }

    /**
     * タスク登録画面を表示する
     * @return 遷移先JSP
     */
    public String showRegist() {

        int projectId = parseInt(getParam("project_id", "projectId"));

        request.setAttribute("mode", "regist");
        request.setAttribute("task", new TaskDTO());

        setFormData(new TaskService().getTaskFormData(projectId));

        return JSP_TASK_FORM;
    }

    /**
     * タスクを登録する
     * @return 遷移先
     */
    public String regist() {

        TaskDTO taskDto = createTaskDtoForSave(false);

        String errorMessage = validateForSave(taskDto);

        if (hasText(errorMessage)) {
            request.setAttribute("errMsg", errorMessage);
            request.setAttribute("mode", "regist");
            request.setAttribute("task", taskDto);
            setFormData(new TaskService().getTaskFormData(taskDto.getProjectId()));
            return JSP_TASK_FORM;
        }

        TaskService service = new TaskService();
        int result = service.regist(taskDto);

        if (result > 0) {
            return REDIRECT_TASK_LIST + "&msg=" + encode("タスクを登録しました");
        }

        request.setAttribute("errMsg", "タスク登録に失敗しました");
        request.setAttribute("mode", "regist");
        request.setAttribute("task", taskDto);
        setFormData(new TaskService().getTaskFormData(taskDto.getProjectId()));

        return JSP_TASK_FORM;
    }

    /**
     * タスク編集画面を表示する
     * @return 遷移先JSP
     */
    public String showUpdate() {

        int taskId = parseInt(getParam("task_id", "taskId"));

        if (taskId <= 0) {
            return REDIRECT_TASK_LIST + "&msg=" + encode("タスクIDが不正です");
        }

        TaskService service = new TaskService();
        TaskDTO taskDto = service.findById(taskId);

        if (taskDto == null) {
            return REDIRECT_TASK_LIST + "&msg=" + encode("対象タスクが見つかりません");
        }

        request.setAttribute("mode", "update");
        request.setAttribute("task", taskDto);

        setFormData(service.getTaskFormData(taskDto.getProjectId()));

        return JSP_TASK_EDIT;
    }

    /**
     * タスクを更新する
     * @return 遷移先
     */
    public String update() {

        TaskDTO taskDto = createTaskDtoForSave(true);

        String errorMessage = validateForSave(taskDto);

        if (hasText(errorMessage)) {
            request.setAttribute("errMsg", errorMessage);
            request.setAttribute("mode", "update");
            request.setAttribute("task", taskDto);
            setFormData(new TaskService().getTaskFormData(taskDto.getProjectId()));
            return JSP_TASK_FORM;
        }

        TaskService service = new TaskService();
        int result = service.update(taskDto);

        if (result > 0) {
            return "redirect:Controller?page_id=T002&task_id=" + taskDto.getTaskId()
                    + "&msg=" + encode("タスクを更新しました");
        }

        request.setAttribute("errMsg", "タスク更新に失敗しました");
        request.setAttribute("mode", "update");
        request.setAttribute("task", taskDto);
        setFormData(new TaskService().getTaskFormData(taskDto.getProjectId()));

        return JSP_TASK_FORM;
    }

    /**
     * タスクステータスを変更する
     * @return 遷移先
     */
    public String changeStatus() {

        int taskId = parseInt(getParam("task_id", "taskId"));
        String status = getParam("status");

        if (taskId <= 0 || !hasText(status)) {
            return REDIRECT_TASK_LIST + "&msg=" + encode("タスク状態を変更できませんでした");
        }

        TaskService service = new TaskService();
        int result = service.changeStatus(taskId, status);

        if (result > 0) {
            return "redirect:Controller?page_id=T002&task_id=" + taskId
                    + "&msg=" + encode("タスク状態を変更しました");
        }

        return "redirect:Controller?page_id=T002&task_id=" + taskId
                + "&msg=" + encode("タスク状態の変更に失敗しました");
    }

    /**
     * タスクを削除する
     * @return 遷移先
     */
    public String delete() {

        int taskId = parseInt(getParam("task_id", "taskId"));

        if (taskId <= 0) {
            return REDIRECT_TASK_LIST + "&msg=" + encode("タスクIDが不正です");
        }

        TaskService service = new TaskService();
        int result = service.delete(taskId);

        if (result > 0) {
            return REDIRECT_TASK_LIST + "&msg=" + encode("タスクを削除しました");
        }

        return "redirect:Controller?page_id=T002&task_id=" + taskId
                + "&msg=" + encode("タスク削除に失敗しました");
    }

    /**
     * 保存用DTOを作る
     * 登録と更新で共通利用する
     * @param includeId 更新時はtrue
     * @return タスクDTO
     */
    private TaskDTO createTaskDtoForSave(boolean includeId) {

        TaskDTO taskDto = new TaskDTO();

        if (includeId) {
            taskDto.setTaskId(parseInt(getParam("task_id", "taskId")));
        }

        taskDto.setProjectCode(parseInt(getParam("projectCode", "projectCode")));
        taskDto.setTaskName(getParam("task_name", "taskName"));
        taskDto.setProjectId(parseInt(getParam("project_id", "projectId")));
        taskDto.setManagerId(parseInt(getParam("manager_id", "managerId")));
        taskDto.setStartDate(getParam("start_date", "startDate"));
        taskDto.setDueDate(getParam("due_date", "dueDate"));
        taskDto.setEstimatedManhours(parseFloat(getParam("estimated_manhours", "estimatedManhours")));
        taskDto.setProgress(parseIntDefault(getParam("progress"), 0));
        taskDto.setStatus(getParam("status"));
        taskDto.setPriority(getParam("priority"));
        taskDto.setDescription(getParam("description"));

        return taskDto;
    }

    /**
     * 検索条件DTOを作る
     * TaskSearchConditionは作らずTaskDTOで代用する
     * @return 検索条件DTO
     */
    private TaskDTO createSearchCondition() {

        TaskDTO condition = new TaskDTO();

        condition.setTaskName(getParam("keyword", "task_name", "taskName"));
        condition.setProjectId(parseInt(getParam("project_id", "projectId")));
        condition.setStatus(getParam("status"));
        condition.setManagerId(parseInt(getParam("manager_id", "managerId")));

        return condition;
    }

    /**
     * 保存前の入力チェックを行う
     * @param taskDto タスクDTO
     * @return エラーメッセージ
     */
    private String validateForSave(TaskDTO taskDto) {

        if (taskDto.getTaskId() < 0) {
            return "タスクIDが不正です";
        }

        if (!hasText(taskDto.getTaskName())) {
            return "タスク名を入力してください";
        }

        if (taskDto.getProjectId() <= 0) {
            return "案件を選択してください";
        }

        if (taskDto.getEstimatedManhours() < 0) {
            return "見積工数は0以上で入力してください";
        }

        if (!hasText(taskDto.getStatus())) {
            return "ステータスを選択してください";
        }

        if (!hasText(taskDto.getPriority())) {
            return "優先度を選択してください";
        }

        if (taskDto.getProgress() < 0 || taskDto.getProgress() > 100) {
            return "進捗率は0から100で入力してください";
        }

        return "";
    }

    /**
     * List形式の画面表示用データをrequestへ設定する
     * Mapを使わず固定順で受け取る
     * @param formDataList 画面表示用データList
     */
    @SuppressWarnings("unchecked")
    private void setFormData(List<Object> formDataList) {

        if (formDataList == null || formDataList.size() < 4) {
            return;
        }

        List<ProjectsDTO> projectList = (List<ProjectsDTO>) formDataList.get(0);
        List<UserDTO> userList = (List<UserDTO>) formDataList.get(1);
        ProjectsDTO selectedProject = (ProjectsDTO) formDataList.get(2);
        Integer selectedProjectId = (Integer) formDataList.get(3);

        request.setAttribute("projectList", projectList);
        request.setAttribute("userList", userList);
        request.setAttribute("selectedProject", selectedProject);
        request.setAttribute("selectedProjectId", selectedProjectId);
    }

    /**
     * requestから値を取得する
     * 複数候補を順番に確認する
     * @param names 取得候補のname
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
     * 文字列をintに変換する
     * 数値以外の場合は-1を返す
     * @param value 変換前の文字列
     * @return 変換後の数値
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
     * 文字列をintに変換する
     * 数値以外の場合は指定した初期値を返す
     * @param value 変換前の文字列
     * @param defaultValue 初期値
     * @return 変換後の数値
     */
    private int parseIntDefault(String value, int defaultValue) {

        if (!hasText(value)) {
            return defaultValue;
        }

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * 文字列をfloatに変換する
     * 数値以外の場合は0を返す
     * @param value 変換前の文字列
     * @return 変換後の数値
     */
    private float parseFloat(String value) {

        if (!hasText(value)) {
            return 0;
        }

        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * 文字列が入力されているか確認する
     * nullと空文字を未入力として扱う
     * @param value 確認する文字列
     * @return 入力ありならtrue
     */
    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }

    /**
     * URLに入れる文字列をエンコードする
     * redirect時の日本語文字化けを防ぐ
     * @param value エンコード前の文字列
     * @return エンコード後の文字列
     */
    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    /**
     * redirect後のメッセージをrequestへ入れる
     */
    private void setMessageFromParameter() {

        String message = getParam("msg");

        if (hasText(message)) {
            request.setAttribute("successMsg", message);
        }
    }
}