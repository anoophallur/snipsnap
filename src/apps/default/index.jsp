<%@ page import="org.snipsnap.app.Application,
                 org.snipsnap.config.Configuration"%>
 <%--
  ** Template for redirection the root page to the start page
  ** @author Matthias L. Jugel
  ** @version $Id: index.jsp 1256 2003-12-11 13:24:57Z leo $
  --%>

<%
  Configuration snipConfig = Application.get().getConfiguration();
  response.sendRedirect(snipConfig.getSnipUrl(snipConfig.getStartSnip()));
  return;
%>