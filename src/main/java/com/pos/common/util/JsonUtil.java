package com.pos.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static String toJson(Object o){
        try {
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            return "Json serialize failed: " + o.toString();
        }

    }
}
