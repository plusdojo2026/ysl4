<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<c:set var="displayTask" value="${task}" />
<c:if test="${empty displayTask}">
    <c:set var="displayTask" value="${taskDto}" />
</c:if>

<c:set var="displayWorkLogList" value="${workLogList}" />
<c:if test="${empty displayWorkLogList}">
    <c:set var="displayWorkLogList" value="${displayTask.workLogList}" />
</c:if>

<c:set var="remainingManhours" value="${displayTask.estimatedManhours - displayTask.actualManhours}" />

<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <title>タスク詳細 | TaskManager</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/task.css">
</head>

<body class="task-detail-page">

    <%@ include file="/WEB-INF/jsp/header.jsp" %>

    <main class="task-detail-main">

        <section class="task-detail-hero">
            <div class="task-detail-hero-image">
                <img src="${pageContext.request.contextPath}/img/smileelephant.png"
                     alt="タスク詳細"
                     class="task-detail-elephant">
            </div>

            <div class="task-detail-hero-text">
                <div class="task-breadcrumb">
                    <a href="${pageContext.request.contextPath}/Controller?page_id=T001">タスク一覧</a>
                    <span>&gt;</span>
                    <span><c:out value="${displayTask.taskName}" /></span>
                </div>

                <h1 class="task-detail-title">タスク詳細</h1>
                <p class="task-detail-lead">タスク情報と工数ログを確認できます</p>
            </div>

            <div class="task-detail-actions">
                <form action="${pageContext.request.contextPath}/Controller" method="get">
                    <input type="hidden" name="page_id" value="P002">
                    <input type="hidden" name="project_id" value="${displayTask.projectId}">
                    <button type="submit" class="task-main-btn">
                        案件詳細へ
                    </button>
                </form>

                <form action="${pageContext.request.contextPath}/Controller" method="get">
                    <input type="hidden" name="page_id" value="T004">
                    <input type="hidden" name="task_id" value="${displayTask.taskId}">
                    <button type="submit" class="task-outline-btn">
                        編集
                    </button>
                </form>

                <form action="${pageContext.request.contextPath}/Controller" method="post" class="task-status-form">
                    <input type="hidden" name="page_id" value="T002">
                    <input type="hidden" name="task_id" value="${displayTask.taskId}">

                    <select name="status" id="statusSelect" class="task-status-select">
                        <option value="未着手" ${displayTask.status == '未着手' ? 'selected' : ''}>未着手</option>
                        <option value="進行中" ${displayTask.status == '進行中' ? 'selected' : ''}>進行中</option>
                        <option value="完了" ${displayTask.status == '完了' ? 'selected' : ''}>完了</option>
                        <option value="保留" ${displayTask.status == '保留' ? 'selected' : ''}>保留</option>
                    </select>

                    <button type="submit" name="button_id" value="ステータス変更" class="task-status-btn">
                        変更
                    </button>
                </form>
            </div>
        </section>

        <section class="task-detail-card">
            <div class="task-info-panel">
                <h2 class="task-card-title">
                    <c:out value="${displayTask.taskName}" />
                </h2>

                <p class="task-project-link">
                    <a href="${pageContext.request.contextPath}/Controller?page_id=P002&project_id=${displayTask.projectId}">
                        <c:out value="${displayTask.projectName}" />
                    </a>
                </p>

                <dl class="task-info-list">
                    <div>
                        <dt>担当者</dt>
                        <dd><c:out value="${displayTask.managerName}" /></dd>
                    </div>

                    <div>
                        <dt>ステータス</dt>
                        <dd>
                            <span class="task-status-label">
                                <c:out value="${displayTask.status}" />
                            </span>
                        </dd>
                    </div>

                    <div>
                        <dt>優先度</dt>
                        <dd>
                            <span class="task-priority-label">
                                <c:out value="${displayTask.priority}" />
                            </span>
                        </dd>
                    </div>

                    <div>
                        <dt>開始日</dt>
                        <dd><c:out value="${displayTask.startDate}" /></dd>
                    </div>

                    <div>
                        <dt>期限</dt>
                        <dd><c:out value="${displayTask.dueDate}" /></dd>
                    </div>

                    <div>
                        <dt>説明</dt>
                        <dd><c:out value="${displayTask.description}" /></dd>
                    </div>
                </dl>
            </div>

            <div class="task-progress-panel">
                <div class="progress-header">
                    <span>進捗率</span>
                    <strong><c:out value="${displayTask.progress}" />%</strong>
                </div>

                <div class="progress-bar-area">
                    <div class="progress-bar">
                        <div class="progress-bar-fill"
                             style="width: ${displayTask.progress}%;">
                        </div>
                    </div>

                    <div class="progress-scale">
                        <span>0%</span>
                        <span>25%</span>
                        <span>50%</span>
                        <span>75%</span>
                        <span>100%</span>
                    </div>
                </div>

                <div class="manhour-grid">
                    <div class="manhour-card">
                        <img src="${pageContext.request.contextPath}/img/estmanhours.png" alt="見積工数">
                        <div>
                            <span>見積工数</span>
                            <strong><c:out value="${displayTask.estimatedManhours}" />h</strong>
                        </div>
                    </div>

                    <div class="manhour-card">
                        <img src="${pageContext.request.contextPath}/img/actualmanhour.png" alt="実績工数">
                        <div>
                            <span>実績工数</span>
                            <strong><c:out value="${displayTask.actualManhours}" />h</strong>
                        </div>
                    </div>

                    <div class="manhour-card">
                        <img src="${pageContext.request.contextPath}/img/advance.png" alt="進捗率">
                        <div>
                            <span>進捗率</span>
                            <strong><c:out value="${displayTask.progress}" />%</strong>
                        </div>
                    </div>

                    <div class="manhour-card">
                        <img src="${pageContext.request.contextPath}/img/clockmark.png" alt="残工数">
                        <div>
                            <span>残工数</span>
                            <strong>
                                <c:choose>
                                    <c:when test="${remainingManhours < 0}">
                                        0h
                                    </c:when>
                                    <c:otherwise>
                                        <c:out value="${remainingManhours}" />h
                                    </c:otherwise>
                                </c:choose>
                            </strong>
                        </div>
                    </div>
                </div>
            </div>
        </section>

        <section class="worklog-card">
            <div class="worklog-header">
                <div class="worklog-title-area">
                    <img src="${pageContext.request.contextPath}/img/calender.png" alt="工数ログ">
                    <h2>工数ログ</h2>
                    <span>最新10件</span>
                </div>

                <button type="button" class="worklog-open-btn" id="openWorkLogModalButton">
                    ＋ 工数入力
                </button>
            </div>

            <div class="worklog-table-wrap">
                <table class="worklog-table">
                    <thead>
                        <tr>
                            <th>作業日</th>
                            <th>担当者</th>
                            <th>工数</th>
                            <th>作業内容</th>
                            <th>操作</th>
                        </tr>
                    </thead>

                    <tbody>
                        <c:forEach var="log" items="${displayWorkLogList}" varStatus="status">
                            <c:if test="${status.index < 10}">
                                <tr>
                                    <td><c:out value="${log.workDate}" /></td>
                                    <td><c:out value="${log.userName}" /></td>
                                    <td><c:out value="${log.manHours}" />h</td>
                                    <td><c:out value="${log.jobContents}" /></td>
                                    <td>
                                        <form action="${pageContext.request.contextPath}/Controller"
                                              method="post"
                                              data-confirm="この工数ログを削除しますか">
                                            <input type="hidden" name="page_id" value="W001">
                                            <input type="hidden" name="task_id" value="${displayTask.taskId}">
                                            <input type="hidden" name="work_logs_id" value="${log.workLogsId}">
                                            <button type="submit" name="button_id" value="削除" class="worklog-delete-btn">
                                                …
                                            </button>
                                        </form>
                                    </td>
                                </tr>
                            </c:if>
                        </c:forEach>

                        <c:if test="${empty displayWorkLogList}">
                            <tr>
                                <td colspan="5" class="empty-cell">表示できる工数ログがありません</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>

            <c:if test="${not empty displayWorkLogList}">
                <p class="worklog-note">最新10件を表示しています</p>
            </c:if>
        </section>
    </main>

    <div class="worklog-modal-backdrop" id="workLogModal">
        <div class="worklog-modal-card">
            <div class="worklog-modal-header">
                <h2>工数入力</h2>
                <button type="button" class="worklog-modal-close" id="closeWorkLogModalButton">×</button>
            </div>

            <form action="${pageContext.request.contextPath}/Controller"
                  method="post"
                  id="workLogForm"
                  class="worklog-form">

                <input type="hidden" name="page_id" value="W001">
                <input type="hidden" name="task_id" value="${displayTask.taskId}">

                <div class="worklog-form-grid">
                    <div class="worklog-form-field">
                        <label>作業者</label>
                        <input type="text" value="${loginUser.name}" readonly>
                    </div>

                    <div class="worklog-form-field">
                        <label for="workDate">作業日</label>
                        <input type="date" id="workDate" name="work_date">
                    </div>

                    <div class="worklog-form-field">
                        <label for="manHours">工数</label>
                        <input type="number"
                               id="manHours"
                               name="man_hours"
                               min="0.5"
                               max="24"
                               step="0.5"
                               placeholder="例 1.5">
                    </div>

                    <div class="worklog-form-field worklog-form-wide">
                        <label for="jobContents">作業内容</label>
                        <textarea id="jobContents"
                                  name="job_contents"
                                  maxlength="255"
                                  placeholder="作業内容を入力"></textarea>
                    </div>
                </div>

                <div class="worklog-form-actions">
                    <button type="button" class="task-outline-btn" id="clearWorkLogButton">
                        クリア
                    </button>

                    <button type="submit" name="button_id" value="登録" class="task-main-btn">
                        登録
                    </button>
                </div>
            </form>
        </div>
    </div>

    <%@ include file="/WEB-INF/jsp/footer.jsp" %>

    <script src="${pageContext.request.contextPath}/js/common.js"></script>
    <script src="${pageContext.request.contextPath}/js/task.js"></script>
</body>
</html>