<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ログイン</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
</head>

<body>
<header>
<%@ include file="/WEB-INF/jsp/header.jsp" %>
</header>
<footer>
<%@ include file="/WEB-INF/jsp/footer.jsp" %>
</footer>	
		
		<div class ="main">
			<h2></h2>
			<h4>メンバー登録</h4><span class="msg">${msg}</span>
			<form method="POST" action="<c:url value='/Controller'/>">
				<input type="hidden" name="page_id" value="M001">
				<table>
					<tr>
						<td>ログインID<span>必須</span></td>
						<td><input type="text" name="id" value="${param.id }" required></td>					
						<td>氏名<span>必須</span></td>
						<td><input type="text" name="name" value="${param.name }"></td>
					</tr>
					<tr>
                        <td>新規パスワード<span>必須</span></td>
						<td><input type="password" name="pw" value=""><i class="fa-solid fa-eye"></i></td>
                        <td>新規パスワード確認<span>必須</span></td>
						<td><input type="password" name="pw" value=""><i class="fa-solid fa-eye"></i></td>
					</tr>
					<tr>
                        <td>メールアドレス<span>任意</span></td>
						<td><input type="text" name="address" value="${param.address }"></td>
						<td>権限<span>必須</span></td>
						<td >
							<label>
							    <input type="radio" name="kan" value="0" <c:if test="${param.kan == '0'}">checked</c:if>>
							    一般ユーザー
                                <input type="radio" name="kan" value="0" <c:if test="${param.kan == '0'}">checked</c:if>>
                                管理者
						    </label>
                    </tr>
                    <tr>
                        <td>状態<span>必須</span></td>
                        <td>
						    <label>
							    <input type="radio" name="kan" value="1" <c:if test="${param.kan == '1'}">checked</c:if>>
							    管理者
						    </label>
                        </td>						  
						    <td colspan="2">
						    	<input type="submit" name="button_id" value="メンバー一覧へ" onclick="return delete()">
						  		<input type="submit" name="button_id" value="登録" onclick="return regist()">
						  	</td>						  
						</td>
					</tr>
				</table>
			</form>				
		</div>
		<script>		
		function regist(){
			if (!confirm("登録します。よろしいですか？")) {
			  // OK（はい）を押したときの処理
				return false;
			} 
		}
		</script>
</body>
</html>