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
        <h1 class="title"><fmt:message key="credit.request"/></h1>
    </c:if>
    <%--<c:if test="${not empty sessionScope.user and sessionScope.user.isAdmin()}">
        <h1 class="title"><fmt:message key="accounts"/></h1>
    </c:if>--%>
    <hr/>
</div>
<div class="container">
    <div class="row col-md-4">
        <c:choose>
            <c:when test="${not empty requestScope.creditRequests}">
                <c:forEach var="creditRequest" items="${requestScope.creditRequests}">
                    <c:if test="${creditRequest.isPending()}">
                        <ul class="list-group list-group-flush">
                            <li class="list-group-item"><b><fmt:message key="credit.request"/></b>:
                                <c:out value="${creditRequest.getRequestNumber()}"/>
                            </li>
                            <li class="list-group-item"><b><fmt:message key="credit.limit"/></b>:
                                <c:out value="${creditRequest.getCreditLimit()}"/>
                                <fmt:message key="currency"/>
                            </li>
                            <li class="list-group-item"><b><fmt:message key="interest.rate"/></b>:
                                <c:out value="${creditRequest.getInterestRate()}"/>%
                            </li>
                            <li class="list-group-item"><b><fmt:message key="validity.date"/></b>:
                                <fmt:formatDate type="date" value="${creditRequest.getValidityDate()}"/>
                            </li>
                            <li class="list-group-item"><b><fmt:message key="account.status"/></b>:
                                <c:out value="${creditRequest.getStatus().getName()}"/>
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
                                            <form action="your_url" method="post">
                                                <input type="hidden" name="command" value="login_post"/>
                                                <button type="submit" class="btn-link"><fmt:message
                                                        key="account.close"/></button>
                                            </form>
                                        </li>

                                            <%-- <c:if test="${card.isBlocked() and sessionScope.user.isManager()}">
                                                 <li class="divider"></li>
                                                 <li>
                                                     <form action="your_url" method="post">
                                                         <input type="hidden" name="command" value="login_post"/>
                                                         <button type="submit" class="btn-link"><fmt:message
                                                                 key="account.unblock"/></button>
                                                     </form>
                                                 </li>
                                             </c:if>--%>
                                    </ul>
                                </div>
                            </li>
                        </ul>
                    </c:if>
                    <%--<tr>
                        <td><c:out value="${debitAccounts.getAccountNumber()}"/></td>
                        <td>
                            <c:out value="${debitAccounts.getBalance()}"/>
                            <fmt:message key="currency"/>
                        </td>
                        <td><c:out value="${debitAccounts.getCreditLimit()}"/>
                            <fmt:message key="currency"/>
                        </td>
                        <td><c:out value="${debitAccounts.getInterestRate()}"/>%</td>
                        <td><c:out value="${debitAccounts.getAccruedInterest()}"/>
                            <fmt:message key="currency"/>
                        </td>
                        <td><c:out value="${debitAccounts.getStatus().getName()}"/></td>
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
        <form action="${pageContext.request.contextPath}/site/user/credit_request" method="get">
            <input type="hidden" name="command" value="new.request"/>
            <button type="submit" class="btn btn-info"><fmt:message key="create.new"/></button>
        </form>
    </div>
</div>
<jsp:include page="/WEB-INF/views/snippets/footer.jsp"/>
</body>
</html>
