<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<jsp:directive.attribute name="allDays" required="true"
                         type="java.time.DayOfWeek[]"
                         description="All days"/>

<jsp:directive.attribute name="docWD" required="false"
                         type="java.util.Set<java.time.DayOfWeek>"
                         description="Doctor's working days"/>

<div class="container" style="margin: 15px">
    <label class="label"><fmt:message key="page.working_days"/></label>
    <table class="table is-striped is-fullwidth is-narrow">
        <thead>
        <tr style="text-align: center">
            <th style="min-width: 14%; width: 14%; text-align: center; vertical-align: middle">
                <fmt:message key="day.monday"/></th>
            <th style="min-width: 14%; width: 14%; text-align: center; vertical-align: middle">
                <fmt:message key="day.tuesday"/></th>
            <th style="min-width: 14%; width: 14%; text-align: center; vertical-align: middle">
                <fmt:message
                        key="day.wednesday"/></th>
            <th style="min-width: 14%; width: 14%; text-align: center; vertical-align: middle">
                <fmt:message
                        key="day.thursday"/></th>
            <th style="min-width: 14%; width: 14%; text-align: center; vertical-align: middle">
                <fmt:message
                        key="day.friday"/></th>
            <th style="min-width: 14%; width: 14%; text-align: center; vertical-align: middle">
                <fmt:message
                        key="day.saturday"/></th>
            <th style="min-width: 14%; width: 14%; text-align: center; vertical-align: middle">
                <fmt:message
                        key="day.sunday"/></th>
        </tr>
        </thead>
        <tbody>
        <tr class="is-centered">
            <c:forEach items="${allDays}" var="day">
                <c:choose>
                    <c:when test="${docWD != null && docWD.contains(day)}">
                        <td style="min-width: 14%; width: 14%; text-align: center; vertical-align: middle"><input
                                name="${day}" value="1" checked type="checkbox"></td>
                    </c:when>
                    <c:otherwise>
                        <td style="min-width: 14%; width: 14%; text-align: center; vertical-align: middle"><input
                                name="${day}" value="1" type="checkbox"></td>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </tr>
        </tbody>
    </table>
</div>