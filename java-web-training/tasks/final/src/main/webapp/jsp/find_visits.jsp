<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ taglib prefix="part" tagdir="/WEB-INF/tags" %>

<%@ page import="by.training.hospital.command.ApplicationCommandConstants" %>

<div class="columns is-centered" style="margin: 20px">
    <div class="column is-half">
        <form action="${pageContext.request.contextPath}/?command=${ApplicationCommandConstants.FIND_VISITS_CMD}"
              method="post">
            <part:input inputLabel="page.firstname" inputName="firstName"
                        inputPh="page.firstname" isRequired="false"
                        inputValue=""/>

            <part:input inputLabel="page.lastname" inputName="lastName"
                        inputPh="page.lastname" isRequired="false"
                        inputValue=""/>

            <label for="dateInput" class="label"><fmt:message key="page.date"/></label>
            <input id="dateInput" name="dateInput" type="text" class="input">
            <input style="display: none" id="submitButton" type="submit">
        </form>
        <div class="columns is-centered">
            <div class="column is-four-fifths">
                <div class="buttons is-centered" style="margin: 10px">
                    <button class="button is-link"
                            onclick="document.getElementById('submitButton').click()">
                        <fmt:message key="page.submit"/></button>
                    <button class="button is-link is-light"
                            onclick="window.location.href='/?command=${ApplicationCommandConstants.SHOW_DOCTOR_VISITS_CMD}'">
                        <fmt:message key="page.cancel"/>
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    var today = new Date();
    bulmaCalendar.attach(document.querySelector('#dateInput'), {
        dateFormat: 'DD-MM-YYYY',
        type: 'date',
        weekStart: 1,
        showHeader: false,
        showFooter: false,
        color: 'info',
        overlay: true
    });

</script>