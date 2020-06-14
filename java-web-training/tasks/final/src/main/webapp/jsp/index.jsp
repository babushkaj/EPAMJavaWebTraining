<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<script type="text/javascript">
    function changeLang(name) {
        document.cookie = "lang=" + name;
        window.location.reload(true);
    }
</script>

<fmt:setLocale value="${requestScope.get('lang')}"/>
<fmt:setBundle basename="i18n/hospital" scope="application"/>

<html>
<head>
    <title><fmt:message key="page.title"/></title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bulma.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bulma-calendar.min.css"/>
    <script src='${pageContext.request.contextPath}/js/bulma-calendar.min.js'></script>
    <meta charset="utf-8">
</head>
<body>

<section class="hero is-light">
    <div class="hero-body">
        <div class="container">
            <h1 class="title">
                <nav class="level">
                    <!-- Left side -->
                    <div class="level-left">
                        <div class="level-item">
                            <a style="color: #0a0a0a"
                               href="${pageContext.request.contextPath}/"><fmt:message key="page.welcome"/>
                            </a>
                        </div>
                    </div>

                    <!-- Right side -->
                    <div class="level-right">
                        <p class="level-item"><a class="button" href="#" onclick="changeLang('en')"><fmt:message
                                key="page.lang.en"/></a></p>
                        <p class="level-item"><a class="button" href="#" onclick="changeLang('ru')"><fmt:message
                                key="page.lang.ru"/></a></p>

                    </div>
                </nav>
            </h1>
        </div>
    </div>
</section>

<section class="is-fullheight is-medium" style="min-height: 512px">
    <div class="columns">
        <div class="column is-one-fifth container">
            <jsp:include page="commands.jsp"/>
        </div>
        <div class="column is-four-fifths">
            <c:choose>
                <c:when test="${not empty requestScope.includedPage}">
                    <div class="container">
                        <jsp:include page="${requestScope.includedPage}.jsp"/>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="container">
                        <jsp:include page="welcome.jsp"/>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</section>

<footer class="footer has-background-light">
    <div class="content has-text-centered">
        <p>
            <span>2019</span>
        </p>
    </div>
</footer>

</body>

</html>

