<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:directive.attribute name="command" required="true" type="java.lang.String" description="Command name"/>
<jsp:directive.attribute name="buttonName" required="true" type="java.lang.String" description="Button name"/>

<li>
    <a class="button"
       href="${pageContext.request.contextPath}/?command=${command}">
        <fmt:message key="${buttonName}"/>
    </a>
</li>