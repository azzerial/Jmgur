/*
 * Copyright 2015-2020 Austin Keener, Michael Ritter, Florian Spieß, and the JDA contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * This class was taken (and modified) from DV8FromTheWorld's project JDA.
 * https://github.com/DV8FromTheWorld/JDA/blob/master/src/main/java/net/dv8tion/jda/api/utils/data/DataObject.java
 * All credit goes to the original authors.
 */

package net.azzerial.jmgur.api.utils.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.MapType;
import net.azzerial.jmgur.api.exceptions.ParsingException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class DataObject {

    private static final Logger log = LoggerFactory.getLogger(DataObject.class);
    private static final ObjectMapper mapper;
    private static final SimpleModule module;
    private static final MapType mapType;

    static {
        mapper = new ObjectMapper();
        module = new SimpleModule();
        module.addAbstractTypeMapping(Map.class, HashMap.class);
        module.addAbstractTypeMapping(List.class, ArrayList.class);
        mapper.registerModule(module);
        mapType = mapper.getTypeFactory().constructRawMapType(HashMap.class);
    }

    final Map<String, Object> data;

    /* Static Constructors */

    @NotNull
    public static DataObject createEmpty() {
        return new DataObject(new HashMap<>());
    }

    @NotNull
    public static DataObject fromJson(@NotNull byte[] data) {
        try {
            return new DataObject(mapper.readValue(data, mapType));
        } catch (IOException ex) {
            throw new ParsingException(ex);
        }
    }

    @NotNull
    public static DataObject fromJson(@NotNull String json) {
        try {
            return new DataObject(mapper.readValue(json, mapType));
        } catch (IOException ex) {
            throw new ParsingException(ex);
        }
    }

    @NotNull
    public static DataObject fromJson(@NotNull InputStream stream) {
        try {
            return new DataObject(mapper.readValue(stream, mapType));
        } catch (IOException ex) {
            throw new ParsingException(ex);
        }
    }

    @NotNull
    public static DataObject fromJson(@NotNull Reader stream) {
        try {
            return new DataObject(mapper.readValue(stream, mapType));
        } catch (IOException ex) {
            throw new ParsingException(ex);
        }
    }

    /* Constructors */

    DataObject(@NotNull Map<String, Object> data) {
        this.data = data;
    }

    /* Getters & Setters */

    public boolean hasKey(@NotNull String key) {
        return data.containsKey(key);
    }

    public boolean isNull(@NotNull String key) {
        return data.get(key) == null;
    }

    public boolean isType(@NotNull String key, @NotNull DataType type) {
        return type.isType(data.get(key));
    }

    @NotNull
    public DataObject getObject(@NotNull String key) {
        return optObject(key).orElseThrow(() -> valueError(key, "DataObject"));
    }

    @NotNull
    @SuppressWarnings("unchecked")
    public Optional<DataObject> optObject(@NotNull String key) {
        Map<String, Object> child = null;
        try {
            child = (Map<String, Object>) get(Map.class, key);
        } catch (ClassCastException ex) {
            log.error("Unable to extract child data", ex);
        }
        return child == null ? Optional.empty() : Optional.of(new DataObject(child));
    }

    @NotNull
    public DataArray getArray(@NotNull String key) {
        return optArray(key).orElseThrow(() -> valueError(key, "DataArray"));
    }

    @NotNull
    @SuppressWarnings("unchecked")
    public Optional<DataArray> optArray(@NotNull String key) {
        List<Object> child = null;
        try {
            child = (List<Object>) get(List.class, key);
        } catch (ClassCastException ex) {
            log.error("Unable to extract child data", ex);
        }
        return child == null ? Optional.empty() : Optional.of(new DataArray(child));
    }

    @NotNull
    public Optional<Object> opt(@NotNull String key) {
        return Optional.ofNullable(data.get(key));
    }

    @NotNull
    public Object get(@NotNull String key) {
        Object value = data.get(key);
        if (value == null)
            throw valueError(key, "any");
        return value;
    }

    @NotNull
    public String getString(@NotNull String key) {
        String value = getString(key, null);
        if (value == null)
            throw valueError(key, "String");
        return value;
    }

    @Contract("_, !null -> !null")
    public String getString(@NotNull String key, @Nullable String defaultValue) {
        String value = get(String.class, key, UnaryOperator.identity(), String::valueOf);
        return value == null ? defaultValue : value;
    }

    public boolean getBoolean(@NotNull String key) {
        return getBoolean(key, false);
    }

    public boolean getBoolean(@NotNull String key, boolean defaultValue) {
        Boolean value = get(Boolean.class, key, Boolean::parseBoolean, null);
        return value == null ? defaultValue : value;
    }

    public long getLong(@NotNull String key) {
        Long value = get(Long.class, key, Long::parseLong, Number::longValue);
        if (value == null)
            throw valueError(key, "long");
        return value;
    }

    public long getLong(@NotNull String key, long defaultValue) {
        Long value = get(Long.class, key, Long::parseLong, Number::longValue);
        return value == null ? defaultValue : value;
    }

    public long getUnsignedLong(@NotNull String key) {
        Long value = get(Long.class, key, Long::parseUnsignedLong, Number::longValue);
        if (value == null)
            throw valueError(key, "unsigned long");
        return value;
    }

    public long getUnsignedLong(@NotNull String key, long defaultValue) {
        Long value = get(Long.class, key, Long::parseUnsignedLong, Number::longValue);
        return value == null ? defaultValue : value;
    }

    public int getInt(@NotNull String key) {
        Integer value = get(Integer.class, key, Integer::parseInt, Number::intValue);
        if (value == null)
            throw valueError(key, "int");
        return value;
    }

    public int getInt(@NotNull String key, int defaultValue) {
        Integer value = get(Integer.class, key, Integer::parseInt, Number::intValue);
        return value == null ? defaultValue : value;
    }

    public int getUnsignedInt(@NotNull String key) {
        Integer value = get(Integer.class, key, Integer::parseUnsignedInt, Number::intValue);
        if (value == null)
            throw valueError(key, "unsigned int");
        return value;
    }

    public int getUnsignedInt(@NotNull String key, int defaultValue) {
        Integer value = get(Integer.class, key, Integer::parseUnsignedInt, Number::intValue);
        return value == null ? defaultValue : value;
    }

    /* Methods */

    @NotNull
    public DataObject remove(@NotNull String key) {
        data.remove(key);
        return this;
    }

    @NotNull
    public DataObject putNull(@NotNull String key) {
        data.put(key, null);
        return this;
    }

    @NotNull
    public DataObject put(@NotNull String key, @Nullable Object value) {
        if (value instanceof DataObject)
            data.put(key, ((DataObject) value).data);
        else if (value instanceof DataArray)
            data.put(key, ((DataArray) value).data);
        else
            data.put(key, value);
        return this;
    }

    @NotNull
    public Collection<Object> values() {
        return data.values();
    }

    @NotNull
    public Set<String> keys() {
        return data.keySet();
    }

    public byte[] toJson() {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            mapper.writeValue(outputStream, data);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public String toString() {
        try {
            return mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new ParsingException(e);
        }
    }

    @NotNull
    public Map<String, Object> toMap() {
        return data;
    }

    /* Internal */

    private ParsingException valueError(String key, String expectedType) {
        return new ParsingException(String.format("Unable to resolve value with key \"%s\" to type %s: %s, in object: %s", key, expectedType, data.get(key), data));
    }

    @Nullable
    private <T> T get(@NotNull Class<T> type, @NotNull String key) {
        return get(type, key, null, null);
    }

    @Nullable
    private <T> T get(@NotNull Class<T> type, @NotNull String key, @Nullable Function<String, T> stringParse, @Nullable Function<Number, T> numberParse) {
        Object value = data.get(key);

        if (value == null)
            return null;
        if (type.isAssignableFrom(value.getClass()))
            return type.cast(value);
        if (value instanceof Number && numberParse != null)
            return numberParse.apply((Number) value);
        else if (value instanceof String && stringParse != null)
            return stringParse.apply((String) value);

        throw new ParsingException(String.format("Cannot parse value for \"%s\" into type %s: %s instance of %s", key, type.getSimpleName(), value, value.getClass().getSimpleName()));
    }
}
