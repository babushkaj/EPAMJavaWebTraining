<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ tag import="by.training.hospital.entity.VisitStatus" %>

<jsp:directive.attribute name="status" required="true" type="by.training.hospital.entity.VisitStatus" description="Visit status"/>

<c:choose>
    <c:when test="${status == VisitStatus.PLANNED}">
        <span class="has-text-info">${status}</span>
    </c:when>
    <c:when test="${status == VisitStatus.CANCELED}">
        <span class="has-text-danger">${status}</span>
    </c:when>
    <c:when test="${status == VisitStatus.COMPLETED}">
        <span class="has-text-success">${status}</span>
    </c:when>
</c:choose>