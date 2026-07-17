<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
</head>
<body>
	<div class="header">
		Taskmanager　
		<p><a href="/webappAns/RegistServlet">ホーム</a></p>
		<p><a href="/webappAns/SearchServlet">案件一覧</a></p>
		<p><a href="/webappAns/LogoutServlet">タスク管理</a></p>
		<p><a href="/webappAns/LogoutServlet">月次集計</a></p>
		<p><a href="/webappAns/LogoutServlet">タスク管理</a></p>
		<p><a href="/webappAns/LogoutServlet">マイページ</a></p>
		  <form method="POST" action="<c:url value='/Controller'/>"  id="logout" onclick="return logout()">
			<input type="hidden" name="page_id" value="none">
			<input type="submit" name="button_id" value="ログアウト" >	
		  </form>	
		</c:if>
		
	</div>
	<script>
	//ログアウトボタンが押されたときの処理
		function logout(){
			if (confirm("本当にログアウトしますか？")) {
			  // OK（はい）を押したときの処理
			  return true;
			} else {
			  return false;
			}
		}
	</script>
</body>
</html>