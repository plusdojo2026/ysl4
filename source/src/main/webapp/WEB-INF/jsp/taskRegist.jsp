<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>タスク登録画面</title>

<link rel="stylesheet"
      href="${pageContext.request.contextPath}/css/common.css">

</head>

<body>

<%@ include file="/WEB-INF/jsp/header.jsp" %>



    <!-- 上の画像-->
        <div>
            <h1 class="title-main">タスク登録</h1>
            <p class="title-sub">
                新しいタスク詳細を入力してください。
            </p>
           </div>


    <!-- 右側説明 -->
    <div class="sub-header-right">
        <img class="regist-elephant"
				src="${pageContext.request.contextPath}/img/smileelephant.png">

        <p>新しいタスクを入力してください</p>
       
    </div>

    <!-- 案件コード -->
    <div class="field">
        <label>
            案件コード
            <c:out value="${(task.projectCode}" />
		
        </label>
    </div>

    <div class="error">
        ${errorMessage}
    </div>
	<form >
    <!-- 案件名 -->
    <div class="field">
        <label>
         案件名
         <span class="must">必須</span>
          <c:forEach var="pib" items="${projectList}" >
	          	<select name="status">
	           		<option><c:out value="${pib.pojectName}" /></option>
				</select>
			</c:forEach>
        </label>

        
    </div>

    <!-- タスク名 -->
    <form id="projectForm" method="POST" action="<c:url value='/Controller'/>">
    <input type="hidden" name="page_id" value="T003">
    <div class="field">
        <label>タスク名</label>

        <input type="text" id="task_name" name="task_name">
    </div>

    <!-- ステータス -->
    <div class="field">
        <label>
            ステータス<span class="must">必須</span>
        </label>

        <select id="status" name="status">
	        <option value="not_started">未着手</option>
	        <option value="in_progress">進行中</option>
	        <option value="done">完了</option>
	        <option value="on_hold">保留</option>
        </select>
    </div>

    <!-- 優先度 -->
    <div class="field">
        <label>
            優先度
            <span class="must">必須</span>
        </label>

        <select id="priority"name="priority">
            <option value="in_progress">進行中</option>
            <option value="done">完了</option>
            <option value="canceled">中止</option>
        </select>
    </div>

    <!-- 担当者 -->
    <div class="field">
        <label>
            担当者
            <span class="must">必須</span>
        </label>
		<c:forEach var="uib" items="${userList}" >
	        <select id="assigne" name="assigne">
		    	<option><c:out value="${uib.name}" /></option>
	        </select>
        </c:forEach>
    </div>

    <!-- 開始日 -->
    <div class="field">
        <label>
            開始日
            <span class="must">必須</span>
        </label>

        <input type="date" id="start_date" name="start_date" placeholder="YYYY/MM/DD">
    </div>

    <!-- 期限 -->
    <div class="field">
        <label>期限</label>

        <input type="date" id="due_date" name="due_date" placeholder="YYYY/MM/DD">
    </div>

    <!-- 見積もり工数 -->
    <div class="field">
        <label>
            見積もり工数
            <span class="must">必須</span>
        </label>

        <input type="text" id="estimated-manhours" name="estimated-manhours">
    </div>

    <!-- 進捗率 -->
    <div class="field">
        <label>
            進捗率
            <span class="must">必須</span>
        </label>

        <input type="range" name="progress" min="0" max="100" step="5" value="0">
    </div>

    <!-- 説明 -->
    <div class="field">
        <label>説明</label>

        <input type="text" id="description" name="description" style="width: 500px;">
    </div>

    <!-- 保存 -->
    <button type="submit" name="botton_id" value="登録">
        保存
    </button>

</form>

<!-- 戻る -->
<button type ="button" id="back" onclick="history.back()">
    戻る
</button>

<!-- キャンセル（入力クリア）-->
<button type="button" id="clearBtn">
    キャンセル
</button>

<script>
document.getElementById("clearBtn")
    .addEventListener("click", function () {
        document.getElementById("projectForm").reset();
    });
</script>

</body>
</html>