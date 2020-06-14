<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ taglib prefix="part" tagdir="/WEB-INF/tags" %>

<%@ page import="by.training.hospital.command.ApplicationCommandConstants" %>
<%@ page import="by.training.hospital.entity.Role" %>
<%@ page import="by.training.hospital.entity.Specialization" %>
<%@ page import="java.time.DayOfWeek" %>

<div class="container" style="margin: 20px">
    <form action="${pageContext.request.contextPath}/?command=${requestScope.action}" method="post">
        <div class="columns">
            <%--<input type="hidden" name="userId" value="${requestScope.userId}">--%>
            <div class="column is-half">
                <%--login input--%>
                <part:input inputLabel="page.login" inputName="login"
                            inputPh="page.login_ph" inputTitle="title.login_format"
                            inputPattern="^[A-Za-z0-9_-]{5,25}$" isRequired="true"
                            inputValue="${requestScope.userLogin}" inputErr="${requestScope.loginErr}"/>

                <%--firstname input--%>
                <part:input inputLabel="page.firstname" inputName="firstname"
                            inputPh="page.firstname" inputTitle="title.firstname_format"
                            inputPattern="^[A-Za-zА-Яа-я-]{1,25}$" isRequired="true"
                            inputValue="${requestScope.userFirstName}" inputErr="${requestScope.firstnameErr}"/>

                <%--lastname input--%>
                <part:input inputLabel="page.lastname" inputName="lastname"
                            inputPh="page.lastname" inputTitle="title.lastname_format"
                            inputPattern="^[A-Za-zА-Яа-я-]{1,25}$" isRequired="true"
                            inputValue="${requestScope.userLastName}" inputErr="${requestScope.lastnameErr}"/>

                <%--email input--%>
                <part:input inputLabel="page.email" inputName="email"
                            inputPh="page.email" inputTitle="title.email_format"
                            inputPattern="^[A-Za-z0-9._%+-]{5,18}@[A-Za-z]{4,8}\.[A-Za-z]{2,4}$" isRequired="true"
                            inputValue="${requestScope.userEmail}" inputErr="${requestScope.emailErr}"/>

                <%--phone input--%>
                <part:input inputLabel="page.phone" inputName="phone"
                            inputPh="page.phone" inputTitle="title.phone_format"
                            inputPattern="^((29)|(33)|(44)|(25))-(([1-9]{1})([0-9]{6}))$" isRequired="true"
                            inputValue="${requestScope.userPhone}" inputErr="${requestScope.phoneErr}"/>
            </div>

            <div class="column is-half">
                <%--region input--%>
                <part:input inputLabel="page.region" inputName="region"
                            inputPh="page.region" inputTitle="title.region_format"
                            inputPattern="^[А-Я]{1}[а-яё]{4,12}$" isRequired="true"
                            inputValue="${requestScope.userRegion}" inputErr="${requestScope.regionErr}"/>

                <%--city input--%>
                <part:input inputLabel="page.city" inputName="city"
                            inputPh="page.city" inputTitle="title.city_format"
                            inputPattern="^[А-Яа-яё\.-]{2,25}$" isRequired="true"
                            inputValue="${requestScope.userCity}" inputErr="${requestScope.cityErr}"/>

                <%--street input--%>
                <part:input inputLabel="page.street" inputName="street"
                            inputPh="page.street" inputTitle="title.street_format"
                            inputPattern="^[А-Яа-яё\.-]{2,25}$" isRequired="true"
                            inputValue="${requestScope.userStreet}" inputErr="${requestScope.streetErr}"/>

                <%--house input--%>
                <part:input inputLabel="page.house" inputName="house"
                            inputPh="page.house" inputTitle="title.house_format"
                            inputPattern="^([1-9]{1})([0-9А-Яа-яё\.\/-]){0,4}$" isRequired="true"
                            inputValue="${requestScope.userHouse}" inputErr="${requestScope.houseErr}"/>

                <%--flat input--%>
                <part:input inputLabel="page.apartment" inputName="apartment"
                            inputPh="page.apartment" inputTitle="title.apartment_format"
                            inputPattern="^([1-9]{1})([0-9А-Яа-яё\.\/-]){0,4}$" isRequired="false"
                            inputValue="${requestScope.userApartment}" inputErr="${requestScope.apartmentErr}"/>
            </div>
        </div>

        <div class="columns is-centered">
            <div class="column is-half">

                <%--section for admin or doctor only--%>
                <c:if test="${sessionScope.currentUserRole == Role.ADMIN or sessionScope.currentUserRole == Role.DOCTOR}">
                    <hr class="navbar-divider">

                    <%--HERE SHOULD BE FIELDS WITH SPECS, WORKING DAYS AND DOC_DESC--%>
                    <c:if test="${requestScope.userRole != Role.VISITOR}">
                        <part:docSpec fieldName="specSelect" specs="${Specialization.values()}"
                                      chosenDocSpec="${requestScope.docSpec}"/>
                        <c:if test="${requestScope.specErr != null}">
                            <p class="help is-danger" style="min-height: 10px"><fmt:message
                                    key="${requestScope.specErr}"/></p>
                        </c:if>
                        <%--working days checkboxes--%>
                        <part:workingDays allDays="${DayOfWeek.values()}"
                                          docWD="${requestScope.workingDays}"/>
                        <c:if test="${requestScope.daysErr != null}">
                            <p class="help is-danger" style="min-height: 10px"><fmt:message
                                    key="${requestScope.daysErr}"/></p>
                        </c:if>
                        <%--doctor description--%>
                        <div class="container" style="margin: 15px">
                            <label class="label"><fmt:message key="page.doc_info"/></label>
                            <textarea name="description" class="textarea has-fixed-size"
                                      placeholder="<fmt:message key="page.doc_desc_ph"/>"
                                      rows="7" maxlength="700">${requestScope.description}</textarea>
                        </div>
                    </c:if>
                </c:if>

                <c:if test="${requestScope.action == ApplicationCommandConstants.REGISTRATION_CMD}">
                    <div class="container" style="margin: 15px">
                            <%--pass1--%>
                        <part:pass fieldName="password1" passLabelName="page.password"
                                   fieldPlaceholder="page.password_enter_ph"/>
                            <%--pass2--%>
                        <part:pass fieldName="password2" passLabelName="page.password_repeat"
                                   fieldPlaceholder="page.password_repeat_ph"/>
                        <c:if test="${requestScope.passwordErr != null}">
                            <p class="help is-danger" style="min-height: 10px"><fmt:message
                                    key="${requestScope.passwordErr}"/></p>
                        </c:if>
                    </div>
                </c:if>
                <input style="display: none" id="submitButton" type="submit">
            </div>
        </div>
    </form>
    <div class="columns is-centered">
        <div class="column is-two-fifths">
            <div class="buttons is-centered">
                <button class="button is-link"
                        onclick="document.getElementById('submitButton').click()">
                    <fmt:message key="page.submit"/></button>
                <button class="button is-link is-light"
                        onclick="window.history.back()">
                    <fmt:message key="page.cancel"/></button>
            </div>
        </div>
    </div>
</div>
