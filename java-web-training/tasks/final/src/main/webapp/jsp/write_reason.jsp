<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ page import="by.training.hospital.command.ApplicationCommandConstants" %>

<div class="container" style="margin: 15px">
    <div class="columns is-centered">
        <div class="column is-half">
            <form action="${pageContext.request.contextPath}/?command=${ApplicationCommandConstants.MAKE_APPOINTMENT_CMD}" method="post">
                <input type="hidden" name="visitTime" value="${requestScope.visitTime}">
                <input type="hidden" name="doctorUserId" value="${requestScope.doctorUserId}">
                <div class="container" style="margin: 15px">
                    <label class="label"><fmt:message key="page.visit_reason"/>:</label>
                    <textarea name="visitReason" class="textarea has-fixed-size"
                              placeholder="<fmt:message key="page.visit_reason"/>..."
                              rows="4"
                              maxlength="300"
                              required="required"></textarea>
                </div>
                <input style="display: none" id="submitButton" type="submit">
            </form>
            <div class="buttons is-centered">
                <button class="button is-link"
                        onclick="document.getElementById('submitButton').click()">
                    <fmt:message key="page.submit"/>
                </button>
                <button class="button is-link is-light" onclick="window.history.back()">
                    <fmt:message key="page.back"/>
                </button>
            </div>
        </div>
    </div>
</div>