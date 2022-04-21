package com.trilobiet.oapen.sitesearch;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter @AllArgsConstructor 
@Jacksonized @Builder
class Article {
	
	private final String title;
	private final String content;
	private final String slug;
	private final boolean publish;
	private final double score;
	
	@JsonDeserialize(using = MongoDateConverter.class)
	private final Date updatedAt;
	
	private final List<Topic> topics;

	@Override
	public String toString() {
		return score + " " + title + " (" + slug + ")" + " topics: " + topics;
	}
	
}

@Getter @AllArgsConstructor 
@Jacksonized @Builder
class Topic {
	
	private final String name;
	private final String slug;
	private final Boolean publish;
	private final List<MongoId> sections;
	
	@Override
	public String toString() {
		return "topic: " + slug 
				+ " sections: " + getSections()
				;
	}
	
}

class MongoId {
	
	@JsonProperty("$oid")
	String id;

	@Override
	public String toString() {
		return id;
	}
	
}
