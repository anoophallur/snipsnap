/**
 * @author Paulo Abrantes (pabrantes@pabrantes.net)
 */

package org.snipsnap.semanticweb.rss;

import java.util.List;

import org.snipsnap.feeder.Feeder;
import org.snipsnap.snip.Snip;
import org.snipsnap.snip.SnipSpace;
import org.snipsnap.snip.SnipSpaceFactory;

public class CommentsForPostFeeder implements Feeder {

	private Snip snip;

	private SnipSpace space;

	public CommentsForPostFeeder() {
		space = SnipSpaceFactory.getInstance();
		snip = null;
	}

	public CommentsForPostFeeder(String snipName) {
		this();
		snip = space.load(snipName);
	}

	public CommentsForPostFeeder(Snip snip) {
		this();
		this.snip = snip;
	}
	
	public String getName() {
		return "commentsForPost";
	}

	public List getFeed() {
		return snip.getComments().getComments();	
	}

	public Snip getContextSnip() {
		return snip;
	}

	public void setContextSnip(Snip snip) {
		this.snip = snip;
	}

}
