package com.trilobiet.oapen.sitesearch.mysql;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter @AllArgsConstructor 
@Jacksonized @Builder
class Article {
	
	private final String title;
	private final String content;
	private final String articleSlug;
	private final String topicSlug;
	private final String sectionSlug;
	private final boolean publish;
	private final double score;
	private final Date updatedAt;

	@Override
	public String toString() {
		return score + " " + title + " (" + articleSlug + ")";
	}
}


