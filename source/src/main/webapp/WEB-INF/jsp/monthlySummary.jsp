<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="displaySummary" value="${monthlySummary}" />
<c:if test="${empty displaySummary}">
    <c:set var="displaySummary" value="${summary}" />
</c:if>

<c:set var="displayProjectSummaryList" value="${projectSummaryList}" />
<c:if test="${empty displayProjectSummaryList}">
    <c:set var="displayProjectSummaryList" value="${displaySummary.projectSummaryList}" />
</c:if>

<c:set var="displayMemberSummaryList" value="${memberSummaryList}" />
<c:if test="${empty displayMemberSummaryList}">
    <c:set var="displayMemberSummaryList" value="${displaySummary.memberSummaryList}" />
</c:if>

<c:set var="displayWorkLogList" value="${workLogList}" />
<c:if test="${empty displayWorkLogList}">
    <c:set var="displayWorkLogList" value="${displaySummary.monthlyWorkLogList}" />
</c:if>

<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <title>月次集計r</title>

    <%-- DataTables用CSS --%>
    <link rel="stylesheet" href="https://cdn.datatables.net/t/bs-3.3.6/jqc-1.12.0,dt-1.10.11/datatables.min.css">

    <%-- 共通CSS --%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">

    <%-- 月次集計CSS --%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/summary.css">
</head>

