/*
 * This file is part of "SnipSnap Wiki/Weblog".
 *
 * Please visit http://snipsnap.org/ for updates and contact.
 *
 * Copyright (c) 2006-2007 Paulo Abrantes 
 * All Rights Reserved.   
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

package org.snipsnap.jcaptcha;

import com.octo.captcha.service.image.DefaultManageableImageCaptchaService;
import com.octo.captcha.service.image.ImageCaptchaService;

public class JCaptchaSingleton {

	private static ImageCaptchaService instance = null;

	public static ImageCaptchaService getInstance() {
		if (instance == null) {
			instance = new DefaultManageableImageCaptchaService();
		}
		return instance;
	}
}
