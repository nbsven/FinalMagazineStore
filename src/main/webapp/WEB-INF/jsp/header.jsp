<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <c:url value="${requestScope['javax.servlet.forward.servlet_path']}" var="en">
        <c:param name="localeCode" value="en"/>
    </c:url>

    <c:url value="${requestScope['javax.servlet.forward.servlet_path']}" var="ru">
        <c:param name="localeCode" value="ru"/>
    </c:url>

    <%--<nav class="navbar navbar-expand-lg navbar-light bg-light">--%>
    <a class="navbar-brand" href="<c:url value="/"/>"><spring:message code="text.home"/></a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNavDropdown"
            aria-controls="navbarNavDropdown" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarNavDropdown">
        <ul class="navbar-nav">
            <c:if test="${sessionScope.sessionAccount != null}">
                <li class="nav-item">
                    <a class="nav-link" href="<c:url value="/catalog"/>"><spring:message
                            code="text.catalog"/></a>
                </li>
                <c:if test="${sessionScope.sessionAccount.getRole() == 'ROLE_ADMIN'}">
                    <li class="nav-item">
                        <a class="nav-link" href="<c:url value="/payments"/>"><spring:message code="text.payments"/></a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="<c:url value="/publishers"/>"><spring:message
                                code="text.publishers.list"/></a>
                    </li>
                </c:if>
            </c:if>
            <li class="nav-item dropdown">
                <a class="nav-link dropdown-toggle" href="#" id="navbarDropdownMenuLink" data-toggle="dropdown"
                   aria-haspopup="true" aria-expanded="false">
                    <spring:message code="text.choose_lang"/>
                </a>
                <div class="dropdown-menu" aria-labelledby="navbarDropdownMenuLink">
                    <a class="dropdown-item" href="${en}"><spring:message
                            code="lang.en"/></a>
                    <a class="dropdown-item" href="${ru}"><spring:message
                            code="lang.ru"/></a>
                </div>
            </li>
        </ul>
        <ul class="navbar-nav ml-auto">
            <c:choose>
                <c:when test="${sessionScope.sessionAccount == null}">
                    <li class="nav-item">
                        <a class="nav-link" href="<c:url value="/login"/>"><spring:message code="text.sign_in"/></a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="<c:url value="/registration"/>"><spring:message
                                code="text.sign_up"/></a>
                    </li>
                </c:when>
                <c:otherwise>
                    <li class="nav-item">
                        <a class="nav-link" href="<c:url value="/sign_out"/>"><spring:message
                                code="text.sign_out"/></a>
                    </li>
                </c:otherwise>
            </c:choose>
        </ul>
    </div>
</nav>
