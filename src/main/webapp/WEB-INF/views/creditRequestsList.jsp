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


<div class="panel-title text-center row col-md-12">

    <h1 class="title">
        <fmt:message key="credit.requests"/>
        <hr/>
</div>

<div class="container">
    <div class="row col-md-12">

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
        <c:choose>
            <c:when test="${not empty requestScope.creditRequests}">
                <table class="table table-hover">
                    <thead>
                    <tr>
                        <th scope="col"><fmt:message key="user"/></th>
                        <th scope="col"><fmt:message key="interest.rate"/></th>
                        <th scope="col"><fmt:message key="credit.limit"/></th>
                        <th scope="col"><fmt:message key="validity.date"/></th>
                        <th scope="col" class="th-custom"><fmt:message key="action"/></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="creditRequest" items="${requestScope.creditRequests}">
                        <c:if test="${creditRequest.isPending()}">
                            <tr>
                                <td><c:out value="${creditRequest.getAccountHolder().getEmail()}"/></td>
                                <td><c:out value="${creditRequest.getInterestRate()}"/>%</td>
                                <td><c:out value="${creditRequest.getCreditLimit()}"/>&nbsp<fmt:message key="currency"/></td>
                                <td><fmt:formatDate type="date" value="${creditRequest.getValidityDate()}"/></td>
                                <td>
                                    <div class="group-style">
                                        <form action="${pageContext.request.contextPath}/site/manager/requests/confirm" method="post" class="col-xs-6">
                                            <input type="hidden" name="command" value="confirm"/>
                                            <input type="hidden" name="creditRequest" value="${creditRequest.getRequestNumber()}"/>
                                            <button type="submit" class="btn btn-info"><fmt:message key="account.confirm"/></button>
                                        </form>
                                        <form action="${pageContext.request.contextPath}/site/manager/requests/reject" method="post">
                                            <input type="hidden" name="command" value="reject"/>
                                            <input type="hidden" name="creditRequest" value="${creditRequest.getRequestNumber()}"/>
                                            <button type="submit" class="btn btn-danger"><fmt:message key="account.reject"/></button>
                                        </form>
                                    </div>
                                </td>
                            </tr>
                        </c:if>
                    </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <div class="alert alert-info">
                    <strong><fmt:message key="info"/></strong> <fmt:message key="request.no.matches"/>
                </div>
            </c:otherwise>
        </c:choose>

    </div>
</div>
<jsp:include page="/WEB-INF/views/snippets/footer.jsp"/>
</body>
</html>
