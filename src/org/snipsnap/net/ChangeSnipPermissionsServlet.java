package org.snipsnap.net;

/**
 * @author Paulo Abrantes (pabrantes@pabrantes.net)
 * 
 * This Servlet is responsabile for changing the 
 * permissions of a given snip.
 */
import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.snipsnap.app.Application;
import org.snipsnap.snip.Snip;
import org.snipsnap.snip.SnipSpace;
import org.snipsnap.snip.SnipSpaceFactory;
import org.snipsnap.user.Permissions;
import org.snipsnap.user.User;
import org.snipsnap.user.Permissions.PermissionType;
import org.snipsnap.user.Roles.RoleType;

public class ChangeSnipPermissionsServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		String snipName = request.getParameter("snip");
		Snip snip = SnipSpaceFactory.getInstance().load(snipName);
		request.setAttribute("snip",snip);
		 RequestDispatcher dispatcher = request.getRequestDispatcher("/exec/editPermissions.jsp");
	     dispatcher.forward(request, response);
	     return;
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String snipName = request.getParameter("snip");
		SnipSpace space = SnipSpaceFactory.getInstance();
		Snip snip = (snipName != null) ? space.load(
				snipName) : null;
		User user = Application.get().getUser();

		if (snip == null
				|| user != null
				&& (!snip.getOwner().equals(user.getLogin()) && !user.isAdmin())) {
			// should add error messages and go to somewhere.
			return;
		}
		String readValue = request.getParameter("read");
		String editValue = request.getParameter("edit");
		String postValue = request.getParameter("post");
		String attachValue = request.getParameter("attach");
		Permissions permissions = snip.getPermissions();
		recyclePermission(permissions, PermissionType.VIEW, RoleType
				.valueOf(readValue.toUpperCase()));
		recyclePermission(permissions, PermissionType.EDIT, RoleType
				.valueOf(editValue.toUpperCase()));
		recyclePermission(permissions, PermissionType.POST, RoleType
				.valueOf(postValue.toUpperCase()));
		recyclePermission(permissions, PermissionType.ATTACH, RoleType
				.valueOf(attachValue.toUpperCase()));
		
		space.store(snip);
		String referer = request.getParameter("referer");
		
	    response.sendRedirect(referer);

	}

	private void recyclePermission(Permissions permission,
			PermissionType permissionType, RoleType roleType) {
		permission.remove(permissionType);
		permission.add(permissionType, roleType);
	}

}