<body class="summary-page">

    <%-- 共通ヘッダー --%>
    <%@ include file="/WEB-INF/jsp/header.jsp" %>

    <main class="summary-main">

        <section class="summary-hero">
            <div class="summary-hero-image">
                <img class="summary-elephant"
                     src="${pageContext.request.contextPath}/img/clockmark.png"
                     alt="月次集計">
            </div>

            <div class="summary-hero-text">
                <h1 class="summary-title">月次集計</h1>
                <p class="summary-lead">対象月の工数を案件別・メンバー別に確認できます</p>
            </div>
        </section>

        <section class="summary-search">
            <form id="monthlySummaryForm" class="summary-form" action="${pageContext.request.contextPath}/Controller" method="post" novalidate>

                <input type="hidden" name="page_id" value="S001">

                <div class="summary-search-grid">
                    <div class="summary-field">
                        <label for="targetMonth">対象月</label>
                        <input type="month" id="targetMonth" name="target_month" value="${targetMonth}" required>
                    </div>

                    <div class="summary-button-area">
                        <button type="submit" name="button_id" value="検索"class="submit-btn">
                            表示
                        </button>

                        <button type="submit" name="button_id" value="CSV出力" class="csv-btn">
                            CSV出力
                        </button>
                    </div>
                </div>
            </form>
        </section>

        <section class="summary-dashboard">
            <div class="summary-count-card">
                <img src="${pageContext.request.contextPath}/img/actualmanhour.png" alt="月間総工数">

                <div class="summary-count-text">
                    <span class="summary-count-label">月間総工数</span>
                    <span class="summary-count-value">
                        <c:out value="${displaySummary.monthlyTotalManHours}" default="0" />
                    </span>
                    <span class="summary-count-unit">h</span>
                </div>
            </div>

            <div class="summary-count-card">
                <img src="${pageContext.request.contextPath}/img/totalprojects.png"
                     alt="対象案件数">

                <div class="summary-count-text">
                    <span class="summary-count-label">対象案件数</span>
                    <span class="summary-count-value">
                        <c:out value="${displaySummary.projectCount}" default="0" />
                    </span>
                    <span class="summary-count-unit">件</span>
                </div>
            </div>

            <div class="summary-count-card">
                <img src="${pageContext.request.contextPath}/img/people.png"
                     alt="稼働メンバー数">

                <div class="summary-count-text">
                    <span class="summary-count-label">稼働メンバー数</span>
                    <span class="summary-count-value">
                        <c:out value="${displaySummary.activeMemberCount}" default="0" />
                    </span>
                    <span class="summary-count-unit">人</span>
                </div>
            </div>

            <div class="summary-count-card">
                <img src="${pageContext.request.contextPath}/img/warning.png"
                     alt="超過案件数">

                <div class="summary-count-text">
                    <span class="summary-count-label">超過案件数</span>
                    <span class="summary-count-value">
                        <c:out value="${displaySummary.overrunProjectCount}" default="0" />
                    </span>
                    <span class="summary-count-unit">件</span>
                </div>
            </div>
        </section>

        <section class="summary-two-column">
            <div class="summary-card">
                <div class="summary-card-header">
                    <h2>案件別工数</h2>
                </div>

                <div class="summary-table-wrap">
                    <table id="project-summary-table" class="summary-table table table-bordered">
                        <thead>
                            <tr>
                                <th>案件コード</th>
                                <th>案件名</th>
                                <th>見積工数</th>
                                <th>実績工数</th>
                                <th>達成率</th>
                            </tr>
                        </thead>

                        <tbody>
                            <c:forEach var="project" items="${displayProjectSummaryList}">
                                <tr>
                                    <td>
                                        <c:out value="${project.projectCode}" />
                                    </td>

                                    <td>
                                        <c:out value="${project.projectName}" />
                                    </td>

                                    <td>
                                        <c:out value="${project.estimatedManhours}" />h
                                    </td>

                                    <td>
                                        <c:out value="${project.actualManhours}" />h
                                    </td>

                                    <td>
                                        <c:out value="${project.achievementRate}" />%
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>

            <div class="summary-card">
                <div class="summary-card-header">
                    <h2>メンバー別工数</h2>
                </div>

                <div class="summary-table-wrap">
                    <table id="member-summary-table" class="summary-table table table-bordered">
                        <thead>
                            <tr>
                                <th>メンバー名</th>
                                <th>実績工数</th>
                                <th>見積工数</th>
                                <th>担当タスク数</th>
                                <th>達成率</th>
                            </tr>
                        </thead>

                        <tbody>
                            <c:forEach var="member" items="${displayMemberSummaryList}">
                                <tr>
                                    <td>
                                        <c:out value="${member.userName}" />
                                    </td>

                                    <td>
                                        <c:out value="${member.actualManHours}" />h
                                    </td>

                                    <td>
                                        <c:out value="${member.estimatedManhours}" />h
                                    </td>

                                    <td>
                                        <c:out value="${member.taskCount}" />
                                    </td>

                                    <td>
                                        <c:out value="${member.achievementRate}" />%
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </section>

        <section class="summary-card">
            <div class="summary-card-header">
                <h2>工数明細</h2>
            </div>

            <div class="summary-detail-search">
                <label for="worklog-keyword-filter">キーワード</label>

                <div class="keyword-input-wrap">
                    <span class="search-icon">⌕</span>
                    <input type="text"
                           id="worklog-keyword-filter"
                           placeholder="日付、案件名、タスク名、担当者、作業内容で検索">
                </div>

                <button type="button" id="worklog-clear-button" class="clear-btn">
                    クリア
                </button>
            </div>

            <div class="summary-table-wrap">
                <table id="worklog-summary-table" class="summary-table table table-bordered">
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
                        <c:forEach var="log" items="${displayWorkLogList}">
                            <tr>
                                <td>
                                    <c:out value="${log.workDate}" />
                                </td>

                                <td>
                                    <c:out value="${log.projectName}" />
                                </td>

                                <td>
                                    <c:out value="${log.taskName}" />
                                </td>

                                <td>
                                    <c:out value="${log.userName}" />
                                </td>

                                <td>
                                    <c:out value="${log.manHours}" />h
                                </td>

                                <td>
                                    <c:out value="${log.jobContents}" />
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </section>

    </main>

    <%-- 共通フッター --%>
    <%@ include file="/WEB-INF/jsp/footer.jsp" %>

    <script src="https://cdn.datatables.net/t/bs-3.3.6/jqc-1.12.0,dt-1.10.11/datatables.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/common.js"></script>
    <script src="${pageContext.request.contextPath}/js/summary.js"></script>
</body>
</html>