<!--
  ** Template for redirection the root page to the start page
  ** @author Matthias L. Jugel
  ** @version $Id: index.jsp 1606 2004-05-17 10:56:18Z leo $
  -->

<% response.sendRedirect(request.getContextPath() + "/"); return; %>