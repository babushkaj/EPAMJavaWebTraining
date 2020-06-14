<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:directive.attribute name="fieldName" required="true" type="java.lang.String" description="Input field name"/>
<jsp:directive.attribute name="passLabelName" required="true" type="java.lang.String" description="Label name"/>
<jsp:directive.attribute name="fieldPlaceholder" required="true" type="java.lang.String"
                         description="Placeholder name"/>

<label class="label"><fmt:message key="${passLabelName}"/></label>
<div class="control">
    <input class="input" name="${fieldName}" type="password"
           placeholder="<fmt:message key="${fieldPlaceholder}"/>"
           pattern=".{8,25}"
           title="<fmt:message key="title.password"/>"
           required="required">
</div>

