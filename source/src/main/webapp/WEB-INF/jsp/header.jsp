<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="passwordModalOpenFlag" value="${sessionScope.passwordModalOpen}" />
<c:set var="passwordMessageTypeValue" value="${sessionScope.passwordMessageType}" />
<c:set var="passwordMessageValue" value="${sessionScope.passwordMessage}" />

<header class="app-header">
	<div class="header-inner">

		<div class="header-left">
			<div class="app-logo">
				<img src="${pageContext.request.contextPath}/img/logo.png"
					alt="TaskManager">
			</div>

			<nav class="side-nav">
				<a href="${pageContext.request.contextPath}/Controller?page_id=H001"
					class="nav-link"
					data-page="H001">
					ホーム
				</a>

				<a href="${pageContext.request.contextPath}/Controller?page_id=P001"
					class="nav-link"
					data-page="P001">
					案件一覧
				</a>

				<a href="${pageContext.request.contextPath}/Controller?page_id=T001"
					class="nav-link"
					data-page="T001">
					タスク一覧
				</a>

				<a href="${pageContext.request.contextPath}/Controller?page_id=S001"
					class="nav-link"
					data-page="S001">
					月次集計
				</a>

				<c:if test="${loginUser.isAdmin}">
					<a href="${pageContext.request.contextPath}/Controller?page_id=M001"
						class="nav-link"
						data-page="M001">
						メンバー管理
					</a>
				</c:if>
			</nav>
		</div>

		<div class="header-right">
			<div class="user-info-area">
				<img class="user-status"
					src="${pageContext.request.contextPath}/img/people.png"
					alt="ログインユーザー">

				<span class="user-name">
					<c:out value="${loginUser.name}" />さん
				</span>
			</div>

			<div class="header-actions">
				<button type="button"
					class="pw-reset-btn"
					id="openPasswordModalButton">
					パスワード変更
				</button>

				<form action="${pageContext.request.contextPath}/Controller"
					method="post"
					class="logout-form"
					data-confirm="ログアウトしますか">

					<input type="hidden" name="page_id" value="none">

					<button type="submit"
						name="button_id"
						value="ログアウト"
						class="logout-btn">
						ログアウト
					</button>
				</form>
			</div>
		</div>

	</div>
</header>

<div class="password-modal-overlay" id="passwordChangeModal">
	<div class="password-modal-card">

		<div class="password-modal-header">
			<h2 class="password-modal-title">パスワード変更</h2>

			<button type="button"
				class="password-modal-close"
				id="closePasswordModalButton">
				×
			</button>
		</div>

		<c:if test="${not empty passwordMessageValue}">
			<div class="password-modal-message ${passwordMessageTypeValue}">
				<c:out value="${passwordMessageValue}" />
			</div>
		</c:if>

		<form action="${pageContext.request.contextPath}/Controller"
			method="post"
			id="passwordChangeForm"
			class="password-change-form"
			novalidate>

			<input type="hidden" name="page_id" value="L002">

			<div class="password-form-field">
				<label for="current_password">現在のパスワード</label>
				<input type="password"
					id="current_password"
					name="current_password"
					autocomplete="current-password">
			</div>

			<div class="password-form-field">
				<label for="new_password">新しいパスワード</label>
				<input type="password"
					id="new_password"
					name="new_password"
					autocomplete="new-password">

				<p class="password-input-note">
					6文字以上で入力してください
				</p>
			</div>

			<div class="password-form-field">
				<label for="confirm_password">新しいパスワード確認</label>
				<input type="password"
					id="confirm_password"
					name="confirm_password"
					autocomplete="new-password">
			</div>

			<div class="password-modal-actions">
				<button type="button"
					class="password-cancel-btn"
					id="cancelPasswordModalButton">
					キャンセル
				</button>

				<button type="submit"
					name="button_id"
					value="変更"
					class="password-save-btn">
					変更
				</button>
			</div>
		</form>
	</div>
</div>

<c:if test="${passwordModalOpenFlag}">
	<input type="hidden" id="passwordModalInitialOpen" value="true">
</c:if>

<c:remove var="passwordModalOpen" scope="session" />
<c:remove var="passwordMessageType" scope="session" />
<c:remove var="passwordMessage" scope="session" />