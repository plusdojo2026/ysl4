<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>案件一覧</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}<c:url value='/css/project.css' />">
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
    
    <form id="serchForm">
    <!-- キーワード検索 -->
    <label>キーワード</label>
	<input type="text" id="keyword" name="keyword" required placeholder="案件名・顧客名・案件コード">

	<!-- ステータス -->
	<label>ステータス</label>
    <select name="status">
    <option value="">すべて</option>
    <option value="中止">中止</option>
    <option value="進行中">進行中</option>
    <option value="完了">完了</option>
</select>
	
	
	<!-- 優先度-->
    <label>優先度</label>
	<select name="priority">
    <option value="">すべて</option>
    <option value="高">高</option>
    <option value="中">中</option>
    <option value="低">低</option>
</select>
	

     <!--検索ボタン-->
     <button type="submit">検索</button>
     
     <!-- クリアボタン -->
     <input type="reset" value="クリア">
     <button>クリア</button>
	</form>
	
	
	
	<!-- 案件一覧 -->
	div class="annkennitirann1-1">
	<p class="title2">案件一覧</p>

	ここに件数
<div class="syousai1-2">
<table>
    <tr>
        <th>案件コード</th>
        <th>案件名</th>
        <th>顧客名</th>
        <th>ステータス</th>
        <th>優先度</th>
        <th>PM名</th>
        <th>開始日</th>
        <th>終了予定日</th>
        <th>タスク進捗</th>
        <th>実績工数</th>
        <th>操作</th>
		
    </tr>
<c:forEach var="project" items="${projectList}">
    <tr>
        <td>${project.projectCode}</td>
        <td>${project.projectName}</td>
        <td>${project.customerName}</td>
        <td>${project.status}</td>
        <td>${project.projectManagerId}</t
        <td>${project.startDate}</td>
        <td>${project.dueDate}</td>
        <td>${project.progress}</td>?
        <td>${project.actualManhours}</td>
        <td>
        <button type="button"
                onclick="editProject(${project.projectId})">
                編集
            </button>
        </td>
    </tr>
</c:forEach>
</table>
</div>
</div>
	
	
	

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


<%@ include file="/WEB-INF/jsp/footer.jsp" %>
</body>
</html>