<%@ page import="org.radeox.util.Encoder"%>
 <%--
  ** Template for editing Snip's permissions.
  ** @author Paulo Abrantes
  --%>

<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://snipsnap.com/snipsnap" prefix="s" %>

<div class="snip-wrapper">
  <%-- display snip name or just "create new snip" if there is no existing snip --%>
  <div class="snip-title">
   <h1 class="snip-name"> <c:out value="${snip_name}" escapeXml="true"/> </h1>
  </div>
 
  <%-- display edit part --%>
  <div class="snip-content">
   	<form action="changePermissions" method="post">

                        <input type="hidden" name="snip" value="<c:out value='${snip.name}'/>"/>
                        <input type="hidden" name="referer" value="<c:out value='${referer}' default='${header["REFERER"]}'/>"/>
                        <table class="wiki-table">
                                <tr>
                                <th>Read</th>
                                <td>
                                <select name="read">
                                <option value="ADMIN">Admin</option>
                                <option value="EDITOR">Admin and Editor</option>
                                <option value="z">Everyone</option>
                                </select>
                                </td>
                                </tr>
                                <tr>
                                <th>Edit</th>
                                <td>
                                <select name="edit">    
                                <option value="ADMIN">Admin</option>
                                <option value="EDITOR">Admin and Editor</option>
                                <option value="z">Everyone</option>
                                </select>
                                </td>
                                </tr>
                                <tr>
                                <th>Attach</th>
                                <td>
                                 <select name="attach">
                                <option value="ADMIN">Admin</option>
                                <option value="EDITOR">Admin and Editor</option>
                                <option value="z">Everyone</option>
                                </select>
                                </td>
                                </tr>
                                <tr>
                                <th>Post Comments</th>
                                <td>
                                 <select name="post">
                                <option value="ADMIN">Admin</option>
                                <option value="EDITOR">Admin and Editor</option>
                                <option value="z">Everyone</option>
                                 </select>
                                </td>
                                </tr>
                </table>
                <input type="submit"/>
        </form>
  
  </div>
  
</div>