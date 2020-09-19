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
 * https://github.com/DV8FromTheWorld/JDA/blob/master/src/main/java/net/dv8tion/jda/api/requests/Response.java
 * All credit goes to the original authors.
 */

package net.azzerial.jmgur.api.requests;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import net.azzerial.jmgur.api.Jmgur;
import net.azzerial.jmgur.api.utils.IOFunction;
import net.azzerial.jmgur.api.utils.data.DataArray;
import net.azzerial.jmgur.api.utils.data.DataObject;
import net.azzerial.jmgur.internal.utils.Check;
import net.azzerial.jmgur.internal.utils.IOUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.Optional;
import java.util.stream.Collectors;

public final class Response implements Closeable {

    public static final int ERROR_CODE = -1;
    public static final String ERROR_MESSAGE = "ERROR";
    public static final IOFunction<BufferedReader, DataObject> JSON_SERIALIZE_OBJECT = DataObject::fromJson;
    public static final IOFunction<BufferedReader, DataArray> JSON_SERIALIZE_ARRAY = DataArray::fromJson;

    private final Jmgur api;
    private final okhttp3.Response rawResponse;
    private final int code;
    private final String message;
    private final InputStream body;
    private final Exception exception;

    private boolean converted;
    private Object object;
    private String fallbackString;

    /* Constructors */

    public Response(@NotNull Jmgur api, @NotNull final okhttp3.Response response) {
        this(api, response, response.code(), response.message());
    }

    public Response(@NotNull Jmgur api, @Nullable final okhttp3.Response response, @NotNull final Exception exception) {
        this.api = api;
        this.rawResponse = response;
        this.code = ERROR_CODE;
        this.message = ERROR_MESSAGE;
        this.body = null;
        this.exception = exception;
        this.converted = false;
        this.object = null;
        this.fallbackString = null;
    }

    public Response(@NotNull Jmgur api, @Nullable final okhttp3.Response response, final int code, @NotNull final String message) {
        this.api = api;
        this.rawResponse = response;
        this.code = code;
        this.message = message;
        this.converted = false;
        this.object = null;
        this.fallbackString = null;
        this.exception = null;

        if (response == null)
            this.body = null;
        else {
            try {
                this.body = IOUtil.getBody(response);
            } catch (final Exception e) {
                throw new IllegalStateException("An error occurred while parsing the response for a RestAction", e);
            }
        }
    }

    /* Getters & Setters */

    @NotNull
    public Jmgur getApi() {
        return api;
    }

    public int getCode() {
        return code;
    }

    @NotNull
    public String getMessage() {
        return message;
    }

    @Nullable
    public okhttp3.Response getRawResponse() {
        return rawResponse;
    }

    public boolean isOk() {
        return code > 199 && code < 300;
    }

    public boolean isError() {
        return code > 399 || code == Response.ERROR_CODE;
    }

    public boolean isClientError() {
        return code > 399 && code < 500;
    }

    public boolean isServerError() {
        return code >= 500;
    }

    public boolean isRequesterError() {
        return code == Response.ERROR_CODE;
    }

    @Nullable
    public Exception getException() {
        return exception;
    }

    /* Methods */

    @Override
    public void close() {
        if (rawResponse != null)
            rawResponse.close();
    }

    @NotNull
    public DataArray getArray() {
        return get(DataArray.class, JSON_SERIALIZE_ARRAY);
    }

    @NotNull
    public Optional<DataArray> optArray() {
        return parseBody(true, DataArray.class, JSON_SERIALIZE_ARRAY);
    }

    @NotNull
    public DataObject getObject() {
        return get(DataObject.class, JSON_SERIALIZE_OBJECT);
    }

    @NotNull
    public Optional<DataObject> optObject() {
        return parseBody(true, DataObject.class, JSON_SERIALIZE_OBJECT);
    }

    @NotNull
    @SuppressWarnings("unchecked")
    public <T> T get(@NotNull TypeReference<T> typeRef) {
        Check.notNull(typeRef, "typeRef");
        JavaType type = TypeFactory.defaultInstance().constructType(typeRef);
        Class<T> rawClass = (Class<T>) type.getRawClass();
        return parseBody(rawClass, reader -> api.getSessionConfig().getMapper().readValue(reader, type)).orElseThrow(IllegalStateException::new);
    }

    @NotNull
    public <T> T get(@NotNull Class<T> typeOf, @NotNull IOFunction<BufferedReader, T> parser) {
        return parseBody(typeOf, parser).orElseThrow(IllegalStateException::new);
    }

    @NotNull
    public String getString() {
        return parseBody(String.class, this::readString).orElseGet(() -> fallbackString == null ? "N/A" : fallbackString);
    }

    /* Internal */

    private String readString(@NotNull BufferedReader reader) {
        Check.notNull(reader, "reader");
        return reader.lines().collect(Collectors.joining("\n"));
    }

    private <T> Optional<T> parseBody(@NotNull Class<T> typeOf, @NotNull IOFunction<BufferedReader, T> parser) {
        return parseBody(false, typeOf, parser);
    }

    @SuppressWarnings("ConstantConditions")
    private <T> Optional<T> parseBody(boolean opt, @NotNull Class<T> typeOf, @NotNull IOFunction<BufferedReader, T> parser) {
        Check.notNull(typeOf, "typeOf");
        Check.notNull(parser, "parser");
        if (converted) {
            if (object != null && typeOf.isAssignableFrom(object.getClass()))
                return Optional.of(typeOf.cast(object));
            return Optional.empty();
        }

        converted = true;
        if (body == null || rawResponse == null || rawResponse.body().contentLength() == 0)
            return Optional.empty();

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(body));
            reader.mark(1024);

            T t = parser.apply(reader);
            this.object = t;

            return Optional.ofNullable(t);
        } catch (final Exception e) {
            try {
                reader.reset();
                this.fallbackString = reader.lines().collect(Collectors.joining("\n"));
                reader.close();
            } catch (NullPointerException | IOException ignored) {}

            if (opt)
                return Optional.empty();
            else
                throw new IllegalStateException("An error occurred while parsing the response for a RestAction", e);
        }
    }
}
