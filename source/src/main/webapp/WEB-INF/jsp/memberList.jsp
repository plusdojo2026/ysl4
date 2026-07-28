<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>メンバー管理|TaskManager</title>
<link rel="stylesheet"
	href="https://cdn.datatables.net/t/bs-3.3.6/jqc-1.12.0,dt-1.10.11/datatables.min.css" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/common.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/member.css">

</head>

<body>
	<header>
		<%@ include file="/WEB-INF/jsp/header.jsp"%>
	</header>
	<div class="main">
		<div class="under-header">
			<img class="regist-elephant"
				src="${pageContext.request.contextPath}/img/smileelephant.png">
			<div class="text-wrap">
				<h3>メンバー一覧</h3>
				<h4>登録済みのメンバーの登録・確認・編集ができます。</h4>
				<p>管理者専用</p>

				<form>
					<input type="hidden" name="page_id" value="M002"> <input
						type="submit" class="regist-btn" name="page_id" value="メンバー登録">
				</form>

			</div>
		</div>
		<div class="member-dashboard">
			<div class="member-count">
				<img src="${pageContext.request.contextPath}/img/people.png"
					width="100px" height="100px"> <span> <span
					class="block-box">総メンバー数</span> <span class="actual-member-count">9</span>人
				</span>
			</div>
			<div class="member-count">
				<img src="${pageContext.request.contextPath}/img/validmember.png"
					width="100" height="90"> <span> <span>有効メンバー数</span> <span
					class="actual-member-count">7</span>人
				</span>
			</div>
			<div class="member-count">
				<img src="${pageContext.request.contextPath}/img/adminuser.png"
					width="80" height="90"> <span> <span>管理者数</span> <span
					class="actual-member-count">2</span>人 
			</div>

			<div class="member-count">
				<img src="${pageContext.request.contextPath}/img/invalidmember.png"
					width="100" height="90"> <span>無効メンバー数</span> <span
					class="actual-member-count">1</span>人
			</div>
		</div>

		<form method="POST" action="<c:url value='/Controller'/>">
			<input type="hidden" name="page_id" value="M001">
			<div class="member-search">
				<table>
					<tr>
						<td>キーワード</td>
						<td><input type="text" id="keyword-filter" class="keyword"
							name="keyword" value="" placeholder="氏名などで検索"></td>
						<!-- value="${param.id }"				 -->

						<td>権限</td>

						<td><select id="role-filter">
								<option>すべて</option>
								<option>一般ユーザー</option>
								<option>管理者</option>
						</select></td>

						<td>状態</td>

						<td><select id="status-filter">
								<option>すべて</option>
								<option>有効</option>
								<option>無効</option>
						</select></td>

						<td colspan="2">
							<!--   <input type="submit" class="submit-btn" name="button_id" value="検索" onclick="return regist()"> -->
							<input type="reset" class="clear-btn" name="button_id"
							value="クリア">
						</td>

					</tr>
				</table>
			</div>
		</form>

		<div class="member-list">
			<table border="1" id="foo-table" class="table table-bordered">
				<thead>
					<tr>
						<th>ログインID</th>
						<th>氏名</th>
						<th>メールアドレス</th>
						<th>権限</th>
						<th>状態</th>
						<th>登録日</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="e" items="${list}" varStatus="status">
						<tr>
							<td>${e.loginId}</td>
							<td>${e.name}</td>
							<td>${e.email}</td>
							<td><c:choose>
									<c:when test="${e.isAdmin}">
            							管理者
        							</c:when>
									<c:otherwise>
            							一般ユーザー
        							</c:otherwise>
								</c:choose></td>
							<td>
								<c:choose>
									<c:when test="${e.isValid}">
            							有効
        							</c:when>
									<c:otherwise>
            							無効
        							</c:otherwise>
								</c:choose>
							</td>
							<td>${e.createdAt}</td>
							<form>
							<td>
								<input type="submit" class="edit-btn" name="button_id"
								value="編集" onclick="return edit()">
							</td>
							</form>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>

	</div>


	<script
		src="https://cdn.datatables.net/t/bs-3.3.6/jqc-1.12.0,dt-1.10.11/datatables.min.js"></script>
	<script>
    jQuery(function($){
    	//   デフォルトの設定を変更（日本語化）--------------------
        $.extend( $.fn.dataTable.defaults, {
            language: {
                url: "http://cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Japanese.json"
            }
        });
    	 //------------------------------------------------
		//データテーブルを使用
		$("#foo-table").DataTable({
		paging: true, // ページング
		searching: true, // 検索ボックス
		ordering: true,// ソート（列ヘッダクリック）
		info: true,// "〜件中 〜件を表示"の表示
		lengthChange: true// 表示件数変更プルダウン
		dom: 'lrtip'
		})

		//キーワード検索を連携
		$('#keywordFilter').on('keyup', function () {
		    table.search(this.value).draw();
		});

		//DataTablesカスタムフィルターを追加
		$.fn.dataTable.ext.search.push(
			    function(settings, data, dataIndex) {

			        var role = $('#roleFilter').val();
			        var status = $('#statusFilter').val();

			        var rowRole = data[3]; //権限列
			        var rowStatus = data[4]; //状態列

			        if (role && rowRole !== role) {
			            return false;
			        }

			        if (status && rowStatus !== status) {
			            return false;
			        }

			        return true;
			    }
			);


		//セレクト検索時に再検索
		$('#roleFilter, #statusFilter').on('change', function () {
		    table.draw();
		});

		//クリアボタン
		$('.clear-btn').on('click', function () {

    		$('#keywordFilter').val('');
    		$('#roleFilter').val('');
    		$('#statusFilter').val('');

    		table.search('');
    		table.draw();
});
`
		
	});
 </script>
</body>

<%@ include file="/WEB-INF/jsp/footer.jsp"%>

</html>