<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="diseases">
    <h2>Enfermedades</h2>

    <table id="diseasesTable" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 150px;">Symptoms</th>
            <th style="width: 200px;">Severity</th>
            <th>Cure</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${diseases}" var="disease">
            <tr>
               
               
                <td>
                   
                <c:out value="${disease.symptoms}"/>
                </td>
                <td>
                    <c:out value="${disease.severity}"/>
                </td>
                <td>
                    <c:out value="${disease.cure}"/>
                </td>

                  <td>
                    <spring:url value="/diseases/{diseaseId}" var="diseaseUrl">
                        <spring:param name="diseaseId" value="${disease.id}"/>
                    </spring:url>
                   		 <a href="${fn:escapeXml(diseaseUrl)}">Show</a>
                   		 
                   	    <spring:url value="/diseases/{diseaseId}/edit" var="diseaseUrl">
                        <spring:param name="diseaseId" value="${disease.id}"/>
                    </spring:url>
                    	 <a href="${fn:escapeXml(diseaseUrl)}">Update</a>
                   	
                   	  <spring:url value="/diseases/delete/{diseaseId}" var="diseaseUrl">
                        <spring:param name="diseaseId" value="${disease.id}"/>
                    </spring:url>
                   		 <a href="${fn:escapeXml(diseaseUrl)}">Delete</a>
                </td>
                
                
               
           
            </tr>
        </c:forEach>
        </tbody>
    </table>
</petclinic:layout>