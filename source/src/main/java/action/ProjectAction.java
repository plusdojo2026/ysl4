package action;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import model.ProjectsDTO;
import model.UserDTO;
import service.ProjectService;

/**
 * 案件関連の画面処理を担当するAction
 * Controllerから呼ばれてServiceへ処理を依頼する
 */
public class ProjectAction {

    /** 案件一覧画面 */
    private static final String JSP_PROJECT_LIST = "/WEB-INF/jsp/projectList.jsp";

    /** 案件詳細画面 */
    private static final String JSP_PROJECT_DETAIL = "/WEB-INF/jsp/projectDetail.jsp";

    /** 案件登録編集画面 */
    private static final String JSP_PROJECT_FORM = "/WEB-INF/jsp/projectForm.jsp";

    /** 案件一覧へのリダイレクト */
    private static final String REDIRECT_PROJECT_LIST = "redirect:Controller?page_id=P001";

    /** request */
    private final HttpServletRequest request;

    /**
     * requestを受け取る
     * @param request 画面から送られた情報
     */
    public ProjectAction(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * 案件一覧を表示する
     * @return 遷移先JSP
     */
    public String list() {

        ProjectService service = new ProjectService();
        List<ProjectsDTO> projectList = service.selectAll();

        request.setAttribute("projectList", projectList);
        request.setAttribute("condition", new ProjectsDTO());

        setFormData(service.getProjectFormData());
        setMessageFromParameter();

        return JSP_PROJECT_LIST;
    }

    /**
     * 案件検索を行う
     * @return 遷移先JSP
     */
    public String search() {

        ProjectsDTO condition = createSearchCondition();

        ProjectService service = new ProjectService();
        List<ProjectsDTO> projectList = service.search(condition);

        request.setAttribute("projectList", projectList);
        request.setAttribute("condition", condition);
        request.setAttribute("keyword", condition.getProjectName());

        setFormData(service.getProjectFormData());

        return JSP_PROJECT_LIST;
    }

    /**
     * 案件登録画面を表示する
     * @return 遷移先JSP
     */
    public String showRegist() {

        ProjectService service = new ProjectService();

        request.setAttribute("mode", "regist");
        request.setAttribute("project", new ProjectsDTO());

        setFormData(service.getProjectFormData());

        return JSP_PROJECT_FORM;
    }

    /**
     * 案件を登録する
     * @return 遷移先
     */
    public String regist() {

        ProjectsDTO projectDto = createProjectDtoForSave(false);

        String errorMessage = validateForRegist(projectDto);

        if (hasText(errorMessage)) {
            request.setAttribute("errMsg", errorMessage);
            request.setAttribute("mode", "regist");
            request.setAttribute("project", projectDto);
            setFormData(new ProjectService().getProjectFormData());
            return JSP_PROJECT_FORM;
        }

        ProjectService service = new ProjectService();

        if (service.existsProjectCode(projectDto.getProjectCode())) {
            request.setAttribute("errMsg", "同じ案件コードが既に登録されています");
            request.setAttribute("mode", "regist");
            request.setAttribute("project", projectDto);
            setFormData(service.getProjectFormData());
            return JSP_PROJECT_FORM;
        }

        int result = service.regist(projectDto);

        if (result > 0) {
            return REDIRECT_PROJECT_LIST + "&msg=" + encode("案件を登録しました");
        }

        request.setAttribute("errMsg", "案件登録に失敗しました");
        request.setAttribute("mode", "regist");
        request.setAttribute("project", projectDto);
        setFormData(service.getProjectFormData());

        return JSP_PROJECT_FORM;
    }

    /**
     * 案件詳細を表示する
     * @return 遷移先JSP
     */
    public String detail() {

        int projectId = parseInt(getParam("project_id", "projectId"));

        if (projectId <= 0) {
            return REDIRECT_PROJECT_LIST + "&msg=" + encode("案件IDが不正です");
        }

        ProjectService service = new ProjectService();
        ProjectsDTO projectDto = service.findDetail(projectId);

        if (projectDto == null) {
            return REDIRECT_PROJECT_LIST + "&msg=" + encode("対象案件が見つかりません");
        }

        request.setAttribute("project", projectDto);
        request.setAttribute("taskList", projectDto.getTaskList());
        request.setAttribute("latestWorkLogList", projectDto.getLatestWorkLogList());

        setMessageFromParameter();

        return JSP_PROJECT_DETAIL;
    }

    /**
     * 案件編集画面を表示する
     * @return 遷移先JSP
     */
    public String showUpdate() {

        int projectId = parseInt(getParam("project_id", "projectId"));

        if (projectId <= 0) {
            return REDIRECT_PROJECT_LIST + "&msg=" + encode("案件IDが不正です");
        }

        ProjectService service = new ProjectService();
        ProjectsDTO projectDto = service.findById(projectId);

        if (projectDto == null) {
            return REDIRECT_PROJECT_LIST + "&msg=" + encode("対象案件が見つかりません");
        }

        request.setAttribute("mode", "update");
        request.setAttribute("project", projectDto);

        setFormData(service.getProjectFormData());

        return JSP_PROJECT_FORM;
    }

    /**
     * 案件を更新する
     * @return 遷移先
     */
    public String update() {

        ProjectsDTO projectDto = createProjectDtoForSave(true);

        String errorMessage = validateForUpdate(projectDto);

        if (hasText(errorMessage)) {
            request.setAttribute("errMsg", errorMessage);
            request.setAttribute("mode", "update");
            request.setAttribute("project", projectDto);
            setFormData(new ProjectService().getProjectFormData());
            return JSP_PROJECT_FORM;
        }

        ProjectService service = new ProjectService();
        int result = service.update(projectDto);

        if (result > 0) {
            return "redirect:Controller?page_id=P002&project_id=" + projectDto.getProjectId()
                    + "&msg=" + encode("案件を更新しました");
        }

        request.setAttribute("errMsg", "案件更新に失敗しました");
        request.setAttribute("mode", "update");
        request.setAttribute("project", projectDto);
        setFormData(service.getProjectFormData());

        return JSP_PROJECT_FORM;
    }

    /**
     * 案件ステータスを変更する
     * @return 遷移先
     */
    public String changeStatus() {

        int projectId = parseInt(getParam("project_id", "projectId"));
        String status = getParam("status");

        if (projectId <= 0 || !hasText(status)) {
            return REDIRECT_PROJECT_LIST + "&msg=" + encode("案件ステータスを変更できませんでした");
        }

        ProjectService service = new ProjectService();
        int result = service.changeStatus(projectId, status);

        if (result > 0) {
            return "redirect:Controller?page_id=P002&project_id=" + projectId
                    + "&msg=" + encode("案件ステータスを変更しました");
        }

        return "redirect:Controller?page_id=P002&project_id=" + projectId
                + "&msg=" + encode("案件ステータスの変更に失敗しました");
    }

    /**
     * 保存用DTOを作る
     * 登録と更新で共通利用する
     * @param includeId 更新時はtrue
     * @return 案件DTO
     */
    private ProjectsDTO createProjectDtoForSave(boolean includeId) {

        ProjectsDTO projectDto = new ProjectsDTO();

        if (includeId) {
            projectDto.setProjectId(parseInt(getParam("project_id", "projectId")));
        }

        projectDto.setProjectCode(getParam("project_code", "projectCode"));
        projectDto.setProjectName(getParam("project_name", "projectName"));
        projectDto.setCustomerName(getParam("customer_name", "customerName"));
        projectDto.setCreateMemberId(getLoginUserId());
        projectDto.setProjectManagerId(parseInt(getParam("project_manager_id", "projectManagerId")));
        projectDto.setStartDate(getParam("start_date", "startDate"));
        projectDto.setDueDate(getParam("due_date", "dueDate"));
        projectDto.setEstimatedManhours(parseFloat(getParam("estimated_manhours", "estimatedManhours")));
        projectDto.setDescription(getParam("description"));
        projectDto.setStatus(getParam("status"));
        projectDto.setPriority(getParam("priority"));

        return projectDto;
    }

    /**
     * 検索条件DTOを作る
     * ProjectSearchConditionは作らずProjectDTOで代用する
     * @return 検索条件DTO
     */
    private ProjectsDTO createSearchCondition() {

        ProjectsDTO condition = new ProjectsDTO();

        condition.setProjectName(getParam("keyword", "project_name", "projectName"));
        condition.setStatus(getParam("status"));
        condition.setPriority(getParam("priority"));
        condition.setProjectManagerId(parseInt(getParam("project_manager_id", "projectManagerId")));

        return condition;
    }

    /**
     * 登録前の入力チェックを行う
     * @param projectDto 案件DTO
     * @return エラーメッセージ
     */
    private String validateForRegist(ProjectsDTO projectDto) {

        if (!hasText(projectDto.getProjectCode())) {
            return "案件コードを入力してください";
        }

        if (!hasText(projectDto.getProjectName())) {
            return "案件名を入力してください";
        }

        if (projectDto.getEstimatedManhours() < 0) {
            return "予算工数は0以上で入力してください";
        }

        if (!hasText(projectDto.getStatus())) {
            return "ステータスを選択してください";
        }

        if (!hasText(projectDto.getPriority())) {
            return "優先度を選択してください";
        }

        return "";
    }

    /**
     * 更新前の入力チェックを行う
     * @param projectDto 案件DTO
     * @return エラーメッセージ
     */
    private String validateForUpdate(ProjectsDTO projectDto) {

        if (projectDto.getProjectId() <= 0) {
            return "案件IDが不正です";
        }

        if (!hasText(projectDto.getProjectName())) {
            return "案件名を入力してください";
        }

        if (projectDto.getEstimatedManhours() < 0) {
            return "予算工数は0以上で入力してください";
        }

        if (!hasText(projectDto.getStatus())) {
            return "ステータスを選択してください";
        }

        if (!hasText(projectDto.getPriority())) {
            return "優先度を選択してください";
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

        if (formDataList == null || formDataList.size() < 3) {
            return;
        }

        List<UserDTO> managerList = (List<UserDTO>) formDataList.get(0);
        List<String> statusList = (List<String>) formDataList.get(1);
        List<String> priorityList = (List<String>) formDataList.get(2);

        request.setAttribute("managerList", managerList);
        request.setAttribute("statusList", statusList);
        request.setAttribute("priorityList", priorityList);
    }

    /**
     * ログインユーザーIDを取得する
     * 未ログインまたは不正な場合は0を返す
     * @return ログインユーザーID
     */
    private int getLoginUserId() {

        HttpSession session = request.getSession(false);

        if (session == null) {
            return 0;
        }

        Object loginUser = session.getAttribute("loginUser");

        if (!(loginUser instanceof UserDTO)) {
            return 0;
        }

        return ((UserDTO) loginUser).getUserId();
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