<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ja">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>タスク編集画面</title>

    <link
        rel="stylesheet"
        href="${pageContext.request.contextPath}/css/common.css"
    >

    <link
        rel="stylesheet"
        href="${pageContext.request.contextPath}/css/task.css"
    >
</head>

<body class="task-edit-page">

    <%@ include file="/WEB-INF/jsp/header.jsp" %>

    <main class="task-edit-main">

        <!-- 画面タイトル -->
        <section class="task-edit-headline">

            <!-- タイトル -->
            <div class="task-edit-title-area">

                <h1 class="title-main">
                    タスク編集
                </h1>

                <p class="title-sub">
                    登録済みタスクの内容を編集してください
                </p>

            </div>

            <!-- 画面案内 -->
            <div class="task-edit-guide">

                <img
                    class="task-edit-elephant"
                    src="${pageContext.request.contextPath}/img/smileelephant.png"
                    alt="タスク編集"
                >

                <p>
                    登録済みタスク情報を更新してください
                </p>

            </div>

        </section>

        <!-- エラーメッセージ -->
        <c:if test="${not empty errMsg}">

            <div class="task-edit-error">
                <c:out value="${errMsg}" />
            </div>

        </c:if>

        <!-- タスク編集フォーム -->
        <form
            id="taskEditForm"
            class="task-edit-form"
            method="POST"
            action="<c:url value='/Controller' />"
        >

            <!-- 画面ID -->
            <input
                type="hidden"
                name="page_id"
                value="T004"
            >

            <!-- 更新対象タスクID -->
            <input
                type="hidden"
                name="task_id"
                value="<c:out value='${task.taskId}' />"
            >

            <!-- 案件ID -->
            <input
                type="hidden"
                name="project_id"
                value="<c:out value='${task.projectId}' />"
            >

            <!-- 案件コード -->
            <input
                type="hidden"
                name="projectCode"
                value="<c:out value='${task.projectCode}' />"
            >

            <!-- 入力領域 -->
            <div class="task-edit-content">

                <!-- 編集項目 -->
                <div class="task-edit-fields">

                    <!-- 案件情報 -->
                    <section class="task-edit-section">

                        <h2 class="task-edit-section-title">
                            案件情報
                        </h2>

                        <div class="task-edit-grid">

                            <!-- 案件コード -->
                            <div class="field">

                                <label>
                                    案件コード
                                </label>

                                <div class="task-edit-readonly">

                                    <c:out value="${task.projectCode}" />

                                </div>

                            </div>

                            <!-- 案件名 -->
                            <div class="field">

                                <label>
                                    案件名
                                </label>

                                <div class="task-edit-readonly">

                                    <c:choose>

                                        <c:when test="${not empty selectedProject}">
                                            <c:out value="${selectedProject.projectName}" />
                                        </c:when>

                                        <c:otherwise>
                                            <c:out value="${task.projectName}" />
                                        </c:otherwise>

                                    </c:choose>

                                </div>

                            </div>

                        </div>

                    </section>

                    <!-- 基本情報 -->
                    <section class="task-edit-section">

                        <h2 class="task-edit-section-title">
                            基本情報
                        </h2>

                        <div class="task-edit-grid">

                            <!-- タスク名 -->
                            <div class="field field-full">

                                <label for="task_name">
                                    タスク名
                                    <span class="must">必須</span>
                                </label>

                                <input
                                    type="text"
                                    id="task_name"
                                    name="task_name"
                                    value="<c:out value='${task.taskName}' />"
                                    maxlength="100"
                                    required
                                >

                            </div>

                            <!-- ステータス -->
                            <div class="field">

                                <label for="status">
                                    ステータス
                                    <span class="must">必須</span>
                                </label>

                                <select
                                    id="status"
                                    name="status"
                                    required
                                >

                                    <option
                                        value="not-started"
                                        <c:if test="${task.status == 'not-started'}">
                                            selected
                                        </c:if>
                                    >
                                        未着手
                                    </option>

                                    <option
                                        value="in-progress"
                                        <c:if test="${task.status == 'in-progress'}">
                                            selected
                                        </c:if>
                                    >
                                        進行中
                                    </option>

                                    <option
                                        value="done"
                                        <c:if test="${task.status == 'done'}">
                                            selected
                                        </c:if>
                                    >
                                        完了
                                    </option>

                                    <option
                                        value="on-hold"
                                        <c:if test="${task.status == 'on-hold'}">
                                            selected
                                        </c:if>
                                    >
                                        保留
                                    </option>

                                </select>

                            </div>

                            <!-- 優先度 -->
                            <div class="field">

                                <label for="priority">
                                    優先度
                                    <span class="must">必須</span>
                                </label>

                                <select
                                    id="priority"
                                    name="priority"
                                    required
                                >

                                    <option
                                        value="high"
                                        <c:if test="${task.priority == 'high'}">
                                            selected
                                        </c:if>
                                    >
                                        高
                                    </option>

                                    <option
                                        value="medium"
                                        <c:if test="${task.priority == 'medium'}">
                                            selected
                                        </c:if>
                                    >
                                        中
                                    </option>

                                    <option
                                        value="low"
                                        <c:if test="${task.priority == 'low'}">
                                            selected
                                        </c:if>
                                    >
                                        低
                                    </option>

                                </select>

                            </div>

                            <!-- 担当者 -->
                            <div class="field field-full">

                                <label for="manager_id">
                                    担当者
                                    <span class="must">必須</span>
                                </label>

                                <select
                                    id="manager_id"
                                    name="manager_id"
                                    required
                                >

                                    <option value="">
                                        担当者を選択してください
                                    </option>

                                    <c:forEach
                                        var="uib"
                                        items="${userList}"
                                    >

                                        <option
                                            value="<c:out value='${uib.userId}' />"
                                            <c:if test="${task.managerId == uib.userId}">
                                                selected
                                            </c:if>
                                        >
                                            <c:out value="${uib.name}" />
                                        </option>

                                    </c:forEach>

                                </select>

                            </div>

                        </div>

                    </section>

                    <!-- 日程と工数 -->
                    <section class="task-edit-section">

                        <h2 class="task-edit-section-title">
                            日程と工数
                        </h2>

                        <div class="task-edit-grid">

                            <!-- 開始日 -->
                            <div class="field">

                                <label for="start_date">
                                    開始日
                                    <span class="must">必須</span>
                                </label>

                                <input
                                    type="date"
                                    id="start_date"
                                    name="start_date"
                                    value="<c:out value='${task.startDate}' />"
                                    required
                                >

                            </div>

                            <!-- 期限 -->
                            <div class="field">

                                <label for="due_date">
                                    期限
                                </label>

                                <input
                                    type="date"
                                    id="due_date"
                                    name="due_date"
                                    value="<c:out value='${task.dueDate}' />"
                                >

                            </div>

                            <!-- 見積工数 -->
                            <div class="field">

                                <label for="estimated_manhours">
                                    見積工数
                                    <span class="must">必須</span>
                                </label>

                                <div class="task-edit-unit-input">

                                    <input
                                        type="number"
                                        id="estimated_manhours"
                                        name="estimated_manhours"
                                        value="<c:out value='${task.estimatedManhours}' />"
                                        min="0"
                                        step="0.5"
                                        required
                                    >

                                    <span>
                                        h
                                    </span>

                                </div>

                            </div>

                            <!-- 進捗率 -->
                            <div class="field">

                                <div class="task-edit-progress-heading">

                                    <label for="progress">
                                        進捗率
                                        <span class="must">必須</span>
                                    </label>

                                    <output
                                        id="progressValue"
                                        for="progress"
                                    >
                                        <c:out value="${task.progress}" />%
                                    </output>

                                </div>

                                <input
                                    type="range"
                                    id="progress"
                                    name="progress"
                                    min="0"
                                    max="100"
                                    step="5"
                                    value="<c:out value='${task.progress}' />"
                                >

                                <div class="task-edit-progress-scale">

                                    <span>0%</span>
                                    <span>50%</span>
                                    <span>100%</span>

                                </div>

                            </div>

                            <!-- 説明 -->
                            <div class="field field-full">

                                <label for="description">
                                    説明
                                </label>

                                <textarea
                                    id="description"
                                    name="description"
                                    maxlength="1000"
                                    rows="6"
                                ><c:out value="${task.description}" /></textarea>

                            </div>

                        </div>

                    </section>

                </div>

                <!-- 工数と進捗の概要 -->
                <aside class="task-edit-summary">

                    <h2 class="task-edit-summary-title">
                        タスク概要
                    </h2>

                    <!-- 見積工数 -->
                    <div class="task-edit-summary-card">

                        <img
                            src="${pageContext.request.contextPath}/img/estmanhours.png"
                            alt=""
                        >

                        <div>

                            <span class="task-edit-summary-label">
                                見積工数
                            </span>

                            <strong id="estimatedSummary">
                                <c:out value="${task.estimatedManhours}" /> h
                            </strong>

                        </div>

                    </div>

                    <!-- 実績工数 -->
                    <div class="task-edit-summary-card">

                        <img
                            src="${pageContext.request.contextPath}/img/estmanhours.png"
                            alt=""
                        >

                        <div>

                            <span class="task-edit-summary-label">
                                実績工数
                            </span>

                            <strong>
                                <c:out value="${task.actualManhours}" /> h
                            </strong>

                        </div>

                    </div>

                    <!-- タスク進捗 -->
                    <div class="task-edit-summary-card">

                        <img
                            src="${pageContext.request.contextPath}/img/owntask.png"
                            alt=""
                        >

                        <div>

                            <span class="task-edit-summary-label">
                                タスク進捗
                            </span>

                            <strong id="progressSummary">
                                <c:out value="${task.progress}" />%
                            </strong>

                        </div>

                    </div>

                    <!-- 更新日時 -->
                    <div class="task-edit-date-info">

                        <span>
                            最終更新
                        </span>

                        <strong>
                            <c:choose>

                                <c:when test="${not empty task.updatedAt}">
                                    <c:out value="${task.updatedAt}" />
                                </c:when>

                                <c:otherwise>
                                    未更新
                                </c:otherwise>

                            </c:choose>
                        </strong>

                    </div>

                </aside>

            </div>

            <!-- 操作ボタン -->
            <div class="task-edit-button-area">

                <!-- 保存 -->
                <button
                    type="submit"
                    class="task-edit-save-button"
                    name="button_id"
                    value="更新"
                >
                    保存
                </button>

                <!-- 戻る -->
                <button
                    type="button"
                    class="task-edit-back-button"
                    id="backButton"
                >
                    戻る
                </button>

                <!-- 入力内容を元に戻す -->
                <button
                    type="button"
                    class="task-edit-reset-button"
                    id="resetButton"
                >
                    クリア
                </button>

            </div>

        </form>

    </main>

    <script>

        document.addEventListener("DOMContentLoaded", function () {

            const form = document.getElementById("taskEditForm")
            const progress = document.getElementById("progress")
            const progressValue = document.getElementById("progressValue")
            const progressSummary = document.getElementById("progressSummary")
            const estimatedManhours = document.getElementById("estimated_manhours")
            const estimatedSummary = document.getElementById("estimatedSummary")
            const backButton = document.getElementById("backButton")
            const resetButton = document.getElementById("resetButton")

            // 進捗率表示を更新
            function updateProgressDisplay() {

                const progressText = progress.value + "%"

                progressValue.textContent = progressText
                progressSummary.textContent = progressText

            }

            // 見積工数表示を更新
            function updateEstimatedDisplay() {

                const estimatedValue =
                    estimatedManhours.value === ""
                        ? "0"
                        : estimatedManhours.value

                estimatedSummary.textContent = estimatedValue + " h"

            }

            // 進捗率変更時
            progress.addEventListener("input", function () {

                updateProgressDisplay()

            })

            // 見積工数変更時
            estimatedManhours.addEventListener("input", function () {

                updateEstimatedDisplay()

            })

            // 戻るボタン押下時
            backButton.addEventListener("click", function () {

                history.back()

            })

            // キャンセルボタン押下時
            resetButton.addEventListener("click", function () {

                form.reset()
                updateProgressDisplay()
                updateEstimatedDisplay()

            })

            // 初期表示
            updateProgressDisplay()
            updateEstimatedDisplay()

        })

    </script>

</body>

</html>