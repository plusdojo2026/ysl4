<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="ja">

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>メンバー登録</title>

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/common.css">

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/member.css">
</head>

<body class="member-regist-page">

	<%@ include file="/WEB-INF/jsp/header.jsp"%>

	<main class="member-regist-main">

		<!-- 画面上部 -->
		<section class="member-regist-headline">

			<!-- タイトル -->
			<div class="member-regist-title-area">

				<span class="member-regist-admin-label"> 管理者専用 </span>

				<h1 class="member-regist-title">メンバー登録</h1>

				<p class="member-regist-subtitle">新しいメンバーの情報を入力してください</p>

			</div>

			<!-- 画面案内 -->
			<div class="member-regist-guide">

				<div class="member-regist-guide-text">

					<strong> 新しい仲間を追加 </strong>

					<p>必要な情報を入力して登録してください</p>

				</div>

				<img class="member-regist-elephant"
					src="${pageContext.request.contextPath}/img/heartelephant.png"
					alt="">

			</div>

		</section>
		<!-- エラーメッセージ -->
		<c:if test="${not empty errMsg}">

			<div class="member-regist-error" role="alert">
				<c:out value="${errMsg}" />
			</div>

		</c:if>

		<!-- 登録完了メッセージ -->
		<c:if test="${not empty successMsg}">

			<div class="member-regist-message" role="status">
				<c:out value="${successMsg}" />
			</div>

		</c:if>
		<!-- 登録フォーム -->
		<form id="memberRegistForm" class="member-regist-form" method="POST"
			action="<c:url value='/Controller' />">

			<!-- 画面ID -->
			<input type="hidden" name="page_id" value="M001">

			<!-- 基本情報 -->
			<section class="member-form-section">

				<h2 class="member-form-section-title">基本情報</h2>

				<div class="member-form-grid">

					<!-- ログインID -->
					<div class="member-field">

						<label for="login_id"> ログインID <span
							class="member-required">必須</span>
						</label> <input type="text" id="login_id" name="login_id"
							value="<c:out value='${user.loginId}' />" maxlength="50"
							autocomplete="username" required>

						<p class="member-field-note">ログイン時に使用するIDを入力してください</p>

					</div>

					<!-- 氏名 -->
					<div class="member-field">

						<label for="name"> 氏名 <span class="member-required">必須</span>
						</label> <input type="text" id="name" name="name"
							value="<c:out value='${user.name}' />" maxlength="100"
							autocomplete="name" required>

					</div>

					<!-- メールアドレス -->
					<div class="member-field member-field-full">

						<label for="email"> メールアドレス <span class="member-required">必須</span>
						</label> <input type="email" id="email" name="email"
							value="<c:out value='${user.email}' />" maxlength="255"
							autocomplete="email" required>

					</div>

				</div>

			</section>

			<!-- パスワード -->
			<section class="member-form-section">

				<h2 class="member-form-section-title">初期パスワード</h2>

				<div class="member-form-grid">

					<!-- 初期パスワード -->
					<div class="member-field">

						<label for="initial_password"> 初期パスワード <span
							class="member-required">必須</span>
						</label>

						<div class="member-password-wrap">

							<input type="password" id="initial_password"
								name="initial_password" minlength="6" maxlength="100"
								autocomplete="new-password" required>

							<button type="button" class="member-password-toggle"
								data-target="initial_password" aria-label="初期パスワードを表示">
								表示</button>

						</div>

						<p class="member-field-note">6文字以上で入力してください</p>

					</div>

					<!-- 確認用パスワード -->
					<div class="member-field">

						<label for="confirm_password"> 初期パスワード確認 <span
							class="member-required">必須</span>
						</label>

						<div class="member-password-wrap">

							<input type="password" id="confirm_password"
								name="confirm_password" minlength="6" maxlength="100"
								autocomplete="new-password" required>

							<button type="button" class="member-password-toggle"
								data-target="confirm_password" aria-label="確認用パスワードを表示">
								表示</button>

						</div>

						<p id="passwordMatchMessage"
							class="member-field-note member-password-message"></p>

					</div>

				</div>

			</section>

			<!-- 権限 -->
			<section class="member-form-section">

				<h2 class="member-form-section-title">権限設定</h2>

				<div class="member-field">

					<label> 権限 <span class="member-required">必須</span>
					</label>

					<div class="member-radio-group">

						<!-- 一般ユーザー -->
						<label class="member-radio-card"> <input type="radio"
							name="is_admin" value="0"
							<c:if test="${not user.isAdmin}">
                                    checked
                                </c:if>>

							<span class="member-radio-content"> <strong>
									一般ユーザー </strong> <small> 通常のタスク管理機能を利用できます </small>

						</span>

						</label>

						<!-- 管理者 -->
						<label class="member-radio-card"> <input type="radio"
							name="is_admin" value="1"
							<c:if test="${user.isAdmin}">
                                    checked
                                </c:if>>

							<span class="member-radio-content"> <strong> 管理者
							</strong> <small> メンバー管理機能を利用できます </small>

						</span>

						</label>

					</div>

				</div>

				<!-- 登録時は有効固定 -->
				<div class="member-valid-info">

					<span class="member-valid-mark"> 有効 </span>

					<p>登録したメンバーは有効状態で作成されます</p>

				</div>

			</section>

			<!-- ボタン -->
			<div class="member-form-actions">

				<!-- メンバー一覧 -->
				<a class="member-list-button"
					href="<c:url value='/Controller?page_id=M001' />"> メンバー一覧へ </a>

				<!-- 保存して終了 -->
				<button type="submit" class="member-save-button" name="button_id"
					value="保存して終了">保存して終了</button>

				<!-- 続けて登録 -->
				<button type="submit" class="member-continue-button"
					name="button_id" value="保存して続けて登録">保存して続けて登録</button>

			</div>

		</form>

	</main>

	<%@ include file="/WEB-INF/jsp/footer.jsp"%>

	<script>

        document.addEventListener("DOMContentLoaded", function () {

            const form = document.getElementById("memberRegistForm")
            const passwordInput =
                document.getElementById("initial_password")
            const confirmPasswordInput =
                document.getElementById("confirm_password")
            const passwordMatchMessage =
                document.getElementById("passwordMatchMessage")
            const passwordToggleButtons =
                document.querySelectorAll(".member-password-toggle")

            // パスワード表示を切り替える
            passwordToggleButtons.forEach(function (button) {

                button.addEventListener("click", function () {

                    const targetId = button.dataset.target
                    const targetInput =
                        document.getElementById(targetId)

                    const isPassword =
                        targetInput.type === "password"

                    targetInput.type =
                        isPassword ? "text" : "password"

                    button.textContent =
                        isPassword ? "非表示" : "表示"

                    button.setAttribute(
                        "aria-label",
                        isPassword
                            ? "パスワードを非表示"
                            : "パスワードを表示"
                    )

                })

            })

            // パスワード一致状態を表示する
            function validatePasswordMatch() {

                const password = passwordInput.value
                const confirmPassword =
                    confirmPasswordInput.value

                if (confirmPassword === "") {

                    confirmPasswordInput.setCustomValidity("")
                    passwordMatchMessage.textContent = ""
                    passwordMatchMessage.classList.remove(
                        "is-error",
                        "is-success"
                    )

                    return true
                }

                if (password !== confirmPassword) {

                    confirmPasswordInput.setCustomValidity(
                        "確認用パスワードが一致しません"
                    )

                    passwordMatchMessage.textContent =
                        "確認用パスワードが一致しません"

                    passwordMatchMessage.classList.add("is-error")
                    passwordMatchMessage.classList.remove("is-success")

                    return false
                }

                confirmPasswordInput.setCustomValidity("")

                passwordMatchMessage.textContent =
                    "パスワードが一致しています"

                passwordMatchMessage.classList.add("is-success")
                passwordMatchMessage.classList.remove("is-error")

                return true

            }

            // パスワード入力時
            passwordInput.addEventListener(
                "input",
                validatePasswordMatch
            )

            // 確認用パスワード入力時
            confirmPasswordInput.addEventListener(
                "input",
                validatePasswordMatch
            )

            // 送信前にパスワードを確認する
            form.addEventListener("submit", function (event) {

                if (!validatePasswordMatch()) {

                    event.preventDefault()
                    confirmPasswordInput.reportValidity()
                    confirmPasswordInput.focus()

                }

            })

        })

    </script>

</body>

</html>