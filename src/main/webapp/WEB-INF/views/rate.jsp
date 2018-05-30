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
    <c:if test="${not empty sessionScope.user}">
        <h1 class="title"><fmt:message key="deposit.annual.rate"/></h1>
    </c:if>
    <hr/>
</div>

<div class="container">
    <div class="row col-md-4">

        <form class="form-inline" action="${pageContext.request.contextPath}/site/manager/annual_rate"
              method="post">
            <ul class="list-group list-group-flush">
                <li class="list-group-item"><b><fmt:message key="current.annual.rate"/></b>:
                    <c:choose>
                    <c:when test="${not empty requestScope.validRate}">
                        <c:out value="${requestScope.validRate.getAnnualRate()}"/>
                    </c:when>
                        <c:otherwise>
                            <fmt:message key="rate.not.found"/>
                        </c:otherwise>
                    </c:choose>
                </li>
                <li class="list-group-item"><b><fmt:message key="new.annual.rate"/></b>:
                    <input name="annualRate" type="number" class="form-control rate-input" id="annualRate"
                           step="0.1" pattern="^\d{1,3}.?\d{1,1}$"
                           title="<fmt:message key="deposit.annual.rate"/>">&nbsp%
                </li>
                <li class="list-group-item">
                    <input type="hidden" name="command" value="update.rate"/>
                    <button type="submit" class="btn btn-info">
                        <fmt:message key="update"/>
                    </button>
                </li>
            </ul>
        </form>
    </div>
</div>
<jsp:include page="/WEB-INF/views/snippets/footer.jsp"/>
</body>
</html>
