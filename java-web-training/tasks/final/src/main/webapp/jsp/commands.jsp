<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="part" tagdir="/WEB-INF/tags" %>

<%@ page import="by.training.hospital.command.ApplicationCommandConstants" %>
<%@ page import="by.training.hospital.entity.Role" %>

<aside class="menu">
    <ul class="menu-list">
        <h1 class="title">
            <c:choose>
                <c:when test="${sessionScope.currentUserRole != null}">
                    <c:if test="${sessionScope.currentUserRole == Role.ADMIN}">
                        <part:cmdBtn command="${ApplicationCommandConstants.SHOW_USERS_CMD}" buttonName="page.all_users"/>
                        <part:cmdBtn command="${ApplicationCommandConstants.SHOW_EDIT_USER_CMD}" buttonName="page.add_doctor"/>
                        <part:cmdBtn command="${ApplicationCommandConstants.CHANGE_PASSWORD_CMD}" buttonName="page.change_password"/>
                    </c:if>
                    <c:if test="${sessionScope.currentUserRole == Role.VISITOR}">
                        <part:cmdBtn command="${ApplicationCommandConstants.SHOW_PROFILE_CMD}" buttonName="page.show_profile"/>
                        <part:cmdBtn command="${ApplicationCommandConstants.GET_DOCTORS_CMD}" buttonName="page.doctors"/>
                        <part:cmdBtn command="${ApplicationCommandConstants.FIND_DOCTORS_CMD}" buttonName="page.find_doctor"/>
                        <part:cmdBtn command="${ApplicationCommandConstants.SHOW_VISITOR_VISITS_CMD}" buttonName="page.show_user_visits"/>
                    </c:if>
                    <c:if test="${sessionScope.currentUserRole== Role.DOCTOR}">
                        <part:cmdBtn command="${ApplicationCommandConstants.SHOW_PROFILE_CMD}" buttonName="page.show_profile"/>
                        <part:cmdBtn command="${ApplicationCommandConstants.SHOW_DOCTOR_VISITS_CMD}" buttonName="page.show_user_visits"/>
                        <part:cmdBtn command="${ApplicationCommandConstants.FIND_VISITS_CMD}" buttonName="page.find_visits"/>
                    </c:if>
                    <part:cmdBtn command="${ApplicationCommandConstants.LOGOUT_CMD}" buttonName="page.logout"/>
                </c:when>
                <c:otherwise>
                    <part:cmdBtn command="${ApplicationCommandConstants.GET_DOCTORS_CMD}" buttonName="page.doctors"/>
                    <part:cmdBtn command="${ApplicationCommandConstants.FIND_DOCTORS_CMD}" buttonName="page.find_doctor"/>
                    <part:cmdBtn command="${ApplicationCommandConstants.SHOW_EDIT_USER_CMD}" buttonName="page.registration"/>
                    <part:cmdBtn command="${ApplicationCommandConstants.LOGIN_CMD}" buttonName="page.login_btn"/>
                </c:otherwise>
            </c:choose>
        </h1>
    </ul>
</aside>
