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

<c:if test="${not empty requestScope.messages}">
    <div class="alert alert-success">
        <c:forEach items="${requestScope.messages}" var="message">
            <strong><fmt:message key="info"/></strong> <fmt:message key="${message}"/><br>
        </c:forEach>
    </div>
</c:if>

<c:if test="${not empty requestScope.errors}">
    <div class="alert alert-danger">
        <c:forEach items="${requestScope.errors}" var="error">
            <strong><fmt:message key="error"/></strong> <fmt:message key="${error}"/><br>
        </c:forEach>
    </div>
</c:if>
<div class="panel-title text-center row col-md-12">
    <c:if test="${not empty sessionScope.user and not sessionScope.user.isManager()}">
        <h1 class="title"><fmt:message key="credit.request"/></h1>
    </c:if>
    <hr/>
</div>
<div class="container">
    <div class="row col-md-4">
        <c:choose>
            <c:when test="${not empty requestScope.creditRequests}">
                <c:forEach var="creditRequest" items="${requestScope.creditRequests}">
                    <c:if test="${creditRequest.isPending()}">
                        <ul class="list-group list-group-flush">
                            <li class="list-group-item"><b><fmt:message key="credit.request"/></b>:
                                <c:out value="${creditRequest.getRequestNumber()}"/>
                            </li>
                            <li class="list-group-item"><b><fmt:message key="credit.limit"/></b>:
                                <c:out value="${creditRequest.getCreditLimit()}"/>
                                <fmt:message key="currency"/>
                            </li>
                            <li class="list-group-item"><b><fmt:message key="interest.rate"/></b>:
                                <c:out value="${creditRequest.getInterestRate()}"/>%
                            </li>
                            <li class="list-group-item"><b><fmt:message key="validity.date"/></b>:
                                <fmt:formatDate type="date" value="${creditRequest.getValidityDate()}"/>
                            </li>
                            <li class="list-group-item"><b><fmt:message key="account.status"/></b>:
                                <c:out value="${creditRequest.getStatus().getName()}"/>
                            </li>
                            <li class="list-group-item">
                                <div class="btn-group group-style">
                                    <button type="button" class="custom-btn btn btn-info dropdown-toggle"
                                            data-toggle="dropdown">
                                        <span class="caret"></span>
                                        <span class="sr-only">Toggle Dropdown</span>
                                    </button>
                                    <ul class="dropdown-menu" role="menu">
                                        <li>
                                            <form action="${pageContext.request.contextPath}/site/user/close" method="post">
                                                <input type="hidden" name="command" value="request.close"/>
                                                <input type="hidden" name="creditRequest" value="${creditRequest.getRequestNumber()}"/>
                                                <button type="submit" class="btn-link"><fmt:message key="cancel"/></button>
                                            </form>
                                        </li>
                                    </ul>
                                </div>
                            </li>
                        </ul>
                    </c:if>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <div class="alert alert-info">
                    <strong><fmt:message key="info"/></strong> <fmt:message key="request.no.matches"/>
                </div>
            </c:otherwise>
        </c:choose>
        <form action="${pageContext.request.contextPath}/site/user/credit_request" method="get">
            <input type="hidden" name="command" value="new.request"/>
            <button type="submit" class="btn btn-info"><fmt:message key="create.new"/></button>
        </form>
    </div>
</div>
<jsp:include page="/WEB-INF/views/snippets/footer.jsp"/>
</body>
</html>
