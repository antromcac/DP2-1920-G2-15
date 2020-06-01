<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<petclinic:layout pageName="owners">

    <h2>Owner Information</h2>


    <table class="table table-striped">
        <tr>
            <th>Name</th>
            <td><b><c:out value="${owner.firstName} ${owner.lastName}"/></b></td>
        </tr>
        <tr>
            <th>Address</th>
            <td><c:out value="${owner.address}"/></td>
        </tr>
        <tr>
            <th>City</th>
            <td><c:out value="${owner.city}"/></td>
        </tr>
        <tr>
            <th>Telephone</th>
            <td><c:out value="${owner.telephone}"/></td>
        </tr>
    </table>

    <spring:url value="{ownerId}/edit" var="editUrl">
        <spring:param name="ownerId" value="${owner.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Edit Owner</a>

    <spring:url value="{ownerId}/pets/new" var="addUrl">
        <spring:param name="ownerId" value="${owner.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(addUrl)}" class="btn btn-default">Add New Pet</a>

    <br/>
    <br/>
    <br/>
    <h2>Pets and Visits</h2>

    <table class="table table-striped">
        <c:forEach var="pet" items="${owner.pets}">

            <tr>
                <td valign="top">
                    <dl class="dl-horizontal">
                        <dt>Name</dt>
                        <dd><c:out value="${pet.name}"/></dd>
                        <dt>Birth Date</dt>
                        <dd><petclinic:localDate date="${pet.birthDate}" pattern="yyyy-MM-dd"/></dd>
                        <dt>Type</dt>
                        <dd><c:out value="${pet.type.name}"/></dd>
                    </dl>
                </td>
                <td valign="top">
                    <table class="table-condensed">
                        <thead>
                        <tr>
                            <th>Visit Date</th>
                            <th>Description</th>
                             <th>Show Chip</th>
                              <th>Update Chip</th>
                               <sec:authorize access= "hasAuthority('veterinarian')">
                             <th>Disease</th>
                             </sec:authorize>
                        </tr>
                        </thead>
                        <c:forEach var="visit" items="${pet.visits}">
                            <tr>
                                <td><petclinic:localDate date="${visit.date}" pattern="yyyy-MM-dd"/></td>
                                <td><c:out value="${visit.description}"/></td>
                            </tr>
                        </c:forEach>
                        <tr>
                            <td>
                                <spring:url value="/owners/{ownerId}/pets/{petId}/edit" var="petUrl">
                                    <spring:param name="ownerId" value="${owner.id}"/>
                                    <spring:param name="petId" value="${pet.id}"/>
                                </spring:url>
                                <a href="${fn:escapeXml(petUrl)}">Edit Pet</a>
                            </td>
                            <td>
                                <spring:url value="/owners/{ownerId}/pets/{petId}/visits/new" var="visitUrl">
                                    <spring:param name="ownerId" value="${owner.id}"/>
                                    <spring:param name="petId" value="${pet.id}"/>
                                </spring:url>
                                <a href="${fn:escapeXml(visitUrl)}">Add Visit</a>
                            </td>
                            
                            <c:if test="${pet.chip != null}">
                            	<td>
                                	<spring:url value="/owners/{ownerId}/pets/{petId}/chips/{chipId}" var="chipUrl">
                                    	<spring:param name="ownerId" value="${owner.id}"/>
                                    	<spring:param name="petId" value="${pet.id}"/>
                                    	<spring:param name="chipId" value="${pet.chip.id}"/>
                                	</spring:url>
                                	<a href="${fn:escapeXml(chipUrl)}">Show chip</a>
                                </td>
                                <td>
                                    <spring:url value="/owners/{ownerId}/pets/{petId}/chips/{chipId}/edit" var="updChipUrl">
                                        <spring:param name="ownerId" value="${owner.id}"/>
                                        <spring:param name="petId" value="${pet.id}"/>
                                        <spring:param name="chipId" value="${pet.chip.id}"/>
                                    </spring:url>
                                    <a href="${fn:escapeXml(updChipUrl)}">Update Chip</a>
                                </td>
                            </c:if>
                            <c:if test="${pet.chip == null}">
                                <td>
                                    <spring:url value="/owners/{ownerId}/pets/{petId}/chips/new" var="chipUrl">
                                    <spring:param name="ownerId" value="${owner.id}"/>
                                    <spring:param name="petId" value="${pet.id}"/>
                                    </spring:url>
                                    <a href="${fn:escapeXml(chipUrl)}">Add Chip</a>
                                </td>
                            </c:if>
                            <sec:authorize access= "hasAuthority('veterinarian')">
                            <td>
                                <spring:url value="/diseases/new/{petId}" var="diseaseUrl">
                                    <spring:param name="diseaseId" value="${disease.id}"/>
                                    <spring:param name="petId" value="${pet.id}"/>
                                </spring:url>
                                <a href="${fn:escapeXml(diseaseUrl)}">Add Disease</a>
                          </td>
                        </sec:authorize>
                        </tr>
                         
                    </table>
                </td>
            </tr>

        </c:forEach>
    </table>

</petclinic:layout>
