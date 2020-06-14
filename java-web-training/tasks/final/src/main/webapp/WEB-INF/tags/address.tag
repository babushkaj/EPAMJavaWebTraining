<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:directive.attribute name="address" required="true" type="by.training.hospital.dto.AddressDTO"
                         description="User address"/>

<div class="container">
    <div class="container">
        <h6 class="title is-4">
            <span><fmt:message key="page.address"/></span>
        </h6>
    </div>
    <c:if test="${address.region != null}">
        <span><fmt:message key="page.region"/>: ${address.region}</span><br/>
    </c:if>
    <c:if test="${address.city != null}">
        <span><fmt:message key="page.city"/>: ${address.city}</span><br/>
    </c:if>
    <c:if test="${address.street != null}">
        <span><fmt:message key="page.street"/>: ${address.street} </span>
    </c:if>
    <c:if test="${address.house != null}">
        <span>${address.house}</span>
    </c:if>
    <c:if test="${!address.apartment.isEmpty()}">
        <span>/ ${address.apartment}</span>
    </c:if>
</div>
