<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>案件登録画面</title>

<link rel="stylesheet"
      href="${pageContext.request.contextPath}/">

</head>

<body>

<%@ include file="/WEB-INF/jsp/header.jsp" %>

<form id="projectForm"
      method="POST"
      action="ProjectServlet?action=regist">

    <!-- 上の画像-->
        <div>
            <h1 class="title-main">案件登録</h1>
            <p class="title-sub">
                新しい案件詳細を入力してください。
            </p>
           </div>


    <!-- 右側説明 -->
    <div class="sub-header-right">
        象さんの画像

        <p>入力内容を確認して保存しましょう</p>
        <p>担当PMは有効なメンバーから選択してください</p>
        <p>保存後は案件一覧に戻ります</p>
    </div>

    <!-- 案件コード -->
    <div class="field">
        <label>
            案件コード
            <span class="must">必須</span>
        </label>

        <input type="text"
               id="project_code"
               name="project_code">
    </div>

    <div class="error">
        ${errorMessage}
    </div>

    <!-- 案件名 -->
    <div class="field">
        <label>
            案件名
            <span class="must">必須</span>
        </label>

        <input type="text"
               id="project_name"
               name="project_name">
    </div>

    <!-- 顧客名 -->
    <div class="field">
        <label>顧客名</label>

        <input type="text"
               id="customer_name"
               name="customer_name">
    </div>

    <!-- 担当PM -->
    <div class="field">
        <label>
            担当PM
            <span class="must">必須</span>
        </label>

        <select id="project_manager_id"
                name="project_manager_id">
        </select>
    </div>

    <!-- ステータス -->
    <div class="field">
        <label>
            ステータス
            <span class="must">必須</span>
        </label>

        <select id="status"
                name="status">
            <option value="in_progress">進行中</option>
            <option value="done">完了</option>
            <option value="canceled">中止</option>
        </select>
    </div>

    <!-- 優先度 -->
    <div class="field">
        <label>
            優先度
            <span class="must">必須</span>
        </label>

        <select id="priority"
                name="priority">
            <option value="middle">中</option>
            <option value="high">高</option>
            <option value="low">低</option>
        </select>
    </div>

    <!-- 見積工数 -->
    <div class="field">
        <label>
            見積工数
            <span class="must">必須</span>
        </label>

        <input type="text"
               id="estimated_manhours"
               name="estimated_manhours">
    </div>

    <!-- 実績工数 -->
    <div class="field">
        <label>実績工数</label>

        <input type="text"
               id="actual_manhours"
               name="actual_manhours">
    </div>

    <!-- 開始日 -->
    <div class="field">
        <label>
            開始日
            <span class="must">必須</span>
        </label>

        <input type="text"
               id="start_date"
               name="start_date"
               placeholder="YYYY/MM/DD">
    </div>

    <!-- 期限 -->
    <div class="field">
        <label>
            期限
            <span class="must">必須</span>
        </label>

        <input type="text"
               id="due_date"
               name="due_date"
               placeholder="YYYY/MM/DD">
    </div>

    <!-- 説明 -->
    <div class="field">
        <label>説明</label>

        <input type="text"
               id="description"
               name="description">
    </div>

    <!-- 保存 -->
    <button type="submit">
        保存
    </button>

</form>

<!-- 戻る -->
<button type ="button" id=back>
    戻る
</button>

<!-- キャンセル（入力クリア）-->
<button type="button"
        id="clearBtn">
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