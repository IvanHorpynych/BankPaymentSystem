<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="customTag" uri="/WEB-INF/customTags/selectedPageTag" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<fmt:setLocale value='${sessionScope.locale}'/>
<fmt:setBundle basename="i18n.lang"/>

<c:set var="creditAccountsPage" scope="page" value="/WEB-INF/views/creditAccounts.jsp"/>
<c:set var="debitAccountsPage" scope="page" value="/WEB-INF/views/debitAccounts.jsp"/>
<c:set var="depositAccountsPage" scope="page" value="/WEB-INF/views/depositAccounts.jsp"/>
<c:set var="cardsPage" scope="page" value="/WEB-INF/views/cards.jsp"/>
<c:set var="newPaymentPage" scope="page" value="/WEB-INF/views/newPayment.jsp"/>
<c:set var="paymentsPage" scope="page" value="/WEB-INF/views/payments.jsp"/>
<c:set var="loginPage" scope="page" value="/WEB-INF/views/login.jsp"/>
<c:set var="signUpPage" scope="page" value="/WEB-INF/views/signup.jsp"/>
<c:set var="replenishPage" scope="page" value="/WEB-INF/views/replenish.jsp"/>
<c:set var="createRequest" scope="page" value="/WEB-INF/views/newRequest.jsp"/>
<c:set var="creditRequestPage" scope="page" value="/WEB-INF/views/creditRequests.jsp"/>
<c:set var="creditRequestsListPage" scope="page" value="/WEB-INF/views/creditRequestsList.jsp"/>
<c:set var="usersListPage" scope="page" value="/WEB-INF/views/users.jsp"/>
<c:set var="ratePage" scope="page" value="/WEB-INF/views/rate.jsp"/>

<c:set var="currPage" scope="page">
    <customTag:currPage/>
</c:set>

