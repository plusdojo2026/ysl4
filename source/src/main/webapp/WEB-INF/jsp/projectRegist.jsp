 <%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>案件登録画面</title>

 
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/project.css">
</head>

<body>
<%@ include file="/WEB-INF/jsp/header.jsp" %>

<main class="project-regist-area">


    <!-- 上の画像-->
        <!-- <section class="title-area"> -->
			 <div class="zou"><img src= "${pageContext.request.contextPath}/img/elephant(1).png"></div>

            <div class="text-area">
				<h1 class ="projectimg">案件登録</h1>
            	<p class ="subtitle">
                新しい案件詳細を入力してください。</p>
			</div>
			<!-- </section> -->
			
        

<form id="projectForm"
      method="POST"
      action="ProjectServlet?action=regist">
	<input type ="hidden" name="page_id" value="P003">



    <!-- 案件コード -->
    <!-- <div class="field"> -->
		 <!-- 右側説明 -->
    <div class="sub-header-right"> 
       <!-- <img class="zousann" src = "${pageContext.request.contextPath}/img/elephant(1).png"> -->

	   <div class ="setumeidesu" style="border: 2px  solid#F6ADC6;">
        入力内容を確認して保存しましょう<br>
        担当PMは有効なメンバーから選択してください<br>
        保存後は案件一覧に戻ります

	
    </div>
	</div> 

	
<div class="field"></div>
		<table id="main-table">
		  <tr>
          <td>
            案件コード
            <span class="required-item">必須</span> <span class="error"> ${errorMessage}</span><br>
		
           <input type="text"
               id="project_code"
               name="project_code"></td>
    <!-- 案件名 -->
	 		<td>
            案件名
            <span class="required-item">必須</span><br>
		

        <input type="text"
               id="project_name"
               name="project_name"></td>
          </tr>
          <tr>
    <!-- 顧客名 -->
        <td>顧客名<br>

        <input type="text"
               id="customer_name"
               name="customer_name"></td>

    <!-- 担当PM -->
        <td>担当PM
    <span class="required-item">必須</span><br>

    <select id="project_manager_id"
            name="project_manager_id">
        <c:forEach var="project" items="${projectList}">
            <option value="${project.projectManagerId}">
                ${project.projectManagerId}
            </option>
        </c:forEach>
    </select>
</td>
        
</tr>
<tr>
    <!-- ステータス -->
        <td>
            ステータス
            <span class="required-item">必須</span><br>
		

       <select id="status"
                name="status">
            <option value="in_progress">進行中</option>
            <option value="done">完了</option>
            <option value="canceled">中止</option>
        </select></td>

    <!-- 優先度 -->
        <td>
            優先度
            <span class="required-item">必須</span><br>
		

        <select id="priority"
                name="priority">
            <option value="middle">中</option>
            <option value="high">高</option>
            <option value="low">低</option>
        </select></td>

    <!-- 見積工数 -->
        <td>
            見積工数
            <span class="required-item">必須</span><br>
		

        <input type="text"
               id="estimated_manhours"
               name="estimated_manhours"></td>

    <!-- 実績工数 -->
        <td>実績工数<br>

        <input type="text"
               id="actual_manhours"
               name="actual_manhours"></td>
</tr>
<tr>
    <!-- 開始日 -->
        <td>
            開始日
            <span class="required-item">必須</span><br>
		

        <input type="text"
               id="start_date"
               name="start_date"
               placeholder="YYYY/MM/DD"></td>

    <!-- 期限 -->
        <td>
            期限
            <span class="required-item">必須</span><br>
		

        <input type="text"
               id="due_date"
               name="due_date"
               placeholder="YYYY/MM/DD"></td>
</tr>
<tr>
    <!-- 説明 -->
        <td>説明<br>

        <input type="text"
               id="description"
               name="description"></td>
			   </tr>


    </div>

</table>
    


<div class ="projectbtn">
<!-- 戻る -->
<button type ="button" id="back" onclick="history.go(-1);">
    戻る
</button>

<!-- キャンセル（入力クリア）-->
<button type="button"
        id="clearBtn">
    キャンセル
</button>

<!-- 保存 -->
    <button type="submit" id="comp">
        保存
    </button>
</div>
</form>

<script>
document.getElementById("clearBtn")
    .addEventListener("click", function () {
        document.getElementById("projectForm").reset();
    });
</script>
<%@ include file="/WEB-INF/jsp/footer.jsp" %>
</main>
</body>
</html>