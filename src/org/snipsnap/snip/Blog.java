/*
 * This file was part of "SnipSnap Wiki/Weblog".
 *
 * Copyright (c) 2002 Stephan J. Schmidt, Matthias L. Jugel
 * All Rights Reserved.
 *
 * And now it's also part of Snip It
 * Copyright (c) 2007 Paulo Abrantes
 * All rights Reserved
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

package org.snipsnap.snip;

import java.sql.Date;
import java.util.List;

/**
 * Blog encapsulates snips with
 * blog functionality.
 *
 * @author stephan
 * @version $Id: Blog.java 925 2003-08-07 10:01:29Z stephan $
 */

public interface Blog {
  public String getName();

  public Snip post(String content, String title);

  public Snip post(String content);

  public Snip post(String content, Date date);

  public List getFlatPosts();

  public List<Snip> getPosts(int count);

  public List<Snip> getPosts(int begin, int end);
  
  public int getPostsCount();
  
  public Snip getSnip();
}
