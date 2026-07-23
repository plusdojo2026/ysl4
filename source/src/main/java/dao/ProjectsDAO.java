package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.ProjectsDTO;

public class ProjectsDAO {

	public Connection conn = null;

	//コネクションを保持するコンストラクタ
	public ProjectsDAO(Connection conn) {
		this.conn = conn;
	}

	//プロジェクト一覧を取得するメソッド
	public ArrayList<ProjectsDTO> selectAll() throws SQLException {

		ArrayList<ProjectsDTO> projectList = new ArrayList<ProjectsDTO>();

		// SELECT文を準備する
		String sql = "SELECT * FROM projects";

		// デバッグ（SQL文の確認用）
		System.out.println(sql);

		// まとめる
		PreparedStatement pStmt = conn.prepareStatement(sql);

		// SELECT文を実行し、結果表を取得する
		ResultSet rs = pStmt.executeQuery();

		// 移し替え
		while (rs.next()) {

			ProjectsDTO dto = new ProjectsDTO();
			dto.setProjectId(rs.getInt("project_id"));
			dto.setProjectName(rs.getString("project_name"));
			dto.setProjectCode(rs.getString("project_code"));
			dto.setProjectManagerId(rs.getInt("project_manager_id"));
			dto.setCustomerName(rs.getString("customer_name"));
			dto.setCreateMemberId(rs.getInt("create_member_id"));
			dto.setStartDate(rs.getString("start_date"));
			dto.setDueDate(rs.getString("due_date"));
			dto.setDescription(rs.getString("description"));
			dto.setStatus(rs.getString("status"));
			dto.setPriority(rs.getString("priority"));
			dto.setcAt(rs.getTimestamp("c_at"));
			dto.setuAt(rs.getTimestamp("u_at"));

			projectList.add(dto);
		}

		// serviceに返却する
		return projectList;
	}

	// 案件検索
	public List<ProjectsDTO> search(String condition)
			throws SQLException {

		List<ProjectsDTO> projectList = new ArrayList<ProjectsDTO>();

		// SELECT文を準備する
		String sql = "SELECT * FROM projects "
				+ "WHERE project_name LIKE ? "
				+ "OR project_code LIKE ? "
				+ "OR customer_name LIKE ?";

		// デバッグ
		System.out.println(sql);

		// SQLをまとめる
		PreparedStatement pStmt = conn.prepareStatement(sql);

		pStmt.setString(1, "%" + condition + "%");
		pStmt.setString(2, "%" + condition + "%");
		pStmt.setString(3, "%" + condition + "%");

		// SELECT実行
		ResultSet rs = pStmt.executeQuery();

		// DTOへ移し替え
		while (rs.next()) {

			ProjectsDTO dto = new ProjectsDTO();

			dto.setProjectId(rs.getInt("project_id"));
			dto.setProjectName(rs.getString("project_name"));
			dto.setProjectCode(rs.getString("project_code"));
			dto.setProjectManagerId(rs.getInt("project_manager_id"));
			dto.setCustomerName(rs.getString("customer_name"));
			dto.setCreateMemberId(rs.getInt("create_member_id"));
			dto.setStartDate(rs.getString("start_date"));
			dto.setDueDate(rs.getString("due_date"));
			dto.setDescription(rs.getString("description"));
			dto.setStatus(rs.getString("status"));
			dto.setPriority(rs.getString("priority"));
			dto.setcAt(rs.getTimestamp("c_at"));
			dto.setuAt(rs.getTimestamp("u_at"));

			projectList.add(dto);
		}

		return projectList;
	}

	//案件IDで１件取得
	public ProjectsDTO findById(int projectId) throws SQLException {

		ProjectsDTO dto = null;

		// SELECT文を準備する
		String sql = "SELECT * FROM projects WHERE project_id=?";

		// デバッグ（SQL文の確認用）
		System.out.println(sql);

		// まとめる
		PreparedStatement pStmt = conn.prepareStatement(sql);
		pStmt.setInt(1, projectId);

		// SELECT文を実行し、結果表を取得する
		ResultSet rs = pStmt.executeQuery();

		// 移し替え
		while (rs.next()) {

			dto = new ProjectsDTO();
			dto.setProjectId(rs.getInt("project_id"));
			dto.setProjectName(rs.getString("project_name"));
			dto.setProjectCode(rs.getString("project_code"));
			dto.setProjectManagerId(rs.getInt("project_manager_id"));
			dto.setCustomerName(rs.getString("customer_name"));
			dto.setCreateMemberId(rs.getInt("create_member_id"));
			dto.setStartDate(rs.getString("start_date"));
			dto.setDueDate(rs.getString("due_date"));
			dto.setDescription(rs.getString("description"));
			dto.setStatus(rs.getString("status"));
			dto.setPriority(rs.getString("priority"));
			dto.setcAt(rs.getTimestamp("c_at"));
			dto.setuAt(rs.getTimestamp("u_at"));

		}

		// serviceに返却する
		return dto;
	}

