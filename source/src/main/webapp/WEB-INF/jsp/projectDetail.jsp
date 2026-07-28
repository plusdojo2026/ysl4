<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>案件詳細</title>
<link rel="stylesheet" href="/webapp/css/common.css">
	<link rel="stylesheet" href="/webapp/css/project.css">


</head>

<body>
<%@ include file="/WEB-INF/jsp/header.jsp" %>

	<form method="POST" action="<c:url value='/Controller'/>">
	</form>

	<!-- 上の左側の象さんの画像 -->
	<div class ="sub-header-left">
	 <img src = "/webapp/img/elephant(1).png">
	</div>
	<!-- タイトル部分 -->
	<div>
	<h1 class = "title-main">案件詳細</h1>
	<p class = "title-sub">案件詳細と関連タスク・工数ログを確認出来ます</p>
	</div>	
	


<!-- 戻るボタン -->
<button type="button"
onclick="location.href='ProjectServlet?action=list'">
    戻る
</button>

<!-- 編集ボタン-->
<button type="button"03&project_id=${project.project_id}>
    編集
</button>

<!-- タスク追加ボタン-->
<button type="button"
        onclick="location.href='/button'">
    ＋タスク追加
</button>
  
<div class = "syousai1">
	<label>案件コード</label> ${project.projectCode}

     <label>案件名</label> ${project.projectName}

	<label>顧客名</label> ${project.customerName}

	<label>PM</label> ${project.projectManagerId}

	<label>ステータス</label> ${project.status}

	<label>優先度</label> ${project.priority}

	<label>期間</label> 
	${project.startDate} ～ ${project.dueDate}

	<label>見積工数</label> ${project.estimatedManhours}


	<label>実績工数</label> ${project.actualManhours}

	<label>進捗</label> ${project.ptiority}

	<label>説明</label> ${project.description}

</div>

<div class="syosai2-1">
	<p class ="title2">関連タスク一覧</p>
	ここで件数取得書けない



<div class ="syousai2-2">
	<table>
    <tr>
        <th>タスク名</th>
        <th>担当者</th>
        <th>ステータス</th>
        <th>期限</th>
        <th>見積工数</th>
		<th>実績工数</th>
		<th>進捗</th>
		<th>操作</th>
    </tr>
<c:forEach var="task" items="${taskList}">
    <tr>
        <td>${task.taskName}</td>
        <td>${task.managerId}</td>
        <td>${task.status}</td>
        <td>${task.dueDate}</td>
        <td>${task.estimatedManhours}</td>
        <td>${task.actualManhours}</td>
        <td>
    <progress value="${task.progress}" max="100"></progress>
    ${task.progress}%
</td>
        <td>
            <button type="button"
                onclick="deleteTask(${task.taskId})">
                削除
            </button>
        </td>
    </tr>
</c:forEach>
</table>
</div>
</div>
<script>
function deleteTask(taskId) {
    if (confirm("このタスクを削除しますか？")) {
        location.href = "Controller?page_id=T003&task_id=" + taskId;　？？？
    }
}
</script>

<div class="syousai3-1">
	<p class="title3">工数ログ（${fn:length(latestWorkLogList)}件</p>

	ここに工数登録ボタン
<div class="syousai3-2">
<table>
    <tr>
        <th>作業日</th>
        <th>タスク名</th>
        <th>担当者</th>
        <th>工数</th>
        <th>作業内容</th>
		
    </tr>
<c:forEach var="log" items="${latestWorkLogList}">
    <tr>
        <td>${log.workDate}</td>
        <td>${log.taskName}</td>
        <td>${log.managerId}</td>
        <td>${log.manHours}</td>
        <td>${log.jobContents}</td>
        
    </tr>
</c:forEach>
</table>
</div>
</div>



<%@ include file="/WEB-INF/jsp/footer.jsp" %>
</body>
</html>

