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

<div class="container">
    <div class="row col-md-12">
        <c:if test="${not empty requestScope.messages}">
            <div class="alert alert-success">
                <c:forEach items="${requestScope.messages}" var="message">
                    <strong><fmt:message key="info"/></strong> <fmt:message key="${message}"/><br>
                </c:forEach>
            </div>
        </c:if>

        <c:if test="${not empty requestScope.warning and not empty requestScope.amount}">
            <div class="alert alert-warning">
                <fmt:message key="${requestScope.warning}"/>&nbsp${requestScope.amount}<fmt:message key="currency"/><br>
            </div>
        </c:if>


        <c:if test="${not empty requestScope.errors}">
            <div class="alert alert-danger">
                <c:forEach items="${requestScope.errors}" var="error">
                    <strong><fmt:message key="error"/></strong> <fmt:message key="${error}"/><br>
                </c:forEach>
            </div>
        </c:if>

    </div>
</div>

<jsp:include page="/WEB-INF/views/snippets/footer.jsp"/>
</body>
</html>
