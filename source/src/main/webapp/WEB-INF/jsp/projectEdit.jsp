<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>案件編集</title>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/project.css">
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
		
		<!-- 案件コード -->
		<div class="field">
		<table>
		<tr>
			<td>案件コード</td> <td><input type="text" name="project_code"
				value="${project.projectCode}" readonly></td>
		

		<!-- 案件名 まだ-->
		
			<td>案件名<span class="repuired-item">必須</span></td> <td>
			<select name="project_name">
           <option value="${project.projectName}">
          <c:out value="${project.projectName}" />
            </option></select>
				</td>
		

		<!-- 担当PM -->
		
			<td>担当PM<span class="repuired-item">必須</span></td><td>
			 <select name="project_manager_id" >
				<option value="${project.projectManagerName}">
				<c:out value="${project.projectManagerName}"/>
				</option></select>
				</td>
			
		
</tr>
<tr>
		<!-- 顧客名 -->
		
			<td>顧客名</td><td> <input type="text" id="customer_name"
				name="customer_name" value="${project.customerName}"></td>
		

		<!-- 開始日 -->
		
			<td>開始日<span class="repuired-item">必須</span></td><td> <input type="text"
				id="start_date" name="start_date" value="${project.startDate}"
				placeholder="YYYY/MM/DD"></td>
	

		<!-- 期限 -->
		
			<td>期限<span class="repuired-item">必須</span></td><td> <input type="date"
				id="due_date" name="due_date" value="${project.dueDate}"
				placeholder="YYYY/MM/DD"></td>
		
</tr>
<tr>
		<!-- ステータス　まだ -->
		
		    <td> ステータス <span class="repuired-item">必須</span>
			</td><td> <select id="status" name="status" value="${project.status}">
				<option value="in_progress">進行中</option>
				<option value="done">完了</option>
				<option value="canceled">中止</option>
			</select></td>
			
		

		<!-- 優先度 　まだ-->
		
			<td> 優先度 <span class="repuired-item">必須</span>
			</td> <td><select id="priority" name="priority" value="${project.priority}">
				<option value="middle">中</option>
				<option value="high">高</option>
				<option value="low">低</option>
			</select></td>
		
		

		<!-- 見積工数 -->
		
			<td> 
				見積工数 <span class="repuired-item">必須</span>
			</td> 
			<td><input type="text" id="estimated_manhours" name="estimated_manhours" 
			value="${project.estimatedManhours}"></td>
		

		<!-- 実績工数 -->
		
			<td>
			実績工数
			</td> 
			<td><input type="text" id="actual_manhours"
				name="actual_manhours" value="${project.actualManhours}"></td>
		
</tr>
<tr>
		<!-- 説明 -->
		<td>説明<input type="text" value="${project.description}" id="description" ></td>
		</table>
		
		</div>
		</tr>
		

        
		<!--  予算工数　　ここから分からん-->
		<div class="kousuu">
		<table >
		<tr>
		<td><img class="regist-elephant"src="${pageContext.request.contextPath}/img/estmanhours.png">
		
		</td><td>予算工数<br><c:out value="${project.estimatedManhours}" />h</td>
		

		<!--  実績工数-->
		
		<td><img class="regist-elephant"src="${pageContext.request.contextPath}/img/estmanhours.png">
		</td><td>	実績工数<c:out value="${project.actualManhours}" />h</td>
		

		<!--  予算消化率-->
		
		<td>
			<img class="regist-elephant"src="${pageContext.request.contextPath}/img/advance.png">
		</td>
		<td>
			予算消化率
			<a href=r/dke/sj>
				<c:out value="${(project.actualManhours/project.estimatedManhours)*100}" />
			</a>
		</td>

		<!--  タスク進捗-->
		
		<td><img class="regist-elephant"src="${pageContext.request.contextPath}/img/owntask.png">
		</td>
		<td>
		タスク進捗
		<c:out value="${project.progressRate}" /></td></div></tr>
		</td>
	</tr>
  </table>
</div>
		<!--<style>
  body {
    font-family: sans-serif;
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
-->


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
<%-- <input type="text" name="current" id="current" value="${projectDto.completedTaskCount}">
<input type="text" name="total" id="total" value="${projectDto.taskCount}">  --%>

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


<div class ="btnirir">
		<!-- 戻るボタン -->
		<button type="button" id="back" onclick="history.back();">戻る</button>

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
    
    <button type="button"  id="move" onclick="goToPage()">案件詳細へ</button>
    
  		<!-- 保存ボタン -->
	<input type="submit"  id="save" wname="botton_id" value="保存">
	</div>
	
      </form><%@ include file="/WEB-INF/jsp/footer.jsp" %>
</body>
</html>
      
