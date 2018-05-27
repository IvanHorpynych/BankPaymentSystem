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
    <h1 class="title"><fmt:message key="account.replenish"/></h1>
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

    </div>
</div>


<div class="container">
    <div class="row col-md-12">
            <form class="form-inline" action="${pageContext.request.contextPath}/site/user/new_payment" method="post">
                <div class="col-md-5">
                    <ul class="list-group list-group-flush">
                        <li class="list-group-item">
                            <label for="cardFrom">
                                <fmt:message key="card.from"/>:
                            </label>
                            <select name="cardFrom" class="form-control" id="cardFrom">
                                <c:forEach var="card" items="${requestScope.cards}">
                                    <c:if test="${card.isActive() and card.getAccount().isActive()}">
                                        <option>${card.getCardNumber()}&nbsp(${card.getAccount().getBalance()}
                                            <fmt:message key="currency"/>)
                                        </option>
                                    </c:if>
                                </c:forEach>
                            </select>
                        </li>
                        <li class="list-group-item"><b><fmt:message key="enter"/>&nbsp cvv:</b>
                            <input name="cvv" type="text" class="form-control default-min-input" id="cvv" pattern="^\d{3}$"
                                   title="<fmt:message key="cvv.title"/>">
                        </li>
                        <li class="list-group-item"><b><fmt:message key="enter"/>&nbsp pin:</b>
                            <input name="pin" type="text" class="form-control default-min-input" id="pin" pattern="^\d{4}$"
                                   title="<fmt:message key="pin.title"/>">
                        </li>
                    </ul>
                </div>
                <div class="col-md-3">
                    <ul class="list-group list-group-flush">
                        <li class="list-group-item"><b><fmt:message key="card.to"/>:</b>
                            <input name="cardTo" type="text" class="form-control default-input" id="cardTo" pattern="^\d{16}$"
                                   title="<fmt:message key="card.title"/>"
                                   value="<c:out value="${requestScope.cardTo}"/>">
                        </li>
                    </ul>
                </div>
                <div class="col-md-4">
                    <ul class="list-group list-group-flush">
                        <li class="list-group-item"><b><fmt:message key="amount"/></b>
                            <input name="amount" type="number" class="form-control" id="amount" step="0.0001"
                                   placeholder="<fmt:message key="enter.amount" />">
                            <fmt:message key="currency"/>
                        </li>
                        <li class="list-group-item text-right">
                            <input type="hidden" name="command" value="payment.do"/>
                            <button type="submit" class="btn btn-info">
                                <fmt:message key="create"/>
                            </button>
                        </li>
                    </ul>
                </div>
            </form>
    </div>
</div>


<jsp:include page="/WEB-INF/views/snippets/footer.jsp"/>
</body>
</html>