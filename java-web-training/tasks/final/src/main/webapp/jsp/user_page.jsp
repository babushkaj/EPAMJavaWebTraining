<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="part" tagdir="/WEB-INF/tags" %>

<%@ page import="by.training.hospital.command.ApplicationCommandConstants" %>
<%@ page import="by.training.hospital.entity.Role" %>
<%@ page import="java.time.DayOfWeek" %>

<div style="margin: 15px" class="container">
    <div class="columns is-centered">
        <div class="column is-two-thirds">
            <div class="card">
                <div class="card-content">
                    <div class="content">
                        <div class="columns is-centered">
                            <div class="column is-four-fifths">
                                <div class="columns">
                                    <div class="column is-half">
                                        <%--SHOW USER INFO--%>
                                        <c:if test="${requestScope.user.userInfo != null}">
                                            <part:info userInfo="${requestScope.user.userInfo}"/>
                                        </c:if>
                                    </div>
                                    <div class="column is-half">
                                        <%--SHOW ADDRESS--%>
                                        <c:if test="${requestScope.user.address != null}">
                                            <part:address address="${requestScope.user.address}"/>
                                        </c:if>
                                    </div>
                                </div>
                                <c:if test="${requestScope.user.doctorInfo.spec != null}">
                                    <div class="column is-centered">
                                <span class="title is-6" style="margin: 20px">
                                    <fmt:message key="page.spec"/> : <part:specByName
                                        docSpec="${requestScope.user.doctorInfo.spec}"/>
                                </span><br>
                                    </div>
                                </c:if>
                                <c:if test="${requestScope.user.doctorInfo.workingDays != null}">
                                    <div class="column is-centered">
                                <span class="title is-6" style="margin: 20px">
                                     <fmt:message key="page.working_days"/>:
                                    <c:forEach items="${DayOfWeek.values()}" var="dayFromEnum">
                                        <c:forEach items="${requestScope.user.doctorInfo.workingDays}" var="oneDay">
                                            <c:if test="${dayFromEnum == oneDay}">
                                                <part:dayByName day="${oneDay}"/>
                                            </c:if>
                                        </c:forEach>
                                    </c:forEach>
                                </span>
                                    </div>
                                </c:if>
                            </div>
                        </div>
                        <div class="buttons is-centered">
                            <c:choose>
                                <c:when test="${sessionScope.currentUserRole == Role.ADMIN}">
                                    <button class="button is-link is-light"
                                            onclick="window.location.href='${pageContext.request.contextPath}/?command=${ApplicationCommandConstants.SHOW_USERS_CMD}&chosenUsers=${sessionScope.lastChosenUsersGroup}&pageNumber=${sessionScope.lastVisitedPageNumber}'">
                                        <fmt:message key="page.back"/>
                                    </button>
                                </c:when>
                                <c:otherwise>
                                    <button class="button is-primary"
                                            onclick="window.location.href='${pageContext.request.contextPath}/?command=${ApplicationCommandConstants.CHANGE_PASSWORD_CMD}'">
                                        <fmt:message key="page.change_password"/>
                                    </button>
                                    <button class="button is-link"
                                            onclick="window.location.href='${pageContext.request.contextPath}/?command=${ApplicationCommandConstants.SHOW_EDIT_USER_CMD}'">
                                        <fmt:message key="page.edit"/>
                                    </button>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>