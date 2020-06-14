<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="part" tagdir="/WEB-INF/tags" %>

<div class="columns is-centered has-text-centered">
    <div class="column is-two-thirds is-centered">
        <div class="container" style="margin: 15px">
            <label class="label"><fmt:message key="page.visit_time"/>:</label>
        </div>
        <div class="tile is-ancestor">
            <div class="tile is-vertical is-centered">
                <div class="tile is-parent">
                    <part:timeTile visits="${requestScope.visitsMap}" visitTime="9-00"/>
                    <part:timeTile visits="${requestScope.visitsMap}" visitTime="9-30"/>
                    <part:timeTile visits="${requestScope.visitsMap}" visitTime="10-00"/>
                    <part:timeTile visits="${requestScope.visitsMap}" visitTime="10-30"/>
                </div>

                <div class="tile is-parent">
                    <part:timeTile visits="${requestScope.visitsMap}" visitTime="11-00"/>
                    <part:timeTile visits="${requestScope.visitsMap}" visitTime="11-30"/>
                    <part:timeTile visits="${requestScope.visitsMap}" visitTime="12-00"/>
                    <part:timeTile visits="${requestScope.visitsMap}" visitTime="12-30"/>
                </div>

                <div class="tile is-parent">
                    <part:timeTile visits="${requestScope.visitsMap}" visitTime="13-00"/>
                    <part:timeTile visits="${requestScope.visitsMap}" visitTime="13-30"/>
                    <part:timeTile visits="${requestScope.visitsMap}" visitTime="14-00"/>
                    <part:timeTile visits="${requestScope.visitsMap}" visitTime="14-30"/>
                </div>

                <div class="tile is-parent">
                    <part:timeTile visits="${requestScope.visitsMap}" visitTime="15-00"/>
                    <part:timeTile visits="${requestScope.visitsMap}" visitTime="15-30"/>
                    <part:timeTile visits="${requestScope.visitsMap}" visitTime="16-00"/>
                    <part:timeTile visits="${requestScope.visitsMap}" visitTime="16-30"/>
                </div>
            </div>
        </div>
      <div class="buttons is-centered">
          <button class="button is-link is-light" onclick="window.history.back()">
              <fmt:message key="page.back"/>
          </button>
      </div><br>


    </div>
</div>


