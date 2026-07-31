<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="displayProject" value="${project}" />
<c:if test="${empty displayProject}">
	<c:set var="displayProject" value="${projectDto}" />
</c:if>

<c:set var="displayManagerList" value="${managerList}" />
<c:if test="${empty displayManagerList}">
	<c:set var="displayManagerList" value="${userList}" />
</c:if>

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>案件編集</title>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/project.css">
</head>

<body class="project-edit-page">

	<%@ include file="/WEB-INF/jsp/header.jsp"%>

	<main class="project-edit-main">

		<section class="project-edit-header-card">
			<div class="project-edit-header-left">
				<img class="project-edit-elephant"
					src="${pageContext.request.contextPath}/img/smileelephant.png"
					alt="案件編集">

				<div class="project-edit-title-area">
					<h1 class="project-edit-title">案件編集</h1>
					<p class="project-edit-subtitle">登録済み案件情報を更新してください</p>
				</div>
			</div>

			<div class="project-edit-guide">
				入力内容を確認して保存しましょう<br>
				担当PMは有効メンバーから選択してください
			</div>
		</section>

		<c:if test="${not empty errMsg}">
			<div class="project-edit-error">
				<c:out value="${errMsg}" />
			</div>
		</c:if>

		<c:if test="${not empty errorMessage}">
			<div class="project-edit-error">
				<c:out value="${errorMessage}" />
			</div>
		</c:if>

		<form id="projectEditForm"
			class="project-edit-form"
			method="post"
			action="${pageContext.request.contextPath}/Controller"
			novalidate>

			<input type="hidden" name="page_id" value="P004">
			<input type="hidden" name="project_id" id="project_id" value="${displayProject.projectId}">

			<section class="project-edit-form-card">
				<div class="project-edit-form-grid">

					<div class="project-edit-field">
						<label for="project_code">
							案件コード
						</label>

						<input type="text"
							id="project_code"
							name="project_code"
							class="project-edit-input project-edit-readonly"
							value="${displayProject.projectCode}"
							readonly>
					</div>

					<div class="project-edit-field">
						<label for="project_name">
							案件名
							<span class="required-item">必須</span>
						</label>

						<input type="text"
							id="project_name"
							name="project_name"
							class="project-edit-input"
							value="${displayProject.projectName}">
					</div>

					<div class="project-edit-field">
						<label for="customer_name">
							顧客名
						</label>

						<input type="text"
							id="customer_name"
							name="customer_name"
							class="project-edit-input"
							value="${displayProject.customerName}">
					</div>

					<div class="project-edit-field">
						<label for="project_manager_id">
							担当PM
							<span class="required-item">必須</span>
						</label>

						<select id="project_manager_id"
							name="project_manager_id"
							class="project-edit-select">
							<option value="">選択してください</option>

							<c:forEach var="manager" items="${displayManagerList}">
								<option value="${manager.userId}"
									${manager.userId == displayProject.projectManagerId ? 'selected' : ''}>
									<c:out value="${manager.name}" />
								</option>
							</c:forEach>

							<c:if test="${empty displayManagerList}">
								<option value="" disabled>
									有効メンバーがありません
								</option>
							</c:if>
						</select>
					</div>

					<div class="project-edit-field">
						<label for="start_date">
							開始日
							<span class="required-item">必須</span>
						</label>

						<input type="date"
							id="start_date"
							name="start_date"
							class="project-edit-input"
							value="${displayProject.startDate}">
					</div>

					<div class="project-edit-field">
						<label for="due_date">
							期限
							<span class="required-item">必須</span>
						</label>

						<input type="date"
							id="due_date"
							name="due_date"
							class="project-edit-input"
							value="${displayProject.dueDate}">
					</div>

					<div class="project-edit-field">
						<label for="status">
							ステータス
							<span class="required-item">必須</span>
						</label>

						<select id="status"
							name="status"
							class="project-edit-select">
							<option value="">選択してください</option>
							<option value="進行中" ${displayProject.status == '進行中' ? 'selected' : ''}>進行中</option>
							<option value="完了" ${displayProject.status == '完了' ? 'selected' : ''}>完了</option>
							<option value="中止" ${displayProject.status == '中止' ? 'selected' : ''}>中止</option>
						</select>
					</div>

					<div class="project-edit-field">
						<label for="priority">
							優先度
							<span class="required-item">必須</span>
						</label>

						<select id="priority"
							name="priority"
							class="project-edit-select">
							<option value="">選択してください</option>
							<option value="高" ${displayProject.priority == '高' ? 'selected' : ''}>高</option>
							<option value="中" ${displayProject.priority == '中' ? 'selected' : ''}>中</option>
							<option value="低" ${displayProject.priority == '低' ? 'selected' : ''}>低</option>
						</select>
					</div>

					<div class="project-edit-field">
						<label for="estimated_manhours">
							見積工数
							<span class="required-item">必須</span>
						</label>

						<input type="number"
							id="estimated_manhours"
							name="estimated_manhours"
							class="project-edit-input"
							min="0"
							step="0.5"
							value="${displayProject.estimatedManhours}">
					</div>

					<div class="project-edit-field">
						<label for="actual_manhours">
							実績工数
						</label>

						<input type="number"
							id="actual_manhours"
							name="actual_manhours"
							class="project-edit-input project-edit-readonly"
							value="${displayProject.actualManhours}"
							readonly>
					</div>

					<div class="project-edit-field project-edit-field-full">
						<label for="description">
							説明
						</label>

						<textarea id="description"
							name="description"
							class="project-edit-textarea"
							rows="4"><c:out value="${displayProject.description}" /></textarea>
					</div>
				</div>
			</section>

			<section class="project-edit-summary-card">
				<div class="project-edit-summary-grid">

					<div class="project-edit-summary-item">
						<img class="project-edit-summary-icon"
							src="${pageContext.request.contextPath}/img/estmanhours.png"
							alt="見積工数">

						<div class="project-edit-summary-text">
							<span class="project-edit-summary-label">見積工数</span>
							<span class="project-edit-summary-value">
								<span id="summaryEstimatedManhours">
									<c:out value="${displayProject.estimatedManhours}" default="0" />
								</span>h
							</span>
						</div>
					</div>

					<div class="project-edit-summary-item">
						<img class="project-edit-summary-icon"
							src="${pageContext.request.contextPath}/img/actualmanhour.png"
							alt="実績工数">

						<div class="project-edit-summary-text">
							<span class="project-edit-summary-label">実績工数</span>
							<span class="project-edit-summary-value">
								<span id="summaryActualManhours">
									<c:out value="${displayProject.actualManhours}" default="0" />
								</span>h
							</span>
						</div>
					</div>

					<div class="project-edit-summary-item">
						<img class="project-edit-summary-icon"
							src="${pageContext.request.contextPath}/img/advance.png"
							alt="予算消化率">

						<div class="project-edit-summary-text">
							<span class="project-edit-summary-label">予算消化率</span>
							<span class="project-edit-summary-value">
								<span id="budgetRateText">0</span>%
							</span>
						</div>
					</div>

					<div class="project-edit-summary-item">
						<img class="project-edit-summary-icon"
							src="${pageContext.request.contextPath}/img/owntask.png"
							alt="タスク進捗">

						<div class="project-edit-summary-text">
							<span class="project-edit-summary-label">タスク進捗</span>
							<span class="project-edit-summary-value">
								<span id="taskProgressText">
									<c:out value="${displayProject.completedTaskCount}" default="0" />
									/
									<c:out value="${displayProject.taskCount}" default="0" />
								</span>
							</span>
						</div>
					</div>
				</div>

				<input type="hidden" id="completed_task_count" value="${displayProject.completedTaskCount}">
				<input type="hidden" id="task_count" value="${displayProject.taskCount}">
				<input type="hidden" id="progress_rate" value="${displayProject.progressRate}">

				<div class="project-edit-progress-label">
					<span id="label-fraction"></span>
					<span id="label-pct"></span>
				</div>

				<div class="project-edit-progress-track">
					<div class="project-edit-progress-bar" id="js-bar"></div>
				</div>
			</section>

			<div class="project-edit-button-area">
				<a href="${pageContext.request.contextPath}/Controller?page_id=P001"
					class="project-edit-button project-edit-button-sub">
					戻る
				</a>

				<a href="${pageContext.request.contextPath}/Controller?page_id=P002&project_id=${displayProject.projectId}"
					class="project-edit-button project-edit-button-sub project-edit-button-wide">
					案件詳細へ
				</a>

				<button type="submit"
					name="button_id"
					value="更新"
					class="project-edit-button project-edit-button-main">
					保存
				</button>
			</div>
		</form>

	</main>

	<%@ include file="/WEB-INF/jsp/footer.jsp"%>

	<script src="${pageContext.request.contextPath}/js/common.js"></script>

	<script>
		'use strict';

		/**
		 * 案件編集画面の初期処理
		 */
		document.addEventListener('DOMContentLoaded', function () {

			const form = document.getElementById('projectEditForm');
			const estimatedManhours = document.getElementById('estimated_manhours');

			if (!form) {
				return;
			}

			updateProjectEditSummary();

			if (estimatedManhours) {
				estimatedManhours.addEventListener('input', function () {
					updateProjectEditSummary();
				});
			}

			form.addEventListener('submit', function (event) {

				if (!validateProjectEditForm()) {
					event.preventDefault();
					return;
				}

				if (!window.confirm('案件情報を更新しますか')) {
					event.preventDefault();
				}
			});
		});

		/**
		 * 案件編集フォームを確認する
		 * @return {boolean} 正常ならtrue
		 */
		function validateProjectEditForm() {

			const projectName = getProjectEditValue('project_name');
			const projectManagerId = getProjectEditValue('project_manager_id');
			const startDate = getProjectEditValue('start_date');
			const dueDate = getProjectEditValue('due_date');
			const status = getProjectEditValue('status');
			const priority = getProjectEditValue('priority');
			const estimatedManhours = getProjectEditValue('estimated_manhours');

			if (!projectName) {
				window.alert('案件名を入力してください');
				return false;
			}

			if (!projectManagerId) {
				window.alert('担当PMを選択してください');
				return false;
			}

			if (!startDate) {
				window.alert('開始日を入力してください');
				return false;
			}

			if (!dueDate) {
				window.alert('期限を入力してください');
				return false;
			}

			if (startDate > dueDate) {
				window.alert('期限は開始日以降の日付を選択してください');
				return false;
			}

			if (!status) {
				window.alert('ステータスを選択してください');
				return false;
			}

			if (!priority) {
				window.alert('優先度を選択してください');
				return false;
			}

			if (!estimatedManhours) {
				window.alert('見積工数を入力してください');
				return false;
			}

			const estimatedValue = Number(estimatedManhours);

			if (Number.isNaN(estimatedValue)) {
				window.alert('見積工数は数値で入力してください');
				return false;
			}

			if (estimatedValue < 0) {
				window.alert('見積工数は0以上で入力してください');
				return false;
			}

			if (estimatedValue * 2 !== Math.floor(estimatedValue * 2)) {
				window.alert('見積工数は0.5単位で入力してください');
				return false;
			}

			return true;
		}

		/**
		 * 案件編集画面の集計表示を更新する
		 */
		function updateProjectEditSummary() {

			const estimated = Number(getProjectEditValue('estimated_manhours') || 0);
			const actual = Number(getProjectEditValue('actual_manhours') || 0);
			const completedTaskCount = Number(getProjectEditValue('completed_task_count') || 0);
			const taskCount = Number(getProjectEditValue('task_count') || 0);
			const progressRateInput = Number(getProjectEditValue('progress_rate') || 0);

			const estimatedText = document.getElementById('summaryEstimatedManhours');
			const actualText = document.getElementById('summaryActualManhours');
			const budgetRateText = document.getElementById('budgetRateText');
			const taskProgressText = document.getElementById('taskProgressText');
			const labelFraction = document.getElementById('label-fraction');
			const labelPct = document.getElementById('label-pct');
			const bar = document.getElementById('js-bar');

			let budgetRate = 0;
			let taskProgressRate = progressRateInput;

			if (estimated > 0) {
				budgetRate = actual / estimated * 100;
			}

			if (taskProgressRate <= 0 && taskCount > 0) {
				taskProgressRate = completedTaskCount / taskCount * 100;
			}

			if (estimatedText) {
				estimatedText.textContent = formatProjectEditNumber(estimated);
			}

			if (actualText) {
				actualText.textContent = formatProjectEditNumber(actual);
			}

			if (budgetRateText) {
				budgetRateText.textContent = formatProjectEditNumber(budgetRate);
			}

			if (taskProgressText) {
				taskProgressText.textContent = completedTaskCount + ' / ' + taskCount;
			}

			if (labelFraction) {
				labelFraction.textContent = '完了タスク ' + completedTaskCount + ' / ' + taskCount;
			}

			if (labelPct) {
				labelPct.textContent = formatProjectEditNumber(taskProgressRate) + '%';
			}

			if (bar) {
				bar.style.width = Math.max(0, Math.min(taskProgressRate, 100)) + '%';
			}
		}

		/**
		 * 入力値を取得する
		 * @param {string} id 対象ID
		 * @return {string} 入力値
		 */
		function getProjectEditValue(id) {

			const target = document.getElementById(id);

			if (!target) {
				return '';
			}

			return target.value.trim();
		}

		/**
		 * 表示用の数値に整える
		 * @param {number} value 数値
		 * @return {string} 表示用数値
		 */
		function formatProjectEditNumber(value) {

			if (!Number.isFinite(value)) {
				return '0';
			}

			const rounded = Math.round(value * 10) / 10;

			if (Number.isInteger(rounded)) {
				return String(rounded);
			}

			return rounded.toFixed(1);
		}
	</script>
</body>
</html>