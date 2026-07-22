<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <%-- ブラウザバックでログイン後画面が残ることを防ぐ --%>
    <meta http-equiv="Cache-Control" content="no-store, no-cache, must-revalidate">
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Expires" content="0">
    <title>ログアウト | TaskManager</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/summary.css">
</head>
<body class="logout-page">

    <main class="logout-wrapper">
        <section class="logout-card">
            <h1 class="logout-title">お疲れ様でした！</h1>
            <p class="logout-message">ログアウトしました</p>
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/Controller?page_id=L001">ログイン画面へ</a>
        </section>
    </main>

</body>
</html>