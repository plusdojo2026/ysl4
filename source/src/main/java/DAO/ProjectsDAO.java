package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Model.ProjectsDTO;

public class ProjectsDAO {
	
	public Connection conn = null;
	
	//コネクションを保持するコンストラクタ
		public ProjectsDAO(Connection conn) {
			this.conn=conn;
		}
		

              //プロジェクト一覧を取得するメソッド
             public ArrayList<ProjectsDTO> selectAll() throws SQLException {

            ArrayList<ProjectsDTO> projectList =
            new ArrayList<ProjectsDTO>();

               // SELECT文を準備する
               String sql = "SELECT * FROM projects";

               // デバッグ（SQL文の確認用）
               System.out.println(sql);

               // まとめる
             PreparedStatement pStmt = conn.prepareStatement(sql);

             // SELECT文を実行し、結果表を取得する
             ResultSet rs = pStmt.executeQuery();

           // 移し替え
              while(rs.next()) {

               ProjectsDTO dto = new ProjectsDTO();
                dto.setProjectId(rs.getInt("project_id"));
                dto.setProjectName(rs.getString("project_name"));
                dto.setProjectCode(rs.getString("project_code"));
                dto.setProjectManagerId(rs.getInt("projct_manager_id"));
                dto.setCustomerName(rs.getString("customer_name"));
                dto.setCreateMemberId(rs.getInt("create_member_id"));
                dto.setStartDate(rs.getDate("start_date"));
                dto.setDueDate(rs.getDate("due_date"));
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
             
             public ProjectsDTO findById(int projectId) throws SQLException {

                 ProjectsDTO dto = null;

                    // SELECT文を準備する
                    String sql = "SELECT * FROM projects WHERE project_id=?";

                    // デバッグ（SQL文の確認用）
                    System.out.println(sql);

                    // まとめる
                  PreparedStatement pStmt = conn.prepareStatement(sql);
                  pStmt.setInt(1,projectId);

                  // SELECT文を実行し、結果表を取得する
                  ResultSet rs = pStmt.executeQuery();

                // 移し替え
                   while(rs.next()) {

                     dto = new ProjectsDTO();
                     dto.setProjectId(rs.getInt("project_id"));
                     dto.setProjectName(rs.getString("project_name"));
                     dto.setProjectCode(rs.getString("project_code"));
                     dto.setProjectManagerId(rs.getInt("projct_manager_id"));
                     dto.setCustomerName(rs.getString("customer_name"));
                     dto.setCreateMemberId(rs.getInt("create_member_id"));
                     dto.setStartDate(rs.getDate("start_date"));
                     dto.setDueDate(rs.getDate("due_date"));
                     dto.setDescription(rs.getString("description"));
                     dto.setStatus(rs.getString("status"));
                     dto.setPriority(rs.getString("priority"));
                     dto.setcAt(rs.getTimestamp("c_at"));
                     dto.setuAt(rs.getTimestamp("u_at"));
                     
                       
         }

                    // serviceに返却する
                      return dto;
                  }
     }


