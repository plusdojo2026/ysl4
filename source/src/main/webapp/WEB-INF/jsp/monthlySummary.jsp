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
    <title>月次集計 | TaskManager</title>
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
                    <h1 class="page-title">月次集計</h1>
                    <p class="page-subtitle">月別の工数を確認</p>
                </div>

                <div class="header-actions">
                    <button type="button" class="btn btn-secondary" data-open-password-modal>パスワード変更</button>

                    <form action="${pageContext.request.contextPath}/Controller" method="post" data-confirm="ログアウトしますか">
                        <input type="hidden" name="page_id" value="none">
                        <button type="submit" name="button_id" value="ログアウト" class="btn btn-outline">ログアウト</button>
                    </form>
                </div>
            </header>

            <c:if test="${not empty errMsg}">
                <div class="js-message" data-type="error" data-open-password-modal="${openPasswordModal}"><c:out value="${errMsg}" /></div>
            </c:if>

            <c:if test="${not empty successMsg}">
                <div class="js-message" data-type="success"><c:out value="${successMsg}" /></div>
            </c:if>

            <main class="content-area">
                <section class="section-card">
                    <form id="monthlySummaryForm" class="summary-search-form" action="${pageContext.request.contextPath}/Controller" method="get" novalidate>
                        <input type="hidden" name="page_id" value="S001">

                        <div class="form-group summary-search-item">
                            <label for="targetMonth">対象月</label>
                            <%-- type monthの値はyyyy-MMの文字列として送信される --%>
                            <input type="month" id="targetMonth" name="target_month" value="${targetMonth}" required>
                        </div>

                        <div class="form-group summary-search-item">
                            <label for="budgetHours">予算工数</label>
                            <%-- float想定のため0.5刻みで入力する --%>
                            <input type="number" id="budgetHours" name="budget_hours" value="${monthlySummary.budgetHours}" min="0" step="0.5" required>
                        </div>

                        <div class="summary-search-button-area">
                            <button type="submit" class="btn btn-primary">表示</button>
                        </div>
                    </form>
                </section>

                <section class="summary-card-grid">
                    <article class="summary-card">
                        <p class="summary-label">月間総工数</p>
                        <p class="summary-value"><c:out value="${monthlySummary.totalHours}" />h</p>
                    </article>
                    <article class="summary-card">
                        <p class="summary-label">予算工数</p>
                        <p class="summary-value"><c:out value="${monthlySummary.budgetHours}" />h</p>
                    </article>
                    <article class="summary-card">
                        <p class="summary-label">予算比</p>
                        <p class="summary-value" id="budgetRateText" data-actual-hours="${monthlySummary.totalHours}" data-budget-hours="${monthlySummary.budgetHours}">
                            <c:out value="${monthlySummary.budgetRate}" />%
                        </p>
                    </article>
                    <article class="summary-card">
                        <p class="summary-label">対象案件数</p>
                        <p class="summary-value"><c:out value="${monthlySummary.projectCount}" /></p>
                    </article>
                    <article class="summary-card">
                        <p class="summary-label">作業メンバー数</p>
                        <p class="summary-value"><c:out value="${monthlySummary.memberCount}" /></p>
                    </article>
                </section>

                <section class="summary-two-column">
                    <div class="section-card">
                        <div class="section-header">
                            <h2>案件別工数</h2>
                        </div>

                        <div class="table-wrap">
                            <table class="data-table">
                                <thead>
                                    <tr>
                                        <th>案件名</th>
                                        <th>顧客名</th>
                                        <th>実績工数</th>
                                        <th>割合</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="project" items="${projectSummaryList}">
                                        <tr>
                                            <td><c:out value="${project.projectName}" /></td>
                                            <td><c:out value="${project.customerName}" /></td>
                                            <td><c:out value="${project.totalHours}" />h</td>
                                            <td><c:out value="${project.percentage}" />%</td>
                                        </tr>
                                    </c:forEach>

                                    <c:if test="${empty projectSummaryList}">
                                        <tr>
                                            <td colspan="4" class="empty-cell">表示できる案件別工数がありません</td>
                                        </tr>
                                    </c:if>
                                </tbody>
                            </table>
                        </div>
                    </div>

                    <div class="section-card">
                        <div class="section-header">
                            <h2>メンバー別工数</h2>
                        </div>

                        <div class="table-wrap">
                            <table class="data-table">
                                <thead>
                                    <tr>
                                        <th>メンバー名</th>
                                        <th>実績工数</th>
                                        <th>作業日数</th>
                                        <th>割合</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="member" items="${memberSummaryList}">
                                        <tr>
                                            <td><c:out value="${member.userName}" /></td>
                                            <td><c:out value="${member.totalHours}" />h</td>
                                            <td><c:out value="${member.workDayCount}" /></td>
                                            <td><c:out value="${member.percentage}" />%</td>
                                        </tr>
                                    </c:forEach>

                                    <c:if test="${empty memberSummaryList}">
                                        <tr>
                                            <td colspan="4" class="empty-cell">表示できるメンバー別工数がありません</td>
                                        </tr>
                                    </c:if>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </section>

                <section class="section-card">
                    <div class="section-header">
                        <h2>工数明細</h2>
                    </div>

                    <div class="table-wrap">
                        <table class="data-table detail-table">
                            <thead>
                                <tr>
                                    <th>日付</th>
                                    <th>案件名</th>
                                    <th>タスク名</th>
                                    <th>担当者</th>
                                    <th>工数</th>
                                    <th>作業内容</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="log" items="${workLogList}">
                                    <tr>
                                        <td><c:out value="${log.workDate}" /></td>
                                        <td><c:out value="${log.projectName}" /></td>
                                        <td><c:out value="${log.taskName}" /></td>
                                        <td><c:out value="${log.userName}" /></td>
                                        <td><c:out value="${log.manHours}" />h</td>
                                        <td><c:out value="${log.jobContents}" /></td>
                                    </tr>
                                </c:forEach>

                                <c:if test="${empty workLogList}">
                                    <tr>
                                        <td colspan="6" class="empty-cell">表示できる工数明細がありません</td>
                                    </tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>
                </section>
            </main>
        </div>
    </div>

    <script src="${pageContext.request.contextPath}/js/validation.js"></script>
    <script src="${pageContext.request.contextPath}/js/confirm.js"></script>
    <script src="${pageContext.request.contextPath}/js/common.js"></script>
    <script src="${pageContext.request.contextPath}/js/summary.js"></script>
</body>
</html>