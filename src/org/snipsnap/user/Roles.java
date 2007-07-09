/*
 * This file was part of "SnipSnap Wiki/Weblog".
 * That code is Copyright (c) 2002 Stephan J. Schmidt, Matthias L. Jugel
 * All Rights Reserved. (visit http://snipsnap.org)
 *
 * Now this file is part of Snip It, a SnipSnap fork  
 * The new code is Copyright (c) 2006-2007 Paulo Abrantes
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

package org.snipsnap.user;

import java.util.*;

/**
 * User roleSet
 * 
 * @author Paulo Abrantes (pabrantes@pabrantes.net)
 * @author stephan
 * @version $Id: Roles.java 864 2003-05-23 10:47:26Z stephan $
 */

public class Roles {

	public enum RoleType {
		AUTHENTICATED("Authenticated"), OWNER("Owner"), ADMIN("Admin"), EDITOR(
				"Editor"), NOCOMMENT("NoComment"); 
		/*
		 * I really don't agree with having no comment as a role
		 * but I'm interested in preserving backward compatibility
		 * with snipsnap.
		 * 
		 * Paulo Abrantes 18-04-2007
		 */

		private String role;

		private RoleType(String role) {
			this.role = role;
		}

		public String getRoleName() {
			return role;
		}
		
	}

	public final static String AUTHENTICATED = RoleType.AUTHENTICATED
			.getRoleName();

	public final static String OWNER = RoleType.OWNER.getRoleName();

	public final static String ADMIN = RoleType.ADMIN.getRoleName();

	public final static String EDITOR = RoleType.EDITOR.getRoleName();

	public final static String NOCOMMENT = RoleType.NOCOMMENT.getRoleName();

	private static Set<RoleType> ROLES = null;

	private Set<RoleType> roleSet;

	public static Set<RoleType> allRoles() {
		if (ROLES == null) {
			ROLES = new TreeSet<RoleType>();
			ROLES.add(RoleType.EDITOR);
			ROLES.add(RoleType.ADMIN);
			ROLES.add(RoleType.NOCOMMENT);
			ROLES = Collections.unmodifiableSet(ROLES);
		}
		return ROLES;
	}

	public Set getAllRoles() {
		return Roles.allRoles();
	}

	public Roles(Roles roles) {
		roleSet = new HashSet<RoleType>(roles.roleSet);
	}

	public Roles(String roleString) {
		this.roleSet = deserialize(roleString);
	}

	public Roles() {
		roleSet = new HashSet<RoleType>();
	}

	public Roles(Set<RoleType> roleSet) {
		this.roleSet = new HashSet<RoleType>(roleSet);
	}

	public boolean isEmpty() {
		return roleSet.isEmpty();
	}

	@Deprecated
	public void remove(String role) {
		remove(RoleType.valueOf(role.toUpperCase()));
	}

	public void remove(RoleType role) {
		roleSet.remove(role);
	}

	@Deprecated
	public void add(String role) {
		this.add(RoleType.valueOf(role.toUpperCase()));
	}

	public void add(RoleType role) {
		roleSet.add(role);
	}

	public void addAll(Roles roles) {
		this.roleSet.addAll(roles.getRoleSet());
	}

	public Iterator<RoleType> iterator() {
		return roleSet.iterator();
	}

	@Deprecated
	public boolean contains(String role) {
		return contains(RoleType.valueOf(role.toUpperCase()));
	}

	public boolean contains(RoleType role) {
		return roleSet.contains(role);
	}

	public boolean containsAny(Roles roles) {

		Roles myRoles = this;
		Iterator<RoleType> iterator = roles.iterator();
		while (iterator.hasNext()) {
			RoleType role = iterator.next();
			if (myRoles.contains(role)) {
				return true;
			}
		}
		return false;
	}

	public Set<RoleType> getRoleSet() {
		return Collections.unmodifiableSet(roleSet);
	}

	public String toString() {
		return serialize(roleSet);
	}

	private String serialize(Set<RoleType> roles) {
		if (null == roles || roles.isEmpty()) {
			return "";
		}

		StringBuffer buffer = new StringBuffer();
		Iterator<RoleType> iterator = roles.iterator();
		while (iterator.hasNext()) {
			RoleType role = iterator.next();
			buffer.append(role.getRoleName());
			if (iterator.hasNext()) {
				buffer.append(":");
			}
		}
		return buffer.toString();
	}

	private Set<RoleType> deserialize(String roleString) {
		Set<RoleType> roles = new HashSet<RoleType>();

		if (roleString != null && roleString.length() > 0) {

			StringTokenizer st = new StringTokenizer(roleString, ":");

			while (st.hasMoreTokens()) {
				roles.add(RoleType.valueOf(st.nextToken().toUpperCase()));
			}
		}

		return roles;
	}

	public int hashCode() {
		return getRoleSet().hashCode();
	}

	public boolean equals(Roles obj) {
		return getRoleSet().equals(obj.getRoleSet());
	}
}
