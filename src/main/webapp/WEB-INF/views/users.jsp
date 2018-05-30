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
        <fmt:message key="users"/>
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
            <c:when test="${not empty requestScope.users}">
                <table class="table table-hover">
                    <thead>
                    <tr>
                        <th scope="col"><fmt:message key="firstname"/></th>
                        <th scope="col"><fmt:message key="lastname"/></th>
                        <th scope="col"><fmt:message key="email"/></th>
                        <th scope="col"><fmt:message key="phone"/></th>
                        <th scope="col"><fmt:message key="action"/></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="user" items="${requestScope.users}">
                        <c:if test="${user.isUser()}">
                            <tr>
                                <td><c:out value="${user.getFirstName()}"/></td>
                                <td><c:out value="${user.getLastName()}"/></td>
                                <td><c:out value="${user.getEmail()}"/></td>
                                <td><c:out value="${user.getPhoneNumber()}"/></td>
                                <td>
                                    <div class="btn-group group-style">
                                        <button type="button" class="custom-btn btn btn-info dropdown-toggle"
                                                data-toggle="dropdown">
                                            <span class="caret"></span>
                                            <span class="sr-only">Toggle Dropdown</span>
                                        </button>
                                        <ul class="dropdown-menu" role="menu">
                                            <li>
                                                <form action="${pageContext.request.contextPath}/site/manager/credit_accounts" method="get">
                                                    <input type="hidden" name="command" value="credit.get"/>
                                                    <input type="hidden" name="user" value="${user.getId()}"/>
                                                    <button type="submit" class="btn-link"><fmt:message key="credit.accounts"/></button>
                                                </form>
                                            </li>
                                            <li class="divider"></li>
                                            <li>
                                                <form action="${pageContext.request.contextPath}/site/manager/debit_accounts" method="get">
                                                    <input type="hidden" name="command" value="debit.get"/>
                                                    <input type="hidden" name="user" value="${user.getId()}"/>
                                                    <button type="submit" class="btn-link"><fmt:message key="debit.accounts"/></button>
                                                </form>
                                            </li>
                                            <li class="divider"></li>
                                            <li>
                                                <form action="${pageContext.request.contextPath}/site/manager/deposit_accounts" method="get">
                                                    <input type="hidden" name="command" value="deposit.get"/>
                                                    <input type="hidden" name="user" value="${user.getId()}"/>
                                                    <button type="submit" class="btn-link"><fmt:message key="deposit.accounts"/></button>
                                                </form>
                                            </li>
                                            <li class="divider"></li>
                                            <li>
                                                <form action="${pageContext.request.contextPath}/site/manager/cards" method="get">
                                                    <input type="hidden" name="command" value="card.get"/>
                                                    <input type="hidden" name="user" value="${user.getId()}"/>
                                                    <button type="submit" class="btn-link"><fmt:message key="cards"/></button>
                                                </form>
                                            </li>
                                        </ul>
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
                    <strong><fmt:message key="info"/></strong> <fmt:message key="no.registered.users"/>
                </div>
            </c:otherwise>
        </c:choose>

    </div>
</div>
<jsp:include page="/WEB-INF/views/snippets/footer.jsp"/>
</body>
</html>
