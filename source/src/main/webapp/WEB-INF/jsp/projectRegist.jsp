<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="displayProject" value="${project}" />
<c:if test="${empty displayProject}">
    <c:set var="displayProject" value="${projectDto}" />
</c:if>

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>案件登録画面</title>

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
        src="${pageContext.request.contextPath}/img/elephant(1).png" alt="案件登録">
    </div>


    <div class="project-hero-text">
        <h1 class="project-title">案件登録</h1>
        <p class="subtitle">新しい案件詳細を入力してください</p>
    </div>
</section>
</div>

    <form id="projectForm"
          method="post"
          action="${pageContext.request.contextPath}/Controller"
          novalidate>

        <input type="hidden" name="page_id" value="P003">


<div class="atama">
        <div class="sub-header-right">
            <div class="setumeidesu" style="border: 2px solid #F6ADC6;">
                入力内容を確認して保存しましょう<br>
                担当PMは有効なメンバーから選択してください<br>
                保存後は案件一覧に戻ります
            </div>
        </div>

        <c:if test="${not empty errMsg}">
            <div class="error">
                <c:out value="${errMsg}" />
            </div>
        </c:if>

        <c:if test="${not empty errorMessage}">
            <div class="error">
                <c:out value="${errorMessage}" />
            </div>
        </c:if>

        <div class="field-a">
            <table id="main-table">
                <tr>
                    <td>
                        案件コード
                        <span class="required-item">必須</span><br>

                        <input type="text"
                               id="project_code"
                               name="project_code"
                               value="${displayProject.projectCode}">
                    </td>

                    <td>
                        案件名
                        <span class="required-item">必須</span><br>

                        <input type="text"
                               id="project_name"
                               name="project_name"
                               value="${displayProject.projectName}">
                    </td>
                

                
                    <td>
                        顧客名<br>

                        <input type="text"
                               id="customer_name"
                               name="customer_name"
                               value="${displayProject.customerName}">
                    </td>
</tr>
<tr>
                    <td>
                        担当PM
                        <span class="required-item">必須</span><br>

                        <select id="project_manager_id"
                                name="project_manager_id">
                            <option value="">選択してください</option>

                            <c:forEach var="manager" items="${managerList}">
                                <option value="${manager.userId}"
                                    ${manager.userId == displayProject.projectManagerId ? 'selected' : ''}>
                                    <c:out value="${manager.name}" />
                                </option>
                            </c:forEach>

                            <c:if test="${empty managerList}">
                                <option value="" disabled>
                                    有効なメンバーがいません
                                </option>
                            </c:if>
                        </select>
                    </td>
                

                
                    <td>
                        ステータス
                        <span class="required-item">必須</span><br>

                        <select id="status"
                                name="status">
                            <option value="">選択してください</option>
                            <option value="進行中" ${displayProject.status == '進行中' ? 'selected' : ''}>進行中</option>
                            <option value="完了" ${displayProject.status == '完了' ? 'selected' : ''}>完了</option>
                            <option value="中止" ${displayProject.status == '中止' ? 'selected' : ''}>中止</option>
                        </select>
                    </td>

                    <td>
                        優先度
                        <span class="required-item">必須</span><br>

                        <select id="priority"
                                name="priority">
                            <option value="">選択してください</option>
                            <option value="中" ${displayProject.priority == '中' ? 'selected' : ''}>中</option>
                            <option value="高" ${displayProject.priority == '高' ? 'selected' : ''}>高</option>
                            <option value="低" ${displayProject.priority == '低' ? 'selected' : ''}>低</option>
                        </select>
                    </td>
                </tr>

                <tr>
                    <td>
                        見積工数
                        <span class="required-item">必須</span><br>

                        <input type="number"
                               id="estimated_manhours"
                               name="estimated_manhours"
                               min="0"
                               step="0.5"
                               value="${displayProject.estimatedManhours}">
                    </td>

                    <td>
                        実績工数<br>

                        <input type="number"
                               id="actual_manhours"
                               name="actual_manhours"
                               value="0"
                               readonly>
                    </td>
                

                
                    <td>
                        開始日
                        <span class="required-item">必須</span><br>

                        <input type="date"
                               id="start_date"
                               name="start_date"
                               value="${displayProject.startDate}">
                    </td>

                    <td>
                        期限
                        <span class="required-item">必須</span><br>

                        <input type="date"
                               id="due_date"
                               name="due_date"
                               value="${displayProject.dueDate}">
                    </td>
                </tr>

                <tr>
                    <td colspan="2">
                        説明<br>

                        <input type="text"
                               id="description"
                               name="description"
                               value="${displayProject.description}">
                    </td>
                </tr>
            </table>
        </div>
</div>


        <div class="projectbtn">
            <button type="button" id="back">
                戻る
            </button>

            <button type="button" id="clearBtn">
                キャンセル
            </button>

            <button type="submit" id="comp" name="button_id" value="登録">
                保存
            </button>
        </div>
    </form>

</main>

<%@ include file="/WEB-INF/jsp/footer.jsp" %>

<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script src="${pageContext.request.contextPath}/js/common.js"></script>
<script src="${pageContext.request.contextPath}/js/project.js"></script>
</body>
</html>