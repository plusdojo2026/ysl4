<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="displayDashboard" value="${dashboard}" />

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<%-- ログアウト後に画面キャッシュが残ることを防ぐ --%>
<meta http-equiv="Cache-Control" content="no-store, no-cache, must-revalidate">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Expires" content="0">

<title>ホーム</title>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/summary.css">
</head>

<body class="home-page">

	<%@ include file="/WEB-INF/jsp/header.jsp"%>

	<main class="home-main">

		<section class="welcome-section">
			<div class="welcome-text">
				<img src="${pageContext.request.contextPath}/img/defaultelephant.png"
					class="welcome-icon"
					alt="ホーム">

				<div class="welcome-message">
					<h1 class="home-title">
						おかえりなさい、<c:out value="${loginUser.name}" />さん
					</h1>

					<p class="home-lead">
						今日のタスクと案件状況を確認しましょう
					</p>
				</div>
			</div>
		</section>

		<c:if test="${not empty errMsg}">
			<div class="js-message" data-type="error" data-open-password-modal="${openPasswordModal}">
				<c:out value="${errMsg}" />
			</div>
		</c:if>

		<c:if test="${not empty successMsg}">
			<div class="js-message" data-type="success">
				<c:out value="${successMsg}" />
			</div>
		</c:if>

		<section class="summary-card-grid">

			<article class="summary-card card-tasks">
				<div class="card-icon">
					<img src="${pageContext.request.contextPath}/img/owntask.png"
						alt="担当タスク">
				</div>

				<div class="summary-info">
					<p class="summary-label">担当タスク</p>
					<p class="summary-value">
						<span>
							<c:out value="${displayDashboard.assignedTaskCount}" default="0" />
						</span>件
					</p>
				</div>

				<a href="${pageContext.request.contextPath}/Controller?page_id=T001"
					class="card-link">
					タスク一覧 ＞
				</a>
			</article>

			<article class="summary-card card-projects">
				<div class="card-icon">
					<img src="${pageContext.request.contextPath}/img/advance.png"
						alt="進行中案件">
				</div>

				<div class="summary-info">
					<p class="summary-label">進行中案件</p>
					<p class="summary-value">
						<span>
							<c:out value="${displayDashboard.inProgressProjectCount}" default="0" />
						</span>件
					</p>
				</div>

				<a href="${pageContext.request.contextPath}/Controller?page_id=P001"
					class="card-link">
					案件一覧 ＞
				</a>
			</article>

			<article class="summary-card card-alert">
				<div class="card-icon">
					<img src="${pageContext.request.contextPath}/img/warning.png"
						alt="期限超過タスク">
				</div>

				<div class="summary-info">
					<p class="summary-label">期限超過タスク</p>
					<p class="summary-value warning-text">
						<span>
							<c:out value="${displayDashboard.overdueTaskCount}" default="0" />
						</span>件
					</p>
				</div>

				<a href="${pageContext.request.contextPath}/Controller?page_id=T001"
					class="card-link">
					タスク一覧 ＞
				</a>
			</article>

			<article class="summary-card manhour-thismonth">
				<div class="card-icon">
					<img src="${pageContext.request.contextPath}/img/actualmanhour.png"
						alt="今月の工数">
				</div>

				<div class="summary-info">
					<p class="summary-label">今月の工数</p>
					<p class="summary-value">
						<span>
							<c:out value="${displayDashboard.thisMonthWorkHours}" default="0" />
						</span>h
					</p>
				</div>

				<a href="${pageContext.request.contextPath}/Controller?page_id=S001"
					class="card-link">
					月次集計 ＞
				</a>
			</article>

		</section>

		<section class="dashboard-grid">

			<div class="section-card">
				<div class="section-header">
					<h2>進行中案件</h2>

					<a href="${pageContext.request.contextPath}/Controller?page_id=P001"
						class="text-link">
						一覧へ
					</a>
				</div>

				<div class="table-wrap">
					<table class="data-table">
						<thead>
							<tr>
								<th>案件コード</th>
								<th>案件名</th>
								<th>顧客名</th>
								<th>優先度</th>
								<th>期限</th>
							</tr>
						</thead>

						<tbody>
							<c:forEach var="project" items="${displayDashboard.inProgressProjectList}">
								<tr>
									<td>
										<c:out value="${project.projectCode}" />
									</td>

									<td>
										<a href="${pageContext.request.contextPath}/Controller?page_id=P002&project_id=${project.projectId}">
											<c:out value="${project.projectName}" />
										</a>
									</td>

									<td>
										<c:out value="${project.customerName}" />
									</td>

									<td>
										<span class="badge">
											<c:out value="${project.priority}" />
										</span>
									</td>

									<td>
										<c:out value="${project.dueDate}" />
									</td>
								</tr>
							</c:forEach>

							<c:if test="${empty displayDashboard.inProgressProjectList}">
								<tr>
									<td colspan="5" class="empty-cell">
										表示できる案件がありません
									</td>
								</tr>
							</c:if>
						</tbody>
					</table>
				</div>
			</div>

			<div class="section-card">
				<div class="section-header">
					<h2>自分の担当タスク</h2>

					<a href="${pageContext.request.contextPath}/Controller?page_id=T001"
						class="text-link">
						一覧へ
					</a>
				</div>

				<div class="table-wrap">
					<table class="data-table">
						<thead>
							<tr>
								<th>タスク名</th>
								<th>案件名</th>
								<th>状態</th>
								<th>進捗</th>
								<th>期限</th>
							</tr>
						</thead>

						<tbody>
							<c:forEach var="task" items="${displayDashboard.assignedTaskList}">
								<tr class="${task.overdue ? 'overdue-row' : ''}">
									<td>
										<a href="${pageContext.request.contextPath}/Controller?page_id=T002&task_id=${task.taskId}">
											<c:out value="${task.taskName}" />
										</a>
									</td>

									<td>
										<c:out value="${task.projectName}" />
									</td>

									<td>
										<span class="badge">
											<c:out value="${task.status}" />
										</span>
									</td>

									<td>
										<div class="home-progress-cell">
											<progress max="100" value="${task.progress}">
												<c:out value="${task.progress}" />%
											</progress>

											<span>
												<c:out value="${task.progress}" />%
											</span>
										</div>
									</td>

									<td>
										<c:out value="${task.dueDate}" />
									</td>
								</tr>
							</c:forEach>

							<c:if test="${empty displayDashboard.assignedTaskList}">
								<tr>
									<td colspan="5" class="empty-cell">
										表示できるタスクがありません
									</td>
								</tr>
							</c:if>
						</tbody>
					</table>
				</div>
			</div>

		</section>
	</main>

	<%@ include file="/WEB-INF/jsp/footer.jsp"%>

	<script src="${pageContext.request.contextPath}/js/common.js"></script>
</body>
</html>