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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/summary.css">
  
</head>

<body class="home-page">
<%@ include file="/WEB-INF/jsp/header.jsp" %>
  <div class="main-area">
    <div home-top>
        <img src="${pageContext.request.contextPath}/img/defaultelephant.png">
        <p class="page-subtitle">おかえりなさい、
            <c:out value="${loginUser.name}"/>
            <!-- 語尾のハート -->
            <img src="${pageContext.request.contextPath}/img/heart.png"></p>
        <p>今日のタスクとプロジェクト状況を確認しましょう。</p>
    </div>

    <%-- 共通JSがこの内容をalertで表示する --%>
      <c:if test="${not empty errMsg}">
        <div class="js-message" data-type="error" data-open-password-modal="${openPasswordModal}">
            <c:out value="${errMsg}" />
        </div>
      </c:if>

    <%-- 共通JSがこの内容をalertで表示する --%>
      <c:if test="${not empty successMsg}">
        <div class="js-message" data-type="success">
            <c:out value="${successMsg}" />
        </div>
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
     <footer>
				<%@ include file="/WEB-INF/jsp/footer.jsp" %>
</footer>

    <script src="${pageContext.request.contextPath}/js/validation.js"></script>
    <script src="${pageContext.request.contextPath}/js/confirm.js"></script>
    <script src="${pageContext.request.contextPath}/js/common.js"></script>
</body>
</html>