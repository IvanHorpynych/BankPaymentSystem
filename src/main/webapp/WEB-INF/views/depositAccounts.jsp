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
        <h1 class="title"><fmt:message key="deposit.accounts"/></h1>
    </c:if>
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
                                <c:if test="${depositAccount.isActive() and not sessionScope.user.isManager()}">
                                    <form action="${pageContext.request.contextPath}/site/user/replenish" method="get"
                                          class="col-xs-8 main-btn">
                                        <input type="hidden" name="command" value="withdraw"/>
                                        <input type="hidden" name="senderAccount"
                                               value="${depositAccount.getAccountNumber()}"/>
                                        <button type="submit" class="btn btn-info"><fmt:message
                                                key="deposit.withdraw"/></button>
                                    </form>
                                </c:if>
                                <button type="button" class="custom-btn btn btn-info dropdown-toggle"
                                        data-toggle="dropdown">
                                    <span class="caret"></span>
                                    <span class="sr-only">Toggle Dropdown</span>
                                </button>
                                <ul class="dropdown-menu" role="menu">
                                    <li>
                                        <form action="${pageContext.request.contextPath}/site/payments"
                                              method="get">
                                            <input type="hidden" name="command" value="accountPayments"/>
                                            <input type="hidden" name="account"
                                                   value="${depositAccount.getAccountNumber()}"/>
                                            <button type="submit" class="btn-link"><fmt:message
                                                    key="payment.histrory"/></button>
                                        </form>
                                    </li>
                                    <c:if test="${not depositAccount.isClosed() and not sessionScope.user.isManager()}">
                                        <li class="divider"></li>
                                        <li>
                                            <form action="${pageContext.request.contextPath}/site/user/replenish"
                                                  method="get">
                                                <input type="hidden" name="command" value="replenish"/>
                                                <input type="hidden" name="refillableAccount"
                                                       value="${depositAccount.getAccountNumber()}"/>
                                                <button type="submit" class="btn-link"><fmt:message
                                                        key="account.replenish"/></button>
                                            </form>
                                        </li>
                                    </c:if>
                                    <c:if test="${depositAccount.isActive() and not sessionScope.user.isManager()}">
                                        <li class="divider"></li>
                                        <li>
                                            <form action="${pageContext.request.contextPath}/site/user/close"
                                                  method="post">
                                                <input type="hidden" name="command" value="account.close"/>
                                                <input type="hidden" name="account"
                                                       value="${depositAccount.getAccountNumber()}"/>
                                                <button type="submit" class="btn-link"><fmt:message
                                                        key="account.close"/></button>
                                            </form>
                                        </li>
                                    </c:if>
                                    <c:if test="${depositAccount.isActive() and sessionScope.user.isManager()}">
                                        <li class="divider"></li>
                                        <li>
                                            <form action="${pageContext.request.contextPath}/site/manager/accounts/block" method="post">
                                                <input type="hidden" name="command" value="account.block"/>
                                                <input type="hidden" name="account" value="${depositAccount.getAccountNumber()}"/>
                                                <button type="submit" class="btn-link"><fmt:message key="account.block"/></button>
                                            </form>
                                        </li>
                                    </c:if>
                                    <c:if test="${depositAccount.isBlocked() and sessionScope.user.isManager()}">
                                        <li class="divider"></li>
                                        <li>
                                            <form action="${pageContext.request.contextPath}/site/manager/accounts/unblock" method="post">
                                                <input type="hidden" name="command" value="account.unblock"/>
                                                <input type="hidden" name="account" value="${depositAccount.getAccountNumber()}"/>
                                                <button type="submit" class="btn-link"><fmt:message key="account.unblock"/></button>
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
        <c:if test="${not empty sessionScope.user and not sessionScope.user.isManager()}">
            <form action="${pageContext.request.contextPath}/site/user/deposit_accounts" method="post">
                <input type="hidden" name="command" value="new.deposit"/>
                <button type="submit" class="btn btn-info"><fmt:message key="create.new"/></button>
            </form>
        </c:if>
    </div>
</div>
<jsp:include page="/WEB-INF/views/snippets/footer.jsp"/>
</body>
</html>
