<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="part" tagdir="/WEB-INF/tags" %>

<%@ page import="by.training.hospital.command.ApplicationCommandConstants" %>

<div class="container" style="margin: 15px">

    <div class="container has-text-centered">
        <form action="${pageContext.request.contextPath}/?command=${ApplicationCommandConstants.SHOW_USERS_CMD}"
              method="POST" style="vertical-align: center">
            <table class="table is-striped is-fullwidth is-narrow">
                <thead>
                <tr>
                    <th colspan="5" style="text-align: center; vertical-align: middle">
                        <fmt:message key="page.users_question"/>
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td style="min-width: 25%; width: 25%; text-align: center; vertical-align: middle">
                        <input type="radio" id="all" name="chosenUsers" value="all">
                        <label for="all"><fmt:message key="page.visits_on_page_all"/> </label>
                    </td>
                    <td style="min-width: 25%; width: 25%; text-align: center; vertical-align: middle">
                        <input type="radio" id="visitors" name="chosenUsers" value="visitors">
                        <label for="visitors"><fmt:message key="page.users_on_page_visitors"/></label>
                    </td>
                    <td style="min-width: 25%; width: 25%; text-align: center; vertical-align: middle">
                        <input type="radio" id="doctors" name="chosenUsers" value="doctors">
                        <label for="doctors"><fmt:message key="page.users_on_page_doctors"/></label>
                    </td>
                    <td style="min-width: 25%; width: 25%; text-align: center; vertical-align: middle">
                        <input class="button is-link" type="submit" value=<fmt:message key="page.show"/>>
                    </td>
                </tr>
                </tbody>
            </table>
        </form>
    </div>

    <table class="table is-striped is-fullwidth is-narrow">
        <thead>
        <tr style="text-align: center">
            <th style="min-width: 15%; width: 15%"><fmt:message key="page.login"/></th>
            <th style="min-width: 15%; width: 15%"><fmt:message key="page.firstname"/></th>
            <th style="min-width: 15%; width: 15%"><fmt:message key="page.lastname"/></th>
            <th style="min-width: 15%; width: 15%; text-align: center"><fmt:message key="page.role"/></th>
            <th style="min-width: 13%; width: 13%; text-align: center"><fmt:message key="page.info"/></th>
            <th style="min-width: 13%; width: 13%; text-align: center"><fmt:message key="page.edit"/></th>
            <th style="min-width: 13%; width: 13%; text-align: center"><fmt:message key="page.block"/></th>
        </tr>
        </thead>

        <tbody>
        <c:forEach items="${requestScope.users}" var="user">

            <tr>
                <td style="vertical-align: middle">${user.login}</td>
                <td style="vertical-align: middle">${user.userInfo.firstName}</td>
                <td style="vertical-align: middle">${user.userInfo.lastName}</td>
                <td style="text-align: center; vertical-align: middle">${user.role}</td>
                <td style="vertical-align: middle">
                    <input class="button is-small is-fullwidth is-info is-light is-centered"
                           onclick="location.href='${pageContext.request.contextPath}/?command=${ApplicationCommandConstants.SHOW_PROFILE_CMD}&userId=${user.id}'"
                           value="<fmt:message key="page.info"/>"/>

                </td>
                <td style="vertical-align: middle">
                    <input class="button is-small is-fullwidth is-warning is-light is-centered"
                           onclick="location.href='${pageContext.request.contextPath}/?command=${ApplicationCommandConstants.SHOW_EDIT_USER_CMD}&userId=${user.id}'"
                           value="<fmt:message key="page.edit"/>"/>
                </td>
                <td style="vertical-align: middle">
                    <c:choose>
                        <c:when test="${user.blocked == true}">
                            <form action="${pageContext.request.contextPath}/" method="POST">
                                <input type="hidden" name="userId" value="${user.id}"/>
                                <input type="hidden" name="command"
                                       value="${ApplicationCommandConstants.BLOCK_USER_CMD}"/>
                                <input class="button is-small is-fullwidth is-danger is-light is-centered" type="submit"
                                       value="<fmt:message key="page.unblock"/>"/>
                            </form>
                        </c:when>
                        <c:otherwise>
                            <form action="${pageContext.request.contextPath}/" method="POST">
                                <input type="hidden" name="userId" value="${user.id}"/>
                                <input type="hidden" name="command"
                                       value="${ApplicationCommandConstants.BLOCK_USER_CMD}"/>
                                <input class="button is-small is-fullwidth is-danger is-light is-centered" type="submit"
                                       value="<fmt:message key="page.block"/>"/>
                            </form>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>

        </c:forEach>
        </tbody>
    </table>
    <div style="margin: 10px">
        <part:pgn currentPage="${requestScope.currentPage}"
                  maxPage="${requestScope.maxPage}"
                  pageCommand="${ApplicationCommandConstants.SHOW_USERS_CMD}"
                  checked="${requestScope.checkedAttr}"/>
    </div>
</div>

<script>
    document.getElementById('${sessionScope.lastChosenUsersGroup}').checked = true;
</script>