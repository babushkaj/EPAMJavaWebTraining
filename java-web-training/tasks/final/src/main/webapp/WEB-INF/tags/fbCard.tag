<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:directive.attribute name="feedback" required="true" type="by.training.hospital.dto.VisitFeedbackDTO"
                         description="FeedbackDTO"/>

<div style="display: inline-block; width: 45%; margin: 1px; min-height: 210px; vertical-align: top">
    <div class="card">
        <div class="card-content">
            <div class="content">
                <span> <fmt:message
                        key="page.visitor"/>: ${feedback.visitorFirstName} ${feedback.visitorLastName} </span><br><br>
                <span><fmt:message key="page.visitor_feedback"/>:</span><br>
                <span>${feedback.visitorMess}</span><br><br>
                <c:if test="${feedback.doctorMess != null}">
                    <span><fmt:message key="page.doctor_feedback"/>:</span><br>
                    <span>${feedback.doctorMess}</span>
                </c:if>
            </div>
        </div>
    </div>
</div>