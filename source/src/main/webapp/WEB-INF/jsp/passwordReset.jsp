<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="Cache-Control" content="no-store, no-cache, must-revalidate">
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Expires" content="0">
    <title>パスワードリセット | TaskManager</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/summary.css">
</head>
<body>

    <main class="simple-page-wrapper">
        <section class="section-card simple-card">
            <h1 class="page-title">パスワードリセット</h1>
            <p class="page-subtitle">管理者がメンバーのパスワードを再設定する</p>

            <c:if test="${not empty errMsg}">
                <div class="js-message" data-type="error"><c:out value="${errMsg}" /></div>
            </c:if>

            <c:if test="${not empty successMsg}">
                <div class="js-message" data-type="success"><c:out value="${successMsg}" /></div>
            </c:if>

            <form id="passwordResetForm" class="form-area" action="${pageContext.request.contextPath}/Controller" method="post" novalidate>
                <input type="hidden" name="page_id" value="M004">
                <input type="hidden" name="button_id" value="リセット">

                <div class="form-group">
                    <label for="userId">ユーザーID</label>
                    <input type="text" id="userId" name="user_id" value="${param.user_id}" required>
                </div>

                <div class="form-group">
                    <label for="newPassword">新しいパスワード</label>
                    <input type="password" id="newPassword" name="new_password" required>
                </div>

                <div class="form-group">
                    <label for="confirmPassword">確認用パスワード</label>
                    <input type="password" id="confirmPassword" name="confirm_password" required>
                </div>

                <div class="button-row">
                    <a href="${pageContext.request.contextPath}/Controller?page_id=M001" class="btn btn-outline">戻る</a>
                    <button type="submit" class="btn btn-primary">リセット</button>
                </div>
            </form>
        </section>
    </main>

    <script src="${pageContext.request.contextPath}/js/validation.js"></script>
    <script src="${pageContext.request.contextPath}/js/common.js"></script>
</body>
</html>