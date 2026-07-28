package service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dao.ProjectsDAO;
import dao.TaskDAO;
import dao.UserDAO;
import dao.WorkLogDAO;
import model.ProjectsDTO;
import model.TaskDTO;
import model.UserDTO;
import model.WorkLogDTO;

/**
* 案件管理の業務処理を担当するService.
* 入力チェック、DAO呼び出し、トランザクション制御を行う.
*/
public class ProjectService extends DBAccess {

    /** 進行中 */
    private static final String STATUS_IN_PROGRESS = "進行中";

    /** 完了 */
    private static final String STATUS_COMPLETED = "完了";

    /** 中止 */
    private static final String STATUS_CANCELED = "中止";

    /** 高 */
    private static final String PRIORITY_HIGH = "高";

    /** 中 */
    private static final String PRIORITY_MIDDLE = "中";

    /** 低 */
    private static final String PRIORITY_LOW = "低";

    /**
    * 案件を全件取得する.
    * @return 案件一覧.
    */
    
    public List<ProjectsDTO> selectAll() {

        List<ProjectsDTO> projectList = new ArrayList<>();

        try {
            access();

            ProjectsDAO projectsDao = new ProjectsDAO(conn);
            projectList = projectsDao.selectAll();

            commit();

        } catch (SQLException e) {
            rollback();
            e.printStackTrace();

        } finally {
            close();
        }

        return projectList;
    }

    /**
    * 検索条件に合う案件を取得する.
    * ProjectSearchConditionは作らずProjectDTOを条件として使う.
    *
    * @param condition 検索条件DTO.
    * @return 案件一覧.
    */
    public List<ProjectsDTO> search(ProjectsDTO condition) {

        List<ProjectsDTO> projectList = new ArrayList<>();

        try {
            access();

            ProjectsDAO projectsDao = new ProjectsDAO(conn);
            projectList = projectsDao.search(condition);

            commit();

        } catch (SQLException e) {
            rollback();
            e.printStackTrace();

        } finally {
            close();
        }

        return projectList;
    }

    /**
    * 案件IDで1件取得する.
    *
    * @param projectId 案件ID.
    * @return 案件DTO.
    */
    public ProjectsDTO findById(int projectId) {

        ProjectsDTO projectDto = null;

        if (projectId <= 0) {
            return null;
        }

        try {
            access();

            ProjectsDAO projectsDao = new ProjectsDAO(conn);
            projectDto = projectsDao.findById(projectId);

            commit();

        } catch (SQLException e) {
            rollback();
            e.printStackTrace();

        } finally {
            close();
        }

        return projectDto;
    }

    /**
    * 案件詳細情報を取得する.
    * 案件情報、タスク一覧、最新工数ログをまとめる.
    *
    * @param projectId 案件ID.
    * @return 案件詳細DTO.
    */
    
    public ProjectsDTO findDetail(int projectId) {

        ProjectsDTO projectDto = null;

        if (projectId <= 0) {
            return null;
        }

        try {
            access();

            ProjectsDAO projectsDao = new ProjectsDAO(conn);
            TaskDAO taskDao = new TaskDAO(conn);
            WorkLogDAO workLogDao = new WorkLogDAO(conn);

            projectDto = projectsDao.findById(projectId);

            if (projectDto != null) {

                List<TaskDTO> taskList = taskDao.selectByProjectId(projectId);
                List<WorkLogDTO> latestWorkLogList = workLogDao.selectLatestByProjectId(projectId);

                int taskCount =taskDao.countAllByProjectId(projectId);
                int completedTaskCount = taskDao.countCompletedByProjectId(projectId);

                float progressRate = 0;

                if (taskCount > 0) {
                    progressRate = ((float) completedTaskCount / taskCount) * 100;
                }

                projectDto.setTaskList(taskList);
                projectDto.setLatestWorkLogList(latestWorkLogList);

                projectDto.setTaskCount(taskCount);
                projectDto.setCompletedTaskCount(completedTaskCount);
                projectDto.setProgressRate(progressRate);
            }

            commit();

        } catch (SQLException e) {
            rollback();
            e.printStackTrace();

        } finally {
            close();
        }

        return projectDto;
    }

