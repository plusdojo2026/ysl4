<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="displayProjectList" value="${projectList}" />
<c:if test="${empty displayProjectList}">
	<c:set var="displayProjectList" value="${projects}" />
</c:if>
<c:if test="${empty displayProjectList}">
	<c:set var="displayProjectList" value="${list}" />
</c:if>

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>案件一覧</title>

<%-- DataTables用CSS --%>
<link rel="stylesheet"
	href="https://cdn.datatables.net/t/bs-3.3.6/jqc-1.12.0,dt-1.10.11/datatables.min.css">

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/project.css">
</head>

<body>

	<%@ include file="/WEB-INF/jsp/header.jsp"%>

	<main class="main">

		<section class="sub-header-left">
			<img class="regist-elephant"
				src="${pageContext.request.contextPath}/img/smileelephant.png"
				alt="案件一覧">

			<div>
				<h1 class="title-main">案件一覧</h1>
				<p class="title-sub">登録済み案件を検索・確認出来ます</p>
			</div>

			<div class="member-hero-action">
				<form action="${pageContext.request.contextPath}/Controller" method="get">
					<input type="hidden" name="page_id" value="P003">

					<button type="submit" class="regist-btn">
						<span class="plus-mark">＋</span>
						新規登録
					</button>
				</form>
			</div>
		</section>

		<section class="member-search">
			<form id="projectSearchForm" onsubmit="return false;">

				<input type="hidden" name="page_id" value="P001">

				<label for="project-keyword-filter">キーワード</label>
				<input type="text"
					id="project-keyword-filter"
					class="keyword"
					name="keyword"
					placeholder="案件名・顧客名・案件コード">

				<label for="project-status-filter">ステータス</label>
				<select id="project-status-filter" name="status">
					<option value="">すべて</option>
					<option value="中止">中止</option>
					<option value="進行中">進行中</option>
					<option value="完了">完了</option>
				</select>

				<label for="project-priority-filter">優先度</label>
				<select id="project-priority-filter" name="priority">
					<option value="">すべて</option>
					<option value="高">高</option>
					<option value="中">中</option>
					<option value="低">低</option>
				</select>

				<input type="button"
					id="project-clear-button"
					class="clear-btn"
					value="クリア">

			</form>
		</section>

		<section class="annkennitirann1-1">
			<p class="title2">案件一覧</p>

			<p class="project-count-text">
				表示件数：
				<span id="project-display-count">0</span>
				/
				<span id="project-total-count">0</span>
				件
				<span class="project-count-space"></span>
				進行中：
				<span id="project-progress-count">0</span>
				件
				完了：
				<span id="project-done-count">0</span>
				件
				中止：
				<span id="project-stop-count">0</span>
				件
			</p>

			<div class="syousai1-2">
				<table id="project-table" class="member-table table table-bordered">
					<thead>
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
					</thead>

					<tbody>
						<c:forEach var="project" items="${displayProjectList}">
							<tr>
								<td>
									<c:out value="${project.projectCode}" />
								</td>

								<td>
									<a href="${pageContext.request.contextPath}/Controller?page_id=P002&project_id=${project.projectId}"
										class="html"
										data-page="P002">
										<c:out value="${project.projectName}" />
									</a>
								</td>

								<td>
									<c:out value="${project.customerName}" />
								</td>

								<td>
									<span class="project-status-text">
										<c:out value="${project.status}" />
									</span>
								</td>

								<td>
									<span class="project-priority-text">
										<c:out value="${project.priority}" />
									</span>
								</td>

								<td>
									<c:out value="${project.projectManagerName}" />
								</td>

								<td>
									<c:out value="${project.startDate}" />
								</td>

								<td>
									<c:out value="${project.dueDate}" />
								</td>

								<td>
									<div class="project-progress-cell">
										<span class="project-progress-text">
											<c:out value="${project.completedTaskCount}" />
											/
											<c:out value="${project.taskCount}" />
										</span>

										<progress max="100"
											value="${project.progressRate}">
											<c:out value="${project.progressRate}" />%
										</progress>

										<span class="project-progress-rate">
											<c:out value="${project.progressRate}" />%
										</span>
									</div>
								</td>

								<td>
									<c:out value="${project.actualManhours}" />h
								</td>

								<td>
									<form action="${pageContext.request.contextPath}/Controller" method="get">
										<input type="hidden" name="page_id" value="P004">
										<input type="hidden" name="project_id" value="${project.projectId}">

										<button type="submit" class="edit-btn">
											編集
										</button>
									</form>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</section>

	</main>

	<%@ include file="/WEB-INF/jsp/footer.jsp"%>

	<script src="https://cdn.datatables.net/t/bs-3.3.6/jqc-1.12.0,dt-1.10.11/datatables.min.js"></script>
	<script src="${pageContext.request.contextPath}/js/common.js"></script>
	<script src="${pageContext.request.contextPath}/js/project.js"></script>

</body>
</html>