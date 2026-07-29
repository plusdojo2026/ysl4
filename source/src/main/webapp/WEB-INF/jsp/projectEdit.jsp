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


<form id="projectForm"
      method="POST"
      action="ProjectServlet?action=regist">

		<!-- 上の左側の象さんの画像 -->
		<div class="sub -header-left">
			<img class="regist-elephant"
				src="${pageContext.request.contextPath}/img/smileelephant.png">

			<!-- タイトル部分 -->
			<div>
				<h1 class="title-main">案件編集</h1>
				<p class="title-sub">登録済み案件情報を更新してください。</p>
			</div>
		</div>

		<!-- 必要であればここにカレンダー画像 -->


        
		<!-- 右側の詳細 -->
		
		<form id="projectForms" method="POST" action="<c:url value='/Controller'/>">
		<input type="hidden" name="page_id" value="P004">
		<p class="sakuseisyousai"
		<!-- 案件コード -->
		<div class="field">
		<table>
		<tr>
			<td>案件コード</td> <td><input type="text" name="project_code"
				value="${projectList.projectCode}" readonly></td>
		</div>

		<!-- 案件名 まだ-->
		<div class="field">
			<td>案件名<span class="repuired-item">必須</span></td> <td><select
				name="project_name" value="${projectList.projectName}"></select></td>
		</div>

		<!-- 担当PM -->
		<div class="field">
			<td>担当PM<span class="repuired-item">必須</span></td><td> <select
				name="project_manager_id" value="${projectList.projectManagerName}"></select></td>
		</div>
</tr>
<tr>
		<!-- 顧客名 -->
		<div class="field">
			<td>顧客名</td><td> <input type="text" id="customer_name"
				name="customer_name" value="${projectList.customerName}"></td>
		</div>

		<!-- 開始日 -->
		<div class="field">
			<td>開始日<span class="repuired-item">必須</span></td><td> <input type="text"
				id="start_date" name="start_date" value="${projectList.startDate}"
				placeholder="YYYY/MM/DD"></td>
		</div>

		<!-- 期限 -->
		<div class="field">
			<td>期限<span class="repuired-item">必須</span></td><td> <input type="date"
				id="due_date" name="due_date" value="${projectList.dueDate}"
				placeholder="YYYY/MM/DD"></td>
		</div>
</tr>
<tr>
		<!-- ステータス　まだ -->
		<div class="field">
			<td> ステータス <span class="repuired-item">必須</span>
			</td><td> <select id="status" name="status" value="${projectList.status}">
				<option value="in_progress">進行中</option>
				<option value="done">完了</option>
				<option value="canceled">中止</option>
			</select></td>
		</div>

		<!-- 優先度 　まだ-->
		<div class="field">
			<td> 優先度 <span class="repuired-item">必須</span>
			</td> <td><select id="priority" name="priority" value="${projectList.priority}">
				<option value="middle">中</option>
				<option value="high">高</option>
				<option value="low">低</option>
			</select></td>
		</div>

		<!-- 見積工数 -->
		<div class="field">
			<td> 
				見積工数 <span class="repuired-item">必須</span>
			</td> 
			<td><input type="text" id="estimated_manhours" name="estimated_manhours" 
			value="${projectList.estimatedManhours}"></td>
		</div>

		<!-- 実績工数 -->
		<div class="field">
			<td>
			実績工数
			</td> 
			<td><input type="text" id="actual_manhours"
				name="actual_manhours" value="${projectList.actualManhours}"></td>
		</div>
</tr>
<tr>
		<!-- 説明 -->
		<td><input type="text" value="${projectList.description}" class="descript" ></td>
		</table>
		
		</div>
		</tr>
		
		<!-- 保存ボタン -->
		<input type="submit" name="botton_id" value="更新">
        </form>
        
        
		<!--  予算工数　　ここから分からん-->
		<div class="kousuu">
		<table>
		<tr>
		<td><img class="regist-elephant"src="${pageContext.request.contextPath}/img/estmanhours.png">
		予算工数<c:out value="${projectList.estimatedManhours}" />h</td>
		</div>

		<!--  実績工数-->
		<div class="kousuu">
		<td><img class="regist-elephant"src="${pageContext.request.contextPath}/img/estmanhours.png">
		実績工数<c:out value="${projectList.actualManhours}" />h</td>
		</div>

		<!--  予算消化率-->
		<div class="kousuu">
		<td><img class="regist-elephant"src="${pageContext.request.contextPath}/img/advance.png">
		<a href=r/dke/sj><c:out value="${(projectList.actualManhours/projectList.estimatedManhours)*100}" /></a></td></div>


		<!--  タスク進捗-->
		<div class="kousuu">
		<td><img class="regist-elephant"src="${pageContext.request.contextPath}/img/owntask.png">
		<c:out value="${projectList.progressRate}" /></td></div></tr>
		<style>
  body {
    font-family: sans-serif;
    max-width: 400px;
    margin: 60px auto;
  }

  /* 現在値の表示ラベル（例: 30 / 50 (60%)） */
  .label {
    display: flex;
    justify-content: space-between;
    font-size: 14px;
    margin-bottom: 6px;
  }

  .track {
    background: #eee;
    border-radius: 8px;
    height: 16px;
    overflow: hidden;
  }

  .bar {
    width: 0%;
    height: 100%;
    background: #3378dd;
    transition: width 0.3s ease;
  }
</style>

<!-- 現在値の表示 -->
<div class="label">
  <span id="label-fraction"></span>
  <span id="label-pct"></span>
</div>

<!-- プログレスバーの本体だよ -->
<div class="track">
  <div class="bar" id="js-bar"></div>
</div>

<!-- ここに${xxxxx}みたいな感じで値を入れるよ（下のJSで取得する）-->
<input type="text" name="current" id="current" value="${projectDto.completedTaskCount}">
<input type="text" name="total" id="total" value="${projectDto.taskCount}"> 

<script>
// ☆ここで上記のテキストボックスから取得したデータを入れるよ
const current = document.getElementById("current").value;   // 現在の値（例: 完了数、達成数など）
const total = document.getElementById("total").value;     // 合計の値（例: 全体数、目標値など）
// ここまで！！

// 分数→パーセントの変換
const pct = Math.round((current / total) * 100);

// バーの幅とラベルを反映（ページを開いた瞬間に実行されるようになってる）
document.getElementById('js-bar').style.width = pct + '%';
document.getElementById('label-fraction').textContent = current + ' / ' + total;
document.getElementById('label-pct').textContent = pct + '%';
</script>
		</div>

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
    <%@ include file="/WEB-INF/jsp/footer.jsp" %>
</body>

      
     