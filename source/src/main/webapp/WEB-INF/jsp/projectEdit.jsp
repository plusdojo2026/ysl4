<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>案件編集</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/common.css">
<link rel="stylesheet" href="<c:url value='/css/project.css' />">
</head>

<body>

<%@ include file="/WEB-INF/jsp/header.jsp" %>
	<form method="POST" action="<c:url value='/Controller'/>">


<form id="projectForm"
      method="POST"
      action="ProjectServlet?action=regist">

		<!-- 上の左側の象さんの画像 -->
		<div class="sub -header-left">
			<img src=象さんの画像>

			<!-- タイトル部分 -->
			<div>
				<h1 class="title-main">案件編集</h1>
				<p class="title-sub">登録済み案件情報を更新してください。</p>
			</div>
		</div>

		<!-- 必要であればここにカレンダー画像 -->


        
		<!-- 右側の詳細 -->
		
		<form id="projectForms">
		<p class="sakuseisyousai"
		<!-- 案件コード -->
		<div class="field">
			<label>案件コード</label> <input type="text" name="project_code"
				value="${dto.projectCode}" readonly>
		</div>

		<!-- 案件名 まだ-->
		<div class="field">
			<label>案件名<span class="must">必須</span></label> <select
				name="project_name"></select>
		</div>

		<!-- 担当PM -->
		<div class="field">
			<label>担当PM<span class="must">必須</span></label> <select
				name="project_manager_id"></select>
		</div>

		<!-- 顧客名 -->
		<div class="field">
			<label>顧客名</label> <input type="text" id="customer_name"
				name="customer_name">
		</div>

		<!-- 開始日 -->
		<div class="field">
			<label>開始日<span class="must">必須</span></label> <input type="text"
				id="start_date" name="start_date" placeholder="YYYY/MM/DD">
		</div>

		<!-- 期限 -->
		<div class="field">
			<label>期限<span class="must">必須</span></label> <input type="text"
				id="due_date" name="due_date" placeholder="YYYY/MM/DD">
		</div>

		<!-- ステータス　まだ -->
		<div class="field">
			<label> ステータス <span class="must">必須</span>
			</label> <select id="status" name="status">
				<option value="in_progress">進行中</option>
				<option value="done">完了</option>
				<option value="canceled">中止</option>
			</select>
		</div>

		<!-- 優先度 　まだ-->
		<div class="field">
			<label> 優先度 <span class="must">必須</span>
			</label> <select id="priority" name="priority">
				<option value="middle">中</option>
				<option value="high">高</option>
				<option value="low">低</option>
			</select>
		</div>

		<!-- 見積工数 -->
		<div class="field">
			<label> 見積工数 <span class="must">必須</span>
			</label> <input type="text" id="estimated_manhours" name="estimated_manhours">
		</div>

		<!-- 実績工数 -->
		<div class="field">
			<label>実績工数</label> <input type="text" id="actual_manhours"
				name="actual_manhours">
		</div>

		<!-- 説明 -->
		分からん

        </form>
        
		<!--  予算工数　　ここから分からん-->
		<div class="kousuu"></div>


		<!--  実績工数-->
		<div class="kousuu"></div>


		<!--  予算消化率-->
		<div class="kousuu"></div>


		<!--  タスク進捗-->
		<div class="kousuu"></div>


		<!-- 戻るボタン -->
		<button type="button" onclick="history.back();">戻る</button>

		<!-- キャンセルボタン -->
		<button type="button" id="clearBtn">キャンセル</button>
		<script>
			document.getElementById("clearBtn").addEventListener("click",
					function() {
						document.getElementById("projectForms").reset();
					});
		</script>
		
		<script>

		<!-- 案件詳細ボタン -->
        function goToPage() {
            location.href = 'projectDetail.jsp';
        }
    </script>
    
    <button type="button" onclick="goToPage()">案件詳細へ</button>
</body>

      <!-- 保存ボタン -->
      分からん