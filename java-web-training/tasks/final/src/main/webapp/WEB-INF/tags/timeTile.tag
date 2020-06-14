<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ tag import="by.training.hospital.command.ApplicationCommandConstants" %>

<jsp:directive.attribute name="visits" required="true" type="java.util.Map<java.lang.String, java.lang.Long>" description="Map of visits"/>
<jsp:directive.attribute name="visitTime" required="true" type="java.lang.String" description="Time of visit"/>

<div class="tile is-3 is-child box has-text-centered" onclick="document.getElementById('${visitTime}').click()" style="cursor: pointer; vertical-align: middle">
    <c:choose>
        <c:when test="${visits.containsKey(visitTime)}">
            <span class="label is-3">${visitTime}</span>
            <form action="${pageContext.request.contextPath}/?command=${ApplicationCommandConstants.WRITE_REASON_CMD}" method="post">
                <input type="hidden" name="visitTime" value="${visits[visitTime]}">
                <input type="hidden" name="doctorUserId" value="${requestScope.doctorUserId}">
                <input style="display: none" id="${visitTime}" type="submit">
            </form>
        </c:when>
        <c:otherwise>
            <span class="label has-text-grey is-6">NO</span>
        </c:otherwise>
    </c:choose>
</div>