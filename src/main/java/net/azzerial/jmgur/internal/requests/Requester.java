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
 * https://github.com/DV8FromTheWorld/JDA/blob/master/src/main/java/net/dv8tion/jda/internal/requests/Requester.java
 * All credit goes to the original authors.
 */

package net.azzerial.jmgur.internal.requests;

import net.azzerial.jmgur.api.Jmgur;
import net.azzerial.jmgur.api.JmgurInfo;
import net.azzerial.jmgur.api.requests.Request;
import net.azzerial.jmgur.api.requests.Response;
import net.azzerial.jmgur.api.utils.config.AuthenticationConfig;
import net.azzerial.jmgur.internal.utils.Check;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.internal.http.HttpMethod;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InterruptedIOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.RejectedExecutionException;

public final class Requester {

    public static final Logger LOG = LoggerFactory.getLogger(Requester.class);
    public static final String IMGUR_API_PREFIX = "https://api.imgur.com/";
    public static final String USER_AGENT = "Jmgur (" + JmgurInfo.GITHUB + ", " + JmgurInfo.VERSION + ")";
    public static final MediaType MEDIA_TYPE_JSON  = MediaType.parse("application/json; charset=utf-8");
    public static final RequestBody EMPTY_BODY = RequestBody.create(new byte[0], MEDIA_TYPE_JSON);

    private final Jmgur api;
    private final AuthenticationConfig authConfig;
    private final OkHttpClient httpClient;

    /* Constructors */

    public Requester(@NotNull Jmgur api) {
        Check.notNull(api, "api");
        this.api = api;
        this.authConfig = api.getAuthenticationConfig();
        this.httpClient = api.getSessionConfig().getHttpClient();
    }

    /* Getters & Setters */

    @NotNull
    public Jmgur getApi() {
        return api;
    }

    @NotNull
    public AuthenticationConfig getAuthConfig() {
        return authConfig;
    }

    @NotNull
    public OkHttpClient getHttpClient() {
        return httpClient;
    }

    /* Methods */

    public <T> void request(@NotNull Request<T> request) {
        Check.notNull(request, "request");
        if (api.getThreadingConfig().getRequesterPool().isShutdown())
            throw new RejectedExecutionException("The Requester has been stopped! No new requests can be requested!");
        execute(request);
    }

    /* Internal */

    private void execute(@NotNull Request<?> apiRequest) {
        execute(apiRequest, false);
    }

    private void execute(@NotNull Request<?> apiRequest, boolean retried) {
        final okhttp3.Request.Builder builder = new okhttp3.Request.Builder();
        final Route.CompiledRoute route = apiRequest.getRoute();

        builder.url(IMGUR_API_PREFIX + route.addQueryParams("_format", "json").getCompiledRoute());

        final String method = apiRequest.getRoute().getMethod().toString();
        RequestBody body = apiRequest.getBody();

        if (body == null && HttpMethod.requiresRequestBody(method))
            body = EMPTY_BODY;

        builder.method(method, body)
            .header("user-agent", USER_AGENT)
            .header("accept-encoding", "gzip")
            .header("authorization", route.getAuthHeader().format(this));

        final okhttp3.Request request = builder.build();
        final okhttp3.Response[] responses = new okhttp3.Response[4];
        okhttp3.Response lastResponse = null;
        try {
            int attempt = 0;

            do {
                if (apiRequest.isSkipped())
                    return;

                final Call call = httpClient.newCall(request);
                lastResponse = call.execute();
                responses[attempt] = lastResponse;

                if (lastResponse.code() < 500)
                    break;
                attempt += 1;

                try {
                    Thread.sleep(50 * attempt);
                } catch (InterruptedException ignored) {}
            } while (attempt < 3 && lastResponse.code() >= 500);

            apiRequest.handleResponse(new Response(api, lastResponse));
        } catch (SocketTimeoutException e) {
            if (!retried) {
                execute(apiRequest, true);
                return;
            }
            LOG.error("Requester timed out while executing a request", e);
            apiRequest.handleResponse(new Response(api, lastResponse, e));
        } catch (InterruptedIOException e) {
            LOG.warn("Got interrupted while executing request", e);
        } catch (Exception e) {
            LOG.error("There was an exception while executing a REST request", e);
            apiRequest.handleResponse(new Response(api, lastResponse, e));
        } finally {
            for (final okhttp3.Response r : responses) {
                if (r == null)
                    break;
                r.close();
            }
        }
    }
}
