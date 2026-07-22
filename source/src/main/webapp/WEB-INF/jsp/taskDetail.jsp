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
<!--ここから案件詳細のjsp-->
	<h1>案件詳細</h1>
<!--ここまで案件詳細jsp-->
	
  <button onclick="openModal(${taskList.projectName},${taskList.taskName})">工数登録</button>

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