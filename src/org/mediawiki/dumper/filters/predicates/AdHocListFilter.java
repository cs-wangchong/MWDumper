package org.mediawiki.dumper.filters.predicates;

import java.util.Set;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;

public class AdHocListFilter extends BaseListFilter {

	public AdHocListFilter(String list) {
		super(parseList(list));
	}

	private static Set<String> parseList(String list) {
		Builder<String> builder = ImmutableSet.builder();
		// TODO: Java 8
		// Splitter.on(',').omitEmptyStrings().split(list).forEach(s -> builder.add(s));
		Iterable<String> splitList = Splitter.on(',').omitEmptyStrings().split(list);
		for (String s : splitList) {
			builder.add(s);
		}
		return builder.build();
	}

}