    /**
    * 案件を登録する.
    * 案件コード重複確認後に登録する.
    *
    * @param projectDto 登録する案件DTO.
    * @return 登録件数.
    */
    
    public int regist(ProjectsDTO projectDto) {

        int result = 0;

        if (!isValidForRegist(projectDto)) {
            return 0;
        }

        normalizeDefaultValues(projectDto);

        try {
            access();

            ProjectsDAO projectsDao = new ProjectsDAO(conn);

            if (projectsDao.existsProjectCode(projectDto.getProjectCode())) {
                rollback();
                return 0;
            }

            result = projectsDao.projectInsert(projectDto);

            commit();

        } catch (SQLException e) {
            rollback();
            e.printStackTrace();

        } finally {
            close();
        }

        return result;
    }

    /**
    * 案件を更新する.
    * 案件コードは更新しない.
    *
    * @param projectDto 更新する案件DTO.
    * @return 更新件数.
    */
    
    public int update(ProjectsDTO projectDto) {

        int result = 0;

        if (!isValidForUpdate(projectDto)) {
            return 0;
        }

        normalizeDefaultValues(projectDto);

        try {
            access();

            ProjectsDAO projectsDao = new ProjectsDAO(conn);
            result = projectsDao.projectUpdate(projectDto);

            commit();

        } catch (SQLException e) {
            rollback();
            e.printStackTrace();

        } finally {
            close();
        }

        return result;
    }

    /**
    * 案件ステータスを変更する.
    *
    * @param projectId 案件ID.
    * @param status ステータス.
    * @return 更新件数.
    */
    
    public int changeStatus(int projectId, String status) {

        int result = 0;

        if (projectId <= 0 || !isValidStatus(status)) {
            return 0;
        }

        try {
            access();

            ProjectsDAO projectsDao = new ProjectsDAO(conn);
            result = projectsDao.updateStatus(projectId, status);

            commit();

        } catch (SQLException e) {
            rollback();
            e.printStackTrace();

        } finally {
            close();
        }

        return result;
    }

    /**
    * 案件コードが存在するか確認する.
    *
    * @param projectCode 案件コード.
    * @return 存在する場合true.
    */
    
    public boolean existsProjectCode(String projectCode) {

        boolean exists = false;

        if (!hasText(projectCode)) {
            return false;
        }

        try {
            access();

            ProjectsDAO projectsDao = new ProjectsDAO(conn);
            exists = projectsDao.existsProjectCode(projectCode);

            commit();

        } catch (SQLException e) {
            rollback();
            e.printStackTrace();

        } finally {
            close();
        }

        return exists;
    }

    /**
    * 案件登録、編集画面で使う選択肢を取得する.
    * Mapは使わずListの固定順で返す.
    * index0 PM候補一覧.
    * index1 ステータス一覧.
    * index2 優先度一覧.
    *
    * @return 画面表示用データList.
    */
    
    public List<Object> getProjectFormData() {

        List<Object> formDataList = new ArrayList<>();

        try {
            access();

            UserDAO userDao = new UserDAO(conn);

            List<UserDTO> managerList = userDao.selectValidUsers();
            List<String> statusList = Arrays.asList(STATUS_IN_PROGRESS, STATUS_COMPLETED, STATUS_CANCELED);
            List<String> priorityList = Arrays.asList(PRIORITY_HIGH, PRIORITY_MIDDLE, PRIORITY_LOW);

            formDataList.add(managerList);
            formDataList.add(statusList);
            formDataList.add(priorityList);

            commit();

        } catch (SQLException e) {
            rollback();
            e.printStackTrace();

            formDataList.clear();
            formDataList.add(new ArrayList<UserDTO>());
            formDataList.add(Arrays.asList(STATUS_IN_PROGRESS, STATUS_COMPLETED, STATUS_CANCELED));
            formDataList.add(Arrays.asList(PRIORITY_HIGH, PRIORITY_MIDDLE, PRIORITY_LOW));

        } finally {
            close();
        }

        return formDataList;
    }

