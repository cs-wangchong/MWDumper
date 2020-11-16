package org.mediawiki.dumper.filters.predicates;

import java.util.Set;

import org.mediawiki.dumper.wiki.Page;

public abstract class BaseListFilter extends PredicatePageFilter {

	protected final Set<String> set;

	public BaseListFilter(Set<String> set) {
		this.set = set;
	}

	public boolean test(Page page) {
		return set.contains(page.Title.subjectPage().toString())
			|| set.contains(page.Title.talkPage().toString());
	}

}