<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="displayTaskList" value="${taskList}" />
<c:if test="${empty displayTaskList}">
    <c:set var="displayTaskList" value="${list}" />
</c:if>

<!DOCTYPE html>
<html lang="ja">

<head>
<meta charset="UTF-8">
<title>タスク一覧</title>

<link rel="stylesheet"
    href="https://cdn.datatables.net/t/bs-3.3.6/jqc-1.12.0,dt-1.10.11/datatables.min.css">

<link rel="stylesheet"
    href="${pageContext.request.contextPath}/css/common.css">

<link rel="stylesheet"
    href="${pageContext.request.contextPath}/css/task.css">
</head>

<body>
    <%@ include file="/WEB-INF/jsp/header.jsp"%>

    <div class="main">
        <div class="under-header">
            <img class="regist-elephant"
                src="${pageContext.request.contextPath}/img/elephant(1).png"
                alt="タスク一覧">

            <div class="text-wrap">
                <h3>タスク一覧</h3>
                <h4>登録済みタスクの検索・確認ができます</h4>
            </div>
        </div>

        <div class="member-dashboard">
            <div class="member-count">
                <img src="${pageContext.request.contextPath}/img/owntask.png"
                    width="100" height="100" alt="全タスク">

                <span>
                    <span class="block-box">全タスク</span>
                    <span class="actual-member-count" id="taskTotalCount">0</span>件
                </span>
            </div>

            <div class="member-count">
                <img src="${pageContext.request.contextPath}/img/clockmark.png"
                    width="100" height="100" alt="進行中">

                <span>
                    <span class="block-box">進行中</span>
                    <span class="actual-member-count" id="taskProgressCount">0</span>件
                </span>
            </div>

            <div class="member-count">
                <img src="${pageContext.request.contextPath}/img/warning.png"
                    width="100" height="100" alt="期限超過">

                <span>
                    <span class="block-box">期限超過</span>
                    <span class="actual-member-count" id="taskOverdueCount">0</span>件
                </span>
            </div>

            <div class="member-count">
                <img src="${pageContext.request.contextPath}/img/done.png"
                    width="100" height="100" alt="完了タスク">

                <span>
                    <span class="block-box">完了タスク</span>
                    <span class="actual-member-count" id="taskDoneCount">0</span>件
                </span>
            </div>
        </div>
    </div>

    <input type="hidden" id="login-user-name" value="<c:out value='${loginUser.name}' />">

    <div class="member-search">
        <table>
            <tr>
                <td>キーワード</td>
                <td>
                    <input type="text"
                        id="task-keyword-filter"
                        class="keyword"
                        name="keyword"
                        value=""
                        placeholder="タスク名で検索">
                </td>

                <td>案件</td>
                <td>
                    <select id="task-project-filter">
                        <option value="">すべて</option>

                        <c:forEach var="project" items="${projectList}">
                            <option value="${project.projectName}">
                                <c:out value="${project.projectName}" />
                            </option>
                        </c:forEach>
                    </select>
                </td>

                <td>ステータス</td>
                <td>
                    <select id="task-status-filter">
                        <option value="">すべて</option>
                        <option value="未着手">未着手</option>
                        <option value="進行中">進行中</option>
                        <option value="完了">完了</option>
                        <option value="保留">保留</option>
                    </select>
                </td>
            </tr>
        </table>

        <table>
            <tr>
                <td>担当者</td>
                <td>
                    <select id="task-manager-filter">
                        <option value="">すべて</option>

                        <c:forEach var="user" items="${userList}">
                            <option value="${user.name}">
                                <c:out value="${user.name}" />
                            </option>
                        </c:forEach>
                    </select>
                </td>

                <td>優先度</td>
                <td>
                    <select id="task-priority-filter">
                        <option value="">すべて</option>
                        <option value="高">高</option>
                        <option value="中">中</option>
                        <option value="低">低</option>
                    </select>
                </td>

                <td></td>
                <td></td>
            </tr>
        </table>

        <table>
            <tr>
                <td colspan="3">


                        <button type="button"
                            class="clear-btn"
                            id="task-clear-button">
                            クリア
                        </button>

                        <button type="button"
                            class="submit-btn"
                            id="my-task-button"
                            data-active="false">
                            自分のタスク
                        </button>
                    </div>
                </td>

                <td colspan="3">
                    <div class="button_regist">
                        <a href="${pageContext.request.contextPath}/Controller?page_id=T003"
                           class="submit-btn2">
                            ＋タスク登録
                        </a>
                    </div>
                </td>
            </tr>
        </table>
    </div>

    <br>
    <br>

    <div class="member-list">
        <table border="1" id="foo-table">
            <thead>
                <tr>
                    <th>案件名</th>
                    <th>タスク名</th>
                    <th>担当者</th>
                    <th>ステータス</th>
                    <th>優先度</th>
                    <th>期限</th>
                    <th>見積工数</th>
                    <th>実績工数</th>
                    <th>操作</th>
                </tr>
            </thead>

            <tbody class="member-list">
                <c:forEach var="e" items="${displayTaskList}">
                    <tr>
                        <td>
                            <c:out value="${e.projectName}" />
                        </td>

                        <td>
                            <a href="${pageContext.request.contextPath}/Controller?page_id=T002&task_id=${e.taskId}">
                                <c:out value="${e.taskName}" />
                            </a>
                        </td>

                        <td>
                            <c:out value="${e.managerName}" />
                        </td>

                        <td>
                            <span class="task-status-text">
                                <c:out value="${e.status}" />
                            </span>
                        </td>

                        <td>
                            <span class="task-priority-text">
                                <c:out value="${e.priority}" />
                            </span>
                        </td>

                        <td>
                            <span class="task-due-date">
                                <c:out value="${e.dueDate}" />
                            </span>
                        </td>

                        <td>
                            <span>
                                <c:out value="${e.estimatedManhours}" />
                            </span>
                        </td>

                        <td>
                            <c:out value="${e.actualManhours}" />
                        </td>

                        <td>
                            <a href="${pageContext.request.contextPath}/Controller?page_id=T004&task_id=${e.taskId}">
                                編集
                            </a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

    <script
        src="https://cdn.datatables.net/t/bs-3.3.6/jqc-1.12.0,dt-1.10.11/datatables.min.js">
    </script>

    <script src="${pageContext.request.contextPath}/js/task.js"></script>

    <%@ include file="/WEB-INF/jsp/footer.jsp"%>
</body>

</html>