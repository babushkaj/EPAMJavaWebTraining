<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="part" tagdir="/WEB-INF/tags" %>

<%@ page import="by.training.hospital.command.ApplicationCommandConstants" %>
<%@ page import="by.training.hospital.entity.Role" %>
<%@ page import="java.time.DayOfWeek" %>

<%--to show error occured during making an appointment--%>
<c:if test="${requestScope.error != null}">
    <p class="help is-danger" style="padding: 10px"><fmt:message key="${requestScope.error}"/></p>
</c:if>
<%--show is there is no found doctor--%>
<c:if test="${requestScope.doctors.isEmpty()}">
    Doctor not found.
</c:if>
<%--show doctors from list--%>
<c:forEach var='doc' items="${requestScope.doctors}">
    <div class="card" style="margin: 10px">
        <div class="card-content">
            <div class="media">
                <div class="media-content">
                    <div class="content" style="margin: 15px">
                        <p class="title is-4">
                            <span style="margin: 15px"> ${doc.userInfo.firstName} ${doc.userInfo.lastName}</span><br>
                            <span class="has-text-grey title is-6" style="margin: 20px">
                                    <fmt:message key="page.spec"/> : <part:specByName docSpec="${doc.doctorInfo.spec}"/>
                            </span><br>
                            <span class="title is-6" style="margin: 20px">
                                <fmt:message key="page.working_days"/>:
                                    <c:forEach items="${DayOfWeek.values()}" var="dayFromEnum">
                                        <c:forEach items="${doc.doctorInfo.workingDays}" var="oneDay">
                                            <c:if test="${dayFromEnum == oneDay}">
                                                <part:dayByName day="${oneDay}"/>
                                            </c:if>
                                        </c:forEach>
                                    </c:forEach>
                            </span>
                        </p>
                        <span>${doc.doctorInfo.description}</span>
                    </div>
                </div>
            </div>
            <footer class="card-footer">
                <c:if test="${sessionScope.currentUserRole == Role.VISITOR.toString()}">
                    <a href="${pageContext.request.contextPath}/?command=${ApplicationCommandConstants.CHOOSE_DATE_CMD}&doctorUserId=${doc.id}"
                       class="card-footer-item"><fmt:message key="page.appointment"/></a>
                </c:if>
                <a href="${pageContext.request.contextPath}/?command=${ApplicationCommandConstants.SHOW_DOCTOR_FEEDBACKS_CMD}&doctorUserId=${doc.id}"
                   class="card-footer-item"><fmt:message key="page.feedbacks"/> </a>
            </footer>
        </div>
    </div>
</c:forEach>
<%--to use this page to show doctors with pagination/ all doctors after search--%>
<c:if test="${requestScope.showPagination == null}">
    <div style="margin: 10px">
        <part:pgn currentPage="${requestScope.currentPage}"
                  maxPage="${requestScope.maxPage}"
                  pageCommand="${ApplicationCommandConstants.GET_DOCTORS_CMD}"/>
    </div>
</c:if>



