<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:directive.attribute name="currentPage" required="true" type="java.lang.Long" description="Current page number"/>
<jsp:directive.attribute name="maxPage" required="true" type="java.lang.Long" description="Max page number"/>
<jsp:directive.attribute name="pageCommand" required="true" type="java.lang.String" description="Form command"/>
<jsp:directive.attribute name="checked" required="false" type="java.lang.String" description="Checked item"/>

<div class="columns is-centered">
    <div class="column is-half is-centered">
        <nav class="pagination is-centered" role="navigation" aria-label="pagination">
            <c:if test="${currentPage != 1}">
                <form action="${pageContext.request.contextPath}/?command=${pageCommand}" method="post">
                    <input type="hidden" name="pageNumber" value="${currentPage - 1}"/>
                    <c:if test="${checked != null}">
                        <input type="hidden" name="checked" value="${checked}"/>
                    </c:if>
                    <input style="display: none" id="previous" type="submit">
                </form>
                <a class="pagination-previous" onclick="document.getElementById('previous').click()"><fmt:message key="page.back"/></a>
            </c:if>

            <c:if test="${currentPage != maxPage}">
                <form action="${pageContext.request.contextPath}/?command=${pageCommand}" method="post">
                    <input type="hidden" name="pageNumber" value="${currentPage + 1}"/>
                    <c:if test="${checked != null}">
                        <input type="hidden" name="checked" value="${checked}"/>
                    </c:if>
                    <input style="display: none" id="next" type="submit">
                </form>
                <a class="pagination-next" onclick="document.getElementById('next').click()"><fmt:message key="page.next"/></a>
            </c:if>

            <ul class="pagination-list">
                <li><a class="pagination-link is-current" aria-current="page">${currentPage}</a></li>
            </ul>
        </nav>
    </div>
</div>
