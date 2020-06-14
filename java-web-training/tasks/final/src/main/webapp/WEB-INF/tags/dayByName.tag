<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ tag import="java.time.DayOfWeek" %>

<jsp:directive.attribute name="day" required="true" type="java.time.DayOfWeek" description="Day of week"/>

<c:choose>
    <c:when test="${day == DayOfWeek.MONDAY}">
        <span><fmt:message key="day.monday"/> </span>
    </c:when>
    <c:when test="${day == DayOfWeek.TUESDAY}">
        <span><fmt:message key="day.tuesday"/> </span>
    </c:when>
    <c:when test="${day == DayOfWeek.WEDNESDAY}">
        <span><fmt:message key="day.wednesday"/> </span>
    </c:when>
    <c:when test="${day == DayOfWeek.THURSDAY}">
        <span><fmt:message key="day.thursday"/> </span>
    </c:when>
    <c:when test="${day == DayOfWeek.FRIDAY}">
        <span><fmt:message key="day.friday"/> </span>
    </c:when>
    <c:when test="${day == DayOfWeek.SATURDAY}">
        <span><fmt:message key="day.saturday"/> </span>
    </c:when>
    <c:when test="${day == DayOfWeek.SUNDAY}">
        <span><fmt:message key="day.sunday"/> </span>
    </c:when>
</c:choose>

