/*
 * This file is part of "SnipSnap Wiki/Weblog".
 *
 * Copyright (c) 2002 Stephan J. Schmidt, Matthias L. Jugel
 * All Rights Reserved.
 *
 * Please visit http://snipsnap.org/ for updates and contact.
 *
 * This file is now also part of snip it
 * Copyright (c) 2006-2007 Paulo Abrantes
 * All Rights Reserved
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

package org.snipsnap.render.macro;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import org.radeox.util.i18n.ResourceManager;
import org.snipsnap.app.Application;
import org.snipsnap.config.Configuration;
import org.snipsnap.render.filter.links.BackLinks;
import org.snipsnap.render.macro.parameter.SnipMacroParameter;
import org.snipsnap.snip.Blog;
import org.snipsnap.snip.Snip;
import org.snipsnap.snip.SnipLink;
import org.snipsnap.snip.SnipSpace;
import org.snipsnap.snip.SnipSpaceFactory;
import org.snipsnap.snip.SnipUtil;
import org.snipsnap.util.StringUtil;

/*
 * Macro that displays a weblog. All subsnips are read and displayed in reverse
 * chronological order.
 * 
 * @author stephan @author Paulo Abrantes (added the navigation and short
 * content with keep reading snip title)
 * 
 * @version $Id: WeblogMacro.java 1653 2004-06-11 20:04:56Z leo $
 */

public class WeblogMacro extends SnipMacro {
	private SnipSpace space;

	public WeblogMacro() {
		space = SnipSpaceFactory.getInstance();
	}

	public String getName() {
		return "weblog";
	}

	public String getDescription() {
		return ResourceManager.getString("i18n.messages",
				"macro.weblog.description");
	}

	public String[] getParamDescription() {
		return ResourceManager
				.getString("i18n.messages", "macro.weblog.params").split(";");
	}

	public void execute(Writer writer, SnipMacroParameter params)
			throws IllegalArgumentException, IOException {

		String countParameter = params.get("count");
		int count = (countParameter != null) ? Integer.parseInt(countParameter)
				: 10;
		String snipName = params.get("snip");
		String name = (snipName != null) ? snipName : params
				.getSnipRenderContext().getSnip().getName();
		String paragraphs = params.get("numberOfParagraphs");
		Integer numberOfParagraphs = (paragraphs != null) ? Integer
				.valueOf(paragraphs) : null;

		Blog blog = space.getBlog(name);

		if (null == blog) {
			throw new IllegalArgumentException("unknown weblog " + name);
		}
		// order by name
		// with correct ending /1,/2,/3,...,/11,/12
		Application app = Application.get();
		String startString = (String) app.getParameters().get("since");
		Integer start = (startString == null) ? new Integer(0) : Integer
				.valueOf(startString);
		int totalPosts = blog.getPostsCount();

		List<Snip> posts = (start != null) ? blog
				.getPosts(start, start + count) : blog.getPosts(count);

		int DAY_INDEX = 1;

		writer.write(getNavigationalLinks(start, count, totalPosts));
		String lastDay = "";

		for (Snip entry : posts) {

			String[] entryName = StringUtil.split(entry.getName(), "/");
			int slashOffset = entryName.length - 3;
			String day = (entryName.length > 1 ? entryName[slashOffset
					+ DAY_INDEX] : entryName[0]);

			if (!lastDay.equals(day)) {
				writer
						.write("<div style=\"clear: both;\" class=\"blog-date\">");
				writer.write(SnipUtil.toDate(day));
				lastDay = day;
				writer.write("</div>");
			}

			Configuration conf = Application.get().getConfiguration();

			writer.write("<br/><div class=\"share-class-blog\">");
			if (conf.getFeatureDiggItLinkShow().equals("true")) {
				writer
						.write("<a href=\"http://digg.com/submit?phase=2&amp;url="
								+ entry.getUrl()
								+ "&amp;title=" + entry.getTitle() + "\">");
				SnipLink.appendImage(writer, "digg", "Digg it");
				writer.write("</a> ");
			}
			if (conf.getFeatureDeliciousLinkShow().equals("true")) {
				writer.write("<a href=\"http://del.icio.us/post?url="
						+ entry.getUrl() + "&amp;title="
						+ entry.getTitle() + "\">");
				SnipLink.appendImage(writer, "delicious", "Save to Delicious");
				writer.write("</a> ");
			}
			if (conf.getFeatureCommentsRssShow().equals("true")) {
				writer.write("<a href=\"" + conf.getRealPath()
						+ "/exec/rss?snip=" + entry.getName()
						+ "&amp;type=commentsForPost\">");
				SnipLink.appendImage(writer, "rss-icon", "Comments Feed");
				writer.write("</a> ");
			}
			writer.write("</div>");
			writer.write(getShorted(entry, numberOfParagraphs));

			writer.write("<div class=\"snip-post-comments\">");
			writer.write(entry.getComments().getCommentString());
			writer.write(" | ");
			writer.write(entry.getComments().getPostString());
			writer.write("</div>\n\n");

			if ("true".equals(conf.getFeatureReferrerShow())) {
				writer.write("<div class=\"snip-backlinks\">");
				BackLinks.appendTo(writer, entry.getAccess().getBackLinks(), 5);
				writer.write("</div>");
			}
		}
		writer.write(getNavigationalLinks(start, count, totalPosts));
	}

	private String getShorted(Snip snip, Integer numberOfParagraphs) {
		String content = snip.getXMLContent();
		String[] contents = content.split("<p class=\\\"paragraph\\\"\\/>");
		if (numberOfParagraphs == null || contents.length < numberOfParagraphs) {
			return content;
		} else {
			String shortContent = "";
			for (int i = 0; i < numberOfParagraphs; i++) {
				shortContent += contents[i] + "<p class=\"paragraph\"/>";
			}
			return shortContent
					+ "<p class=\"paragraph\"/><a href=\""
					+ snip.getUrl()
					+ "\">"
					+ ResourceManager.getString("i18n.messages",
							"macro.weblog.keepReading") + " "
					+ snip.getTitle() + "</a><p class=\"paragraph\"/>";
		}
	}

	private String getNavigationalLinks(Integer start, Integer count,
			Integer postsCount) {
		String navigationLinks = "";

		if (start != null && start - count >= 0) {
			navigationLinks += "<div style=\"float: left; padding-bottom: 1px;\"><a href=\"space/start?since="
					+ (start - count)
					+ "\">"
					+ ResourceManager.getString("i18n.messages",
							"macro.weblog.previousPage") + "</a></div>";
		}
		if (start == null || start != null && start + count < postsCount) {
			navigationLinks += "<div style=\"float: right; padding-bottom: 1px;\"><a href=\"space/start?since="
					+ (start == null ? count : start + count)
					+ "\">"
					+ ResourceManager.getString("i18n.messages",
							"macro.weblog.nextPage") + "</a></div>";
		}

		navigationLinks += "<div style=\"clear: both;\"></div>";
		return navigationLinks;
	}
}
