package Service;

import java.sql.SQLException;

import DAO.ProjectsDAO;
import Model.ProjectsDTO;

public class ProjectService extends DBAccess {
	
	public ProjectService() {
		super.access();
	}
	
	//案件を登録するメソッド
	public int projectInsert(ProjectsDTO dto) {
		
		ProjectsDAO dao = new ProjectsDAO(conn);
		int ans = 0;
		
		try {
			ans = dao.projectInsert(dto);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return ans;
		}
		
	//案件参照
    public ProjectsDTO findById(int projectId) {

    ProjectsDTO dto = null;
    ProjectsDAO dao = new ProjectsDAO(super.conn);

    try {
        dto = dao.findById(projectId);
    } catch (SQLException e) {

        System.out.println("SQL文おかしいよ");
        e.printStackTrace();
    }

    super.close();
    return dto;
}
  //更新

 // 案件ステータスを更新するメソッド---------------------------------------
 public int updateStatus(int projectId, String status) {

     ProjectsDAO dao = new ProjectsDAO(super.conn);

     int ans = 0;

     try {

         ans = dao.updateStatus(projectId, status);

     } catch (SQLException e) {

         System.out.println("SQL文おかしいよ");
         e.printStackTrace();

     }

     super.close();

     return ans;
 }

		
	}
