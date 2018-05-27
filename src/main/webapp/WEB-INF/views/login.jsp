<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="i18n.lang"/>

<html>
<head>
    <jsp:include page="/WEB-INF/views/snippets/header.jsp"/>
</head>
<body>
<jsp:include page="/WEB-INF/views/snippets/navbar.jsp"/>
<c:if test="${not empty requestScope.errors}">
    <div class="alert alert-danger">
        <c:forEach items="${requestScope.errors}" var="error">
            <strong><fmt:message key="error"/></strong> <fmt:message key="${error}"/><br>
        </c:forEach>
    </div>
</c:if>
<div class="container">
    <div class="row">
        <div class="col-sm-6 col-md-4 col-md-offset-4">
            <h1 class="text-center login-title"><fmt:message key="login"/></h1>
            <div class="account-wall">
                <img class="profile-img" src="${pageContext.request.contextPath}/resources/someone.png" alt="">
                <form class="form-signin" method="post">
                    <input type="hidden" name="command" value="login.post"/>
                    <input type="text" class="form-control" name="email" placeholder="<fmt:message key="enter.email"/>"
                           value="<c:out value="${requestScope.user.getEmail()}" />" required autofocus>
                    <input type="password" class="form-control" name="password" placeholder="<fmt:message key="enter.password"/>" required>
                    <button class="btn btn-lg btn-primary btn-block" type="submit"><fmt:message key="login" /></button>
                </form>
            </div>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/views/snippets/footer.jsp"/>
</body>
</html>
