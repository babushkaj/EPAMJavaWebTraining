<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ page import="by.training.hospital.command.ApplicationCommandConstants" %>

<div class="columns is-centered" style="margin: 20px">
    <div class="column is-half">
        <form action="${pageContext.request.contextPath}/?command=${ApplicationCommandConstants.LOGIN_CMD}"
              method="post">
            <label class="label"><fmt:message key="page.login"/></label>
            <div class="control">
                <input class="input" name="login" type="text" placeholder="<fmt:message key="page.login"/>"
                       value="${requestScope.login}" required="required">
            </div>
            <label class="label"><fmt:message key="page.password"/></label>
            <div class="control">
                <input class="input" name="password" type="password"
                       placeholder="<fmt:message key="page.password_enter_ph"/>"
                       required="required">
            </div>
            <c:if test="${requestScope.message != null}">
                <p class="help is-danger"><fmt:message key="${requestScope.message}"/></p>
            </c:if>
            <input style="display: none" id="submitButton" type="submit">
        </form>
        <div class="buttons is-centered">
            <button class="button is-link"
                    onclick="document.getElementById('submitButton').click()">
                <fmt:message key="page.login_btn"/></button>
            <button class="button is-link is-light"
                    onclick="window.location.href='${pageContext.request.contextPath}/'">
                <fmt:message key="page.cancel"/></button>
        </div>
    </div>
</div>




