<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>案件一覧</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
<link rel="stylesheet" href="<c:url value='/css/project.css' />">

</head>

<body>

<%@ include file="/WEB-INF/jsp/header.jsp" %>

	<form method="POST" action="<c:url value='/Controller'/>">
	
	<!-- 上の左側の象さんの画像 -->
	<div class ="sub -header-left">
	 <img src = "/webapp/img/elephant(1).png">
	
	<!-- タイトル部分 -->
	<div>
	<h1 class = "title-main">案件一覧<h1>
	<p class = "title-sub">登録済み案件を検索・確認出来ます</p>
	</div>	
	</div>
	
	<script>

		<!-- 新規登録ボタン -->
        function goToPage() {
            location.href = 'projectRegist.jsp';
        }
    </script>
    
    <button type="button" onclick="goToPage()">＋新規登録</button>
    
    
    <!-- キーワード検索 -->
	
	
	<!-- ステータス -->
	
	
	<!-- 優先度-->
	
	
	
	<!-- 案件一覧 -->
	<c:forEach var="c" items="${projectList}" >
						<tr>
							<td>${c.project_code }</td>
							<td>${c.project_name }</td>
							<td>${c.customer_name }</td>
							<td>${c.status }</td>
							<td>${c.priority }</td>
							<td>${c.project_manager_id }</td>
							<td>${c.start_date }</td>
							<td>${c.due_date }</td>
						    <td>${c. }
						    <td>${c.actual_manhours }
							</td>
						</tr>
					</c:forEach>

<!-- タスク進捗現在値の表示 -->
<div class="label">
  <span id="label-fraction"></span>
  <span id="label-pct"></span>
</div>

<!-- プログレスバーの本体だよ -->
<div class="track">
  <div class="bar" id="js-bar"></div>
</div>

<!-- ここに${xxxxx}みたいな感じで値を入れるよ（下のJSで取得する）-->
<input type="hidden" name="current" id="current" value="${formDataList.completedTaskCount}">
<input type="hidden" name="total" id="total" value="${.taskCount}"> 

<script>
// ☆ここで上記のテキストボックスから取得したデータを入れるよ
const current = document.getElementById("current").value;   // 現在の値（例: 完了数、達成数など）
const total = document.getElementById("total").value;     // 合計の値（例: 全体数、目標値など）
// ここまで！！

// 分数→パーセントの変換
const pct = Math.round((current / total) * 100);

// バーの幅とラベルを反映（ページを開いた瞬間に実行されるようになってる）
document.getElementById('js-bar').style.width = pct + '%';
document.getElementById('label-fraction').textContent = current + ' / ' + total;
document.getElementById('label-pct').textContent = pct + '%';
</script>

</body>
</html>