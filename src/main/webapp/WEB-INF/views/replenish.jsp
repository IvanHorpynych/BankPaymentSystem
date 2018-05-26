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
    <h1 class="title"><fmt:message key="account.replenish"/></h1>
    <hr/>
</div>

<div class="container">
    <div class="row col-md-5">
        <div class="panel-title text-center">
            <h1 class="title"></h1>
            <hr/>
        </div>
        <c:if test="${not empty requestScope.accounts  or not empty requestScope.refillableAccount}">
            <form class="form-inline" action="${pageContext.request.contextPath}/site/user/replenish" method="post">
                <ul class="list-group list-group-flush">
                    <li class="list-group-item"><b><fmt:message key="refillable.account"/></b>:
                        <select name="refillableAccount" class="form-control" id="refillableAccount">
                            <option selected="selected">${requestScope.refillableAccount}</option>
                        </select>
                    </li>
                    <li class="list-group-item"><b><fmt:message key="select.account"/></b>:
                        <select name="senderAccount" class="form-control" id="senderAccount">
                            <c:forEach var="senderAccount" items="${requestScope.accounts}">
                                <c:if test="${senderAccount.isActive()}">
                                    <option>${senderAccount.getAccountNumber()}&nbsp(${senderAccount.getBalance()}
                                        <fmt:message key="currency"/>)
                                    </option>
                                </c:if>
                            </c:forEach>
                        </select>
                    </li>
                    <li class="list-group-item"><b><fmt:message key="amount"/></b>
                        <input name="amount" type="number" class="form-control" id="amount"
                               placeholder="<fmt:message key="enter.amount" />">
                        <fmt:message key="currency"/>
                    </li>
                    <li class="list-group-item">
                        <input type="hidden" name="command" value="replenish_do"/>
                        <button type="submit" class="btn btn-danger">
                            <fmt:message key="replenish"/>
                        </button>
                    </li>
                </ul>
            </form>
        </c:if>
    </div>
</div>
<jsp:include page="/WEB-INF/views/snippets/footer.jsp"/>
</body>
</html>
