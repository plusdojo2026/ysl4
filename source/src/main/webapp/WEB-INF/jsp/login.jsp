<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ログイン | TaskManager</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/summary.css">
</head>
<body class="login-page">

    <main class="login-wrapper">
        <section class="login-card">
            <div class="login-title-area">
                <h1 class="login-title">TaskManager</h1>
                <p class="login-subtitle">プロジェクト進捗管理システム</p>
            </div>

            <%-- 共通JSがこの内容をalertで表示する --%>
            <c:if test="${not empty errMsg}">
                <div class="js-message" data-type="error"><c:out value="${errMsg}" /></div>
            </c:if>

            <%-- 共通JSがこの内容をalertで表示する --%>
            <c:if test="${not empty successMsg}">
                <div class="js-message" data-type="success"><c:out value="${successMsg}" /></div>
            </c:if>

            <form id="loginForm" class="form-area" action="${pageContext.request.contextPath}/Controller" method="post" novalidate>
                <%-- Controllerがログイン処理を判定するための値 --%>
                <input type="hidden" name="page_id" value="L001">
                <input type="hidden" name="button_id" value="ログイン">

                <div class="form-group">
                    <label for="loginId">ログインID</label>
                    <input type="text" id="loginId" name="login_id" value="${param.login_id}" autocomplete="username" required>
                </div>

                <div class="form-group">
                    <label for="password">パスワード</label>
                    <input type="password" id="password" name="password" autocomplete="current-password" required>
                </div>

                <button type="submit" class="btn btn-primary btn-full">ログイン</button>
            </form>
        </section>
    </main>

    <script src="${pageContext.request.contextPath}/js/validation.js"></script>
    <script src="${pageContext.request.contextPath}/js/common.js"></script>
</body>
</html>