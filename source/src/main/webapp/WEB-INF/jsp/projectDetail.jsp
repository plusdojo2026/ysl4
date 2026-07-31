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

<body class="member-page">
<%@ include file="/WEB-INF/jsp/header.jsp" %>

<main class="project-main">


<div class="atama">
<section class="project-hero">
    <div class="project-hero-image">
        <img class="regist-elephant"
             src="${pageContext.request.contextPath}/img/elephant(1).png"
             alt="案件詳細">
</div>


        <div class="project-hero-text">
            <h1 class="project-title">案件詳細</h1>
            <p class="subtitle">案件詳細と関連タスク・工数ログを確認出来ます</p>
        </div>
   

<section class="project-dashboard">
    <td colspan="3">
<div class="project-hero-action">
<table id ="aaa">
        <td><form action="${pageContext.request.contextPath}/Controller" method="get">
            <input type="hidden" name="page_id" value="P001">
            <button type="submit" id="back" class="back-btn">
                戻る
            </button>
        </form></td>

        <td><form action="${pageContext.request.contextPath}/Controller" method="get">
            <input type="hidden" name="page_id" value="P004">
            <input type="hidden" name="project_id" value="${displayProject.projectId}">
            <button type="submit" class="edit-btn">
                編集
            </button>
        </form></td>

        <td><form action="${pageContext.request.contextPath}/Controller" method="get">
            <input type="hidden" name="page_id" value="T003">
            <input type="hidden" name="project_id" value="${displayProject.projectId}">
            <button type="submit" class="task-add-btn">
                ＋タスク追加
            </button></td>
        </form>
        </table>
</div>
    </td>
    </section>
</div>
</section>
</div>


<div class="atama">
    <div class="syousai1">
        <div class="detail-row">
        <table border="1">
        <tr>
            <th>案件コード<br></th>
            <td><span><c:out value="${displayProject.projectCode}" /></span></td>
        

        
            <th>案件名</th>
            <td><span><c:out value="${displayProject.projectName}" /></span></td>
     

        
            <th>顧客名</th>
            <td><span><c:out value="${displayProject.customerName}" /></span></td>
       

       
            <th>PM</th>
            <td><span>
                <c:choose>
                    <c:when test="${not empty displayProject.projectManagerName}">
                        <c:out value="${displayProject.projectManagerName}" />
                    </c:when>
                    <c:otherwise>
                        <c:out value="${displayProject.projectManagerId}" />
                    </c:otherwise>
                </c:choose>
            </span></td>
            <th>ステータス</th>
            <td><span><c:out value="${displayProject.status}" /></span></td>
      </tr>
      <tr>

        
            
     

      
            <th>優先度</th>
            <td><span><c:out value="${displayProject.priority}" /></span></td>
        

        
            <th>期間</th>
            <td><span>
                <c:out value="${displayProject.startDate}" />
                ～
                <c:out value="${displayProject.dueDate}" />
            </span></td>
        

       
            <th>見積工数</th>
            <td><span><c:out value="${displayProject.estimatedManhours}" />h</span></td>
        

       
            <th>実績工数</th>
            <td><span><c:out value="${displayProject.actualManhours}" />h</span></td>
       

        
            <th>進捗</th>
            <td><span><c:out value="${displayProject.progressRate}" />%</span></td>
        </div>
</tr>
<tr>
        <div class="detail-row detail-description">
            <th>説明</th><br>
            <td colspan="9"><span><c:out value="${displayProject.description}" /></span></td>
            </tr>
            </table>
        </div>
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
            <table border="1" id="project-table" class="member-table table table-bordered">
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
                            <td><c:out value="${task.status}" /></td>
                            <td><c:out value="${task.dueDate}" /></td>
                            <td><c:out value="${task.estimatedManhours}" />h</td>
                            <td><c:out value="${task.actualManhours}" />h</td>
                            <td><progress value="${task.progress}" max="100"></progress>
                                <c:out value="${task.progress}" />%</td>
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
            <table border="1" id="project-table" class="member-table table table-bordered">
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