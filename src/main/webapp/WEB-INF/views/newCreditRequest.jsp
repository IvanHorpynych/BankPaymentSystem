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
        <h1 class="title"><fmt:message key="create.new"/>&nbsp<fmt:message key="request"/></h1>
    </c:if>
    <hr/>
</div>
<div class="container">
    <div class="row col-md-4">
        <c:if test="${empty requestScope.messages}">
            <form class="form-inline" action="${pageContext.request.contextPath}/site/user/credit_request"
                  method="post">
                <ul class="list-group list-group-flush">
                    <li class="list-group-item"><b><fmt:message key="credit.limit"/></b>:
                        <input name="creditLimit" type="number" class="form-control" id="creditLimit" step="0.0001"
                               placeholder="<fmt:message key="credit.limit" />"
                               value="<c:out value="${requestScope.creditLimit}"/>">
                        <fmt:message key="currency"/>
                    </li>
                    <li class="list-group-item"><b><fmt:message key="interest.rate"/></b>:
                        <input name="interestRate" type="number" class="form-control" id="interestRate"
                               step="0.1" pattern="^\d{1,3}.?\d$"
                               title="<fmt:message key="interest.rate.title"/>"
                               placeholder="<fmt:message key="interest.rate"/>"
                               value="<c:out value="${requestScope.interestRate}"/>">&nbsp%
                    </li>
                    <li class="list-group-item"><b><fmt:message key="validity.date"/></b>:
                        <input name="validityDate" type="date" class="form-control" id="validityDate"
                               title="<fmt:message key="validity.date"/>"
                               value="<fmt:formatDate type="date" value="${requestScope.validityDate}" pattern="yyyy-MM-dd"/>">
                    </li>
                    <li class="list-group-item">
                        <input type="hidden" name="command" value="request.do"/>
                        <button type="submit" class="btn btn-info"><fmt:message key="create.new"/></button>
                    </li>
                </ul>
            </form>
        </c:if>
    </div>
</div>
<jsp:include page="/WEB-INF/views/snippets/footer.jsp"/>
</body>
</html>
