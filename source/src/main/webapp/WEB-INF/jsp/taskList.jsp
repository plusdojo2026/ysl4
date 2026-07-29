<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>


<head>
<meta charset="UTF-8">
<title>タスク一覧</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/common.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/task.css">
<!-- ${pageContext.request.contextPath}/css/common.css -->
</head>

<body>
	<%@ include file="/WEB-INF/jsp/header.jsp"%>

	<div class="main">
		<div class="under-header">
			<img class="regist-elephant"
				src="${pageContext.request.contextPath}/img/elephant(1).png">
			<div class="text-wrap">
				<h3>タスク一覧</h3>
				<h4>登録済みタスクの検索・確認ができます。</h4>
				<!-- <input type="submit" class="regist-btn" name="regist-btn" value="メンバー登録"> -->
			</div>
		</div>
		<div class="member-dashboard">
			<div class="member-count">
				<img src="${pageContext.request.contextPath}/img/owntask.png"
					width="100px" height="100px"> <span> <span
					class="block-box">全タスク</span> <span class="actual-member-count">〇</span>件
				</span>
			</div>
			<div class="member-count">
				<img src="${pageContext.request.contextPath}/img/clockmark.png"
					width="100" height="100"> <span> <span
					class="block-box">進行中</span> <span class="actual-member-count">〇</span>件
				</span>
			</div>
			<div class="member-count">
				<img src="${pageContext.request.contextPath}/img/warning.png"
					width="100px" height="100px"> <span> <span
					class="block-box">期限超過</span> <span class="actual-member-count">〇</span>件
				</span>
			</div>
			<div class="member-count">
				<img src="${pageContext.request.contextPath}/img/done.png"
					width="100" height="100"> <span> <span
					class="block-box">完了タスク</span> <span class="actual-member-count">〇</span>件
				</span>
			</div>
		</div>
	</div>
	<!-- <span class="msg">${msg}</span> -->
	<form>
		<!-- method="POST" action="<c:url value='/Controller'/>" -->
		<input type="hidden" name="page_id" value="T001">
		<div class="member-search">
			<table>
				<tr>
					<td>キーワード</td>
					<td><input type="text" class="keyword" name="keyword" value=""
						placeholder="タスク名で検索"></td>
					<!-- value="${param.id }"				 -->
					<td>案件</td>
					<td><select>
							<option>すべて</option>
							<option>案件１</option>
							<!-- <c:if test="${param.kan == '0'}">checked</c:if> -->
							<option>案件２</option>
							<!-- <c:if test="${param.kan == '1'}"></c:if> -->
					</select></td>
					<td>ステータス</td>
					<td><select>
							<option>--選択してください--</option>
							<option>未着手に戻す</option>
							<option>進行中にする</option>
							<option>完了にする</option>
							<option>保留にする</option>
					</select></td>
			</table>
			<table>
				<td>担当者</td>
				<td><select>
						<option>すべて</option>
						<option>田中</option>
						<option>佐藤</option>
						<option>鈴木</option>
						<option>高橋</option>
						<option>山本</option>
				</select></td>
				<td>優先度</td>
				<td><select>
						<option>すべて</option>
						<option>高</option>
						<option>中</option>
						<option>低</option>
				</select></td>
				<td>   </td>
				<td>   </td>
			</table>
			<table>
				<div class="button_edit">
				<td colspan="3"><input type="submit" class="submit-btn"
					name="button_id" value="検索" onclick="return regist()"> <input
					type="reset" class="clear-btn" name="button_id" value="クリア">
					<input type="submit" class="submit-btn" name="button_id"
					value="自分のタスク"></td>
				</div>
				
				<div class="button_regist">
				<td colspan="3" class="submit-btn2">
				<a href="${pageContext.request.contextPath}/Controller?page_id=T003" class="nav-link">    ＋タスク登録</a>
				</td>
				</div>
			</table>
		</div>
	</form>
	<br>
	<br>
	<div class="member-list">
		<table border="1" id="foo-table" class="table table-bordered">
			<thead>
				<tr>
					<th>案件名</th>
					<th>タスク名</th>
					<th>担当者</th>
					<th>ステータス</th>
					<th>優先度</th>
					<th>期限</th>
					<th>見積工数</th>
					<th>実績工数</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="e" items="${list}" varStatus="status">
					<tr>
						<td>aaaa</td>
						<td><a href="/ysl4/jsp/taskDetail"><c:out
									value="${e.taskName}" /></a></td>
						<td>山田</td>
						<td>valuekfj</td>
						<td><span>一般</span></td>
						<td><span>有効</span></td>
						<td>200h</td>
						<td>120h</td>
						<td><input type="submit" class="edit-btn" name="button_id"
							value="編集" onclick="return edit()"></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>

	<script
		src="https://cdn.datatables.net/t/bs-3.3.6/jqc-1.12.0,dt-1.10.11/datatables.min.js"></script>
	<script>
		a
		jQuery(function($) {
			//   デフォルトの設定を変更（日本語化）--------------------
			$
					.extend(
							$.fn.dataTable.defaults,
							{
								language : {
									url : "http://cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Japanese.json"
								}
							});
			//------------------------------------------------
			//データテーブルを使用
			// $("#foo-table").DataTable({
			//     paging: true,        // ページング
			//     searching: true,    // 検索ボックス
			//     ordering: true,     // ソート（列ヘッダクリック）
			//     info: true,         // "〜件中 〜件を表示"の表示
			//     lengthChange: true  // 表示件数変更プルダウン
			// })
		});
	</script>

	<%@ include file="/WEB-INF/jsp/footer.jsp"%>
</body>

</html>