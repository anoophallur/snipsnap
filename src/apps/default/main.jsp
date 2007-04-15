 <%--
  ** Main layout template.
  ** @author Matthias L. Jugel
  ** @version $Id: main.jsp 1836 2005-10-07 19:38:04Z leo $
  --%>

<%@ page import="org.snipsnap.snip.SnipSpace,
                 org.snipsnap.app.Application,
                 org.snipsnap.snip.SnipSpaceFactory,
                 org.snipsnap.container.Components,
                 org.snipsnap.snip.Snip,
                 java.util.Collection,
                 java.util.Iterator,
                 org.snipsnap.snip.label.TypeLabel"%>
<%@ page pageEncoding="iso-8859-1" %>
<% response.setContentType("text/html; charset="+Application.get().getConfiguration().getEncoding()); %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://snipsnap.com/snipsnap" prefix="s" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" lang="<c:out value='${app.configuration.locale}'/>" xml:lang="<c:out value='${app.configuration.locale}'/>">
 <head>

  <META name="verify-v1" content="zyRWD2PUpwvTzh/xP40dtBvzkjWd35taLJ6bPL9mkQU=" /> 
  <!-- base of this document to make all links relative -->
  <base href="<c:out value='${app.configuration.url}/'/>"/>
  <!-- content type and generator -->
  <meta http-equiv="Content-Type" content="text/html; charset=<c:out value='${app.configuration.encoding}'/>"/>
  <meta http-equiv="Generator" content="SnipSnap/<c:out value="${app.configuration.version}"/>"/>
  
  <!-- meta information for SEO -->
  <c:if test="${app.configuration.featureMetaKeywordsEnabled == true}">
  	<meta name="keywords" content="<c:out value='${app.configuration.featureMetaKeywords}'/>" />
  </c:if>
  
  <c:if test="${app.configuration.featureMetaAuthorEnabled == true}">
  	<meta name="author" content="<c:out value='${app.configuration.featureMetaAuthor}'/>"/>
  </c:if>
  
  <!-- dublin core classification and geographic location information -->
  <s:dublinCore snip="${snip}"/>
  <s:geoUrl/>
  <!-- aggregrator related info -->
  <link rel="EditURI" type="application/rsd+xml" title="RSD" href="<c:out value='${app.configuration.url}/exec/rsd'/>"/>
  
  <link rel="alternate" type="application/rss+xml" title="RSS" href="http://feeds.feedburner.com/PabrantesWeblog"/>

  <link rel="index" href="<c:out value='${app.configuration.url}/space/snipsnap-index'/>"/>
  <!-- icons and stylesheet -->
  <link rel="shortcut icon" href="<c:out value='${app.configuration.url}/favicon.ico'/>"/>
  <link rel="icon" href="<c:out value='${app.configuration.url}/favicon.ico'/>"/>
  <link rel="STYLESHEET" type="text/css" href="<c:out value='${app.configuration.url}/theme/default.css'/>" />
  <link rel="STYLESHEET" type="text/css" href="<c:out value='${app.configuration.url}/theme/print.css'/>" media="print" />
  <!-- title of this document -->
  <title>
	<c:choose>
	<c:when test="${snip.weblog}">
	 <c:out value="${app.configuration.name}" default="SnipSnap"/> :: <c:out value="${app.configuration.tagline}"/>
	</c:when>
	<c:otherwise>
	 <c:out value="${snip.title}"/>
	</c:otherwise>
	</c:choose>
  </title>

<!-- 72d9f9c9911fba283c4de8fcb132ff05 -->

<c:if test="${app.configuration.featureSnapPreviewEnabled == true}"> 
<script defer="defer" id="snap_preview_anywhere" type="text/javascript" src="http://spa.snap.com/snap_preview_anywhere.js?ap=1&amp;key=<c:out value='${app.configuration.featureSnapPreviewUserKey}'/>&amp;sb=1&amp;domain=<c:out value='${app.configuration.realHost}'/>"></script> 

</c:if>

</head>
 <body>
  <div id="page-logo">
   <c:choose>
    <c:when test="${snip.name==app.configuration.startSnip && not(empty app.configuration.logo)}"><s:image root="SnipSnap/config" name="${app.configuration.logo}" alt="${app.configuration.name}"/></c:when>
    <c:when test="${snip.name!=app.configuration.startSnip && not(empty app.configuration.logo)}"><a href="<c:out value='${app.configuration.url}'/>" accesskey="1"><s:image root="SnipSnap/config" name="${app.configuration.logo}" alt="${app.configuration.name}"/></a></c:when>
    <c:when test="${snip.name==app.configuration.startSnip && empty app.configuration.logo}"><c:out value="${app.configuration.name}" default="SnipSnap"/></c:when>
    <c:otherwise><a href="<c:out value='${app.configuration.url}'/>" accesskey="1"><c:out value="${app.configuration.name}" default="SnipSnap"/></a></c:otherwise>
   </c:choose>
  </div>
  <div id="page-title">
   <div id="page-tagline"><c:out value="${app.configuration.tagline}"/></div>
   <div id="page-buttons"><c:import url="util/mainbuttons.jsp"/></div>
  </div>
  <div id="page-wrapper">
   <div id="page-content">
    <c:import url="${page}"/>
    <s:debug/>
   </div>
   <%
     SnipSpace space = (SnipSpace)Components.getComponent(SnipSpace.class);
     for(int i = 1; space.exists("snipsnap-portlet-"+i) || space.exists("SnipSnap/portlet/"+i); i++) {
       Snip snip = space.load("snipsnap-portlet-"+i);
       if(null == snip) {
         snip = space.load("SnipSnap/portlet/" + i);
       }
       pageContext.setAttribute("portlet", snip);
       pageContext.removeAttribute("view_handler");
       pageContext.removeAttribute("mime_type");

           String viewHandler = null;
           String type = null;
           Collection mimeTypes = snip.getLabels().getLabels("TypeLabel");
           if (!mimeTypes.isEmpty()) {
             Iterator handlerIt = mimeTypes.iterator();
             while (handlerIt.hasNext()) {
               TypeLabel typeLabel = (TypeLabel) handlerIt.next();
               viewHandler = typeLabel.getViewHandler();
               // search for default handler if non found
               if (null == viewHandler) {
                 viewHandler = TypeLabel.getViewHandler(typeLabel.getTypeValue());
               }

               if (null != viewHandler) {
                 type = typeLabel.getTypeValue();
                 pageContext.setAttribute("view_handler", viewHandler);
                 pageContext.setAttribute("mime_type", type);
                 break;
               }
             }
           }

   %>
    <div id="page-portlet-<%=i%>-wrapper">
     <div id="page-portlet-<%=i%>">
       <%-- if there is a special view handler, use it, else display standard page --%>
       <c:choose>
         <c:when test="${not empty(view_handler)}">
           <c:catch var="error">
             <c:import url="/plugin/${view_handler}"/>
           </c:catch>
         </c:when>
         <c:otherwise>
           <c:out value="${portlet.XMLContent}" escapeXml="false" />
         </c:otherwise>
       </c:choose>
     </div>
    </div>
   <% } %>
  </div>
  <div id="page-bottom"><s:snip name="snipsnap-copyright"/></div>
 </body>
</html>
