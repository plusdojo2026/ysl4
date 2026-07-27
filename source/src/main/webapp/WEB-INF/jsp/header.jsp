<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
</head>
<body>
	
	<nav class="nav">
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
                    <a href="/ysl4/jsp/logout" id="logout" onclick="return logout()">ログアウト</a>
                </li>
            </ul>
        </div>
</nav>
	<script>
	//ログアウトボタンが押されたときの処理
		function logout(){
			if (confirm("本当にログアウトしますか？")) {
			  return true;
			} else {
			  return false;
			}
		}
	</script>
</body>
</html>