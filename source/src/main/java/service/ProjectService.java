package service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import dao.ProjectsDAO;
import model.ProjectsDTO;

public class ProjectService extends DBAccess {

	public ProjectService() {
		try {
			super.access();
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	//案件一覧取得
	public List<ProjectsDTO> selectAll() {

		List<ProjectsDTO> projectList = null;

		ProjectsDAO dao = new ProjectsDAO(super.conn);

		try {
			projectList = dao.selectAll();
		} catch (SQLException e) {
			e.printStackTrace();
			}

		return projectList;
				

	}

	// 案件検索
	public List<ProjectsDTO> search(String condition) {
	
	List<ProjectsDTO> projectList = null;
	ProjectsDAO dao = new ProjectsDAO(super.conn);
	
	try {
	projectList = dao.search(condition);
	} catch (SQLException e) {
	e.printStackTrace();
	}
	
	super.close();
	
	return projectList;
	
	}
	
	// 案件ID検索
	
	public ProjectsDTO findById(int projectId) {
	ProjectsDTO dto = null;
	
	ProjectsDAO dao = new ProjectsDAO(super.conn);
	
	try {
	
	dto = dao.findById(projectId);
	} catch (SQLException e) {
	e.printStackTrace();
	}

	super.close();
	return dto;
	
	}
	
	// 案件登録
	
	public int regist(ProjectsDTO dto) {
	int ans = 0;
	
	ProjectsDAO dao = new ProjectsDAO(super.conn);
	
	try {
	ans = dao.projectInsert(dto);
	} catch (SQLException e) {
	e.printStackTrace();
	}
	super.close();
	return ans;
	
	}
	
	// 案件更新
	
	public int update(ProjectsDTO dto) {
	
	int ans = 0;
	
	ProjectsDAO dao = new ProjectsDAO(super.conn);
	
	try {
	ans = dao.update(dto);
	} catch (SQLException e) {
	e.printStackTrace();
	}

	super.close();
	return ans;
	
	}
	
	// ステータス変更
	
	public int changeStatus(int projectId, String status) {

	int ans = 0;

	ProjectsDAO dao = new ProjectsDAO(super.conn);

	try {
	ans = dao.updateStatus(projectId, status);
	} catch (SQLException e) {
	e.printStackTrace();
	}
	super.close();
	return ans;
	
	}
	
	// 案件コード重複チェック
	public boolean existsProjectCode(String projectCode) {
	boolean exists = false;
	
	ProjectsDAO dao = new ProjectsDAO(super.conn);

	try {
	exists = dao.existsProjectCode(projectCode);
	} catch (SQLException e) {	
	e.printStackTrace();	
	}

	super.close();
	return exists;
	}
 
	 //詳細情報取得
	public ProjectsDTO findDetail(int projectId)  {
		
		ProjectsDTO dto = null;		
		ProjectsDAO dao = new ProjectsDAO(super.conn);
		
		dto = dao.findDetail(projectId);
		
		super.close();		
		return dto;
	}
	
	//プルダウンなどフォーム表示に必要なデータを取得する
	public Map<String,Object> getProjectFormData() throws SQLException {
		
		Map<String,Object> formData = null;
		
		ProjectsDAO dao = new ProjectsDAO(super.conn);
		
		formData = dao.getProjectFormData();
		
		super.close();
		
	return formData;
	}
}