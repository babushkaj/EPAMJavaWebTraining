<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ tag import="by.training.hospital.entity.Specialization" %>

<jsp:directive.attribute name="docSpec" required="true" type="by.training.hospital.entity.Specialization" description="Specialization"/>

<c:choose>
    <c:when test="${docSpec == Specialization.THERAPIST}">
        <span><fmt:message key="spec.therapist"/> </span>
    </c:when>
    <c:when test="${docSpec == Specialization.OCULIST}">
        <span><fmt:message key="spec.oculist"/> </span>
    </c:when>
    <c:when test="${docSpec == Specialization.OTOLARYNGOLOGIST}">
        <span><fmt:message key="spec.otolaryngologist"/> </span>
    </c:when>
    <c:when test="${docSpec == Specialization.SURGEON}">
        <span><fmt:message key="spec.surgeon"/> </span>
    </c:when>
    <c:when test="${docSpec == Specialization.TRAUMATOLOGIST}">
        <span><fmt:message key="spec.traumatologist"/> </span>
    </c:when>
</c:choose>
