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
        <h1 class="title"><fmt:message key="your.cards"/></h1>
    </c:if>
    <hr/>
</div>

<div class="container">
    <div class="row col-md-4">
        <c:choose>
            <c:when test="${not empty requestScope.cards}">
                <c:forEach var="card" items="${requestScope.cards}">
                    <ul class="list-group list-group-flush">
                        <li class="list-group-item"><b><fmt:message key="card.number"/></b>:
                            <c:out value="${card.getCardNumber()}"/></li>
                        <li class="list-group-item"><b><fmt:message key="card.account"/></b>:
                            <c:out value="${card.getAccount().getAccountNumber()}"/>
                        </li>
                        <li class="list-group-item"><b><fmt:message key="account.balance"/></b>:
                            <c:out value="${card.getAccount().getBalance()}"/>
                            <fmt:message key="currency"/>
                        </li>
                        <li class="list-group-item"><b><fmt:message key="card.exp.date"/></b>:
                            <fmt:formatDate type="date" value="${card.getExpireDate()}"/>
                        </li>
                        <c:if test="${not sessionScope.user.isManager()}">
                            <li class="list-group-item"><b>CVV</b>:
                                <c:out value="${card.getCvv()}"/>
                            </li>
                            <li class="list-group-item"><b>PIN</b>:
                                <c:out value="${card.getPin()}"/>
                            </li>
                        </c:if>
                        <li class="list-group-item"><b><fmt:message key="account.status"/></b>:
                            <c:out value="${card.getStatus().getName()}"/>
                        </li>
                        <li class="list-group-item"><b><fmt:message key="card.type"/></b>:
                            <img class="card-img" src="
                            ${pageContext.request.contextPath}/resources/<c:out value="${card.getType()}"/>.png" alt="">
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
                                        <form action="${pageContext.request.contextPath}/site/payments"
                                              method="get">
                                            <input type="hidden" name="command" value="cardPayments"/>
                                            <input type="hidden" name="cardNumber" value="${card.getCardNumber()}"/>
                                            <button type="submit" class="btn-link"><fmt:message
                                                    key="payment.histrory"/></button>
                                        </form>
                                    </li>
                                    <c:if test="${card.isActive() and sessionScope.user.isManager()}">
                                        <li class="divider"></li>
                                        <li>
                                            <form action="${pageContext.request.contextPath}/site/manager/cards/block" method="post">
                                                <input type="hidden" name="command" value="card.block"/>
                                                <input type="hidden" name="card" value="${card.getCardNumber()}"/>
                                                <button type="submit" class="btn-link"><fmt:message key="account.block"/></button>
                                            </form>
                                        </li>
                                    </c:if>
                                </ul>
                            </div>
                        </li>
                    </ul>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <div class="alert alert-info">
                    <strong><fmt:message key="info"/></strong> <fmt:message key="account.no.matches"/>
                </div>
            </c:otherwise>
        </c:choose>

    </div>
</div>
<jsp:include page="/WEB-INF/views/snippets/footer.jsp"/>
</body>
</html>
