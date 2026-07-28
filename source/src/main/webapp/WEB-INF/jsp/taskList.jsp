<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <title>タスク一覧</title>

   <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
    <%@ include file="/WEB-INF/jsp/header.jsp" %>
</header>

<main class="task-container">

    <section class="page-header">

        <div class="header-image">
            <img src="${pageContextontextPath}/images/elephant.png
        </div>

        <div class="header-title">
            <h1>タスク一覧</h1>
            <p>登録済みタスクを検索・確認できます</p>
        </div>

    </section>

    <section class="summary-area">

        <!-- 全タスク -->
        <div class="summary-card">

            <div class="summary-icon">
                ${pageContext.request.contextPath}/images/all-task.png
            </div>

            <div>
                <div class="summary-title">全タスク</div>
                <div class="summary-count">
                    ${totalTaskCount}件
                </div>
            </div>

        </div>

        <!-- 進行中案件 -->
        <div class="summary-card">

            <div class="summary-icon">
                ${pageContext.request.contextPath}/images/status.png
            </div>

            <div>
                <div class="summary-title">進行中案件</div>
                <div class="summary-count">
                    ${progressTaskCount}件
                </div>
            </div>

        </div>

        <!-- 期限超過 -->
        <div class="summary-card">

            <div class="summary-icon">
                pageContext.request.contextPath}/images/warning.png"
                     alt="期限超過">
            </div>

            <div>
                <div class="summary-title">期限超過</div>
                <div class="summary-count danger">
                    ${overDueTaskCount}件
                </div>
            </div>

        </div>

        <!-- 完了 -->
        <div class="summary-card">

            <div class="summary-icon">
                ${pageContext.request.contextPath}/images/complete.png
            </div>

            <div>
                <div class="summary-title">完了</div>
                <div class="summary-count">
                    ${completedTaskCount}件
                </div>
            </div>

        </div>

    </section>

    <!-- 検索フォーム -->
    ${pageContext.request.contextPath}/task/list

        <section class="search-area">

            <div class="search-item">
                <label>キーワード</label>
                <input type="text"
                       name="keyword"
                       class="input-text"
                       value="${keyword}">
            </div>

            <div class="search-item">
                <label>案件</label>
                <select name="projectId">

                    <option value="">すべて</option>

                    <c:forEach items="${projectList}" var="project">
                        <option value="${project.projectId}">
                            ${project.projectName}
                        </option>
                    </c:forEach>

                </select>
            </div>

            <div class="search-item">
                <label>ステータス</label>
                <select name="status">
                    <option value="">すべて</option>
                    <option value="未着手">未着手</option>
                    <option value="進行中">進行中</option>
                    <option value="完了">完了</option>
                    <option value="中止">中止</option>
                </select>
            </div>

            <div class="search-item">
                <label>担当者</label>
                <select name="managerId">

                    <option value="">すべて</option>

                    <c:forEach items="${userList}" var="user">
                        <option value="${user.userId}">
                            ${user.name}
                        </option>
                    </c:forEach>

                </select>
            </div>

            <div class="search-item">
                <label>優先度</label>
                <select name="priority">
                    <option value="">すべて</option>
                    <option value="高">高</option>
                    <option value="中">中</option>
                    <option value="低">低</option>
                </select>
            </div>

        </section>

        <section class="button-area">

            <label>
                <input type="checkbox"
                       name="overdueOnly"
                       value="true">
                期限超過のみ
            </label>

            <button type="submit" class="submit-btn">
                検索
            </button>

            <button type="reset" class="clear-btn">
                クリア
            </button>

            <button type="submit"
                    name="myTask"
                    value="true"
                    class="submit-btn">
                自分のタスク
            </button>

            pageContext.request.contextPath/task/add"
               class="submit-btn">
                ＋ タスク登録
            </a>

        </section>

    </form>

    <!-- 件数 -->
    <div class="result-header">
        <span class="result-count">
            ${pageInfo.totalCount}件
        </span>
    </div>

    <!-- タスク一覧 -->
    <div class="table-wrapper">

        <table class="task-table">

            <thead>
            <tr>
                <th>案件名</th>
                <th>タスク名</th>
                <th>担当者</th>
                <th>ステータス</th>
                <th>優先度</th>
                <th...