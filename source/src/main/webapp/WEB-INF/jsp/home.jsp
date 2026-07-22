<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <%-- ログアウト後に画面キャッシュが残ることを防ぐ --%>
    <meta http-equiv="Cache-Control" content="no-store, no-cache, must-revalidate">
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Expires" content="0">
    <title>ホーム | TaskManager</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/summary.css">
</head>
<body>

    <div class="app-layout">
        <aside class="side-menu">
            <div class="side-title">TaskManager</div>
            <nav class="side-nav">
                <a href="${pageContext.request.contextPath}/Controller?page_id=H001" class="nav-link" data-page="H001">ホーム</a>
                <a href="${pageContext.request.contextPath}/Controller?page_id=P001" class="nav-link" data-page="P001">案件一覧</a>
                <a href="${pageContext.request.contextPath}/Controller?page_id=T001" class="nav-link" data-page="T001">タスク一覧</a>
                <a href="${pageContext.request.contextPath}/Controller?page_id=S001" class="nav-link" data-page="S001">月次集計</a>
                <c:if test="${loginUser.admin}">
                    <a href="${pageContext.request.contextPath}/Controller?page_id=M001" class="nav-link" data-page="M001">メンバー管理</a>
                </c:if>
            </nav>
        </aside>

        <div class="main-area">
            <header class="app-header">
                <div>
                    <h1 class="page-title">ホーム</h1>
                    <p class="page-subtitle">ログイン中：<c:out value="${loginUser.name}" /></p>
                </div>

                <div class="header-actions">
                    <%-- common.jsがクリック時にモーダルを生成して開く --%>
                    <button type="button" class="btn btn-secondary" data-open-password-modal>パスワード変更</button>

                    <form action="${pageContext.request.contextPath}/Controller" method="post" data-confirm="ログアウトしますか">
                        <input type="hidden" name="page_id" value="none">
                        <button type="submit" name="button_id" value="ログアウト" class="btn btn-outline">ログアウト</button>
                    </form>
                </div>
            </header>

            <%-- 共通JSがこの内容をalertで表示する --%>
            <c:if test="${not empty errMsg}">
                <div class="js-message" data-type="error" data-open-password-modal="${openPasswordModal}"><c:out value="${errMsg}" /></div>
            </c:if>

            <%-- 共通JSがこの内容をalertで表示する --%>
            <c:if test="${not empty successMsg}">
                <div class="js-message" data-type="success"><c:out value="${successMsg}" /></div>
            </c:if>

            <main class="content-area">
                <section class="summary-card-grid home-card-grid">
                    <article class="summary-card">
                        <p class="summary-label">進行中案件</p>
                        <p class="summary-value"><c:out value="${dashboard.inProgressProjectCount}" /></p>
                    </article>
                    <article class="summary-card">
                        <p class="summary-label">担当タスク</p>
                        <p class="summary-value"><c:out value="${dashboard.assignedTaskCount}" /></p>
                    </article>
                    <article class="summary-card">
                        <p class="summary-label">期限超過</p>
                        <p class="summary-value warning-text"><c:out value="${dashboard.overdueTaskCount}" /></p>
                    </article>
                    <article class="summary-card">
                        <p class="summary-label">今月の工数</p>
                        <p class="summary-value"><c:out value="${dashboard.thisMonthWorkHours}" />h</p>
                    </article>
                </section>

                <%-- 自分の予定は追加機能のためコメントアウト --%>
                <%--
                <section class="section-card">
                    <div class="section-header">
                        <h2>自分の予定</h2>
                    </div>
                    <div class="schedule-list">
                        <p>予定表示は追加機能で実装</p>
                    </div>
                </section>
                --%>

                <section class="dashboard-grid">
                    <div class="section-card">
                        <div class="section-header">
                            <h2>進行中案件</h2>
                            <a href="${pageContext.request.contextPath}/Controller?page_id=P001" class="text-link">一覧へ</a>
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
                                    <c:forEach var="project" items="${dashboard.inProgressProjectList}">
                                        <tr>
                                            <td><c:out value="${project.projectCode}" /></td>
                                            <td>
                                                <a href="${pageContext.request.contextPath}/Controller?page_id=P003&project_id=${project.projectId}">
                                                    <c:out value="${project.projectName}" />
                                                </a>
                                            </td>
                                            <td><c:out value="${project.customerName}" /></td>
                                            <td><span class="badge"><c:out value="${project.priority}" /></span></td>
                                            <td><c:out value="${project.dueDate}" /></td>
                                        </tr>
                                    </c:forEach>

                                    <c:if test="${empty dashboard.inProgressProjectList}">
                                        <tr>
                                            <td colspan="5" class="empty-cell">表示できる案件がありません</td>
                                        </tr>
                                    </c:if>
                                </tbody>
                            </table>
                        </div>
                    </div>

                    <div class="section-card">
                        <div class="section-header">
                            <h2>自分の担当タスク</h2>
                            <a href="${pageContext.request.contextPath}/Controller?page_id=T001" class="text-link">一覧へ</a>
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
                                    <c:forEach var="task" items="${dashboard.assignedTaskList}">
                                        <tr class="${task.overdue ? 'overdue-row' : ''}">
                                            <td>
                                                <a href="${pageContext.request.contextPath}/Controller?page_id=T002&task_id=${task.taskId}">
                                                    <c:out value="${task.taskName}" />
                                                </a>
                                            </td>
                                            <td><c:out value="${task.projectName}" /></td>
                                            <td><span class="badge"><c:out value="${task.status}" /></span></td>
                                            <td><c:out value="${task.progress}" />%</td>
                                            <td><c:out value="${task.dueDate}" /></td>
                                        </tr>
                                    </c:forEach>

                                    <c:if test="${empty dashboard.assignedTaskList}">
                                        <tr>
                                            <td colspan="5" class="empty-cell">表示できるタスクがありません</td>
                                        </tr>
                                    </c:if>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </section>
            </main>
        </div>
    </div>

    <script src="${pageContext.request.contextPath}/js/validation.js"></script>
    <script src="${pageContext.request.contextPath}/js/confirm.js"></script>
    <script src="${pageContext.request.contextPath}/js/common.js"></script>
</body>
</html>