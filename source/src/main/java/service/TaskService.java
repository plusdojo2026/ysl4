package service;

import java.sql.SQLException;
import java.util.ArrayList;
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
 * タスク関連の業務処理を担当するService
 * 入力チェック、状態補正、DAO呼び出し、トランザクション制御を行う
 */
public class TaskService extends DBAccess {

    /** 未着手 */
    private static final String STATUS_NOT_STARTED = "未着手";

    /** 進行中 */
    private static final String STATUS_IN_PROGRESS = "進行中";

    /** 完了 */
    private static final String STATUS_COMPLETED = "完了";

    /** 保留 */
    private static final String STATUS_PENDING = "保留";

    /**
     * タスクを全件取得する
     * @return タスク一覧
     */
    public List<TaskDTO> selectAll() {

        List<TaskDTO> taskList = new ArrayList<>();

        try {
            access();

            TaskDAO taskDao = new TaskDAO(conn);
            taskList = taskDao.selectAll();

            commit();

        } catch (SQLException e) {
            rollback();
            e.printStackTrace();

        } finally {
            close();
        }

        return taskList;
    }

    /**
     * 案件IDに紐づくタスクを取得する
     * @param projectId 案件ID
     * @return タスク一覧
     */
    public List<TaskDTO> selectByProjectId(int projectId) {

        List<TaskDTO> taskList = new ArrayList<>();

        if (projectId <= 0) {
            return taskList;
        }

        try {
            access();

            TaskDAO taskDao = new TaskDAO(conn);
            taskList = taskDao.selectByProjectId(projectId);

            commit();

        } catch (SQLException e) {
            rollback();
            e.printStackTrace();

        } finally {
            close();
        }

        return taskList;
    }

    /**
     * 検索条件に合うタスクを取得する
     * @param condition 検索条件DTO
     * @return タスク一覧
     */
    public List<TaskDTO> search(TaskDTO condition) {

        List<TaskDTO> taskList = new ArrayList<>();

        try {
            access();

            TaskDAO taskDao = new TaskDAO(conn);
            taskList = taskDao.search(condition);

            commit();

        } catch (SQLException e) {
            rollback();
            e.printStackTrace();

        } finally {
            close();
        }

        return taskList;
    }

    /**
     * タスクIDで1件取得する
     * 工数ログ一覧もDTOへ設定する
     * @param taskId タスクID
     * @return タスクDTO
     */
    public TaskDTO findById(int taskId) {

        TaskDTO taskDto = null;

        if (taskId <= 0) {
            return null;
        }

        try {
            access();

            TaskDAO taskDao = new TaskDAO(conn);
            WorkLogDAO workLogDao = new WorkLogDAO(conn);

            taskDto = taskDao.findById(taskId);

            if (taskDto != null) {
                List<WorkLogDTO> workLogList = workLogDao.selectByTaskId(taskId);
                taskDto.setWorkLogList(workLogList);
            }

            commit();

        } catch (SQLException e) {
            rollback();
            e.printStackTrace();

        } finally {
            close();
        }

        return taskDto;
    }

