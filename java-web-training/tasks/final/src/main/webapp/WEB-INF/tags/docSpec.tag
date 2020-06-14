<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:directive.attribute name="fieldName" required="true" type="java.lang.String" description="Field name"/>
<jsp:directive.attribute name="specs" required="true"
                         type="by.training.hospital.entity.Specialization[]"
                         description="Specializations"/>

<jsp:directive.attribute name="chosenDocSpec" required="false"
                         type="by.training.hospital.entity.Specialization"
                         description="Specializations of chosen doctor"/>

<div class="container" style="margin: 15px">
    <label for="specSelect" class="label"><fmt:message key="page.spec"/></label>
    <div class="select">
        <select id="specSelect" name="${fieldName}" required="required">
            <c:forEach items="${specs}" var="spec">
                <c:choose>
                    <c:when test="${spec == chosenDocSpec}">
                        <option value="${spec}" selected>${spec}</option>
                    </c:when>
                    <c:otherwise>
                        <option value="${spec}">${spec}</option>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </select>
    </div>
</div>