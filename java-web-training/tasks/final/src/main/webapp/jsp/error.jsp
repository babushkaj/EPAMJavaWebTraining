<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="column has-text-centered is-centered">
    <div class="container" style="margin: 100px">
        <h2 class="title">
            <span><fmt:message key="${requestScope.message}"/> </span>
        </h2>
        <a class="button is-link" href="${pageContext.request.contextPath}/" style="cursor: pointer">
            <fmt:message key="page.main_page"/>
        </a>
    </div>
</div>