    /**
     * タスクを登録する
     * @param taskDto 登録するタスクDTO
     * @return 登録件数
     */
    public int regist(TaskDTO taskDto) {

        int result = 0;

        if (!isValidForSave(taskDto, false)) {
            return 0;
        }

        adjustStatusAndProgress(taskDto);

        try {
            access();

            TaskDAO taskDao = new TaskDAO(conn);
            result = taskDao.insert(taskDto);

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
     * タスクを更新する
     * @param taskDto 更新するタスクDTO
     * @return 更新件数
     */
    public int update(TaskDTO taskDto) {

        int result = 0;

        if (!isValidForSave(taskDto, true)) {
            return 0;
        }

        adjustStatusAndProgress(taskDto);

        try {
            access();

            TaskDAO taskDao = new TaskDAO(conn);
            result = taskDao.update(taskDto);

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
     * タスク状態を変更する
     * 完了なら100、未着手なら0へ補正する
     * @param taskId タスクID
     * @param status ステータス
     * @return 更新件数
     */
    public int changeStatus(int taskId, String status) {

        int result = 0;

        if (taskId <= 0 || !isValidStatus(status)) {
            return 0;
        }

        int progress = decideProgressByStatus(status);

        try {
            access();

            TaskDAO taskDao = new TaskDAO(conn);
            result = taskDao.changeStatus(taskId, status, progress);

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
     * タスクを削除する
     * 関連工数ログを先に削除する
     * @param taskId タスクID
     * @return 削除件数
     */
    public int delete(int taskId) {

        int result = 0;

        if (taskId <= 0) {
            return 0;
        }

        try {
            access();

            WorkLogDAO workLogDao = new WorkLogDAO(conn);
            TaskDAO taskDao = new TaskDAO(conn);

            workLogDao.deleteByTaskId(taskId);
            result = taskDao.delete(taskId);

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
     * タスク登録と編集画面に必要な情報を取得する
     * Mapは使わずListの固定順で返す
     * index0 案件一覧
     * index1 有効ユーザー一覧
     * index2 選択中案件
     * index3 選択中案件ID
     * @param projectId 選択中案件ID
     * @return 画面表示用データList
     */
    public List<Object> getTaskFormData(int projectId) {

        List<Object> formDataList = new ArrayList<>();

        try {
            access();

            ProjectsDAO projectsDao = new ProjectsDAO(conn);
            UserDAO userDao = new UserDAO(conn);

            List<ProjectsDTO> projectList = projectsDao.selectInProgressProjects();
            List<UserDTO> userList = userDao.selectValidUsers();
            ProjectsDTO selectedProject = null;

            if (projectId > 0) {
                selectedProject = projectsDao.findById(projectId);
            }

            formDataList.add(projectList);
            formDataList.add(userList);
            formDataList.add(selectedProject);
            formDataList.add(projectId);

            commit();

        } catch (SQLException e) {
            rollback();
            e.printStackTrace();

            formDataList.clear();
            formDataList.add(new ArrayList<ProjectsDTO>());
            formDataList.add(new ArrayList<UserDTO>());
            formDataList.add(null);
            formDataList.add(projectId);

        } finally {
            close();
        }

        return formDataList;
    }

    /**
     * 保存前の最低限チェックを行う
     * @param taskDto タスクDTO
     * @param requireId 更新時はtrue
     * @return 保存可能ならtrue
     */
    private boolean isValidForSave(TaskDTO taskDto, boolean requireId) {

        if (taskDto == null) {
            return false;
        }

        if (requireId && taskDto.getTaskId() <= 0) {
            return false;
        }

        if (!hasText(taskDto.getTaskName())) {
            return false;
        }

        if (taskDto.getProjectId() <= 0) {
            return false;
        }

        if (taskDto.getEstimatedManhours() < 0) {
            return false;
        }

        if (!isValidStatus(taskDto.getStatus())) {
            return false;
        }

        if (!hasText(taskDto.getPriority())) {
            return false;
        }

        if (taskDto.getProgress() < 0 || taskDto.getProgress() > 100) {
            return false;
        }

        return true;
    }

    /**
     * ステータスと進捗率の整合を取る
     * @param taskDto タスクDTO
     */
    private void adjustStatusAndProgress(TaskDTO taskDto) {

        if (STATUS_COMPLETED.equals(taskDto.getStatus())) {
            taskDto.setProgress(100);
            return;
        }

        if (STATUS_NOT_STARTED.equals(taskDto.getStatus())) {
            taskDto.setProgress(0);
        }
    }

    /**
     * ステータスから進捗率を決める
     * @param status ステータス
     * @return 進捗率
     */
    private int decideProgressByStatus(String status) {

        if (STATUS_COMPLETED.equals(status)) {
            return 100;
        }

        if (STATUS_NOT_STARTED.equals(status)) {
            return 0;
        }

        if (STATUS_IN_PROGRESS.equals(status)) {
            return 50;
        }

        if (STATUS_PENDING.equals(status)) {
            return 50;
        }

        return 0;
    }

    /**
     * ステータスが正常か確認する
     * @param status ステータス
     * @return 正常ならtrue
     */
    private boolean isValidStatus(String status) {

        if (STATUS_NOT_STARTED.equals(status)) {
            return true;
        }

        if (STATUS_IN_PROGRESS.equals(status)) {
            return true;
        }

        if (STATUS_COMPLETED.equals(status)) {
            return true;
        }

        if (STATUS_PENDING.equals(status)) {
            return true;
        }

        return false;
    }

    /**
     * 文字列が入力されているか確認する
     * @param value 確認する文字列
     * @return 入力ありならtrue
     */
    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }
}