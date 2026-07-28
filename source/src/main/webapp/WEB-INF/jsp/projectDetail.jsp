<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>案件詳細</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
<link rel="stylesheet" href="<c:url value='/css/project.css' />">

</head>

<body>
<header>
<%@ include file="/WEB-INF/jsp/header.jsp" %>
</header>
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
	<label>案件コード</label> ${dto.project_code}

     <label>案件名</label> ${dto.project_name}

	<label>顧客名</label> ${dto.customer_name}

	<label>PM</label> ${dto.project_manager_id}

	<label>ステータス</label> ${dto.status}

	<label>優先度</label> ${dto.priority}

	<label>期間</label> 
	${dto.start_date} ～ ${dto.due_date}

	<label>見積工数</label> ${dto.estimated_manhours}


	<label>実績工数</label> ${dto.actual_manhours}

	<label>進捗</label> ${dto.ptiority}

	<label>説明</label> ${dto.description}

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
<script>？？？？？？
function deleteTask(taskId) {
    if (confirm("このタスクを削除しますか？")) {
        location.href = "Controller?page_id=T003&task_id=" + taskId;
    }
}
</script>
</body>

