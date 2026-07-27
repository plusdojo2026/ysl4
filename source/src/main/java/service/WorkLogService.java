package service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import dao.TaskDAO;
import dao.WorkLogDAO;
import model.TaskDTO;
import model.WorkLogDTO;

/**
 * 工数ログ関連の業務処理を担当するService。
 * 工数チェック、DAO呼び出し、トランザクション制御を行う。
 */
public class WorkLogService extends DBAccess {

    /** 最小工数 */
    private static final float MIN_MAN_HOURS = 0.5f;

    /** 最大工数 */
    private static final float MAX_MAN_HOURS = 24.0f;

    /** 0.5刻み判定用 */
    private static final float HALF_HOUR_UNIT = 2.0f;

    /**
     * 工数入力画面に必要なタスク情報を取得する。
     *
     * @param taskId タスクID
     * @return タスクDTO
     */
    public TaskDTO getWorkLogFormData(int taskId) {

        TaskDTO taskDto = null;

        if (taskId <= 0) {
            return null;
        }

        try {
            access();

            TaskDAO taskDao = new TaskDAO(conn);
            taskDto = taskDao.findById(taskId);

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
     * 指定タスクの工数ログを取得する。
     *
     * @param taskId タスクID
     * @return 工数ログ一覧
     */
    public List<WorkLogDTO> selectByTaskId(int taskId) {

        List<WorkLogDTO> workLogList = new ArrayList<>();

        if (taskId <= 0) {
            return workLogList;
        }

        try {
            access();

            WorkLogDAO workLogDao = new WorkLogDAO(conn);
            workLogList = workLogDao.selectByTaskId(taskId);

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
     * 指定案件の最新工数ログを取得する。
     *
     * @param projectId 案件ID
     * @return 最新工数ログ一覧
     */
    public List<WorkLogDTO> selectLatestByProjectId(int projectId) {

        List<WorkLogDTO> workLogList = new ArrayList<>();

        if (projectId <= 0) {
            return workLogList;
        }

        try {
            access();

            WorkLogDAO workLogDao = new WorkLogDAO(conn);
            workLogList = workLogDao.selectLatestByProjectId(projectId);

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
     * 工数ログを登録する。
     *
     * @param workLogDto 工数ログDTO
     * @return 登録件数
     */
    public int regist(WorkLogDTO workLogDto) {

        int result = 0;

        if (workLogDto == null) {
            return 0;
        }

        if (workLogDto.getTaskId() <= 0) {
            return 0;
        }

        if (workLogDto.getUserId() <= 0) {
            return 0;
        }

        if (!hasText(workLogDto.getWorkDate())) {
            return 0;
        }

        if (!isValidDate(workLogDto.getWorkDate())) {
            return 0;
        }

        if (!validateManHours(workLogDto.getManHours())) {
            return 0;
        }

        try {
            access();

            WorkLogDAO workLogDao = new WorkLogDAO(conn);
            result = workLogDao.workLogInsert(workLogDto);

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
     * 工数ログを削除する。
     *
     * @param workLogId 工数ログID
     * @return 削除件数
     */
    public int delete(int workLogId) {

        int result = 0;

        if (workLogId <= 0) {
            return 0;
        }

        try {
            access();

            WorkLogDAO workLogDao = new WorkLogDAO(conn);
            result = workLogDao.workLogDelete(workLogId);

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
     * 指定タスクの工数合計を取得する。
     *
     * @param taskId タスクID
     * @return 工数合計
     */
    public Float sumByTaskId(int taskId) {

        Float totalManHours = 0f;

        if (taskId <= 0) {
            return totalManHours;
        }

        try {
            access();

            WorkLogDAO workLogDao = new WorkLogDAO(conn);
            totalManHours = workLogDao.sumByTaskId(taskId);

            commit();

        } catch (SQLException e) {
            rollback();
            e.printStackTrace();

        } finally {
            close();
        }

        return totalManHours;
    }

    /**
     * 指定案件の工数合計を取得する。
     *
     * @param projectId 案件ID
     * @return 工数合計
     */
    public Float sumByProjectId(int projectId) {

        Float totalManHours = 0f;

        if (projectId <= 0) {
            return totalManHours;
        }

        try {
            access();

            WorkLogDAO workLogDao = new WorkLogDAO(conn);
            totalManHours = workLogDao.sumByProjectId(projectId);

            commit();

        } catch (SQLException e) {
            rollback();
            e.printStackTrace();

        } finally {
            close();
        }

        return totalManHours;
    }

    /**
     * 工数が0.5から24かつ0.5刻みか確認する。
     *
     * @param manHours 工数
     * @return 正常ならtrue
     */
    private boolean validateManHours(Float manHours) {

        if (manHours == null) {
            return false;
        }

        if (manHours < MIN_MAN_HOURS || manHours > MAX_MAN_HOURS) {
            return false;
        }

        return manHours * HALF_HOUR_UNIT
                == Math.floor(manHours * HALF_HOUR_UNIT);
    }

    /**
     * yyyy-MM-dd形式の日付か確認する。
     *
     * @param value 日付文字列
     * @return 正常ならtrue
     */
    private boolean isValidDate(String value) {

        try {
            LocalDate.parse(value);
            return true;

        } catch (RuntimeException e) {
            return false;
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
}