<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<fmt:setLocale value='${sessionScope.locale}'/>
<fmt:setBundle basename="i18n.lang"/>


<nav class="navbar navbar-inverse navbar-fixed-top">
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
                <li>
                    <a href="${pageContext.request.contextPath}/site/user/accounts">
                        <fmt:message key="accounts"/>
                    </a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/site/user/replenish">
                        <fmt:message key="account.replenish"/>
                    </a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/site/user/cards">
                        <fmt:message key="cards"/>
                    </a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/site/user/create">
                        <fmt:message key="payment.create"/>
                    </a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/site/user/payments">
                        <fmt:message key="payment.histrory"/>
                    </a>
                </li>

            </c:if>

            <c:if test="${not empty sessionScope.user and sessionScope.user.isManager()}">
                <li>
                    <a href="${pageContext.request.contextPath}/site/admin/accounts">
                        <fmt:message key="accounts"/>
                    </a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/site/admin/cards">
                        <fmt:message key="cards"/>
                    </a>
                </li>

                <li>
                    <a href="${pageContext.request.contextPath}/site/admin/payments">
                        <fmt:message key="payment.histrory"/>
                    </a>
                </li>
            </c:if>
        </ul>
        <ul class="nav navbar-nav navbar-right">
            <c:if test="${empty sessionScope.user}">
                <li>
                    <a href="${pageContext.request.contextPath}/site/signup">
                        <fmt:message key="signup"/>
                    </a>
                </li>
                <li>
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