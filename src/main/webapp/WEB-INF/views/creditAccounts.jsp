<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="i18n.lang"/>

<html>
<head>
    <jsp:include page="/WEB-INF/views/snippets/header.jsp"/>
    <style>
        .custom-btn {
            padding: 14px 12px;
        }
    </style>
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


<div class="container">
    <div class="row col-md-6">

        <div class="panel-title text-center">
            <c:if test="${not empty sessionScope.user and not sessionScope.user.isManager()}">
                <h1 class="title"><fmt:message key="your.accounts"/></h1>
            </c:if>
            <%--<c:if test="${not empty sessionScope.user and sessionScope.user.isAdmin()}">
                <h1 class="title"><fmt:message key="accounts"/></h1>
            </c:if>--%>
            <hr/>
        </div>
        <c:choose>
            <c:when test="${not empty requestScope.creditAccounts}">
                <%--<table class="table table-bordered table-hover">
                    <thead>
                    <tr>
                        <th><fmt:message key="account.number"/></th>
                        <th><fmt:message key="account.balance"/></th>
                        <th><fmt:message key="credit.limit"/></th>
                        <th><fmt:message key="interest.rate"/></th>
                        <th><fmt:message key="accrued.interest"/></th>
                        <th><fmt:message key="account.status"/></th>
                        <th><fmt:message key="account.action"/></th>
                    </tr>
                    </thead>
                    <tbody>--%>
                    <c:forEach var="creditAccounts" items="${requestScope.creditAccounts}">
                        <ul class="list-group">
                            <li class="list-group-item"><fmt:message key="account.number"/>:
                                <c:out value="${creditAccounts.getAccountNumber()}"/></li>
                            <li class="list-group-item"><fmt:message key="account.balance"/>:
                                <c:out value="${creditAccounts.getBalance()}"/>
                                <fmt:message key="currency"/>
                            </li>
                            <li class="list-group-item"><fmt:message key="credit.limit"/>:
                                <c:out value="${creditAccounts.getCreditLimit()}"/>
                                <fmt:message key="currency"/>
                            </li>
                            <li class="list-group-item"><fmt:message key="interest.rate"/>:
                                <c:out value="${creditAccounts.getInterestRate()}"/>%</li>
                            <li class="list-group-item"><fmt:message key="accrued.interest"/>:
                                <c:out value="${creditAccounts.getAccruedInterest()}"/>
                                <fmt:message key="currency"/>
                            </li>
                            <li class="list-group-item"><fmt:message key="account.status"/>:
                                <c:out value="${creditAccounts.getStatus().getName()}"/>
                            </li>
                            <div class="btn-group">
                                <button type="button" class="btn btn-default">Action</button>
                                <button type="button" class="custom-btn btn btn-default dropdown-toggle"
                                        data-toggle="dropdown">
                                    <span class="caret"></span>
                                    <span class="sr-only">Toggle Dropdown</span>
                                </button>
                                <ul class="dropdown-menu" role="menu">
                                    <li><a href="#">Action</a></li>
                                    <li><a href="#">Another action</a></li>
                                    <li><a href="#">Something else here</a></li>
                                    <li class="divider"></li>
                                    <li><a href="#">Separated link</a></li>
                                </ul>
                            </div>
                        </ul>
                        <%--<tr>
                            <td><c:out value="${creditAccounts.getAccountNumber()}"/></td>
                            <td>
                                <c:out value="${creditAccounts.getBalance()}"/>
                                <fmt:message key="currency"/>
                            </td>
                            <td><c:out value="${creditAccounts.getCreditLimit()}"/>
                                <fmt:message key="currency"/>
                            </td>
                            <td><c:out value="${creditAccounts.getInterestRate()}"/>%</td>
                            <td><c:out value="${creditAccounts.getAccruedInterest()}"/>
                                <fmt:message key="currency"/>
                            </td>
                            <td><c:out value="${creditAccounts.getStatus().getName()}"/></td>
                            <td><div class="btn-group">
                                <button type="button" class="btn btn-danger">Action</button>
                                <button type="button" class="custom-btn btn btn-info dropdown-toggle"
                                        data-toggle="dropdown">
                                    <span class="caret"></span>
                                    <span class="sr-only">Toggle Dropdown</span>
                                </button>
                                <ul class="dropdown-menu" role="menu">
                                    <li><a href="#">Action</a></li>
                                    <li><a href="#">Another action</a></li>
                                    <li><a href="#">Something else here</a></li>
                                    <li class="divider"></li>
                                    <li><a href="#">Separated link</a></li>
                                </ul>
                            </div>
                            </td>
                            &lt;%&ndash;<td>
                                <c:if test="${not sessionScope.user.isManager()}">
                                    <c:if test="${account.isActive()}">
                                        <form action="${pageContext.request.contextPath}/site/user/accounts/block"
                                              method="POST">
                                            <input type="hidden" name="account" value="${account.getAccountNumber()}">
                                            <button type="submit" class='btn btn-info btn-xs'>
                                                <fmt:message key="account.block"/>
                                            </button>
                                        </form>
                                    </c:if>
                                </c:if>
                                <c:if test="${sessionScope.user.isManager()}">
                                    <c:choose>
                                        <c:when test="${account.isBlocked()}">
                                            <form action="${pageContext.request.contextPath}/site/admin/accounts/unblock"
                                                  method="POST">
                                                <input type="hidden" name="account"
                                                       value="${account.getAccountNumber()}">
                                                <button type="submit" class='btn btn-info btn-xs'>
                                                    <fmt:message key="account.unblock"/>
                                                </button>
                                            </form>
                                        </c:when>
                                        <c:when test="${account.isPending()}">
                                            <form action="${pageContext.request.contextPath}/site/admin/accounts/confirm"
                                                  method="POST">
                                                <input type="hidden" name="account"
                                                       value="${account.getAccountNumber()}">
                                                <button type="submit" class='btn btn-info btn-xs'>
                                                    <fmt:message key="account.confirm"/>
                                                </button>
                                            </form>
                                        </c:when>
                                        <c:when test="${account.isActive()}">
                                            <form action="${pageContext.request.contextPath}/site/admin/accounts/block"
                                                  method="POST">
                                                <input type="hidden" name="account"
                                                       value="${account.getAccountNumber()}">
                                                <button type="submit" class='btn btn-info btn-xs'>
                                                    <fmt:message key="account.block"/>
                                                </button>
                                            </form>
                                        </c:when>
                                    </c:choose>
                                </c:if>
                            </td>&ndash;%&gt;
                        </tr>--%>
                    </c:forEach>
                    <%--</tbody>
                </table>--%>
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
