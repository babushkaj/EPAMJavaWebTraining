<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="part" tagdir="/WEB-INF/tags" %>

<%@ page import="by.training.hospital.command.ApplicationCommandConstants" %>
<%@ page import="by.training.hospital.entity.Role" %>

<div class="container" style="margin: 10px">
    <c:choose>
        <c:when test="${requestScope.visits.size() == 0}">
            <div style="text-align: center; vertical-align: center; padding: 200px">
                <span><fmt:message key="page.no_visits"/></span><br><br>
                <div class="buttons is-centered">
                    <c:choose>
                        <c:when test="${sessionScope.currentUserRole == Role.DOCTOR}">
                            <button class="button is-link is-light"
                                    onclick='location.href="${pageContext.request.contextPath}/?command=${ApplicationCommandConstants.SHOW_DOCTOR_VISITS_CMD}"'>
                                <fmt:message key="page.back"/>
                            </button>
                        </c:when>
                        <c:when test="${sessionScope.currentUserRole == Role.VISITOR}">
                            <button class="button is-link is-light"
                                    onclick='location.href="${pageContext.request.contextPath}/?command=${ApplicationCommandConstants.SHOW_VISITOR_VISITS_CMD}"'>
                                <fmt:message key="page.back"/>
                            </button>
                        </c:when>
                    </c:choose>
                </div>
            </div>
        </c:when>
        <c:otherwise>
            <c:if test="${requestScope.showOnlySearchResult == null}">
                <c:choose>
                    <c:when test="${sessionScope.currentUserRole == Role.VISITOR}">
                        <div class="container has-text-centered">
                            <form action="${pageContext.request.contextPath}/?command=${ApplicationCommandConstants.SHOW_VISITOR_VISITS_CMD}"
                                  method="POST" style="vertical-align: center">
                                <table class="table is-striped is-fullwidth is-narrow">
                                    <thead>
                                    <tr>
                                        <th colspan="5" style="text-align: center; vertical-align: middle">
                                            <fmt:message key="page.visits_question"/>
                                        </th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr>
                                        <td style="min-width: 20%; width: 20%; text-align: center; vertical-align: middle">
                                            <input type="radio" id="all" name="choseVisits" value="all">
                                            <label for="all"><fmt:message key="page.visits_on_page_all"/> </label>
                                        </td>
                                        <td style="min-width: 20%; width: 20%; text-align: center; vertical-align: middle">
                                            <input type="radio" id="planned" name="choseVisits" value="planned">
                                            <label for="planned"><fmt:message
                                                    key="page.visits_on_page_planned"/></label>
                                        </td>
                                        <td style="min-width: 20%; width: 20%; text-align: center; vertical-align: middle">
                                            <input type="radio" id="completed" name="choseVisits" value="completed">
                                            <label for="completed"><fmt:message
                                                    key="page.visits_on_page_completed"/></label>
                                        </td>
                                        <td style="min-width: 20%; width: 20%; text-align: center; vertical-align: middle">
                                            <input type="radio" id="canceled" name="choseVisits" value="canceled">
                                            <label for="canceled"><fmt:message
                                                    key="page.visits_on_page_canceled"/></label>
                                        </td>
                                        <td style="min-width: 20%; width: 20%; text-align: center; vertical-align: middle">
                                            <input class="button is-link" type="submit" value=<fmt:message
                                                    key="page.show"/>>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </form>
                        </div>
                    </c:when>

                    <c:when test="${sessionScope.currentUserRole == Role.DOCTOR}">
                        <div class="container has-text-centered">
                            <form action="${pageContext.request.contextPath}/?command=${ApplicationCommandConstants.SHOW_DOCTOR_VISITS_CMD}"
                                  method="POST" style="vertical-align: center">
                                <table class="table is-striped is-fullwidth is-narrow">
                                    <thead>
                                    <tr>
                                        <th colspan="5" style="text-align: center; vertical-align: middle">
                                            <fmt:message key="page.visits_question"/>
                                        </th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr>
                                        <td style="min-width: 33%; width: 33%; text-align: center; vertical-align: middle">
                                            <input type="radio" id="allDoc" name="choseVisits" value="allDoc">
                                            <label for="allDoc"><fmt:message key="page.visits_on_page_all"/></label>
                                        </td>
                                        <td style="min-width: 33%; width: 33%; text-align: center; vertical-align: middle">
                                            <input type="radio" id="today" name="choseVisits" value="today">
                                            <label for="today"><fmt:message key="page.visits_on_page_today"/></label>
                                        </td>
                                        <td style="min-width: 33%; width: 33%; text-align: center; vertical-align: middle">
                                            <input class="button is-link" type="submit" value=<fmt:message
                                                    key="page.show"/>>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </form>
                        </div>
                    </c:when>
                </c:choose>
            </c:if>
            <table class="table is-striped is-fullwidth is-narrow">
                <thead>
                <tr>
                    <th rowspan="2" style="min-width: 10%; width: 10%; text-align: center; vertical-align: middle">
                        <fmt:message key="page.date"/></th>
                    <th rowspan="2" style="min-width: 8%; width: 8%; text-align: center; vertical-align: middle">
                        <fmt:message key="page.time"/></th>
                    <th colspan="2" style="min-width: 12%; width: 12%; text-align: center">
                        <c:choose>
                            <c:when test="${sessionScope.currentUserRole == Role.DOCTOR}">
                                <fmt:message key="page.visitor"/>
                            </c:when>
                            <c:otherwise>
                                <fmt:message key="page.doctor"/>
                            </c:otherwise>
                        </c:choose>
                    </th>
                    <th rowspan="2" style="min-width: 40%; width: 40%; text-align: center; vertical-align: middle">
                        <fmt:message key="page.reason"/></th>
                    <th rowspan="2" style="min-width: 8%; width: 8%; text-align: center; vertical-align: middle">
                        <fmt:message key="page.status"/></th>
                    <th rowspan="2" style="min-width: 10%; width: 10%; text-align: center; vertical-align: middle">
                        <fmt:message key="page.info"/></th>
                </tr>
                <tr>
                    <th style="min-width: 12%; width: 12%; text-align: center"><fmt:message key="page.firstname"/></th>
                    <th style="min-width: 12%; width: 12%; text-align: center"><fmt:message key="page.lastname"/></th>
                </tr>
                </thead>

                <tbody>
                <c:forEach items="${requestScope.visits}" var="visit">

                    <tr>
                        <td style="text-align: center; vertical-align: middle">
                            <fmt:formatDate value="${visit.date}" pattern="dd-MM-yyyy" type="date"/>
                        </td>
                        <td style="text-align: center; vertical-align: middle">
                            <fmt:formatDate value="${visit.date}" pattern="HH-mm" type="time"/>
                        </td>
                        <c:choose>
                            <c:when test="${sessionScope.currentUserRole == Role.DOCTOR}">
                                <td style="vertical-align: middle">${visit.visitorFirstName}</td>
                                <td style="vertical-align: middle">${visit.visitorLastName}</td>
                            </c:when>
                            <c:otherwise>
                                <td style="vertical-align: middle">${visit.doctorFirstName}</td>
                                <td style="vertical-align: middle">${visit.doctorLastName}</td>
                            </c:otherwise>
                        </c:choose>
                        <td style="vertical-align: middle">${visit.cause}</td>
                        <td style="vertical-align: middle"><part:stColor status="${visit.visitStatus}"/></td>
                        <td style="vertical-align: middle">
                            <input class="button is-small is-fullwidth is-info is-light is-centered"
                                   onclick="location.href='${pageContext.request.contextPath}/?command=${ApplicationCommandConstants.SHOW_VISIT_INFO_CMD}&visitId=${visit.id}'"
                                   value="<fmt:message key="page.info"/>"/>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
</div>

<script>
    document.getElementById('${requestScope.lastChosenVisitsGroup}').checked = true;
</script>