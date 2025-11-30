package com.hero.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Utility class for working with JSON using the Gson library.
 * Provides methods for converting objects to JSON strings, converting objects to formatted JSON strings,
 * and parsing JSON strings into objects.
 *
 * @Author Andrea
 * @Date 2025/11/30 11:47
 * @Version 1.0
 */
public final class JsonUtils {

    private static final Gson GSON = new Gson();
    
    private static final Gson GSON_PRETTY = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    private JsonUtils() {
    }

    /**
     * Converts the provided object into its JSON string representation.
     *
     * @param obj the object to be converted to JSON. It can be any object or null.
     * @return the JSON string representation of the object, or null if the input is null.
     */
    public static String toJson(Object obj) {
        if (obj == null) {
            return null;
        }
        return GSON.toJson(obj);
    }

    /**
     * Converts the provided object into a formatted JSON string representation
     * with indentation for better readability.
     *
     * @param obj the object to be converted to a pretty-printed JSON string. It can be any object or null.
     * @return the formatted JSON string representation of the object, or null if the input is null.
     */
    public static String toPrettyJson(Object obj) {
        if (obj == null) {
            return null;
        }
        return GSON_PRETTY.toJson(obj);
    }

    /**
     * Parses the provided JSON string into an object of the specified type.
     *
     * @param json the JSON string to be deserialized. It can be null or empty.
     * @param clazz the class representing the type of object to be deserialized into.
     * @param <T> the generic type of the object to be deserialized.
     * @return the deserialized object of type T, or null if the input is null or empty.
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        return GSON.fromJson(json, clazz);
    }
}