    /**
    * 進行中案件数を取得する.
    * ダッシュボードで使う.
    *
    * @return 進行中案件数.
    */
    
    public int countInProgressProjects() {

        int count = 0;

        try {
            access();

            ProjectsDAO projectsDao = new ProjectsDAO(conn);
            count = projectsDao.countInProgressProjects();

            commit();

        } catch (SQLException e) {
            rollback();
            e.printStackTrace();

        } finally {
            close();
        }

        return count;
    }

    /**
    * 進行中案件一覧を取得する.
    * ダッシュボードで使う.
    *
    * @return 進行中案件一覧.
    */
    
    public List<ProjectsDTO> selectInProgressProjects() {

        List<ProjectsDTO> projectList = new ArrayList<>();

        try {
            access();

            ProjectsDAO projectsDao = new ProjectsDAO(conn);
            projectList = projectsDao.selectInProgressProjects();

            commit();

        } catch (SQLException e) {
            rollback();
            e.printStackTrace();

        } finally {
            close();
        }

        return projectList;
    }

    /**
    * 登録時の最低限チェックを行う.
    *
    * @param projectDto 案件DTO.
    * @return 登録可能ならtrue.
    */
    
    private boolean isValidForRegist(ProjectsDTO projectDto) {

        if (projectDto == null) {
            return false;
        }

        if (!hasText(projectDto.getProjectCode())) {
            return false;
        }

        if (!hasText(projectDto.getProjectName())) {
            return false;
        }

        if (projectDto.getEstimatedManhours() < 0) {
            return false;
        }

        if (hasText(projectDto.getStatus()) && !isValidStatus(projectDto.getStatus())) {
            return false;
        }

        if (hasText(projectDto.getPriority()) && !isValidPriority(projectDto.getPriority())) {
            return false;
        }

        return true;
    }

    /**
    * 更新時の最低限チェックを行う.
    *
    * @param projectDto 案件DTO.
    * @return 更新可能ならtrue.
    */
    
    private boolean isValidForUpdate(ProjectsDTO projectDto) {

        if (projectDto == null) {
            return false;
        }

        if (projectDto.getProjectId() <= 0) {
            return false;
        }

        if (!hasText(projectDto.getProjectName())) {
            return false;
        }

        if (projectDto.getEstimatedManhours() < 0) {
            return false;
        }

        if (hasText(projectDto.getStatus()) && !isValidStatus(projectDto.getStatus())) {
            return false;
        }

        if (hasText(projectDto.getPriority()) && !isValidPriority(projectDto.getPriority())) {
            return false;
        }

        return true;
    }

    /**
    * ステータスと優先度の初期値を補完する.
    *
    * @param projectDto 案件DTO.
    */
    
    private void normalizeDefaultValues(ProjectsDTO projectDto) {

        if (!hasText(projectDto.getStatus())) {
            projectDto.setStatus(STATUS_IN_PROGRESS);
        }

        if (!hasText(projectDto.getPriority())) {
            projectDto.setPriority(PRIORITY_MIDDLE);
        }
    }

    /**
    * ステータスが正常か確認する.
    *
    * @param status ステータス.
    * @return 正常ならtrue.
    */
    
    private boolean isValidStatus(String status) {

        if (STATUS_IN_PROGRESS.equals(status)) {
            return true;
        }

        if (STATUS_COMPLETED.equals(status)) {
            return true;
        }

        if (STATUS_CANCELED.equals(status)) {
            return true;
        }

        return false;
    }

    /**
    * 優先度が正常か確認する.
    *
    * @param priority 優先度.
    * @return 正常ならtrue.
    */
    
    private boolean isValidPriority(String priority) {

        if (PRIORITY_HIGH.equals(priority)) {
            return true;
        }

        if (PRIORITY_MIDDLE.equals(priority)) {
            return true;
        }

        if (PRIORITY_LOW.equals(priority)) {
            return true;
        }

        return false;
    }

    /**
    * 文字列が入力されているか確認する.
    *
    * @param value 確認する文字列.
    * @return 入力ありならtrue.
    */
    
    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }
}