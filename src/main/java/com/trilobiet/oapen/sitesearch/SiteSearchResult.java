package com.trilobiet.oapen.sitesearch;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter @AllArgsConstructor 
@Jacksonized @Builder 
public class SiteSearchResult {
	
	private final String title;
	private final String text;
	private final String slug;
	private final String topic;
	private final String section;
	private final Date updatedAt;
	private final double score;
	
	@Override
	public String toString() {
		return "SiteSearchResult [title=" + title + ", slug=" + getFullSlug() + ", score=" + score + "]"
				+ "\n..." + text + "...\n"
				;
	}
	
	
	public String getFullSlug() {
		return section + "/" + topic; 
	}

	
}



