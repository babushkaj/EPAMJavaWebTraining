<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ page import="by.training.hospital.command.ApplicationCommandConstants" %>
<%@ page import="by.training.hospital.entity.Role" %>
<%@ page import="by.training.hospital.entity.VisitStatus" %>


<div class="columns is-centered" style="margin: 20px">
    <div class="column is-two-thirds">
        <div class="card">
            <div class="card-content">
                <div class="content">
                    <div class="buttons is-centered">
                        <c:choose>
                            <c:when test="${sessionScope.currentUserRole == Role.DOCTOR}">
                                <button class="button is-link is-light"
                                        onclick='location.href="${pageContext.request.contextPath}/?command=${ApplicationCommandConstants.SHOW_DOCTOR_VISITS_CMD}&choseVisits=${sessionScope.lastChosenVisitsGroup}"'>
                                    <fmt:message key="page.back"/>
                                </button>
                            </c:when>
                            <c:when test="${sessionScope.currentUserRole == Role.VISITOR}">
                                <button class="button is-link is-light"
                                        onclick='location.href=
                                                "${pageContext.request.contextPath}/?command=${ApplicationCommandConstants.SHOW_VISITOR_VISITS_CMD}&choseVisits=${sessionScope.lastChosenVisitsGroup}"'>
                                    <fmt:message key="page.back"/>
                                </button>
                            </c:when>
                        </c:choose>
                    </div>
                    <div class="columns">
                        <div class="column is-two-thirds">
                            <span> <fmt:message key="page.visitor"/>:
                                ${requestScope.visitor.userInfo.firstName}
                                ${requestScope.visitor.userInfo.lastName}
                            </span><br>

                            <span> <fmt:message key="page.doctor"/>:
                                ${requestScope.doctor.userInfo.firstName}
                                ${requestScope.doctor.userInfo.lastName}
                            </span>
                        </div>
                        <div class="column is-one-third">
                            <span><fmt:message key="page.date"/>: <fmt:formatDate value="${requestScope.visit.date}"
                                                                                  pattern="dd-MM-yyyy"
                                                                                  type="date"/></span><br>
                            <span><fmt:message key="page.time"/>: <fmt:formatDate value="${requestScope.visit.date}"
                                                                                  pattern="HH-mm"
                                                                                  type="time"/></span>
                        </div>
                    </div>
                    <span><fmt:message key="page.reason"/>:</span><br>
                    <span>${requestScope.visit.cause}</span><br><br>

                    <c:if test="${requestScope.visit.result != null}">
                        <span><fmt:message key="page.visit_result"/>:</span><br>
                        <span>${requestScope.visit.result}</span><br>
                    </c:if>

                    <c:if test="${requestScope.visit.visitStatus == VisitStatus.CANCELED}">
                        <span class="has-text-danger"><fmt:message key="page.canceled_visit"/></span>
                    </c:if>

                    <c:if test="${requestScope.visit.visitStatus == VisitStatus.PLANNED}">
                        <c:choose>
                            <c:when test="${sessionScope.currentUserRole == Role.VISITOR}">
                                <form action="${pageContext.request.contextPath}/" method="POST">

                                    <input type="hidden" name="command"
                                           value="${ApplicationCommandConstants.CANCEL_VISIT_CMD}"/>
                                    <input type="hidden" name="visitId" value="${requestScope.visit.id}"/>
                                    <input style="display: none" id="cancelVisit" type="submit">
                                </form>
                                <c:if test="${requestScope.cancelErr != null}">
                                    <p class="help is-danger" style="min-height: 10px"><fmt:message
                                            key="${requestScope.cancelErr}"/></p>
                                </c:if>
                                <div style="text-align: center">
                                    <button class="button is-danger is-outlined"
                                            onclick="document.getElementById('cancelVisit').click()">
                                        <fmt:message key="page.cancel"/></button>
                                </div>
                            </c:when>
                            <c:when test="${sessionScope.currentUserRole == Role.DOCTOR}">
                                <form action="${pageContext.request.contextPath}/" method="POST">
                            <textarea name="visitResult" class="textarea has-fixed-size" placeholder="" rows="6"
                                      maxlength="600" required="required"></textarea>
                                    <input type="hidden" name="command"
                                           value="${ApplicationCommandConstants.FINISH_VISIT_CMD}"/>
                                    <input type="hidden" name="visitId" value="${requestScope.visit.id}"/>
                                    <input style="display: none" id="submitResult" type="submit">
                                </form>
                                <c:if test="${requestScope.finishErr != null}">
                                    <p class="help is-danger" style="min-height: 10px"><fmt:message
                                            key="${requestScope.finishErr}"/></p>
                                </c:if>

                                <div style="text-align: center">
                                    <button class="button is-link"
                                            onclick="document.getElementById('submitResult').click()">
                                        <fmt:message key="page.finish_btn"/></button>
                                </div>
                            </c:when>
                        </c:choose>
                    </c:if>
                </div>
            </div>
        </div>
        <br>

        <c:if test="${requestScope.visit.visitStatus == VisitStatus.COMPLETED}">
            <div class="card">
                <div class="card-content">
                    <div class="content">

                        <c:choose>
                            <c:when test="${sessionScope.currentUserRole == Role.VISITOR}">
                                <c:choose>
                                    <c:when test="${requestScope.visit.feedback == null}">

                                        <form action="${pageContext.request.contextPath}/" method="POST">
                                    <textarea name="visitorFeedback" class="textarea has-fixed-size" placeholder=""
                                              rows="3" maxlength="300" required="required"></textarea>
                                            <input style="display: none" id="submitVisFeedback" type="submit">
                                            <input type="hidden" name="command"
                                                   value="${ApplicationCommandConstants.LEAVE_VISITOR_FEEDBACK_CMD}"/>
                                            <input type="hidden" name="visitId" value="${requestScope.visit.id}"/>
                                        </form>
                                        <div style="text-align: center">
                                            <button class="button is-link"
                                                    onclick="document.getElementById('submitVisFeedback').click()">
                                                <fmt:message key="page.submit"/></button>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <span><fmt:message key="page.visitor_feedback"/>:</span><br>
                                        <span>${requestScope.visit.feedback.visitorMess}</span><br>
                                        <c:if test="${requestScope.visit.feedback.doctorMess != null}">
                                            <br><span><fmt:message key="page.doctor_feedback"/>:</span><br>
                                            <span>${requestScope.visit.feedback.doctorMess}</span>
                                        </c:if>
                                    </c:otherwise>
                                </c:choose>
                            </c:when>

                            <c:when test="${sessionScope.currentUserRole == Role.DOCTOR}">
                                <c:if test="${requestScope.visit.feedback == null}">
                                    <span><fmt:message key="page.no_feedback"/></span>
                                </c:if>
                                <c:choose>
                                    <c:when test="${requestScope.visit.feedback != null && requestScope.visit.feedback.doctorMess == null}">
                                        <span><fmt:message key="page.visitor_feedback"/>:</span><br>
                                        <span>${requestScope.visit.feedback.visitorMess}</span><br>
                                        <form action="${pageContext.request.contextPath}/" method="POST">
                                    <textarea name="doctorFeedback" class="textarea has-fixed-size" placeholder=""
                                              rows="3" maxlength="300" required="required"></textarea>
                                            <input type="hidden" name="command"
                                                   value="${ApplicationCommandConstants.LEAVE_DOCTOR_FEEDBACK_CMD}"/>
                                            <input type="hidden" name="visitId" value="${requestScope.visit.id}"/>
                                            <input style="display: none" id="submitDocFeedback" type="submit">
                                        </form>

                                        <div style="text-align: center">
                                            <button class="button is-link"
                                                    onclick="document.getElementById('submitDocFeedback').click()">
                                                <fmt:message key="page.submit"/></button>
                                        </div>
                                    </c:when>
                                    <c:when test="${requestScope.visit.feedback != null && requestScope.visit.feedback.doctorMess != null}">
                                        <span><fmt:message key="page.visitor_feedback"/>:</span><br>
                                        <span>${requestScope.visit.feedback.visitorMess}</span><br><br>
                                        <span><fmt:message key="page.doctor_feedback"/>:</span><br>
                                        <span>${requestScope.visit.feedback.doctorMess}</span>
                                    </c:when>
                                </c:choose>
                            </c:when>
                        </c:choose>
                    </div>
                </div>
            </div>
        </c:if>
    </div>
</div>