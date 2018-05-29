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
        <h1 class="title"><fmt:message key="deposit.accounts"/></h1>
    </c:if>
    <%--<c:if test="${not empty sessionScope.user and sessionScope.user.isAdmin()}">
        <h1 class="title"><fmt:message key="accounts"/></h1>
    </c:if>--%>
    <hr/>
</div>

<div class="container">
    <div class="row col-md-4">
        <c:choose>
            <c:when test="${not empty requestScope.depositAccounts}">
                <c:forEach var="depositAccount" items="${requestScope.depositAccounts}">
                    <ul class="list-group list-group-flush">
                        <li class="list-group-item"><b><fmt:message key="account.number"/></b>:
                            <c:out value="${depositAccount.getAccountNumber()}"/></li>
                        <li class="list-group-item"><b><fmt:message key="account.balance"/></b>:
                            <c:out value="${depositAccount.getBalance()}"/>
                            <fmt:message key="currency"/>
                        </li>
                        <li class="list-group-item"><b><fmt:message key="deposit.min.month.balance"/></b>:
                            <c:out value="${depositAccount.getMinBalance()}"/>
                            <fmt:message key="currency"/>
                        </li>
                        <li class="list-group-item"><b><fmt:message key="deposit.annual.rate"/></b>:
                            <c:out value="${depositAccount.getAnnualRate()}"/>%
                        </li>
                        <li class="list-group-item"><b><fmt:message key="account.status"/></b>:
                            <c:out value="${depositAccount.getStatus().getName()}"/>
                        </li>
                        <li class="list-group-item">
                            <div class="btn-group group-style">
                                <c:if test="${depositAccount.isActive()}">
                                    <form action="${pageContext.request.contextPath}/site/user/replenish" method="get" class="col-xs-8 main-btn">
                                        <input type="hidden" name="command" value="withdraw"/>
                                        <input type="hidden" name="senderAccount" value="${depositAccount.getAccountNumber()}"/>
                                        <button type="submit" class="btn btn-info"><fmt:message key="deposit.withdraw"/></button>
                                    </form>
                                </c:if>
                                <button type="button" class="custom-btn btn btn-info dropdown-toggle"
                                        data-toggle="dropdown">
                                    <span class="caret"></span>
                                    <span class="sr-only">Toggle Dropdown</span>
                                </button>
                                <ul class="dropdown-menu" role="menu">
                                    <li>
                                        <form action="${pageContext.request.contextPath}/site/user/payments" method="get">
                                            <input type="hidden" name="command" value="accountPayments"/>
                                            <input type="hidden" name="account" value="${depositAccount.getAccountNumber()}"/>
                                            <button type="submit" class="btn-link"><fmt:message key="payment.histrory"/></button>
                                        </form>
                                    </li>
                                    <c:if test="${not depositAccount.isClosed() and not sessionScope.user.isManager()}">
                                        <li class="divider"></li>
                                        <li>
                                            <form action="${pageContext.request.contextPath}/site/user/replenish" method="get">
                                                <input type="hidden" name="command" value="replenish"/>
                                                <input type="hidden" name="refillableAccount" value="${depositAccount.getAccountNumber()}"/>
                                                <button type="submit" class="btn-link"><fmt:message key="account.replenish"/></button>
                                            </form>
                                        </li>
                                    </c:if>
                                    <li class="divider"></li>
                                    <c:if test="${depositAccount.isActive() and not sessionScope.user.isManager()}">
                                        <li>
                                            <form action="${pageContext.request.contextPath}/site/user/close" method="post">
                                                <input type="hidden" name="command" value="account.close"/>
                                                <input type="hidden" name="account" value="${depositAccount.getAccountNumber()}"/>
                                                <button type="submit" class="btn-link"><fmt:message key="account.close"/></button>
                                            </form>
                                        </li>
                                    </c:if>
                                    <c:if test="${depositAccount.isActive() and sessionScope.user.isManager()}">
                                        <li>
                                            <form action="your_url" method="post">
                                                <input type="hidden" name="command" value="login_post"/>
                                                <button type="submit" class="btn-link"><fmt:message key="account.block"/></button>
                                            </form>
                                        </li>
                                    </c:if>
                                    <c:if test="${depositAccount.isBlocked() and sessionScope.user.isManager()}">
                                        <li>
                                            <form action="your_url" method="post">
                                                <input type="hidden" name="command" value="login_post"/>
                                                <button type="submit" class="btn-link"><fmt:message key="account.unblock"/></button>
                                            </form>
                                        </li>
                                    </c:if>
                                </ul>
                            </div>
                        </li>
                    </ul>
                    <%--<tr>
                        <td><c:out value="${depositAccounts.getAccountNumber()}"/></td>
                        <td>
                            <c:out value="${depositAccounts.getBalance()}"/>
                            <fmt:message key="currency"/>
                        </td>
                        <td><c:out value="${depositAccounts.getCreditLimit()}"/>
                            <fmt:message key="currency"/>
                        </td>
                        <td><c:out value="${depositAccounts.getInterestRate()}"/>%</td>
                        <td><c:out value="${depositAccounts.getAccruedInterest()}"/>
                            <fmt:message key="currency"/>
                        </td>
                        <td><c:out value="${depositAccounts.getStatus().getName()}"/></td>
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
