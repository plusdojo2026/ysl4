<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<c:set var="displayProject" value="${project}" />
<c:if test="${empty displayProject}">
    <c:set var="displayProject" value="${projectDto}" />
</c:if>

<c:set var="displayTaskList" value="${taskList}" />
<c:if test="${empty displayTaskList}">
    <c:set var="displayTaskList" value="${displayProject.taskList}" />
</c:if>

<c:set var="displayWorkLogList" value="${latestWorkLogList}" />
<c:if test="${empty displayWorkLogList}">
    <c:set var="displayWorkLogList" value="${workLogList}" />
</c:if>

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>案件詳細</title>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/project.css">
</head>

<body>
<%@ include file="/WEB-INF/jsp/header.jsp" %>

<main class="main">

    <div class="sub-header-left">
        <img class="regist-elephant"
             src="${pageContext.request.contextPath}/img/elephant(1).png"
             alt="案件詳細">

        <div>
            <h1 class="title-main">案件詳細</h1>
            <p class="title-sub">案件詳細と関連タスク・工数ログを確認出来ます</p>
        </div>
    </div>

    <div class="detail-button-area">

        <form action="${pageContext.request.contextPath}/Controller" method="get">
            <input type="hidden" name="page_id" value="P001">
            <button type="submit" class="back-btn">
                戻る
            </button>
        </form>

        <form action="${pageContext.request.contextPath}/Controller" method="get">
            <input type="hidden" name="page_id" value="P004">
            <input type="hidden" name="project_id" value="${displayProject.projectId}">
            <button type="submit" class="edit-btn">
                編集
            </button>
        </form>

        <form action="${pageContext.request.contextPath}/Controller" method="get">
            <input type="hidden" name="page_id" value="T003">
            <input type="hidden" name="project_id" value="${displayProject.projectId}">
            <button type="submit" class="task-add-btn">
                ＋タスク追加
            </button>
        </form>

    </div>

    <div class="syousai1">
        <div class="detail-row">
            <label>案件コード</label>
            <span><c:out value="${displayProject.projectCode}" /></span>
        </div>

        <div class="detail-row">
            <label>案件名</label>
            <span><c:out value="${displayProject.projectName}" /></span>
        </div>

        <div class="detail-row">
            <label>顧客名</label>
            <span><c:out value="${displayProject.customerName}" /></span>
        </div>

        <div class="detail-row">
            <label>PM</label>
            <span>
                <c:choose>
                    <c:when test="${not empty displayProject.projectManagerName}">
                        <c:out value="${displayProject.projectManagerName}" />
                    </c:when>
                    <c:otherwise>
                        <c:out value="${displayProject.projectManagerId}" />
                    </c:otherwise>
                </c:choose>
            </span>
        </div>

        <div class="detail-row">
            <label>ステータス</label>
            <span><c:out value="${displayProject.status}" /></span>
        </div>

        <div class="detail-row">
            <label>優先度</label>
            <span><c:out value="${displayProject.priority}" /></span>
        </div>

        <div class="detail-row">
            <label>期間</label>
            <span>
                <c:out value="${displayProject.startDate}" />
                ～
                <c:out value="${displayProject.dueDate}" />
            </span>
        </div>

        <div class="detail-row">
            <label>見積工数</label>
            <span><c:out value="${displayProject.estimatedManhours}" />h</span>
        </div>

        <div class="detail-row">
            <label>実績工数</label>
            <span><c:out value="${displayProject.actualManhours}" />h</span>
        </div>

        <div class="detail-row">
            <label>進捗</label>
            <span><c:out value="${displayProject.progressRate}" />%</span>
        </div>

        <div class="detail-row detail-description">
            <label>説明</label>
            <span><c:out value="${displayProject.description}" /></span>
        </div>
    </div>

    <div class="syosai2-1">
        <p class="title2">
            関連タスク一覧
            <span class="detail-count">
                ${fn:length(displayTaskList)}件
            </span>
        </p>

        <div class="syousai2-2">
            <table>
                <thead>
                    <tr>
                        <th>タスク名</th>
                        <th>担当者</th>
                        <th>ステータス</th>
                        <th>期限</th>
                        <th>見積工数</th>
                        <th>実績工数</th>
                        <th>進捗</th>
                        <th>操作</th>
                    </tr>
                </thead>

                <tbody>
                    <c:forEach var="task" items="${displayTaskList}">
                        <tr>
                            <td>
                                <a href="${pageContext.request.contextPath}/Controller?page_id=T002&task_id=${task.taskId}">
                                    <c:out value="${task.taskName}" />
                                </a>
                            </td>

                            <td>
                                <c:choose>
                                    <c:when test="${not empty task.managerName}">
                                        <c:out value="${task.managerName}" />
                                    </c:when>
                                    <c:otherwise>
                                        <c:out value="${task.managerId}" />
                                    </c:otherwise>
                                </c:choose>
                            </td>

                            <td>
                                <c:out value="${task.status}" />
                            </td>

                            <td>
                                <c:out value="${task.dueDate}" />
                            </td>

                            <td>
                                <c:out value="${task.estimatedManhours}" />h
                            </td>

                            <td>
                                <c:out value="${task.actualManhours}" />h
                            </td>

                            <td>
                                <progress value="${task.progress}" max="100"></progress>
                                <c:out value="${task.progress}" />%
                            </td>

                            <td>
                                <div class="table-action-area">
                                    <form action="${pageContext.request.contextPath}/Controller" method="get">
                                        <input type="hidden" name="page_id" value="T004">
                                        <input type="hidden" name="task_id" value="${task.taskId}">
                                        <button type="submit" class="small-edit-btn">
                                            編集
                                        </button>
                                    </form>

                                    <form action="${pageContext.request.contextPath}/Controller"
                                          method="post"
                                          data-confirm="このタスクを削除しますか">
                                        <input type="hidden" name="page_id" value="T002">
                                        <input type="hidden" name="project_id" value="${displayProject.projectId}">
                                        <input type="hidden" name="task_id" value="${task.taskId}">
                                        <button type="submit" name="button_id" value="削除" class="small-delete-btn">
                                            削除
                                        </button>
                                    </form>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>

                    <c:if test="${empty displayTaskList}">
                        <tr>
                            <td colspan="8" class="empty-cell">
                                関連タスクがありません
                            </td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>

    <div class="syousai3-1">
        <p class="title3">
            工数ログ
            <span class="detail-count">
                ${fn:length(displayWorkLogList)}件
            </span>
        </p>

        <div class="syousai3-2">
            <table>
                <thead>
                    <tr>
                        <th>作業日</th>
                        <th>タスク名</th>
                        <th>担当者</th>
                        <th>工数</th>
                        <th>作業内容</th>
                    </tr>
                </thead>

                <tbody>
                    <c:forEach var="log" items="${displayWorkLogList}">
                        <tr>
                            <td><c:out value="${log.workDate}" /></td>
                            <td><c:out value="${log.taskName}" /></td>
                            <td><c:out value="${log.userName}" /></td>
                            <td><c:out value="${log.manHours}" />h</td>
                            <td><c:out value="${log.jobContents}" /></td>
                        </tr>
                    </c:forEach>

                    <c:if test="${empty displayWorkLogList}">
                        <tr>
                            <td colspan="5" class="empty-cell">
                                工数ログがありません
                            </td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>

</main>

<%@ include file="/WEB-INF/jsp/footer.jsp" %>

<script src="${pageContext.request.contextPath}/js/common.js"></script>
</body>
</html>