<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="displayProjectList" value="${projectList}" />
<c:if test="${empty displayProjectList}">
    <c:set var="displayProjectList" value="${list}" />
</c:if>
 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>案件一覧</title>
    <link rel="stylesheet" href="https://cdn.datatables.net/t/bs-3.3.6/jqc-1.12.0,dt-1.10.11/datatables.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/project.css">
    
    <%-- DataTables用CSS --%>
<link rel="stylesheet"
	href="https://cdn.datatables.net/t/bs-3.3.6/jqc-1.12.0,dt-1.10.11/datatables.min.css">
</head>

<body>

<%@ include file="/WEB-INF/jsp/header.jsp" %>

	<form method="POST" action="<c:url value='/Controller'/>">
	
	<!-- 上の左側の象さんの画像 -->
	<div class ="sub -header-left">
	 <img class="regist-elephant"
				src="${pageContext.request.contextPath}/img/smileelephant.png">
	
	<!-- タイトル部分 -->
	<div>
	<h1 class = "title-main">案件一覧<h1>
	<p class = "title-sub">登録済み案件を検索・確認出来ます</p>
	</div>	
	</div>
            <div class="member-hero-action">
                <%-- <form action="${pageContext.request.contextPath}/Controller" method="get">
                    <input type="hidden" name="page_id" value="P003">
                    <button type="submit" class="regist-btn">
                        <span class="plus-mark">＋</span>
                        案件登録
                    </button>
                </form>
                --%>
    <form action="${pageContext.request.contextPath}/Controller" method="get">
		<input type="hidden" name="page_id" value="P003">
		<input type="hidden" name="button_id" value="登録">
					<button type="submit" class="regist-btn">
						<span class="plus-mark">＋</span> 新規登録
					</button>
				</form>
				</div>
    
    <form id="serchForm">
    <input type="hidden" name="page_id" value="P001">
    <!-- キーワード検索 -->
    <label>キーワード</label>
	<input type="text" id="keyword" name="keyword" required placeholder="案件名・顧客名・案件コード">

	<!-- ステータス -->
	<label>ステータス</label>
    <select name="status">
	    <option value="choice">--選択してください--</option>
	    <option value="中止">中止</option>
	    <option value="進行中">進行中</option>
	    <option value="完了">完了</option>
	</select>
	
	
	<!-- 優先度-->
    <label>優先度</label>
	<select name="priority">
    <option value="">すべて</option>
    <option value="高">高</option>
    <option value="中">中</option>
    <option value="低">低</option>
</select>
	

     <!--検索ボタン-->
     <input type="submit" name="botton_id" value="検索">
     
     <!-- クリアボタン -->
     <input type="reset" value="クリア">

	</form>
	
	
	
	<!-- 案件一覧 -->
	<div class="annkennitirann1-1">
	<p class="title2">案件一覧</p>

	ここに件数
<div class="syousai1-2">
<table id="member-table" class="member-table table table-bordered">
    <tr>
        <th>案件コード</th>
        <th>案件名</th>
        <th>顧客名</th>
        <th>ステータス</th>
        <th>優先度</th>
        <th>PM名</th>
        <th>開始日</th>
        <th>終了予定日</th>
        <th>タスク進捗</th>
        <th>実績工数</th>
        <th>操作</th>
		
    </tr>
<c:forEach var="project" items="${projectList}">
    <tr>
        <td><c:out value="${project.projectCode}" /></td>
        <td>
        <a href="${pageContext.request.contextPath}/Controller?page_id=P002" crass="html" data-page="P002">
        <c:out value="${project.projectName}" /></a>
        </td>
        <td><c:out value="${project.customerName}" /></td>
        <td><c:out value="${project.customerName}" /></td>
        <td><c:out value="${project.projectManagerId}" /></td>
        <td><c:out value="${project.startDate}" /></td>
        <td><c:out value="${project.dueDate}" /></td>
        <td><c:out value="${project.progressRate}" /></td>
        <td><progress id="file" max="100" value="<c:out value="${project.actualManhours}" />"><c:out value="${project.actualManhours}" /></td>
		<td>
    		<form action="${pageContext.request.contextPath}//Controller" method="GET">
         	<input type="hidden" name="page_id" value="P004">
        	<input type="hidden" name="project_id" value="${project.projectId}">

        	<button type="submit" class="edit-btn">
            	編集
        	</button>
    </form>
</td>
</tr>
</c:forEach>
</table>
</div>
</div>
	
	
	

<!-- タスク進捗現在値の表示 -->
<div class="label">
  <span id="label-fraction"></span>
  <span id="label-pct"></span>
</div>

<!-- プログレスバーの本体だよ -->
<div class="track">
  <div class="bar" id="js-bar"></div>
</div>

<!-- ここに${xxxxx}みたいな感じで値を入れるよ（下のJSで取得する）-->
<input type="hidden" name="current" id="current" value="${formDataList.completedTaskCount}">
<input type="hidden" name="total" id="total" value="${formDataList.taskCount}"> 

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

    jQuery(function($){
         // デフォルトの設定を変更（日本語化）--------------------
        $.extend( $.fn.dataTable.defaults, {
            language: {
                url: "http://cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Japanese.json"
            }
        });
         //------------------------------------------------
        //データテーブルを使用
        $("#foo-table").DataTable({
            paging: true,        // ページング
            searching: true,    // 検索ボックス
            ordering: true,     // ソート（列ヘッダクリック）
            info: true,         // "〜件中 〜件を表示"の表示
            lengthChange: true  // 表示件数変更プルダウン
        })
    });

</script>


<%@ include file="/WEB-INF/jsp/footer.jsp" %>
</body>
</html>