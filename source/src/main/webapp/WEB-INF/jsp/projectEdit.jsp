<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="displayProject" value="${project}" />
<c:if test="${empty displayProject}">
	<c:set var="displayProject" value="${projectDto}" />
</c:if>
<c:if test="${empty displayProject}">
	<c:set var="displayProject" value="${projectList}" />
</c:if>

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>案件編集</title>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/project.css">
</head>

<body class="member-page">



	<form id="projectEditForm"
		method="post"
		action="${pageContext.request.contextPath}/Controller">

		<input type="hidden" name="page_id" value="P004">
		<input type="hidden" name="project_id" id="project_id" value="${displayProject.projectId}">
	<%@ include file="/WEB-INF/jsp/header.jsp"%>

<main class="project-main">


<div class="atama">
  <section class="project-hero">
		<div class="project-hero-image">
			<img class="regist-elephant"
				src="${pageContext.request.contextPath}/img/smileelephant.png"
				alt="案件編集">
</div>


			<div class="project-hero-text">
				<h1 class="project-title">案件編集</h1>
				<p class="subtitle">登録済み案件情報を更新してください</p>
			</div>
		
		</section>
</div>

<div class="atama">
		<c:if test="${not empty errMsg}">
			<div class="error">
				<c:out value="${errMsg}" />
			</div>
		</c:if>

		<div class="field">
			<table>
				<tr>
					<td>案件コード</td>
					<td>
						<input type="text"
							id="project_code"
							name="project_code"
							value="${displayProject.projectCode}"
							readonly>
					</td>

					<td>案件名<span class="repuired-item">必須</span></td>
					<td>
						<input type="text"
							id="project_name"
							name="project_name"
							value="${displayProject.projectName}">
					</td>

					<td>担当PM<span class="repuired-item">必須</span></td>
					<td>
						<select id="project_manager_id" name="project_manager_id">
							<option value="">選択してください</option>

							<c:forEach var="manager" items="${managerList}">
								<option value="${manager.userId}"
									${manager.userId == displayProject.projectManagerId ? 'selected' : ''}>
									<c:out value="${manager.name}" />
								</option>
							</c:forEach>

							<c:if test="${empty managerList and displayProject.projectManagerId > 0}">
								<option value="${displayProject.projectManagerId}" selected>
									<c:out value="${displayProject.projectManagerName}" />
								</option>
							</c:if>
						</select>
					</td>
				</tr>

				<tr>
					<td>顧客名</td>
					<td>
						<input type="text"
							id="customer_name"
							name="customer_name"
							value="${displayProject.customerName}">
					</td>

					<td>開始日<span class="repuired-item">必須</span></td>
					<td>
						<input type="date"
							id="start_date"
							name="start_date"
							value="${displayProject.startDate}">
					</td>

					<td>期限<span class="repuired-item">必須</span></td>
					<td>
						<input type="date"
							id="due_date"
							name="due_date"
							value="${displayProject.dueDate}">
					</td>
				</tr>

				<tr>
					<td>ステータス<span class="repuired-item">必須</span></td>
					<td>
						<select id="status" name="status">
							<option value="">選択してください</option>

							<c:forEach var="status" items="${statusList}">
								<option value="${status}"
									${status == displayProject.status ? 'selected' : ''}>
									<c:out value="${status}" />
								</option>
							</c:forEach>

							<c:if test="${empty statusList}">
								<option value="進行中" ${displayProject.status == '進行中' ? 'selected' : ''}>進行中</option>
								<option value="完了" ${displayProject.status == '完了' ? 'selected' : ''}>完了</option>
								<option value="中止" ${displayProject.status == '中止' ? 'selected' : ''}>中止</option>
							</c:if>
						</select>
					</td>

					<td>優先度<span class="repuired-item">必須</span></td>
					<td>
						<select id="priority" name="priority">
							<option value="">選択してください</option>

							<c:forEach var="priority" items="${priorityList}">
								<option value="${priority}"
									${priority == displayProject.priority ? 'selected' : ''}>
									<c:out value="${priority}" />
								</option>
							</c:forEach>

							<c:if test="${empty priorityList}">
								<option value="高" ${displayProject.priority == '高' ? 'selected' : ''}>高</option>
								<option value="中" ${displayProject.priority == '中' ? 'selected' : ''}>中</option>
								<option value="低" ${displayProject.priority == '低' ? 'selected' : ''}>低</option>
							</c:if>
						</select>
					</td>
</tr>
<tr>
					<td>見積工数<span class="repuired-item">必須</span></td>
					<td>
						<input type="number"
							id="estimated_manhours"
							name="estimated_manhours"
							min="0"
							step="0.5"
							value="${displayProject.estimatedManhours}">
					</td>
				

				
					<td>実績工数</td>
					<td>
						<input type="number"
							id="actual_manhours"
							name="actual_manhours"
							value="${displayProject.actualManhours}"
							readonly>
					</td>
</tr>
<tr>
					<td>説明</td>
					<td colspan="3">
						<input type="text"
							id="description"
							name="description"
							value="${displayProject.description}">
					</td>
				</tr>
			</table>
		</div>
</div>
<div class="atama">
		<div class="kousuu">
			<table>
				<tr>
					<td>
						<img class="regist-elephant"
							src="${pageContext.request.contextPath}/img/estmanhours.png"
							alt="予算工数">
					</td>
					<td>
						予算工数<br>
						<span id="summaryEstimatedManhours">
							<c:out value="${displayProject.estimatedManhours}" />
						</span>h
					</td>

					<td>
						<img class="regist-elephant"
							src="${pageContext.request.contextPath}/img/actualmanhour.png"
							alt="実績工数">
					</td>
					<td>
						実績工数<br>
						<span id="summaryActualManhours">
							<c:out value="${displayProject.actualManhours}" />
						</span>h
					</td>

					<td>
						<img class="regist-elephant"
							src="${pageContext.request.contextPath}/img/advance.png"
							alt="予算消化率">
					</td>
					<td>
						予算消化率<br>
						<span id="budgetRateText">0</span>%
					</td>

					<td>
						<img class="regist-elephant"
							src="${pageContext.request.contextPath}/img/owntask.png"
							alt="タスク進捗">
					</td>
					<td>
						タスク進捗<br>
						<span id="taskProgressText">
							<c:out value="${displayProject.completedTaskCount}" />
							/
							<c:out value="${displayProject.taskCount}" />
						</span>
					</td>
				</tr>
			</table>
		</div>
</div>

		<input type="hidden" id="completed_task_count" value="${displayProject.completedTaskCount}">
		<input type="hidden" id="task_count" value="${displayProject.taskCount}">
		<input type="hidden" id="progress_rate" value="${displayProject.progressRate}">

		<div class="label">
			<span id="label-fraction"></span>
			<span id="label-pct"></span>
		</div>

		<div class="track">
			<div class="bar" id="js-bar"></div>
		</div>

		<div class="projectbtn">
			<button type="button" id="back">戻る</button>

			<button type="button" id="move">案件詳細へ</button>

			<button type="submit" id="comp" name="button_id" value="更新">
				保存
			</button>
		</div>
	</form>

</main>
	<%@ include file="/WEB-INF/jsp/footer.jsp"%>

	<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
	<script src="${pageContext.request.contextPath}/js/common.js"></script>
	<script src="${pageContext.request.contextPath}/js/project.js"></script>
</body>
</html>