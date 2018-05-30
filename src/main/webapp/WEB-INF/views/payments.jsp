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
    <c:if test="${not empty sessionScope.user}">
        <h1 class="title"><fmt:message key="payments.history"/>
        <c:if test="${not empty requestScope.desiredAccount}">
            : <fmt:message key="account.number"/> <c:out value="${requestScope.desiredAccount}"/>
        </c:if>
        </h1>
    </c:if>
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
            <c:when test="${not empty requestScope.payments}">
                <table class="table table-hover">
                    <thead>
                    <tr>
                        <th scope="col"><fmt:message key="payment.number"/></th>
                        <th scope="col"><fmt:message key="payment.account.from"/></th>
                        <th scope="col"><fmt:message key="payment.card.from"/></th>
                        <th scope="col"><fmt:message key="payment.account.to"/></th>
                        <th scope="col"><fmt:message key="payment.amount"/></th>
                        <th scope="col"><fmt:message key="date"/></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="payment" items="${requestScope.payments}">

                        <tr>
                            <td><c:out value="${payment.getId()}"/></td>
                            <c:choose>
                                <c:when test="${payment.getAccountFrom().getAccountNumber() eq requestScope.desiredAccount}">
                                    <td><b><c:out value="${payment.getAccountFrom().getAccountNumber()}"/></b></td>
                                </c:when>
                                <c:otherwise>
                                    <td><c:out value="${payment.getAccountFrom().getAccountNumber()}"/></td>
                                </c:otherwise>
                            </c:choose>

                            <c:choose>
                                <c:when test="${payment.getCardNumberFrom() eq 0}">
                                    <td>-</td>
                                </c:when>
                                <c:otherwise>
                                    <td><c:out value="${payment.getCardNumberFrom()}"/></td>
                                </c:otherwise>
                            </c:choose>

                            <c:choose>
                                <c:when test="${payment.getAccountTo().getAccountNumber() eq requestScope.desiredAccount}">
                                    <td><b><c:out value="${payment.getAccountTo().getAccountNumber()}"/></b></td>
                                </c:when>
                                <c:otherwise>
                                    <td><c:out value="${payment.getAccountTo().getAccountNumber()}"/></td>
                                </c:otherwise>
                            </c:choose>
                            <td><c:out value="${payment.getAmount()}"/> <fmt:message key="currency"/></td>
                            <td><fmt:formatDate type="date" value="${payment.getDate()}"/></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <div class="alert alert-info">
                    <strong><fmt:message key="info"/></strong> <fmt:message key="payment.no.matches"/>
                </div>
            </c:otherwise>
        </c:choose>

    </div>
</div>
<jsp:include page="/WEB-INF/views/snippets/footer.jsp"/>
</body>
</html>
