<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>タスク編集画面</title>

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/common.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/task.css">

</head>

<body>

	<%@ include file="/WEB-INF/jsp/header.jsp"%>

	<main class="edit-main">

		<!-- 上の画像-->
		<div>
			<h1 class="title-main">タスク編集</h1>
			<p class="title-sub">新しいタスク詳細を入力してください。</p>
		</div>


		<!-- 右側説明 -->
		<div class="sub-header-right">
			<img class="regist-elephant"
				src="${pageContext.request.contextPath}/img/smileelephant.png">

			<p>登録済みタスク情報を更新してください</p>

		</div>
		<div class="edit-form">

			<form id="projectForm" method="POST"
				action="<c:url value='/Controller'/>">
				<input type="hidden" name="page_id" value="T004">
				<!-- 案件コード -->
				<div class="field">
					<label> 案件コード <c:out value="${selectedProject.projectName}" />
					</label>
				</div>

				<div class="error">${errorMessage}</div>

				<!-- 案件名 -->
				<div class="field">
					<label> 案件名 <span class="must">必須</span>
						<option><c:out value="${selectedProject.projectName}" /></option>
					</label>
				</div>
				<!-- タスク名 -->
				<div class="field">
					<label>タスク名</label> <input type="text" id="task_name"
						name="task_name" value="${taskList.taskName}">
				</div>

				<!-- ステータス -->
				<div class="field">
					<label> ステータス<span class="must">必須</span>
					</label> <select id="status" name="status" value="${taskList.status}">
						<option value="not-started">未着手</option>
						<option value="in-progress">進行中</option>
						<option value="done">完了</option>
						<option value="on-hold">保留</option>
					</select>
				</div>

				<!-- 優先度 -->
				<div class="field">
					<label> 優先度 <span class="must">必須</span>
					</label> <select id="status" name="status" value="${taskList.priority}">
						<option value="in_progress">進行中</option>
						<option value="done">完了</option>
						<option value="canceled">中止</option>
					</select>
				</div>

				<!-- 担当者 -->
				<div class="field">
					<label> 担当者 <span class="must">必須</span>
					</label> <select id="assigne" name="assigne">
						<c:forEach var="uib" items="${userList}">
							<option value="${uib.userId}">
								<c:out value="${uib.name}" />
							</option>
						</c:forEach>
					</select>
				</div>
				<!-- 開始日 -->
				<div class="field">
					<label> 開始日 <span class="must">必須</span>
					</label> <input type="date" id="start_date" name="start_date"
						value="${taskList.startDate}">
				</div>

				<!-- 期限 -->
				<div class="field">
					<label>期限</label> <input type="date" id="due_date" name="due_date"
						value="${taskList.dueDate}">
				</div>

				<!-- 見積もり工数 -->
				<div class="field">
					<label> 見積もり工数 <span class="must">必須</span>
					</label> <input type="text" id="estimatedManhours" name="estimatedManhours"
						value="${taskList.estimatedManhours}">
				</div>

				<!-- 進捗率 -->
				<div class="field">
					<label> 進捗率 <span class="must">必須</span>
					</label> <input type="range" name="progress" min="0" max="100" step="5"
						value="${taskList.progress}">
				</div>

				<!-- 説明 -->
				<div class="field">
					<label>説明</label> <input type="text" id="description"
						name="description" value="${taskList.description}"
						style="width: 500px;">
				</div>

				<!--  予算工数 -->
				<div class="kousuu">
					<img class="regist-elephant"
						src="${pageContext.request.contextPath}/img/estmanhours.png">
					見積工数
					<c:out value="${taskList.estimatedManhours}" />
					h
				</div>

				<!--  実績工数-->
				<div class="kousuu">
					<img class="regist-elephant"
						src="${pageContext.request.contextPath}/img/estmanhours.png">
					実績工数
					<c:out value="${task.actualManhours}" />
					h
				</div>

				<!--  タスク進捗-->
				<div class="kousuu">
					<img class="regist-elephant"
						src="${pageContext.request.contextPath}/img/owntask.png">
					<c:out value="${taskList.progressRate}" />
					%

					<!-- 更新 -->
					<button type="submit" name="botton_id" value="更新">保存</button>
			</form>

			<!-- 戻る -->
			<button type="button" id="back" onclick="history.back()">戻る
			</button>



			<!-- キャンセル（入力クリア）-->
			<button type="button" id="clearBtn">キャンセル</button>
		</div>



		<script>
document.getElementById("clearBtn")
    .addEventListener("click", function () {
        document.getElementById("projectForm").reset();
    });
</script>
	</main>
</body>
</html>