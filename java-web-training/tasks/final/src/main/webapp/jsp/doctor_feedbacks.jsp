<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="part" tagdir="/WEB-INF/tags" %>

<div class="columns is-centered" style="margin: 10px">
    <div class="column">
        <div class="buttons is-centered">
            <button class="button is-primary is-outlined is-small" onclick="window.history.back()">
                <fmt:message key="page.back"/>
            </button>
        </div>
    </div>
</div>
<div class="container is-centered" style="margin: 10px 35px;">
    <c:choose>
        <c:when test="${requestScope.feedbacks.size() == 0}">
            <div class="column has-text-centered">
                <span><fmt:message key="page.no_doc_feedbacks"/></span>
            </div>
        </c:when>
        <c:otherwise>
            <c:forEach items="${requestScope.feedbacks}" var="fb">
                <part:fbCard feedback="${fb}"/>
            </c:forEach>
        </c:otherwise>
    </c:choose>
</div>

