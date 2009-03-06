package org.snipsnap.snip;

/**
 * Wraps the SnipTokenizer into an analyzer
 *
 * @author stephan
 * @version $Id: SnipAnalyzer.java 645 2003-01-09 09:49:12Z stephan $
 **/

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;

import java.io.Reader;

public final class SnipAnalyzer extends Analyzer {

  public final TokenStream tokenStream(String field, Reader reader) {
    return new SnipTokenizer(field, reader);
  }
}
