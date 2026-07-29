<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>メンバー編集</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/member.css">

</head>

<body>
<%@ include file="/WEB-INF/jsp/header.jsp" %>	
		
		<div class ="main">
		 <div class="under-header">
			<img class="regist-elephant" src="${pageContext.request.contextPath}/img/smileelephant.png">
			<div class="text-wrap">
			<h3>メンバー編集</h3>
            <h4>登録済みのメンバー情報を編集できます。</h4>
			<p>管理者専用</p>
			</div>
		 </div>
            
            <span class="msg">${msg}</span>
			<form method="POST" action="<c:url value='/Controller'/>"> 
				<input type="hidden" name="page_id" value="M001">
				<div class="member-edit">
				<table>
					<tr>
						<td>ログインID</td>
						<td><input type="text" class="input-text" name="id" value="" required></td>
						<td>氏名<span class="required-item">必須</span></td>
						<td><input type="text" class="input-text" name="name" value="${e.name}"></td>
					</tr>
					<tr>
                        <td>パスワードリセット<span class="required-item">任意</span></td>
						<td><input type="password" class="input-text" name="pw" value=""><i class="fa-solid fa-eye"></i></td>
                        <td>新規パスワード確認<span class="required-item">任意</span></td>
						<td><input type="password" class="input-text" name="pw" value=""><i class="fa-solid fa-eye"></i></td>
					</tr>
					<tr>
                        <td>メールアドレス</td>
						<td><input type="text" class="input-text" name="address" value="${e.email}"></td>
						<td>権限<span class="required-item">必須</span></td>
						<td >
							<label>
							    <input type="radio" name="kan" value="1" checked>
							    一般ユーザー
                                <input type="radio" name="kan" value="2">
                                管理者
						    </label>
                    </tr>
                    <tr>
                        <td>状態<span class="required-item">必須</span></td>
                        <td>
						    <label>
							    <input type="radio" name="kan" value="1" checked>
							    有効
                                 <input type="radio" name="kan" value="2">
							    無効
						    </label>
                        </td>						  
						    <td colspan="2">
						    	<input type="submit" class="cancel-btn" name="button_id" value="メンバー一覧へ" onclick="return list()">
                                <input type="submit" class="submit-btn" name="button_id" value="保存" onclick="return regist()">
						  	</td>						  
						</td>
					</tr>
				</table>
				</div>
			</form>				
		</div>
<%@ include file="/WEB-INF/jsp/footer.jsp" %>
</body>
</html>