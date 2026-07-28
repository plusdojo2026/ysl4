<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<header class="header">
        <aside class="header-menu">
            <div class="side-title">TaskManager</div>
            <nav class="side-nav">
                <a href="${pageContext.request.contextPath}/Controller?page_id=H001" class="nav-link" data-page="H001">ホーム</a>
                <a href="${pageContext.request.contextPath}/Controller?page_id=P001" class="nav-link" data-page="P001">案件一覧</a>
                <a href="${pageContext.request.contextPath}/Controller?page_id=T001" class="nav-link" data-page="T001">タスク一覧</a>
                <a href="${pageContext.request.contextPath}/Controller?page_id=S001" class="nav-link" data-page="S001">月次集計</a>
                <c:if test="${loginUser.isAdmin}">
                    <a href="${pageContext.request.contextPath}/Controller?page_id=M001" class="nav-link" data-page="M001">メンバー管理</a>
                </c:if>
            </nav>
        </aside>
<header>
	<script>
	//ログアウトボタンが押されたときの処理
		function logout(){
			if (confirm("本当にログアウトしますか？")) {
			  return true;
			} else {
			  return false;
			}
		}
	</script>