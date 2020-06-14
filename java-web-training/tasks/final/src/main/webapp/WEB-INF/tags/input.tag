<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:directive.attribute name="inputLabel" required="true" type="java.lang.String" description="Label"/>
<jsp:directive.attribute name="inputName" required="true" type="java.lang.String" description="Name"/>
<jsp:directive.attribute name="inputPh" required="true" type="java.lang.String" description="Placeholder"/>
<jsp:directive.attribute name="inputTitle" required="false" type="java.lang.String" description="Title"/>
<jsp:directive.attribute name="inputPattern" required="false" type="java.lang.String" description="Pattern"/>
<jsp:directive.attribute name="isRequired" required="true" type="java.lang.Boolean" description="Is field required"/>
<jsp:directive.attribute name="inputValue" required="true" type="java.lang.String" description="Value"/>
<jsp:directive.attribute name="inputErr" required="false" type="java.lang.String" description="Error message"/>

<label class="label"><fmt:message key="${inputLabel}"/></label>
<div class="control">
    <input class="input" type="text" name="${inputName}" placeholder="<fmt:message key="${inputPh}"/>"
    <c:if test="${isRequired == 'true'}">
           required="required"
    </c:if>
    <c:if test="${inputPattern != null}">
           pattern="${inputPattern}"
    </c:if>
    <c:if test="${inputTitle != null}">
           title="<fmt:message key="${inputTitle}"/>"
    </c:if>
           value=${inputValue}>
</div>
<div style="min-height: 18px">
    <c:if test="${inputErr != null && !inputErr.isEmpty()}">
        <p class="help is-danger"><fmt:message key="${inputErr}"/></p>
    </c:if>
</div>
