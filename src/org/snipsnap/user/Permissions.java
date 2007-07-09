/*
 * This file is part of "SnipSnap Wiki/Weblog".
 *
 * Copyright (c) 2002 Stephan J. Schmidt, Matthias L. Jugel
 * All Rights Reserved.
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

package org.snipsnap.user;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import org.snipsnap.user.Roles.RoleType;

/**
 * Permissions holds a ACL list with permissions and roles
 * 
 * @author stephan
 * @author Paulo Abrantes (pabrantes@pabrantes.net)
 * @version $Id: Permissions.java 1606 2004-05-17 10:56:18Z leo $
 */

public class Permissions {

	public enum PermissionType {
		EDIT("Edit"), ATTACH("Attach"), POST("Post"), VIEW("View");

		String type;

		private PermissionType(String type) {
			this.type = type;
		}

		public String getPermissionType() {
			return type;
		}
	}

	public final static String EDIT_SNIP = PermissionType.EDIT
			.getPermissionType();

	public final static String ATTACH_TO_SNIP = PermissionType.ATTACH
			.getPermissionType();

	public final static String POST_TO_SNIP = PermissionType.POST
			.getPermissionType();

	public final static String VIEW_SIP = PermissionType.VIEW
			.getPermissionType();

	private Map<PermissionType, Roles> permissions;

	public Permissions() {
		permissions = new HashMap<PermissionType, Roles>();
	}

	public Permissions(Map<PermissionType, Roles> permissions) {
		this.permissions = permissions;
	}

	public Permissions(String permissions) {
		this.permissions = deserialize(permissions);
	}

	public String toString() {
		return serialize();
	}

	public boolean empty() {
		return null == permissions || permissions.isEmpty();
	}

	@Deprecated
	public void remove(String permission, String role) {
		remove(PermissionType.valueOf(permission.toUpperCase()),RoleType.valueOf(role.toUpperCase()));
	}

	public void remove(PermissionType permission, RoleType role) {
		Roles roles = permissions.get(permission);
		if(roles!=null) {
			roles.remove(role);
			if(roles.isEmpty()) {
				permissions.remove(permission);
			}
		}
	}
	
	public void remove(PermissionType permission) {
		if(permissions.containsKey(permission)) {
			permissions.remove(permission);
		}
	}
	
	@Deprecated
	public void add(String permission) {
		add(PermissionType.valueOf(permission.toUpperCase()));
	}

	public void add(PermissionType permission) {
		if (!permissions.containsKey(permission)) {
			permissions.put(permission, new Roles());
		}
	}

	@Deprecated
	public void add(String permission, String role) {
		this.add(PermissionType.valueOf(permission.toUpperCase()), RoleType
				.valueOf(role.toUpperCase()));
	}

	public void add(PermissionType permission, RoleType role) {
		if (!permissions.containsKey(permission)) {
			permissions.put(permission, new Roles());
		}
		((Roles) permissions.get(permission)).add(role);
		return;
	}

	@Deprecated
	public void add(String permissionString, Roles roles) {
		add(PermissionType.valueOf(permissionString.toUpperCase()), roles);
	}

	public void add(PermissionType permission, Roles roles) {
		if (!permissions.containsKey(permission)) {
			permissions.put(permission, new Roles());
		}
		permissions.get(permission).addAll(roles);
	}

	@Deprecated
	public boolean exists(String permission, Roles roles) {
		return exists(PermissionType.valueOf(permission.toUpperCase()),roles);
	}

	public boolean exists(PermissionType permission, Roles roles) {
		if (null == permissions || !permissions.containsKey(permission)) {
			return false;
		}
		Roles permRoles = (Roles) permissions.get(permission);
		return permRoles.containsAny(roles);
	}
	
	public boolean check(PermissionType permission, Roles roles) {
		// Policy: If no permission is set, everything is allowed
		if (null == permissions || !permissions.containsKey(permission)) {
			return true;
		}
		Roles permRoles = (Roles) permissions.get(permission);
		return permRoles.containsAny(roles);
	}

	public Map<PermissionType, Roles> deserialize(String permissions) {
		Map<PermissionType, Roles> perms = new HashMap<PermissionType, Roles>();

		if (permissions != null && permissions.length() > 0) {
			StringTokenizer tokenizer = new StringTokenizer(permissions, "|");
			while (tokenizer.hasMoreTokens()) {
				String serializedPermission = tokenizer.nextToken();
				Roles roles = getRoles(serializedPermission);
				PermissionType permission = getPermission(serializedPermission);
				perms.put(permission, roles);
			}
		}

		return perms;
	}

	private String serialize() {
		StringBuffer permBuffer = new StringBuffer();

		if (this.permissions != null && !this.permissions.isEmpty()) {

			Iterator<PermissionType> iterator = permissions.keySet()
					.iterator();
			while (iterator.hasNext()) {
				PermissionType permission = iterator.next();
				permBuffer.append(permission.getPermissionType());
				permBuffer.append(":");
				Roles roles = permissions.get(permission);
				Iterator<RoleType> rolesIterator = roles.iterator();
				while (rolesIterator.hasNext()) {
					RoleType role = rolesIterator.next();
					permBuffer.append(role.getRoleName());
					if (rolesIterator.hasNext()) {
						permBuffer.append(",");
					}
				}
				if (iterator.hasNext()) {
					permBuffer.append("|");
				}
			}
		}
		return permBuffer.toString();
	}

	private PermissionType getPermission(String serializedPermission) {
		return PermissionType.valueOf(serializedPermission.split(":")[0]
				.toUpperCase());
	}

	private Roles getRoles(String serializedPermission) {
		Roles roles = new Roles();
		String rolesString = serializedPermission.split(":")[1];
		StringTokenizer tokenizer = new StringTokenizer(rolesString, ",");
		while (tokenizer.hasMoreTokens()) {
			String role = tokenizer.nextToken();
			roles.add(RoleType.valueOf(role.toUpperCase()));
		}
		return roles;
	}

}