<nav class="navbar navbar-default navbar-fixed-top">
    <div class="container">
        <div class="navbar-header">
            <a class="navbar-brand fas fa-credit-card" href="${pageContext.request.contextPath}/site/home">&nbspBPS</a>
        </div>
        <ul class="nav navbar-nav">
            <li class="dropdown">
                <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                    <i class="fas fa-globe" aria-hidden="true"></i>
                    ${sessionScope.locale.getLanguage().toUpperCase()}
                    <span class="caret"></span></a>
                <ul class="dropdown-menu">
                    <c:forEach items="${applicationScope.supportedLocales}" var="lang">
                        <li><a href="?lang=${lang}">${lang.toUpperCase()}</a></li>
                    </c:forEach>
                </ul>
            </li>
            <c:if test="${not empty sessionScope.user and not sessionScope.user.isManager()}">
                <c:choose>
                    <c:when test="${creditAccountsPage.equals(currPage) or
                    debitAccountsPage.equals(currPage) or
                    depositAccountsPage.equals(currPage)}">
                        <li class="dropdown active">
                    </c:when>
                    <c:otherwise>
                        <li class="dropdown">
                    </c:otherwise>
                </c:choose>
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                        <fmt:message key="accounts"/>
                        <span class="caret"></span></a>
                    <ul class="dropdown-menu">
                            <li><a href="${pageContext.request.contextPath}/site/user/debit_accounts">
                                <fmt:message key="debit.accounts"/>
                            </a></li>
                            <li><a href="${pageContext.request.contextPath}/site/user/deposit_accounts">
                                <fmt:message key="deposit.accounts"/>
                            </a></li>
                            <li><a href="${pageContext.request.contextPath}/site/user/credit_accounts">
                                <fmt:message key="credit.accounts"/>
                            </a></li>
                    </ul>
                <c:choose>
                    <c:when test="${replenishPage.equals(currPage)}">
                        <li class="active">
                    </c:when>
                    <c:otherwise>
                        <li>
                    </c:otherwise>
                </c:choose>
                    <a>
                        <fmt:message key="account.replenish"/>
                    </a>
                </li>

                <c:choose>
                    <c:when test="${cardsPage.equals(currPage)}">
                        <li class="active">
                    </c:when>
                    <c:otherwise>
                        <li>
                    </c:otherwise>
                </c:choose>
                    <a href="${pageContext.request.contextPath}/site/user/cards">
                        <fmt:message key="cards"/>
                    </a>
                </li>

                <c:choose>
                    <c:when test="${newPaymentPage.equals(currPage)}">
                        <li class="active">
                    </c:when>
                    <c:otherwise>
                        <li>
                    </c:otherwise>
                </c:choose>
                    <a href="${pageContext.request.contextPath}/site/user/new_payment">
                        <fmt:message key="payment.create"/>
                    </a>
                </li>

                <c:choose>
                    <c:when test="${paymentsPage.equals(currPage)}">
                        <li class="active">
                    </c:when>
                    <c:otherwise>
                        <li>
                    </c:otherwise>
                </c:choose>
                    <a href="${pageContext.request.contextPath}/site/user/payments">
                        <fmt:message key="payment.histrory"/>
                    </a>
                </li>

                <c:choose>
                    <c:when test="${createRequest.equals(currPage) or creditRequestPage.equals(currPage)}">
                        <li class="active">
                    </c:when>
                    <c:otherwise>
                        <li>
                    </c:otherwise>
                </c:choose>
                    <a href="${pageContext.request.contextPath}/site/user/credit_request">
                        <fmt:message key="credit.request"/>
                    </a>
                </li>

            </c:if>

            <c:if test="${not empty sessionScope.user and sessionScope.user.isManager()}">
                <c:choose>
                    <c:when test="${usersListPage.equals(currPage)}">
                        <li class="active">
                    </c:when>
                    <c:otherwise>
                        <li>
                    </c:otherwise>
                </c:choose>
                    <a href="${pageContext.request.contextPath}/site/manager/users">
                        <fmt:message key="users"/>
                    </a>
                </li>

                <c:choose>
                    <c:when test="${creditRequestsListPage.equals(currPage)}">
                        <li class="active">
                    </c:when>
                    <c:otherwise>
                        <li>
                    </c:otherwise>
                </c:choose>
                <a href="${pageContext.request.contextPath}/site/manager/requests">
                    <fmt:message key="credit.requests"/>
                </a>
                </li>

                <c:choose>
                    <c:when test="${creditAccountsPage.equals(currPage)
                    or debitAccountsPage.equals(currPage)
                    or depositAccountsPage.equals(currPage)}">
                        <li class="active">
                    </c:when>
                    <c:otherwise>
                        <li>
                    </c:otherwise>
                </c:choose>
                    <a>
                        <fmt:message key="accounts"/>
                    </a>
                </li>

                <c:choose>
                    <c:when test="${cardsPage.equals(currPage)}">
                        <li class="active">
                    </c:when>
                    <c:otherwise>
                        <li>
                    </c:otherwise>
                </c:choose>
                    <a>
                        <fmt:message key="cards"/>
                    </a>
                </li>

                <c:choose>
                    <c:when test="${replenishPage.equals(currPage)}">
                        <li class="active">
                    </c:when>
                    <c:otherwise>
                        <li>
                    </c:otherwise>
                </c:choose>
                <a>
                    <fmt:message key="account.replenish"/>
                </a>
                </li>

                <c:choose>
                    <c:when test="${paymentsPage.equals(currPage)}">
                        <li class="active">
                    </c:when>
                    <c:otherwise>
                        <li>
                    </c:otherwise>
                </c:choose>
                <a>
                    <fmt:message key="payment.histrory"/>
                </a>
                </li>

                <c:choose>
                    <c:when test="${ratePage.equals(currPage)}">
                        <li class="active">
                    </c:when>
                    <c:otherwise>
                        <li>
                    </c:otherwise>
                </c:choose>
                <a href="${pageContext.request.contextPath}/site/manager/annual_rate">
                    <fmt:message key="rate"/>
                </a>
                </li>
            </c:if>
        </ul>
        <ul class="nav navbar-nav navbar-right">
            <c:if test="${empty sessionScope.user}">
                <c:choose>
                    <c:when test="${signUpPage.equals(currPage)}">
                        <li class="active">
                    </c:when>
                    <c:otherwise>
                        <li>
                    </c:otherwise>
                </c:choose>
                    <a href="${pageContext.request.contextPath}/site/signup">
                        <fmt:message key="signup"/>
                    </a>
                </li>
                <c:choose>
                    <c:when test="${loginPage.equals(currPage)}">
                        <li class="active">
                    </c:when>
                    <c:otherwise>
                        <li>
                    </c:otherwise>
                </c:choose>
                    <a href="${pageContext.request.contextPath}/site/login">
                        <fmt:message key="login"/>
                    </a>
                </li>
            </c:if>
            <c:if test="${not empty sessionScope.user}">
                <li>
                    <a href="#">
                        <fmt:message key="welcome"/><c:out value="${sessionScope.user.getFirstName()}"/>
                    </a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/site/logout?command=logout">
                       <fmt:message key="logout"/>
                    </a>
                </li>
            </c:if>
        </ul>
    </div>
</nav>