/*
 * MediaWiki import/export processing tools
 * Copyright 2005 by Brion Vibber
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * $Id$
 */

package org.mediawiki.dumper.filters.predicates;

import java.io.IOException;
// TODO: Java 8
// import java.util.function.Predicate;

import org.mediawiki.dumper.filters.ChainingDumpWriter;
import org.mediawiki.dumper.wiki.Page;
import org.mediawiki.dumper.wiki.Revision;
import org.mediawiki.dumper.writers.DumpWriter;

public abstract class PredicatePageFilter {
	
	// TODO: Remove when this class implements Predicate<Page>
	abstract public boolean test(Page t);

	public static DumpWriter makePredicateDumpWriter(final DumpWriter successorWriter, final PredicatePageFilter filter) {
		return new ChainingDumpWriter(successorWriter) {
			boolean showThisPage = true;

			public void writeStartPage(Page page) throws IOException {
				showThisPage = filter.test(page);
				if (showThisPage) {
					writer.writeStartPage(page);
				}
			}
			
			public void writeEndPage() throws IOException {
				if (showThisPage) {
					writer.writeEndPage();
				}
			}
			
			public void writeRevision(Revision revision) throws IOException {
				if (showThisPage) {
					writer.writeRevision(revision);
				}
			}
			
		};
	}
}
