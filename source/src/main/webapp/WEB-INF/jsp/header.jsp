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
		<form method="POST" action="<c:url value='/Controller'/>"  id="logout" onclick="return logout()">
		<%-- HH = header home--%>>
			<input type="hidden" name="page_id" value="HH">
			<input type="submit" name="button_id" value="ホーム" >	
		  </form>	
		  
		  <form method="POST" action="<c:url value='/Controller'/>"  id="logout" onclick="return logout()">
		  <%-- HP = header project --%>>
			<input type="hidden" name="page_id" value="HP">
			<input type="submit" name="button_id" value="案件一覧" >	
		  </form>	
		  
		  <form method="POST" action="<c:url value='/Controller'/>"  id="logout" onclick="return logout()">
		  <%-- HT = header task --%>>
			<input type="hidden" name="page_id" value="HT">
			<input type="submit" name="button_id" value="タスク管理" >	
		  </form>	
		  
		  <form method="POST" action="<c:url value='/Controller'/>"  id="logout" onclick="return logout()">
		  <%-- HM = header month --%>>
			<input type="hidden" name="page_id" value="HM">
			<input type="submit" name="button_id" value="月次集計" >	
		  </form>	
		  
		  <form method="POST" action="<c:url value='/Controller'/>"  id="logout" onclick="return logout()">
			<input type="hidden" name="page_id" value="HMM">
			<%-- HMM = header member management --%>>
			<input type="submit" name="button_id" value="メンバー管理" >	
		  </form>	
		  
		  マイページ
		  
		  <form method="POST" action="<c:url value='/Controller'/>"  id="logout" onclick="return logout()">
			<input type="hidden" name="page_id" value="HL">
			<%-- HP = header logout --%>>
			<input type="submit" name="button_id" value="ログアウト" >	
		  </form>	
		</c:if>
		
	</div>
	<%-- ><nav class="nav">
        <div class="navbar">
        	<div class="logo">
            	<a href="/ysl4/jsp/home"></a>
            	<p><a href="/ysl4/jsp/home">タスクマネージャー</a></p>
        	</div>
            <ul class="center">
                <li>
                    <a href="/ysl4/jsp/home">ホーム</a>
                </li>
                <li>
                    <a href="/ysl4/jsp/projectList">案件一覧</a>
                </li>
                <li>
                    <a href="/ysl4/jsp/taskList>タスク管理</a>
                </li>
                <li>
                    <a href="/ysl4/jsp/monthlySummary">月次集計</a>
                </li>
                <li>
                	<a href="/ysl4/jsp/menberList">メンバー管理</a>
                </li>
            </ul>
            <ul class="right">
                <li>
                    <a href="/ysl4/jsp/logout" id="login">ログアウト</a>
                </li>
            </ul>
        </div>
</nav>--%>
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