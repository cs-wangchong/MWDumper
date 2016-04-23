/*
 * MediaWiki import/export processing tools
 * Copyright 2005-2016 by Brion Vibber and other contributors
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

package org.mediawiki.importer;

import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.nio.charset.StandardCharsets;

import org.apache.commons.io.output.NullOutputStream;

public class UTF8BoundsTest extends TestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(UTF8BoundsTest.class);
	}

	private String makeRepeated(int numberOfTimes, String source) {
		StringBuilder buffer = new StringBuilder();
		for (int i = 0; i < numberOfTimes; i++) {
			buffer.append(source);
		}
		return buffer.toString();
	}

	private String makeSampleRepeated(int repeat, int padding) {
		// Some scary 4-byte-per-char text from got.wikipedia.org
		String sample = "𐍅𐌰𐌹𐌻𐌰 𐌰𐌽𐌳𐌰𐌽𐌴𐌼𐌰 𐍃𐌹𐌾𐌰𐌹𐌸 𐌰𐌽𐌰 𐌲𐌿𐍄𐌹𐍃𐌺𐌰𐌼𐌼𐌰 𐌿𐍃𐌼𐌴𐍂𐌾𐌰 𐍅𐌹𐌺𐌹𐍀𐌰𐌹𐌳𐌾𐍉𐍃, 𐍆𐍂𐍉𐌳𐌹𐌱𐍉𐌺𐍉𐍃 𐌹𐌽 𐌽𐌰𐍄𐌾𐌰, 𐌸𐌰𐍂𐌴𐌹 𐍈𐌰𐍂𐌾𐌹𐍃 𐌼𐌰𐌲 𐌼𐌹𐌸𐌰𐍂𐌱𐌰𐌹𐌳𐌾𐌰𐌽. 𐍃𐍉 𐍅𐌹𐌺𐌹𐍀𐌰𐌹𐌳𐌾𐌰 𐌲𐌿𐍄𐍂𐌰𐌶𐌳𐌰𐌹 𐌾𐌰𐌷 447 𐌻𐌰𐌿𐌱𐌰𐌽𐍃 𐌷𐌰𐌱𐌰𐌹𐌸.";
		return "<mediawiki xml:lang=\"en\">" +
			"<siteinfo><namespaces><namespace key=\"0\"></namespace></namespaces></siteinfo>" +
			"<page><title>Test</title><revision>" +
			"<id>1</id>" +
			"<timestamp>2016-04-23T16:46:00Z</timestamp>" +
			"<contributor><username>Test</username><id>1</id></contributor>" +
			"<text>" + makeRepeated(padding, " ") + makeRepeated(repeat, sample) + "</text>" +
			"</revision></page></mediawiki>";
	}

	private boolean runImportThingy(String sample) throws IOException {
		OutputStream output = new NullOutputStream();
		InputStream input = new ByteArrayInputStream(sample.getBytes(StandardCharsets.UTF_8));
		DumpWriter sink = new XmlDumpWriter0_10(output);
		XmlDumpReader source = new XmlDumpReader(input, sink);
		
		source.readDump();
		
		return true; // did not throw
	}

	public void testParsingTinyFile() throws IOException {
		String sample = makeSampleRepeated(0, 0);
		assertTrue("tiny file parses ok", runImportThingy(sample));
	}

	public void testParsingManyOffsets() throws IOException {
		int unicodeRepeatCount = 200;
		// known to fail in this range on xerces 2.7.1
		for (int i = 1750; i < 1800; i++) {
			String sample = makeSampleRepeated(unicodeRepeatCount, i);
			assertTrue("file with repeat of " + i + " parses ok", runImportThingy(sample));
		}
	}
}
