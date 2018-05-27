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
<c:if test="${not empty requestScope.errors}">
    <div class="alert alert-danger">
        <c:forEach items="${requestScope.errors}" var="error">
            <strong><fmt:message key="error"/></strong> <fmt:message key="${error}"/><br>
        </c:forEach>
    </div>
</c:if>
<div class="container">
    <form class="form-horizontal" role="form" method="post">
        <input type="hidden" name="command" value="signup.post"/>
        <h2><fmt:message key="create.new.profile"/></h2>
        <div class="form-group">
            <label for="firstname" class="col-sm-3 control-label"><fmt:message key="firstname"/></label>
            <div class="col-sm-9">
                <input type="text" id="firstname" name="firstname"
                       placeholder="<fmt:message key="enter.firstname"/>" class="form-control" autofocus
                       value="<c:out value="${requestScope.user.getFirstName()}"/>"/>
            </div>
        </div>

        <div class="form-group">
            <label for="lastname" class="col-sm-3 control-label"><fmt:message key="lastname"/></label>
            <div class="col-sm-9">
                <input type="text" id="lastname" name="lastname"
                       placeholder="<fmt:message key="enter.lastname"/>" class="form-control" autofocus
                       value="<c:out value="${requestScope.user.getLastName()}"/>"/>
            </div>
        </div>

        <div class="form-group">
            <label for="email" class="col-sm-3 control-label"><fmt:message key="email"/></label>
            <div class="col-sm-9">
                <input type="email" id="email" name="email"
                       placeholder="<fmt:message key="enter.email"/>" class="form-control"
                       value="<c:out value="${requestScope.user.getEmail()}"/>"/>
            </div>
        </div>

        <div class="form-group">
            <label for="phoneNumber" class="col-sm-3 control-label"><fmt:message key="phone"/></label>
            <div class="col-sm-9">
                <input type="tel" id="phoneNumber" name="phoneNumber"
                       placeholder="<fmt:message key="enter.phone"/>" class="form-control"
                       value="<c:out value="${requestScope.user.getPhoneNumber()}"/>"/>
            </div>
        </div>

        <div class="form-group">
            <label for="password" class="col-sm-3 control-label"><fmt:message key="password"/></label>
            <div class="col-sm-9">
                <input type="password" id="password" name="password"
                       placeholder="<fmt:message key="enter.password"/>" class="form-control">
            </div>
        </div>

        <div class="form-group">
            <div class="col-sm-9 col-sm-offset-3">
                <button type="submit" class="btn btn-primary btn-block"><fmt:message key="signup"/></button>
            </div>
        </div>
    </form>
</div>
<jsp:include page="/WEB-INF/views/snippets/footer.jsp"/>
</body>
</html>

