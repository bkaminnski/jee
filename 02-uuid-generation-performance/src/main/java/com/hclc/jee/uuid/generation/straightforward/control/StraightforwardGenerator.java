package com.hclc.jee.uuid.generation.straightforward.control;

import com.hclc.jee.uuid.generation.UuidsGenerator;

import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import java.util.stream.Stream;

@Stateless
public class StraightforwardGenerator {

    public JsonArray getNextBatch() {
        return Stream.of(UuidsGenerator.generateBatch())
                .collect(Json::createArrayBuilder, JsonArrayBuilder::add, JsonArrayBuilder::add)
                .build();
    }
}
