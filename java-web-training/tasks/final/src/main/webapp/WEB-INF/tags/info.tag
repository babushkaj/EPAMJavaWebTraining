<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:directive.attribute name="userInfo" required="true" type="by.training.hospital.dto.UserInfoDTO" description="User's info"/>

<div class="container">
    <div class="container">
        <h6 class="title is-4">
            <span><fmt:message key="page.info"/></span>
        </h6>
    </div>
    <c:if test="${userInfo.firstName != null}">
        <span><fmt:message key="page.firstname"/>: ${userInfo.firstName}</span><br/>
    </c:if>
    <c:if test="${userInfo.lastName != null}">
        <span><fmt:message key="page.lastname"/>: ${userInfo.lastName}</span><br/>
    </c:if>
    <c:if test="${userInfo.email != null}">
        <span>Email: ${userInfo.email}</span><br/>
    </c:if>
    <c:if test="${userInfo.phone != null}">
        <span><fmt:message key="page.phone"/>: ${userInfo.phone}</span><br/>
    </c:if>
</div>

