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
 * https://github.com/DV8FromTheWorld/JDA/blob/master/src/main/java/net/dv8tion/jda/api/utils/data/DataArray.java
 * All credit goes to the original authors.
 */

package net.azzerial.jmgur.api.utils.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.CollectionType;
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

public class DataArray  implements Iterable<Object> {

    private static final Logger log = LoggerFactory.getLogger(DataObject.class);
    private static final ObjectMapper mapper;
    private static final SimpleModule module;
    private static final CollectionType listType;

    static {
        mapper = new ObjectMapper();
        module = new SimpleModule();
        module.addAbstractTypeMapping(Map.class, HashMap.class);
        module.addAbstractTypeMapping(List.class, ArrayList.class);
        mapper.registerModule(module);
        listType = mapper.getTypeFactory().constructRawCollectionType(ArrayList.class);
    }

    final List<Object> data;

    /* Static Constructors */

    @NotNull
    public static DataArray createEmpty() {
        return new DataArray(new ArrayList<>());
    }

    @NotNull
    public static DataArray fromCollection(@NotNull Collection<?> col) {
        return createEmpty().addAll(col);
    }

    @NotNull
    public static DataArray fromJson(@NotNull String json) {
        try {
            return new DataArray(mapper.readValue(json, listType));
        } catch (IOException e) {
            throw new ParsingException(e);
        }
    }

    @NotNull
    public static DataArray fromJson(@NotNull InputStream json) {
        try {
            return new DataArray(mapper.readValue(json, listType));
        } catch (IOException e) {
            throw new ParsingException(e);
        }
    }

    @NotNull
    public static DataArray fromJson(@NotNull Reader json) {
        try {
            return new DataArray(mapper.readValue(json, listType));
        } catch (IOException e) {
            throw new ParsingException(e);
        }
    }

    /* Constructors */

    DataArray(@NotNull List<Object> data) {
        this.data = data;
    }

    /* Getters & Setters */

    public boolean isNull(int index) {
        return data.get(index) == null;
    }

    public boolean isType(int index, @NotNull DataType type) {
        return type.isType(data.get(index));
    }

    public int length() {
        return data.size();
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    @NotNull
    @SuppressWarnings("unchecked")
    public DataObject getObject(int index) {
        Map<String, Object> child = null;
        try {
            child = (Map<String, Object>) get(Map.class, index);
        } catch (ClassCastException ex) {
            log.error("Unable to extract child data", ex);
        }
        if (child == null)
            throw valueError(index, "DataObject");
        return new DataObject(child);
    }

    @NotNull
    @SuppressWarnings("unchecked")
    public DataArray getArray(int index) {
        List<Object> child = null;
        try {
            child = (List<Object>) get(List.class, index);
        } catch (ClassCastException ex) {
            log.error("Unable to extract child data", ex);
        }
        if (child == null)
            throw valueError(index, "DataArray");
        return new DataArray(child);
    }

    @NotNull
    public String getString(int index) {
        String value = get(String.class, index, UnaryOperator.identity(), String::valueOf);
        if (value == null)
            throw valueError(index, "String");
        return value;
    }

    @Contract("_, !null -> !null")
    public String getString(int index, @Nullable String defaultValue) {
        String value = get(String.class, index, UnaryOperator.identity(), String::valueOf);
        return value == null ? defaultValue : value;
    }

    public boolean getBoolean(int index) {
        return getBoolean(index, false);
    }

    public boolean getBoolean(int index, boolean defaultValue) {
        Boolean value = get(Boolean.class, index, Boolean::parseBoolean, null);
        return value == null ? defaultValue : value;
    }

    public int getInt(int index) {
        Integer value = get(Integer.class, index, Integer::parseInt, Number::intValue);
        if (value == null)
            throw valueError(index, "int");
        return value;
    }

    public int getInt(int index, int defaultValue) {
        Integer value = get(Integer.class, index, Integer::parseInt, Number::intValue);
        return value == null ? defaultValue : value;
    }

    public int getUnsignedInt(int index) {
        Integer value = get(Integer.class, index, Integer::parseUnsignedInt, Number::intValue);
        if (value == null)
            throw valueError(index, "unsigned int");
        return value;
    }

    public int getUnsignedInt(int index, int defaultValue) {
        Integer value = get(Integer.class, index, Integer::parseUnsignedInt, Number::intValue);
        return value == null ? defaultValue : value;
    }

    public long getLong(int index) {
        Long value = get(Long.class, index, Long::parseLong, Number::longValue);
        if (value == null)
            throw valueError(index, "long");
        return value;
    }

    public long getLong(int index, long defaultValue) {
        Long value = get(Long.class, index, Long::parseLong, Number::longValue);
        return value == null ? defaultValue : value;
    }

    public long getUnsignedLong(int index) {
        Long value = get(Long.class, index, Long::parseUnsignedLong, Number::longValue);
        if (value == null)
            throw valueError(index, "unsigned long");
        return value;
    }

    public long getUnsignedLong(int index, long defaultValue) {
        Long value = get(Long.class, index, Long::parseUnsignedLong, Number::longValue);
        return value == null ? defaultValue : value;
    }

    /* Methods */

    @NotNull
    public DataArray add(@Nullable Object value) {
        if (value instanceof DataObject)
            data.add(((DataObject) value).data);
        else if (value instanceof DataArray)
            data.add(((DataArray) value).data);
        else
            data.add(value);
        return this;
    }

    @NotNull
    public DataArray addAll(@NotNull Collection<?> values) {
        values.forEach(this::add);
        return this;
    }

    @NotNull
    public DataArray addAll(@NotNull DataArray array) {
        return addAll(array.data);
    }

    @NotNull
    public DataArray insert(int index, @Nullable Object value) {
        if (value instanceof DataObject)
            data.add(index, ((DataObject) value).data);
        else if (value instanceof DataArray)
            data.add(index, ((DataArray) value).data);
        else
            data.add(index, value);
        return this;
    }

    @NotNull
    public DataArray remove(int index) {
        data.remove(index);
        return this;
    }

    @NotNull
    public DataArray remove(@Nullable Object value) {
        data.remove(value);
        return this;
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
    public List<Object> toList() {
        return data;
    }

    @NotNull
    @Override
    public Iterator<Object> iterator() {
        return data.iterator();
    }

    /* Internal */

    private ParsingException valueError(int index, String expectedType) {
        return new ParsingException("Unable to resolve value at " + index + " to type " + expectedType + ": " + data.get(index));
    }

    @Nullable
    private <T> T get(@NotNull Class<T> type, int index) {
        return get(type, index, null, null);
    }

    @Nullable
    private <T> T get(@NotNull Class<T> type, int index, @Nullable Function<String, T> stringMapper, @Nullable Function<Number, T> numberMapper) {
        Object value = data.get(index);

        if (value == null)
            return null;
        if (type.isAssignableFrom(value.getClass()))
            return type.cast(value);
        if (stringMapper != null && value instanceof String)
            return stringMapper.apply((String) value);
        else if (numberMapper != null && value instanceof Number)
            return numberMapper.apply((Number) value);

        throw new ParsingException(String.format("Cannot parse value for index %d into type %s: %s instance of %s", index, type.getSimpleName(), value, value.getClass().getSimpleName()));
    }
}
