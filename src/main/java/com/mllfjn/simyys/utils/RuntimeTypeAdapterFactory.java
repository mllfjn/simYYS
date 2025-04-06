package com.mllfjn.simyys.utils;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class RuntimeTypeAdapterFactory<T> implements TypeAdapterFactory {
    private final Class<T> baseType;
    private final String typeFieldName;
    private final Map<String, Class<? extends T>> subclasses = new LinkedHashMap<>();
    private final Map<Class<? extends T>, String> subtypeToName = new LinkedHashMap<>();

    private RuntimeTypeAdapterFactory(Class<T> baseType, String typeFieldName) {
        this.baseType = baseType;
        this.typeFieldName = typeFieldName;
    }

    public static <T> RuntimeTypeAdapterFactory<T> of(Class<T> baseType, String typeFieldName) {
        return new RuntimeTypeAdapterFactory<>(baseType, typeFieldName);
    }

    public RuntimeTypeAdapterFactory<T> registerSubtype(Class<? extends T> subclass, String typeName) {
        subclasses.put(typeName, subclass);
        subtypeToName.put(subclass, typeName);
        return this;
    }

    @Override
    public <R> TypeAdapter<R> create(Gson gson, TypeToken<R> type) {
        if (type.getRawType() != baseType) return null;

        final Map<String, TypeAdapter<?>> nameToAdapter = new LinkedHashMap<>();
        final Map<Class<?>, TypeAdapter<?>> classToAdapter = new LinkedHashMap<>();
        for (Map.Entry<String, Class<? extends T>> entry : subclasses.entrySet()) {
            TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(entry.getValue()));
            nameToAdapter.put(entry.getKey(), adapter);
            classToAdapter.put(entry.getValue(), adapter);
        }

        return new TypeAdapter<R>() {
            @Override
            public void write(JsonWriter out, R value) throws IOException {
                Class<?> srcType = value.getClass();
                String typeName = subtypeToName.get(srcType);
                if (typeName == null) {
                    throw new JsonParseException("Cannot serialize " + srcType.getName() + "; unregistered subtype.");
                }

                TypeAdapter<R> delegate = (TypeAdapter<R>) classToAdapter.get(srcType);
                JsonElement jsonElement = delegate.toJsonTree(value);

                if (jsonElement.isJsonObject()) {
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    /*if (jsonObject.has(typeFieldName)) {
                        throw new JsonParseException("Field '" + typeFieldName + "' already exists in JSON.");
                    }
                    jsonObject.add(typeFieldName, new JsonPrimitive(typeName));*/
                    gson.toJson(jsonObject, out);
                } else {
                    throw new JsonParseException("Expected a JSON object, got " + jsonElement);
                }
            }

            @Override
            public R read(JsonReader in) throws IOException {
                JsonElement jsonElement = JsonParser.parseReader(in);
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                JsonElement typeElement = jsonObject.get(typeFieldName);

                if (typeElement == null) {
                    throw new JsonParseException("Missing type field '" + typeFieldName + "'.");
                }

                String typeName = typeElement.getAsString();
                TypeAdapter<?> delegate = nameToAdapter.get(typeName);
                if (delegate == null) {
                    throw new JsonParseException("Unknown type " + typeName + ".");
                }

                return (R) delegate.fromJsonTree(jsonElement);
            }
        };
    }
}