<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ja">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>タスク登録画面</title>

    <link
        rel="stylesheet"
        href="${pageContext.request.contextPath}/css/common.css"
    >

    <link
        rel="stylesheet"
        href="${pageContext.request.contextPath}/css/task.css"
    >
</head>

<body class="task-regist-page">

    <%@ include file="/WEB-INF/jsp/header.jsp" %>

    <main class="task-regist-main">

        <!-- 画面タイトル -->
        <section class="task-regist-headline">

            <!-- タイトル -->
            <div class="task-regist-title-area">

                <h1 class="title-main">
                    タスク登録
                </h1>

                <p class="title-sub">
                    新しいタスク詳細を入力してください
                </p>

            </div>

            <!-- 画面案内 -->
            <div class="task-regist-guide">

                <img
                    class="regist-elephant"
                    src="${pageContext.request.contextPath}/img/smileelephant.png"
                    alt="タスク登録"
                >

                <p>
                    新しいタスクを入力してください
                </p>

            </div>

        </section>

        <!-- エラーメッセージ -->
        <c:if test="${not empty errorMessage}">

            <div class="task-regist-error">
                <c:out value="${errorMessage}" />
            </div>

        </c:if>

        <!-- タスク登録フォーム -->
        <form
            id="projectForm"
            class="task-regist-form"
            method="POST"
            action="<c:url value='/Controller' />"
        >

            <!-- 画面ID -->
            <input
                type="hidden"
                name="page_id"
                value="T003"
            >

            <div class="task-regist-grid">

                <!-- 案件コード -->
                <div class="field field-project-code">

                    <label>
                        案件コード
                    </label>

                    <div class="project-code-value">
                        <c:out value="${task.projectCode}" />
                    </div>

                </div>

                <!-- 2列目の配置調整 -->
                <div class="field field-project-code-space"></div>

                <!-- 案件名 -->
                <div class="field">

                    <label for="project_name">
                        案件名
                        <span class="must">必須</span>
                    </label>

                    <select
                        id="project_name"
                        name="project_name"
                        required
                    >

                        <option value="">
                            案件を選択してください
                        </option>

                        <c:forEach
                            var="pib"
                            items="${projectList}"
                        >

                            <option value="${pib.projectName}">
                                <c:out value="${pib.projectName}" />
                            </option>

                        </c:forEach>

                    </select>

                </div>

                <!-- タスク名 -->
                <div class="field">

                    <label for="task_name">
                        タスク名
                    </label>

                    <input
                        type="text"
                        id="task_name"
                        name="task_name"
                        maxlength="100"
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

                        <option value="not_started">
                            未着手
                        </option>

                        <option value="in_progress">
                            進行中
                        </option>

                        <option value="done">
                            完了
                        </option>

                        <option value="on_hold">
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

                        <option value="in_progress">
                            中
                        </option>

                        <option value="done">
                            高
                        </option>

                        <option value="canceled">
                            低
                        </option>

                    </select>

                </div>

                <!-- 担当者 -->
                <div class="field">

                    <label for="assigne">
                        担当者
                        <span class="must">必須</span>
                    </label>

                    <select
                        id="assigne"
                        name="assigne"
                        required
                    >

                        <option value="">
                            担当者を選択してください
                        </option>

                        <c:forEach
                            var="uib"
                            items="${userList}"
                        >

                            <option value="${uib.name}">
                                <c:out value="${uib.name}" />
                            </option>

                        </c:forEach>

                    </select>

                </div>

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
                    >

                </div>

                <!-- 見積もり工数 -->
                <div class="field">

                    <label for="estimated-manhours">
                        見積もり工数
                        <span class="must">必須</span>
                    </label>

                    <div class="manhours-input-wrap">

                        <input
                            type="number"
                            id="estimated-manhours"
                            name="estimated-manhours"
                            min="0"
                            step="0.5"
                            required
                        >

                        <span class="input-unit">
                            時間
                        </span>

                    </div>

                </div>

                <!-- 進捗率 -->
                <div class="field field-progress">

                    <div class="progress-label-row">

                        <label for="progress">
                            進捗率
                            <span class="must">必須</span>
                        </label>

                        <output
                            id="progressValue"
                            for="progress"
                        >
                            0%
                        </output>

                    </div>

                    <input
                        type="range"
                        id="progress"
                        name="progress"
                        min="0"
                        max="100"
                        step="5"
                        value="0"
                    >

                    <div class="progress-scale">

                        <span>0%</span>
                        <span>50%</span>
                        <span>100%</span>

                    </div>

                </div>

                <!-- 説明 -->
                <div class="field field-description">

                    <label for="description">
                        説明
                    </label>

                    <textarea
                        id="description"
                        name="description"
                        maxlength="1000"
                        rows="5"
                    ></textarea>

                </div>

            </div>

            <!-- 操作ボタン -->
            <div class="task-regist-button-area">

                <!-- 保存 -->
                <button
                    type="submit"
                    class="task-regist-save-btn"
                    name="botton_id"
                    value="登録"
                >
                    保存
                </button>

                <!-- 戻る -->
                <button
                    type="button"
                    class="task-regist-back-btn"
                    id="back"
                >
                    戻る
                </button>

                <!-- 入力クリア -->
                <button
                    type="button"
                    class="task-regist-clear-btn"
                    id="clearBtn"
                >
                    クリア
                </button>

            </div>

        </form>

    </main>

    <script>

        document.addEventListener("DOMContentLoaded", function () {

            const projectForm = document.getElementById("projectForm")
            const clearBtn = document.getElementById("clearBtn")
            const back = document.getElementById("back")
            const progress = document.getElementById("progress")
            const progressValue = document.getElementById("progressValue")

            // 進捗率の表示を更新
            function updateProgressValue() {

                progressValue.textContent = progress.value + "%"

            }

            // 進捗率変更時
            progress.addEventListener("input", function () {

                updateProgressValue()

            })

            // 戻るボタン押下時
            back.addEventListener("click", function () {

                history.back()

            })

            // クリアボタン押下時
            clearBtn.addEventListener("click", function () {

                projectForm.reset()
                updateProgressValue()

            })

            // 初期表示
            updateProgressValue()

        })

    </script>

</body>

</html>