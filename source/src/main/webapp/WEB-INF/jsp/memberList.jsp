<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>メンバー一覧 | TaskManager</title>

    <%-- DataTables用CSS --%>
    <link rel="stylesheet" href="https://cdn.datatables.net/t/bs-3.3.6/jqc-1.12.0,dt-1.10.11/datatables.min.css">

    <%-- 共通CSS --%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">

    <%-- メンバー画面用CSS --%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/member.css">
</head>

<body class="member-page">

    <%-- 共通ヘッダー --%>
    <jsp:include page="/WEB-INF/jsp/header.jsp" />

    <main class="member-main">

        <section class="member-hero">
            <div class="member-hero-image">
                <img class="regist-elephant"
                     src="${pageContext.request.contextPath}/img/smileelephant.png"
                     alt="メンバー一覧">
            </div>

            <div class="member-hero-text">
                <h1 class="member-title">メンバー一覧</h1>
                <p class="member-lead">登録済みメンバーを確認・編集できます</p>

                <div class="hero-note-row">
                    <span class="admin-pill">管理者専用</span>
                    <span class="hero-note">メンバー情報を確認・編集できます</span>
                </div>
            </div>

            <div class="member-hero-action">
                <form action="${pageContext.request.contextPath}/Controller" method="get">
                    <input type="hidden" name="page_id" value="M002">
                    <button type="submit" class="regist-btn">
                        <span class="plus-mark">＋</span>
                        メンバー登録
                    </button>
                </form>
            </div>
        </section>

        <section class="member-dashboard">
            <div class="member-count">
                <img src="${pageContext.request.contextPath}/img/people.png"
                     alt="総メンバー数">

                <div class="member-count-text">
                    <span class="member-count-label">総メンバー数</span>
                    <span class="actual-member-count" id="totalMemberCount">0</span>
                    <span class="member-count-unit">人</span>
                </div>
            </div>

            <div class="member-count">
                <img src="${pageContext.request.contextPath}/img/validmember.png"
                     alt="有効メンバー数">

                <div class="member-count-text">
                    <span class="member-count-label">有効メンバー数</span>
                    <span class="actual-member-count" id="validMemberCount">0</span>
                    <span class="member-count-unit">人</span>
                </div>
            </div>

            <div class="member-count">
                <img src="${pageContext.request.contextPath}/img/adminuser.png"
                     alt="管理者数">

                <div class="member-count-text">
                    <span class="member-count-label">管理者数</span>
                    <span class="actual-member-count" id="adminMemberCount">0</span>
                    <span class="member-count-unit">人</span>
                </div>
            </div>

            <div class="member-count">
                <img src="${pageContext.request.contextPath}/img/invalidmember.png"
                     alt="無効メンバー数">

                <div class="member-count-text">
                    <span class="member-count-label">無効メンバー数</span>
                    <span class="actual-member-count" id="invalidMemberCount">0</span>
                    <span class="member-count-unit">人</span>
                </div>
            </div>
        </section>

        <section class="member-search">
            <div class="search-grid">
                <div class="search-field keyword-field">
                    <label for="keyword-filter">キーワード</label>

                    <div class="keyword-input-wrap">
                        <span class="search-icon">⌕</span>
                        <input type="text"
                               id="keyword-filter"
                               class="keyword"
                               name="keyword"
                               placeholder="氏名で検索">
                    </div>
                </div>

                <div class="search-field">
                    <label for="role-filter">権限</label>

                    <select id="role-filter">
                        <option value="">すべて</option>
                        <option value="一般ユーザー">一般ユーザー</option>
                        <option value="管理者">管理者</option>
                    </select>
                </div>

                <div class="search-field">
                    <label for="status-filter">状態</label>

                    <select id="status-filter">
                        <option value="">すべて</option>
                        <option value="有効">有効</option>
                        <option value="無効">無効</option>
                    </select>
                </div>

                <div class="clear-button-area">
                    <button type="button" class="clear-btn" id="clear-search-button">
                        クリア
                    </button>
                </div>
            </div>

        </section>

        <section class="member-list">
            <div class="member-table-wrap">
                <table id="member-table" class="member-table table table-bordered">
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
                        <c:forEach var="e" items="${list}">
                            <tr>
                                <td class="login-id-cell">
                                    <c:out value="${e.loginId}" />
                                </td>

                                <td>
                                    <c:out value="${e.name}" />
                                </td>

                                <td>
                                    <c:out value="${e.email}" />
                                </td>

                                <td>
                                    <c:choose>
                                        <c:when test="${e.isAdmin}">
                                            <span class="role-badge admin-role">管理者</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="role-badge normal-role">一般ユーザー</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>

                                <td>
                                    <c:choose>
                                        <c:when test="${e.isValid}">
                                            <span class="status-badge valid-status">有効</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="status-badge invalid-status">無効</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>

                                <td>
                                    <c:out value="${e.createdAt}" />
                                </td>

                                <td>
                                    <div class="action-cell">
                                        <form action="${pageContext.request.contextPath}/Controller" method="get">
                                            <input type="hidden" name="page_id" value="M003">
                                            <input type="hidden" name="user_id" value="${e.userId}">
                                            <button type="submit" class="edit-btn">
                                                編集
                                            </button>
                                        </form>

                                        <div class="more-menu-wrap">
                                            <button type="button" class="more-btn">…</button>

                                            <div class="more-menu">
                                                <form action="${pageContext.request.contextPath}/Controller" method="get">
                                                    <input type="hidden" name="page_id" value="M004">
                                                    <input type="hidden" name="user_id" value="${e.userId}">
                                                    <button type="submit" class="more-menu-item">
                                                        パスワードリセット
                                                    </button>
                                                </form>

                                                <form action="${pageContext.request.contextPath}/Controller" method="post" data-confirm="このメンバーを有効化しますか">
                                                    <input type="hidden" name="page_id" value="M003">
                                                    <input type="hidden" name="user_id" value="${e.userId}">
                                                    <input type="hidden" name="is_valid" value="true">
                                                    <button type="submit" name="button_id" value="有効化" class="more-menu-item">
                                                        有効化
                                                    </button>
                                                </form>

                                                <form action="${pageContext.request.contextPath}/Controller" method="post" data-confirm="このメンバーを無効化しますか">
                                                    <input type="hidden" name="page_id" value="M003">
                                                    <input type="hidden" name="user_id" value="${e.userId}">
                                                    <input type="hidden" name="is_valid" value="false">
                                                    <button type="submit" name="button_id" value="無効化" class="more-menu-item">
                                                        無効化
                                                    </button>
                                                </form>
                                            </div>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </section>

    </main>

    <%-- 共通フッター --%>
    <jsp:include page="/WEB-INF/jsp/footer.jsp" />

    <%-- DataTables用JS --%>
    <script src="https://cdn.datatables.net/t/bs-3.3.6/jqc-1.12.0,dt-1.10.11/datatables.min.js"></script>

    <%-- 共通JS --%>
    <script src="${pageContext.request.contextPath}/js/common.js"></script>

    <%-- メンバー画面用JS --%>
    <script src="${pageContext.request.contextPath}/js/member.js"></script>
</body>
</html>