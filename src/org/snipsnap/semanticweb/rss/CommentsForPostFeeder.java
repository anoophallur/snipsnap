package org.snipsnap.semanticweb.rss;

import java.util.List;

import org.snipsnap.app.Application;
import org.snipsnap.feeder.Feeder;
import org.snipsnap.snip.Snip;
import org.snipsnap.snip.SnipSpace;
import org.snipsnap.snip.SnipSpaceFactory;

public class CommentsForPostFeeder implements Feeder {

	private String snipName;

	private SnipSpace space;

	public CommentsForPostFeeder() {
		this(Application.get().getConfiguration().getStartSnip());
	}

	public CommentsForPostFeeder(String snipName) {
		this.snipName = snipName;
		space = SnipSpaceFactory.getInstance();
	}

	public String getName() {
		return "commentsForPost";
	}

	public List getFeed() {
		Snip snip = getContextSnip();
		return snip.getComments().getComments();	
	}

	public Snip getContextSnip() {
		return space.load(snipName);
	}

}
