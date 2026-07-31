<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%-- タスク一覧の取得元を統一 --%>
<c:set var="displayTaskList" value="${taskList}" />

<%-- taskListが存在しない場合はlistを使用 --%>
<c:if test="${empty displayTaskList}">
	<c:set var="displayTaskList" value="${list}" />
</c:if>

<!DOCTYPE html>
<html lang="ja">

<head>
<meta charset="UTF-8">

<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>タスク一覧</title>

<%-- DataTables --%>
<link rel="stylesheet" href="https://cdn.datatables.net/t/bs-3.3.6/jqc-1.12.0,dt-1.10.11/datatables.min.css">

<%-- 共通CSS --%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">

<%-- タスク画面CSS --%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/task.css">
</head>

<body class="task-list-page">

	<%-- 共通ヘッダー --%>
	<%@ include file="/WEB-INF/jsp/header.jsp"%>

	<main class="task-list-main">

		<%-- タイトルと集計情報 --%>
		<section class="main">

			<%-- タイトルエリア --%>
			<div class="under-header">

				<img class="regist-elephant"
					src="${pageContext.request.contextPath}/img/elephant(1).png" alt="タスク一覧">

				<div class="text-wrap">
					<h3>タスク一覧</h3>

					<h4>登録済みタスクの検索・確認ができます</h4>
				</div>
			</div>

			<%-- 集計カード --%>
			<div class="member-dashboard">

				<%-- 全タスク --%>
				<div class="member-count">

					<img src="${pageContext.request.contextPath}/img/owntask.png"
						alt="全タスク">

					<div class="member-count-text">
						<span class="block-box"> 全タスク </span> <span
							class="task-count-line"> <span class="actual-member-count"
							id="taskTotalCount">0</span> <span class="task-count-unit">
								件 </span>
						</span>
					</div>
				</div>

				<%-- 進行中 --%>
				<div class="member-count">

					<img src="${pageContext.request.contextPath}/img/clockmark.png"
						alt="進行中">

					<div class="member-count-text">
						<span class="block-box"> 進行中 </span> <span class="task-count-line">
							<span class="actual-member-count" id="taskProgressCount">0</span>

							<span class="task-count-unit"> 件 </span>
						</span>
					</div>
				</div>

				<%-- 期限超過 --%>
				<div class="member-count">

					<img src="${pageContext.request.contextPath}/img/warning.png"
						alt="期限超過">

					<div class="member-count-text">
						<span class="block-box"> 期限超過 </span> <span
							class="task-count-line"> <span class="actual-member-count"
							id="taskOverdueCount">0</span> <span class="task-count-unit">
								件 </span>
						</span>
					</div>
				</div>

				<%-- 完了タスク --%>
				<div class="member-count">

					<img src="${pageContext.request.contextPath}/img/done.png"
						alt="完了タスク">

					<div class="member-count-text">
						<span class="block-box"> 完了タスク </span> <span
							class="task-count-line"> <span class="actual-member-count"
							id="taskDoneCount">0</span> <span class="task-count-unit">
								件 </span>
						</span>
					</div>
				</div>
			</div>
		</section>

		<%-- ログインユーザー名 --%>
		<input type="hidden" id="login-user-name"
			value="<c:out value='${loginUser.name}' />">

		<%-- 検索条件エリア --%>
		<div class="member-search task-search-area">

			<%-- 検索条件 --%>
			<div class="task-search-grid">

				<%-- キーワード --%>
				<div class="task-search-field task-keyword-field">
					<label for="task-keyword-filter"> キーワード </label> <input type="text"
						id="task-keyword-filter" class="keyword" name="keyword" value=""
						placeholder="タスク名で検索">
				</div>

				<%-- 案件 --%>
				<div class="task-search-field">
					<label for="task-project-filter"> 案件 </label> <select
						id="task-project-filter">
						<option value="">すべて</option>

						<c:forEach var="project" items="${projectList}">
							<option value="<c:out value='${project.projectName}' />">
								<c:out value="${project.projectName}" />
							</option>
						</c:forEach>
					</select>
				</div>

				<%-- ステータス --%>
				<div class="task-search-field">
					<label for="task-status-filter"> ステータス </label> <select
						id="task-status-filter">
						<option value="">すべて</option>
						<option value="未着手">未着手</option>
						<option value="進行中">進行中</option>
						<option value="完了">完了</option>
						<option value="保留">保留</option>
					</select>
				</div>

				<%-- 担当者 --%>
				<div class="task-search-field">
					<label for="task-manager-filter"> 担当者 </label> <select
						id="task-manager-filter">
						<option value="">すべて</option>

						<c:forEach var="user" items="${userList}">
							<option value="<c:out value='${user.name}' />">
								<c:out value="${user.name}" />
							</option>
						</c:forEach>
					</select>
				</div>

				<%-- 優先度 --%>
				<div class="task-search-field">
					<label for="task-priority-filter"> 優先度 </label> <select
						id="task-priority-filter">
						<option value="">すべて</option>
						<option value="高">高</option>
						<option value="中">中</option>
						<option value="低">低</option>
					</select>
				</div>
			</div>

			<%-- 検索操作ボタン --%>
			<div class="task-search-actions">

				<%-- 検索操作 --%>
				<div class="button_edit">
					<button type="button" class="clear-btn" id="task-clear-button">
						クリア</button>

					<button type="button" class="submit-btn" id="my-task-button"
						data-active="false">自分のタスク</button>
				</div>

				<%-- タスク登録 --%>
				<div class="button_regist">
					<a
						href="${pageContext.request.contextPath}/Controller?page_id=T003"
						class="submit-btn2"> ＋ タスク登録 </a>
				</div>
			</div>
		</div>
		<%-- タスク一覧エリア --%>
		<div class="member-list task-list-area">

			<%-- 横幅が不足した場合はテーブル部分だけスクロール --%>
			<div class="task-table-wrap">

				<table id="foo-table" class="task-list-table">

					<thead>
						<tr>
							<th>案件名</th>
							<th>タスク名</th>
							<th>担当者</th>
							<th>ステータス</th>
							<th>優先度</th>
							<th>期限</th>
							<th>見積工数</th>
							<th>実績工数</th>
							<th>操作</th>
						</tr>
					</thead>

					<%-- tbodyにはmember-listを付けない --%>
					<tbody>
						<c:forEach var="e" items="${displayTaskList}">
							<tr>
								<td><c:out value="${e.projectName}" /></td>

								<td><a
									href="${pageContext.request.contextPath}/Controller?page_id=T002&amp;task_id=${e.taskId}">
										<c:out value="${e.taskName}" />
								</a></td>

								<td><c:out value="${e.managerName}" /></td>

								<td><span class="task-status-text"> <c:out
											value="${e.status}" />
								</span></td>

								<td><span class="task-priority-text"> <c:out
											value="${e.priority}" />
								</span></td>

								<td><span class="task-due-date"> <c:out
											value="${e.dueDate}" />
								</span></td>

								<td class="task-hours-cell"><c:out
										value="${e.estimatedManhours}" /></td>

								<td class="task-hours-cell"><c:out
										value="${e.actualManhours}" /></td>

								<td class="task-operation-cell"><a
									href="${pageContext.request.contextPath}/Controller?page_id=T004&amp;task_id=${e.taskId}">
										編集 </a></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</main>

	<%-- DataTables --%>
	<script
		src="https://cdn.datatables.net/t/bs-3.3.6/jqc-1.12.0,dt-1.10.11/datatables.min.js">
    </script>

	<%-- タスク画面JavaScript --%>
	<script src="${pageContext.request.contextPath}/js/task.js">
    </script>

	<%-- 共通フッター --%>
	<%@ include file="/WEB-INF/jsp/footer.jsp"%>
</body>

</html>