<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="part" tagdir="/WEB-INF/tags" %>

<%@ page import="by.training.hospital.command.ApplicationCommandConstants" %>
<%@ page import="java.time.DayOfWeek" %>

<div class="container" style="margin: 15px">
    <div class="columns is-centered">
        <div class="column is-half">
            <div class="container has-text-centered">
                <span class="label">${requestScope.doc.userInfo.firstName} ${requestScope.doc.userInfo.lastName}</span>
                <span class="label"><part:specByName docSpec="${requestScope.doc.doctorInfo.spec}"/></span>
                <span class="label"><fmt:message key="page.working_days"/>:
                    <c:forEach items="${DayOfWeek.values()}" var="dayFromEnum">
                        <c:forEach items="${requestScope.doc.doctorInfo.workingDays}" var="oneDay">
                            <c:if test="${dayFromEnum == oneDay}">
                                <part:dayByName day="${oneDay}"/>
                            </c:if>
                        </c:forEach>
                    </c:forEach></span>
            </div>
            <div class="container" style="margin: 10px">
                <form method="post"
                      action="${pageContext.request.contextPath}/?command=${ApplicationCommandConstants.CHOOSE_TIME_CMD}">
                    <input name="dateInput" type="text" class="input" required="required">
                    <input type="hidden" name="doctorUserId" value="${requestScope.doc.id}"/>
                    <input style="display: none" id="submitButton" type="submit">
                </form>
            </div>
            <div class="has-text-centered" style="margin: 10px; min-height: 18px">
                <c:if test="${requestScope.error != null}">
                    <p class="help is-danger"><fmt:message key="${requestScope.error}"/></p>
                </c:if>
            </div>
            <div class="buttons is-centered">
                <button class="button is-link" onclick="document.getElementById('submitButton').click()">
                    <fmt:message key="page.submit"/>
                </button>

                <button class="button is-link is-light" onclick="window.history.back()">
                <%--<button class="button is-link is-light"--%>
                        <%--onclick='location.href="${pageContext.request.contextPath}/?command=${ApplicationCommandConstants.GET_DOCTORS_CMD}"'>--%>
                    <fmt:message key="page.cancel"/>
                </button>
            </div>
        </div>
    </div>
</div>

<script>
    var today = new Date();
    bulmaCalendar.attach(document.querySelector('.input'), {
        dateFormat: 'DD-MM-YYYY',
        type: 'date',
        weekStart: 1,
        minDate: new Date(today.getFullYear(), today.getMonth(), today.getDate()),
        maxDate: new Date(today.getFullYear(), today.getMonth(), today.getDate() + 14),
        showHeader: false,
        showFooter: false,
        color: 'info',
        overlay: true
    });

</script>

