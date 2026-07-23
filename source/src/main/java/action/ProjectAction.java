package action;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import model.ProjectsDTO;
import service.ProjectService;

public class ProjectAction {
	HttpServletRequest request ;
	//コンストラクタ
	public ProjectAction(HttpServletRequest request) {
		this.request=request;
	}
	
	public String list() {
		
		String page = "/WEB-INF/jsp/projectList.jsp";
		
		ProjectService service = new ProjectService();
		
		List<ProjectsDTO> projectList = service.selectAll();
		
		request.setAttribute("projectList",projectList);
		
		return page;
		
	}
	     
	     public String showRegist() {
	    	 
	    	 String page = "WEB-INF/jsp/projectRegist.jsp";
	    	 
	    	 return page;
	     }
	     
	     public String regist()throws UnsupportedEncodingException {
	    	 
	    	 String page = "/WEB-INF/jsp/projectRegist.jsp";
	    	 
	    	 request.setCharacterEncoding("UTF-8");
	    	 
	    	 ProjectsDTO dto = new ProjectsDTO();
	    	 
	    	 
	    	 dto.setProjectCode(request.getParameter("project_code"));
	    	 
	    	 dto.setProjectName(request.getParameter("project_name"));
	    	 
	    	 dto.setCustomerName(request.getParameter("customer_name"));
	    	 
	    	 ProjectService service = new ProjectService();
	    	 
	    	 int ans = service.regist(dto);
	    	 
	    	 if(ans == 1) {
	    		 page = "ProjectServlet?action=list";
	    	 }
	    	 
	    	 return page;
	     }
	
	        public String detail() {
	        	
	        	String page = "/WEB-INF/jsp/projectDetail.jsp";
	        	
	        	int projectId = Integer.parseInt(
	        			
	        			request.getParameter("projectId"));
    	
	        	ProjectService service = new ProjectService();
	        	
	        	ProjectsDTO dto =service.findDetail(projectId);
	        	
	        request.setAttribute("dto",dto);
	        return page;
	        }
			
	        public String showUpdate() {
	        	
	        	String page = "/WEB-INF/jsp/projectEdit.jsp";
	        	
	        	int projectId = Integer.parseInt(
	        			request.getParameter("projectId"));
	        	
	        	ProjectService service = new ProjectService();

	        	ProjectsDTO dto = service.findById(projectId);
	        	
	        	request.setAttribute("dto",dto);
	        	return page;
	        	
	        }
	        
	        public String update()
	                throws UnsupportedEncodingException {

	            String page = "/WEB-INF/jsp/projectUpdate.jsp";

	            request.setCharacterEncoding("UTF-8");

	            ProjectsDTO dto = new ProjectsDTO();

	            dto.setProjectId(
	                    Integer.parseInt(
	                            request.getParameter("project_id")));

	            dto.setProjectCode(
	                    request.getParameter("project_code"));

	            dto.setProjectName(
	                    request.getParameter("project_name"));

	            ProjectService service =
	                    new ProjectService();

	            int ans = service.update(dto);

	            if(ans == 1) {
	            	page = "/ProjectServlet?action=detail"
	            	        + "&projectId="
	            	        + dto.getProjectId();
	            }

	            return page;
	        }
	        
	        public String changeStatus() {

	            String page = "/ProjectServlet?action=list";

	            int projectId =
	                    Integer.parseInt(
	                            request.getParameter("projectId"));

	            String status =
	                    request.getParameter("status");

	            ProjectService service =
	                    new ProjectService();

	            service.changeStatus(
	                    projectId,
	                    status);

	            return page;
	        }
}
	        