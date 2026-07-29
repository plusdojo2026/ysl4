<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
      width: 750px;
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
	<%@ include file="/WEB-INF/jsp/header.jsp" %>

<!--ここから案件詳細のjsp-->
	<h1>案件詳細</h1>
	<p>タスク詳細と工数ログを確認できます</p>
	<div>
	案件詳細へ
	</div>
	<div>
	<a href="/ysl4/jsp/taskEdit">編集</a>
	</div>
	<div>
	<form action="/ysl4/jsp/your-endpoint" method="post" id="status">
	<select name="status">
	
	<option value="未着手">未着手</option>
	<option value="進行中">進行中</option>
	<option value="完了">完了</option>
	<option value="保留">保留</option>
	</select>
	<input type="botton" name="botton_id">
	</form>
	</div>
	
	<div>
	<div>
	<p>案件名</p>
	<c:out value="${taskList.projectName}" /><br>
	</div>
	<div>
	<p>担当者</p>
	<c:out value="${taskList.userName}" /><br>
	</div>
	<div>
	<p>ステータス</p>
	<c:out value="${taskList.status}" /><br>
	</div>
	<div>
	<p>優先度</p>
	<c:out value="${taskList.priority}" /><br>
	</div>
	<div>
	<p>開始日</p>
	<c:out value="${taskList.startDate}" /><br>
	</div>
	<div>
	<p>期限</p>
	<c:out value="${taskList.dueDate}" /><br>
	</div>
	<div>
	<p>説明</p>
	<c:out value="${taskList.discription}" /><br>
	</div>
	<div>
	<p>進捗</p>
	<c:out value="${taskList.progress}" />
	</div>
	<div>
	見積工数<c:out value="${taskList.estimatedManhours}" />h
	</div>
	<div>
	実績工数<c:out value="${task.actualManhours}" />h
	</div>
	<div>
	進捗率
	<c:out value="${taskList.progress}" />%
	</div>
	<div>
	残工数
	<c:out value="${taskList.estimatedManhours-task.actualManhours}" />h
	</div>
	
	</div>
	<c:forEach var="uib" items="${workLogList}" >
	<input type="hidden" name="page_id" value="M001">
	<div>作業日<c:out value="${uib.createdAt}" /></div>>
	<div>担当者<c:out value="${uib.name}" /></div>
	<div>工数<c:out value="${uib.}" /></div>
	<div>作業内容<c:out value="${taskList.progress}" /></div>
	<div>操作</div>
	<input type="submit" name="botton_id" value="工数削除" onclick="deleteMessage()">
	</c:forEach>
	</div>
<!--ここまで案件詳細jsp-->
	
  <button onclick="openModal('${taskList.projectName}','${taskList.taskName}')">工数登録</button>

  <!-- モーダル本体 -->
  <div id="modal" class="modal-background">
    <div class="modal-content">
      <h2>工数入力</h2>
      <form method="POST" action="<c:url value='/Controller'/>">
      <input type="hidden" name="page_id" value="W001">
      作業者
      <input type="text" name="text" value="作業者">
      作業日
      <input type="date" name="text" value="作業日">
      工数
      <input type="text" name="text" value="工数"><br>
      作業内容
      <input type="text" name="text" value="作業内容"><br>
      <input type="reset" value="クリア">
      <input type="submit" name="botton_id" value="登録">
      </form>
      <button class="close-btn" onclick="closeModal()">閉じる</button>
   
  <script src="${pageContext.request.contextPath}/js/common.js"></script>
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

	<%@ include file="/WEB-INF/jsp/footer.jsp" %>
</body>
</html>