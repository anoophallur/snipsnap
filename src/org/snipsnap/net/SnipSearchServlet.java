/*
 * This file is part of "SnipSnap Wiki/Weblog".
 *
 * Copyright (c) 2002 Stephan J. Schmidt, Matthias L. Jugel
 * All Rights Reserved.
 *
 * Copyright (C) 2006-2007 Paulo Abrantes
 * 
 * Please visit http://snipsnap.org/ for updates and contact.
 *
 * --LICENSE NOTICE--
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 * --LICENSE NOTICE--
 */
package org.snipsnap.net;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.lucene.search.Hits;
import org.snipsnap.app.Application;
import org.snipsnap.config.Configuration;
import org.snipsnap.snip.SnipSpace;
import org.snipsnap.snip.SnipSpaceFactory;
import org.snipsnap.util.URLEncoderDecoder;

/**
 * Load a snip to view.
 * @author Matthias L. Jugel
 * @version $Id: SnipSearchServlet.java 1256 2003-12-11 13:24:57Z leo $
 */
public class SnipSearchServlet extends HttpServlet {

  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    String query = request.getParameter("query");
    Configuration config = Application.get().getConfiguration();
    
    if (query != null && query.length() > 0) {
      String encodedQuery = URLEncoderDecoder.encode(query,config.getEncoding());
    	  HttpSession session = request.getSession();
      SnipSpace space = SnipSpaceFactory.getInstance();
      Hits hits = space.search(encodedQuery);
      session.setAttribute("query", encodedQuery);
      session.setAttribute("hits", hits);
      session.setAttribute("startIndex", new Integer(0));
      RequestDispatcher dispatcher = request.getRequestDispatcher("/exec/search.jsp");
      dispatcher.forward(request, response);
      return;
    }

    response.sendRedirect(config.getUrl());
  }

}
