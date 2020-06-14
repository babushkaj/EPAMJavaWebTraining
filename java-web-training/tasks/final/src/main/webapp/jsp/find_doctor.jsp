<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ taglib prefix="part" tagdir="/WEB-INF/tags" %>

<%@ page import="by.training.hospital.command.ApplicationCommandConstants" %>
<%@ page import="by.training.hospital.entity.Specialization" %>
<%@ page import="java.time.DayOfWeek" %>

<div class="columns is-centered" style="margin: 20px">
    <div class="column is-half">
        <form action="${pageContext.request.contextPath}/?command=${ApplicationCommandConstants.FIND_DOCTORS_CMD}"
              method="post">
            <part:input inputLabel="page.firstname" inputName="firstName"
                        inputPh="page.firstname" inputTitle="title.firstname_format"
                        isRequired="false"
                        inputValue="" inputErr="${requestScope.firstnameErr}"/>

            <part:input inputLabel="page.lastname" inputName="lastName"
                        inputPh="page.lastname" inputTitle="title.lastname_format"
                        isRequired="false"
                        inputValue="" inputErr="${requestScope.lastnameErr}"/>
            <part:workingDays allDays="${DayOfWeek.values()}"/>
            <part:docSpec fieldName="specSelect" specs="${Specialization.values()}"/>
            <c:if test="${requestScope.specErr != null}">
                <p class="help is-danger" style="min-height: 10px"><fmt:message
                        key="${requestScope.specErr}"/></p>
            </c:if>
            <input style="display: none" id="submitButton" type="submit">
        </form>
        <div class="columns is-centered">
            <div class="column is-four-fifths">
                <div class="buttons is-centered" style="margin: 10px">
                    <button class="button is-link"
                            onclick="document.getElementById('submitButton').click()">
                        <fmt:message key="page.submit"/></button>
                    <button class="button is-link is-light"
                            onclick="window.location.href='/'">
                        <fmt:message key="page.cancel"/>
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>