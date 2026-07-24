<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>案件詳細</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/common.css">
  <style>
    /* モーダルの背景（暗い部分） */
    .modal-background {
      display: none; /* 最初は非表示 */
      position: fixed;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      background-color: rgba(0,0,0,0.5);
      z-index: 10;
    }

    /* モーダルの本体 */
    .modal-content {
      background-color: white;
      width: 300px;
      margin: 100px auto;
      padding: 20px;
      border-radius: 10px;
      text-align: center;
      z-index: 11;
    }

    /* 閉じるボタン */
    .close-btn {
      margin-top: 10px;
    }
  </style>
</head>
<body>
<header>
<%@ include file="/WEB-INF/jsp/header.jsp" %>
</header>
<footer>
<%@ include file="/WEB-INF/jsp/footer.jsp" %>
</footer>
<!--ここから案件詳細のjsp-->
	<h1>案件詳細</h1>
	<p>タスク詳細と工数ログを確認できます</p>
	<div>
	案件詳細へ
	</div>
	<div>
	<>編集
	</div>
	<div>
	<input type= bottun name="" value="ステータス変更">
	</div>
	
	<div>
	案件名<c:out value="${taskList.projectName}" /><br>
	担当者<c:out value="${taskList.userName}" /><br>
	ステータス<c:out value="${taskList.status}" /><br>
	優先度<c:out value="${taskList.priority}" /><br>
	開始日<c:out value="${taskList.startDate}" /><br>
	期限<c:out value="${taskList.dueDate}" /><br>
	説明<c:out value="${taskList.discription}" /><br>
	進捗<c:out value="${taskList.progress}" />
	見積もり工数
	実績工数
	進捗率
	残工数
</div>
	<c:forEach var="uib" items="${workLogList}" >
	作業日
	担当者
	工数
	作業内容
	操作
	<input type="submit" name="botton" value="削除">
	</c:forEach>
<!--ここまで案件詳細jsp-->
	
  <button onclick="openModal('${taskList.projectName}','${taskList.taskName}')">工数登録</button>

  <!-- モーダル本体 -->
  <div id="modal" class="modal-background">
    <div class="modal-content">
      <h2>工数入力</h2>
      <form>
      作業者
      <input type="text" name="text" value="作業者">
      作業日
      <input type="text" name="text" value="作業日">
      工数
      <input type="text" name="text" value="工数"><br>
      作業内容
      <input type="text" name="text" value="作業内容"><br>
      <input type="reset" value="クリア">
      <input type="submit" name="botton" value="保存">
      </form>
      <button class="close-btn" onclick="closeModal()">閉じる</button>
   

  <script>
    // モーダル表示
    function openModal(projectName,taskName) {
      document.getElementById("modal").style.display = "block";
    }

    // モーダル非表示
    function closeModal() {
      document.getElementById("modal").style.display = "none";
    }
  </script>

</body>
</html>