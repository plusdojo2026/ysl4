<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<header class="app-header">
  <div class="header-left">
    <div class="app-logo">
      <img src="${pageContext.request.contextPath}/img/logo.png">
    </div>
      <nav class="side-nav">
        <a href="${pageContext.request.contextPath}/Controller?page_id=H001" class="nav-link" data-page="H001">ホーム</a>
        <a href="${pageContext.request.contextPath}/Controller?page_id=P001" class="nav-link" data-page="P001">案件一覧</a>
        <a href="${pageContext.request.contextPath}/Controller?page_id=T001" class="nav-link" data-page="T001">タスク一覧</a>
        <a href="${pageContext.request.contextPath}/Controller?page_id=S001" class="nav-link" data-page="S001">月次集計</a>
          <c:if test="${loginUser.isAdmin}">
        <a href="${pageContext.request.contextPath}/Controller?page_id=M001" class="nav-link" data-page="M001">メンバー管理</a>
          </c:if>
      </nav>
  </div>

 <div class="header-right">
  <table>
    <!-- ユーザーのアイコン -->
    <td class="icon-area"><img class="user-status" src="${pageContext.request.contextPath}/img/people.png"></td>
    <td><span class="user-name"><c:out value="${loginUser.name}"/>さん</span></td>
    <td>
    <div class="header-actions">
    <%-- common.jsがクリック時にモーダルを生成して開く --%>
      <button type="button" class="pw-reset-btn" data-open-password-modal>パスワード変更</button>
    </td>
        <form action="${pageContext.request.contextPath}/Controller" method="post" data-confirm="ログアウトしますか">
          <input type="hidden" name="page_id" value="none">
          <td><button type="submit" name="button_id" value="ログアウト" class="logout-btn">ログアウト</button></td>
        </form>
    </div>
  </div>
</table>
</header>
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