	//新規登録
	public int projectInsert(ProjectsDTO dto) throws SQLException {

		String sql = "INSERT INTO projects "
				+ "(project_code, project_name, customer_name, "
				+ "create_member_id, project_manager_id, "
				+ "start_date, due_date, description, "
				+ "estimated_manhours, actual_manhours, "
				+ "status, priority) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		PreparedStatement pStmt = conn.prepareStatement(sql);

		pStmt.setString(1, dto.getProjectCode());
		pStmt.setString(2, dto.getProjectName());
		pStmt.setString(3, dto.getCustomerName());
		pStmt.setInt(4, dto.getCreateMemberId());
		pStmt.setInt(5, dto.getProjectManagerId());
		pStmt.setString(6, dto.getStartDate());
		pStmt.setString(7, dto.getDueDate());
		pStmt.setString(8, dto.getDescription());
		pStmt.setString(9, dto.getStatus());
		pStmt.setString(10, dto.getPriority());

		return pStmt.executeUpdate();
	}

	// 案件更新
	public int update(ProjectsDTO dto) throws SQLException {

		String sql = "UPDATE projects "
				+ "SET project_code = ?, "
				+ "project_name = ?, "
				+ "customer_name = ?, "
				+ "project_manager_id = ?, "
				+ "start_date = ?, "
				+ "due_date = ?, "
				+ "description = ?, "
				+ "estimated_manhours = ?, "
				+ "actual_manhours = ?, "
				+ "status = ?, "
				+ "priority = ? "
				+ "WHERE project_id = ?";

		System.out.println(sql);

		PreparedStatement pStmt = conn.prepareStatement(sql);

		pStmt.setString(1, dto.getProjectCode());
		pStmt.setString(2, dto.getProjectName());
		pStmt.setString(3, dto.getCustomerName());
		pStmt.setInt(4, dto.getProjectManagerId());
		pStmt.setString(5, dto.getStartDate());
		pStmt.setString(6, dto.getDueDate());
		pStmt.setString(7, dto.getDescription());
		pStmt.setInt(8, dto.getEstimatedManhours());
		pStmt.setInt(9, dto.getActualManhours());
		pStmt.setString(10, dto.getStatus());
		pStmt.setString(11, dto.getPriority());
		pStmt.setInt(12, dto.getProjectId());

		int ans = pStmt.executeUpdate();

		return ans;
	}

	

	//優先度更新

	public int updateStatus(int projectId, String status) throws SQLException {

		// UPDATE文を準備する
		String sql = "UPDATE projects "
				+ "SET status = ? "
				+ "WHERE project_id = ?";

		System.out.println(sql);

		// SQLをまとめる
		PreparedStatement pStmt = conn.prepareStatement(sql);

		pStmt.setString(1, status);
		pStmt.setInt(2, projectId);

		// UPDATE実行
		int ans = pStmt.executeUpdate();

		return ans;
	}

	//案件コード重複チェック
	public boolean existsProjectCode(String projectCode) throws SQLException {

		boolean exists = false;

		String sql = "SELECT COUNT(*) "
				+ "FROM projects "
				+ "WHERE project_code = ?";

		System.out.println(sql);

		PreparedStatement pStmt = conn.prepareStatement(sql);

		pStmt.setString(1, projectCode);

		ResultSet rs = pStmt.executeQuery();

		if (rs.next()) {

			if (rs.getInt(1) > 0) {
				exists = true;
			}
		}
		return exists;

	}

	//進行中の案件の件数取得
	public int countInProgressProjects() throws SQLException {

		int count = 0;

		String sql = "SELECT COUNT(*) "
				+ "FROM projects "
				+ "WHERE status = '進行中'";

		PreparedStatement pStmt = conn.prepareStatement(sql);

		ResultSet rs = pStmt.executeQuery();

		if (rs.next()) {
			count = rs.getInt(1);
		}

		return count;
	}

	//進行中案件一覧取得
	public List<ProjectsDTO> selectInProgressProjects()
			throws SQLException {

		List<ProjectsDTO> projectList = new ArrayList<ProjectsDTO>();

		String sql = "SELECT * "
				+ "FROM projects "
				+ "WHERE status = '進行中'";

		PreparedStatement pStmt = conn.prepareStatement(sql);

		ResultSet rs = pStmt.executeQuery();

		while (rs.next()) {

			ProjectsDTO dto = new ProjectsDTO();

			dto.setProjectId(rs.getInt("project_id"));
			dto.setProjectName(rs.getString("project_name"));
			dto.setProjectCode(rs.getString("project_code"));
			dto.setProjectManagerId(rs.getInt("project_manager_id"));
			dto.setCustomerName(rs.getString("customer_name"));
			dto.setCreateMemberId(rs.getInt("create_member_id"));
			dto.setStartDate(rs.getString("start_date"));
			dto.setDueDate(rs.getString("due_date"));
			dto.setDescription(rs.getString("description"));
			dto.setStatus(rs.getString("status"));
			dto.setPriority(rs.getString("priority"));
			dto.setcAt(rs.getTimestamp("c_at"));
			dto.setuAt(rs.getTimestamp("u_at"));

			projectList.add(dto);
		}

		return projectList;
	}

	
	//箱だけ作ったやつ
	public Map<String, Object> getProjectFormData() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	
	public ProjectsDTO findDetail(int projectId) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}
}
