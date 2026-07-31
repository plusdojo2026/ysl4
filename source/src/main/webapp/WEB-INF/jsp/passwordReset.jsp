<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="displayUser" value="${resetUser}" />
<c:if test="${empty displayUser}">
	<c:set var="displayUser" value="${user}" />
</c:if>

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<meta http-equiv="Cache-Control" content="no-store, no-cache, must-revalidate">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Expires" content="0">

<title>パスワードリセット | TaskManager</title>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/summary.css">
</head>

<body class="password-reset-page">

	<%@ include file="/WEB-INF/jsp/header.jsp"%>

	<main class="password-reset-main">

		<section class="password-reset-hero">
			<div class="password-reset-hero-left">
				<img class="password-reset-elephant"
					src="${pageContext.request.contextPath}/img/smileelephant.png"
					alt="パスワードリセット">

				<div class="password-reset-title-area">
					<h1 class="password-reset-title">パスワードリセット</h1>
					<p class="password-reset-lead">
						管理者がメンバーのパスワードを再設定します
					</p>
				</div>
			</div>

			<div class="password-reset-guide">
				新しいパスワードは6文字以上で入力してください<br>
				リセット後、対象メンバーは新しいパスワードでログインできます
			</div>
		</section>

		<c:if test="${not empty errMsg}">
			<div class="password-reset-message password-reset-message-error">
				<c:out value="${errMsg}" />
			</div>
		</c:if>

		<c:if test="${not empty successMsg}">
			<div class="password-reset-message password-reset-message-success">
				<c:out value="${successMsg}" />
			</div>
		</c:if>

		<section class="password-reset-card">

			<form id="passwordResetForm"
				class="password-reset-form"
				action="${pageContext.request.contextPath}/Controller"
				method="post"
				novalidate>

				<input type="hidden" name="page_id" value="M004">
				<input type="hidden" name="button_id" value="リセット">

				<c:choose>
					<c:when test="${not empty displayUser}">
						<input type="hidden" id="user_id" name="user_id" value="${displayUser.userId}">

						<div class="password-reset-target">
							<div class="password-reset-target-icon">
								<img src="${pageContext.request.contextPath}/img/people.png"
									alt="対象メンバー">
							</div>

							<div class="password-reset-target-info">
								<p class="password-reset-target-label">対象メンバー</p>

								<p class="password-reset-target-name">
									<c:out value="${displayUser.name}" />
								</p>

								<p class="password-reset-target-detail">
									ログインID：
									<c:out value="${displayUser.loginId}" />
									<span class="password-reset-separator">/</span>
									メール：
									<c:out value="${displayUser.email}" />
								</p>
							</div>
						</div>
					</c:when>

					<c:otherwise>
						<div class="password-reset-field">
							<label for="user_id">
								ユーザーID
								<span class="required-item">必須</span>
							</label>

							<input type="number"
								id="user_id"
								name="user_id"
								class="password-reset-input"
								value="${param.user_id}"
								min="1"
								placeholder="ユーザーIDを入力">
						</div>
					</c:otherwise>
				</c:choose>

				<div class="password-reset-field">
					<label for="new_password">
						新しいパスワード
						<span class="required-item">必須</span>
					</label>

					<input type="password"
						id="new_password"
						name="new_password"
						class="password-reset-input"
						autocomplete="new-password"
						placeholder="6文字以上で入力">
				</div>

				<div class="password-reset-field">
					<label for="confirm_password">
						確認用パスワード
						<span class="required-item">必須</span>
					</label>

					<input type="password"
						id="confirm_password"
						name="confirm_password"
						class="password-reset-input"
						autocomplete="new-password"
						placeholder="同じパスワードを入力">
				</div>

				<div class="password-reset-note">
					<p>
						対象メンバーの現在パスワードは確認せず、新しいパスワードに上書き出来ます❤
					</p>
				</div>

				<div class="password-reset-button-row">
					<a href="${pageContext.request.contextPath}/Controller?page_id=M001"
						class="password-reset-button password-reset-button-sub">
						戻る
					</a>

					<button type="submit"
						class="password-reset-button password-reset-button-main">
						リセット
					</button>
				</div>
			</form>
		</section>
	</main>

	<%@ include file="/WEB-INF/jsp/footer.jsp"%>

	<script src="${pageContext.request.contextPath}/js/common.js"></script>

	<script>
		'use strict';

		/**

◦ パスワードリセット画面の初期処理.
		 */
		document.addEventListener('DOMContentLoaded', function () {

			const form = document.getElementById('passwordResetForm');

			if (!form) {
				return;
			}

			form.addEventListener('submit', function (event) {

				if (!validatePasswordResetForm()) {
					event.preventDefault();
					return;
				}

				if (!window.confirm('パスワードをリセットしますか')) {
					event.preventDefault();
				}
			});
		});

		/**

◦ パスワードリセットフォームを確認する.
◦ @return {boolean} 正常ならtrue.
		 */
		function validatePasswordResetForm() {

			const userId = getPasswordResetValue('user_id');
			const newPassword = getPasswordResetValue('new_password');
			const confirmPassword = getPasswordResetValue('confirm_password');

			if (!userId) {
				window.alert('ユーザーIDを入力してください');
				return false;
			}

			if (Number(userId) <= 0 || Number.isNaN(Number(userId))) {
				window.alert('ユーザーIDが不正です');
				return false;
			}

			if (!newPassword) {
				window.alert('新しいパスワードを入力してください');
				return false;
			}

			if (newPassword.length < 6) {
				window.alert('新しいパスワードは6文字以上で入力してください');
				return false;
			}

			if (!confirmPassword) {
				window.alert('確認用パスワードを入力してください');
				return false;
			}

			if (newPassword !== confirmPassword) {
				window.alert('新しいパスワードと確認用パスワードが一致しません');
				return false;
			}

			return true;
		}

		/**

◦ 入力値を取得する.
◦ @param {string} id 対象ID.
◦ @return {string} 入力値.
		 */
		function getPasswordResetValue(id) {

			const target = document.getElementById(id);

			if (!target) {
				return '';
			}

			return target.value.trim();
		}
	</script>
</body>
</html>