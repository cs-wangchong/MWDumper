package org.mediawiki.dumper.writers.sql;

import java.io.IOException;

public interface SqlStream {
	public void writeComment(CharSequence sql) throws IOException;
	public void writeStatement(CharSequence sql) throws IOException;
	public void close() throws IOException;
}
