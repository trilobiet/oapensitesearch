package com.trilobiet.oapen.sitesearch;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * 
 * Gets the date value out of a mongo (nested) date field like 
 * "createdAt": {"$date": "2020-01-07T10:23:03.606Z"}
 * 
 * https://stackoverflow.com/questions/42856936/deserialize-mongodb-date-fields-to-java-pojo-using-jackson
 * 
 * @author acdhirr
 *
 */
class MongoDateConverter extends JsonDeserializer<Date> {
	 
	private static final SimpleDateFormat formatter = 
			new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

	@Override
    public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.readValueAsTree();
        try {
            return formatter.parse(node.get("$date").asText());
        } catch (ParseException e) {
            return null;
        }
    }
}

