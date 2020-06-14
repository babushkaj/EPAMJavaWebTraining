<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ taglib prefix="part" tagdir="/WEB-INF/tags" %>

<%@ page import="by.training.hospital.command.ApplicationCommandConstants" %>

<div class="columns is-centered" style="margin: 20px">
    <div class="column is-half">
        <form action="${pageContext.request.contextPath}/?command=${ApplicationCommandConstants.CHANGE_PASSWORD_CMD}"
              method="post">

            <part:pass fieldName="newPassword1" passLabelName="page.password_new_enter"
                       fieldPlaceholder="page.password_new_enter"/>

            <part:pass fieldName="newPassword2" passLabelName="page.password_new_repeat"
                       fieldPlaceholder="page.password_new_repeat"/>

            <c:if test="${requestScope.passwordErr != null}">
                <p class="help is-danger"><fmt:message key="${requestScope.passwordErr}"/></p>
            </c:if>

            <part:pass fieldName="oldPassword" passLabelName="page.password_old_enter"
                       fieldPlaceholder="page.password_old_enter"/>

            <c:if test="${requestScope.oldPasswordErr != null}">
                <p class="help is-danger"><fmt:message key="${requestScope.oldPasswordErr}"/></p>
            </c:if>
            <input style="display: none" id="submitButton" type="submit">
        </form>
        <div class="buttons is-centered">
            <button class="button is-link"
                    onclick="document.getElementById('submitButton').click()">
                <fmt:message key="page.submit"/></button>
            <button class="button is-link is-light"
                    onclick="window.history.back()">
                <fmt:message key="page.cancel"/></button>
        </div>
    </div>
</div>