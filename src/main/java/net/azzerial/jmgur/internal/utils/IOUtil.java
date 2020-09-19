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
 * https://github.com/DV8FromTheWorld/JDA/blob/master/src/main/java/net/dv8tion/jda/internal/utils/IOUtil.java
 * All credit goes to the original authors.
 */

package net.azzerial.jmgur.internal.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class IOUtil {

    private static final Pattern queryParamPattern = Pattern.compile("([^?#&=]+)=([^#&]*)");

    /* --- Web Client --- */

    @NotNull
    public static OkHttpClient.Builder newHttpClientBuilder() {
        final Dispatcher dispatcher = new Dispatcher();
        final ConnectionPool connectionPool = new ConnectionPool(5, 10, TimeUnit.SECONDS);

        dispatcher.setMaxRequestsPerHost(25);

        return new OkHttpClient.Builder()
            .connectionPool(connectionPool)
            .dispatcher(dispatcher);
    }

    @Nullable
    @SuppressWarnings("ConstantConditions")
    public static InputStream getBody(@NotNull okhttp3.Response response) throws IOException {
        Check.notNull(response, "response");
        final String encoding = response.header("Content-Encoding", "");
        final InputStream data = new BufferedInputStream(response.body().byteStream());

        data.mark(256);

        try {
            if (encoding.equalsIgnoreCase("gzip"))
                return new GZIPInputStream(data);
        } catch (ZipException | EOFException e) {
            data.reset();
            return null;
        }
        return data;
    }

    /* --- Http Url --- */

    @NotNull
    public static Map<String, String> getQueryParams(@Nullable String url) {
        if (url == null)
            return Collections.emptyMap();

        final Matcher matcher = queryParamPattern.matcher(url);
        final Map<String, String> params = new HashMap<>();

        while (matcher.find()) {
            params.putIfAbsent(matcher.group(1), matcher.group(2));
        }

        return params;
    }
}
