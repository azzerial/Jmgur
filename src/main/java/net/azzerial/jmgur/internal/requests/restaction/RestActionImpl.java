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
 * https://github.com/DV8FromTheWorld/JDA/blob/master/src/main/java/net/dv8tion/jda/internal/requests/RestActionImpl.java
 * All credit goes to the original authors.
 */

package net.azzerial.jmgur.internal.requests.restaction;

import net.azzerial.jmgur.api.Jmgur;
import net.azzerial.jmgur.api.requests.Request;
import net.azzerial.jmgur.api.requests.Response;
import net.azzerial.jmgur.api.requests.RestFuture;
import net.azzerial.jmgur.api.requests.restaction.RestAction;
import net.azzerial.jmgur.internal.requests.Route;
import net.azzerial.jmgur.internal.utils.Check;
import okhttp3.RequestBody;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class RestActionImpl<T> implements RestAction<T> {

    public static final Logger LOG = LoggerFactory.getLogger(RestAction.class);

    private static Consumer<Object> defaultSuccess = o -> {};
    private static Consumer<? super Throwable> defaultFailure = t -> {
        if (t instanceof CancellationException || t instanceof TimeoutException)
            LOG.debug(t.getMessage());
        else if (LOG.isDebugEnabled())
            LOG.error("RestAction queue returned failure", t);
        else
            LOG.error("RestAction queue returned failure: [{}] {}", t.getClass().getSimpleName(), t.getMessage());
    };
    private static long defaultTimeout = 0;

    private final Jmgur api;
    private final Route.CompiledRoute route;
    private final RequestBody data;
    private final BiFunction<Request<T>, Response, T> handler;

    private long deadline = 0;

    /* Constructors */

    public RestActionImpl(@NotNull Jmgur api, @NotNull Route.CompiledRoute route, @NotNull BiFunction<Request<T>, Response, T> handler) {
        this(api, route, null, handler);
    }

    public RestActionImpl(@NotNull Jmgur api, @NotNull Route.CompiledRoute route, @Nullable RequestBody data, @NotNull BiFunction<Request<T>, Response, T> handler) {
        Check.notNull(api, "api");
        Check.notNull(route, "route");
        Check.notNull(handler, "handler");
        this.api = api;
        this.route = route;
        this.data = data;
        this.handler = handler;
    }

    /* Static Getters & Setters */

    @NotNull
    public static Consumer<? super Throwable> getDefaultFailure() {
        return defaultFailure;
    }

    public static void setDefaultFailure(@Nullable Consumer<? super Throwable> callback) {
        defaultFailure = callback == null ? t -> {} : callback;
    }

    @NotNull
    public static Consumer<Object> getDefaultSuccess() {
        return defaultSuccess;
    }

    public static void setDefaultSuccess(@Nullable Consumer<Object> callback) {
        defaultSuccess = callback == null ? t -> {} : callback;
    }

    public static long getDefaultTimeout() {
        return defaultTimeout;
    }

    public static void setDefaultTimeout(long timeout, @NotNull TimeUnit unit) {
        Check.notNegative(timeout, "timeout");
        Check.notNull(unit, "unit");
        defaultTimeout = unit.toMillis(timeout);
    }

    /* Getters & Setters */

    @NotNull
    @Override
    public Jmgur getApi() {
        return api;
    }

    /* Methods */

    @Override
    public void queue(@Nullable Consumer<? super T> success, @Nullable Consumer<? super Throwable> failure) {
        if (api.getThreadingConfig().getRequesterPool().isShutdown())
            throw new RejectedExecutionException("The Requester has been stopped! No new requests can be requested!");
        api.getThreadingConfig().getRequesterPool().execute(() ->
            api.getRequester().request(new Request<>(
                this,
                success == null ? defaultSuccess : success,
                failure == null ? defaultFailure : failure,
                data,
                getDeadline(),
                route)
            )
        );
    }

    @Nullable
    @Override
    public T complete() {
        try {
            return submit().join();
        } catch (CompletionException e) {
            if (e.getCause() != null) {
                final Throwable cause = e.getCause();

                if (cause instanceof RuntimeException)
                    throw (RuntimeException) cause;
                else if (cause instanceof Error)
                    throw (Error) cause;
            }
            throw e;
        }
    }

    @NotNull
    @Override
    public CompletableFuture<T> submit() {
        return new RestFuture<>(this, data, getDeadline(), route);
    }

    @NotNull
    @Override
    public RestAction<T> deadline(long timestamp) {
        Check.notNegative(timestamp, "timestamp");
        this.deadline = timestamp;
        return this;
    }

    public void handleResponse(@NotNull Request<T> request, @NotNull Response response) {
        Check.notNull(request, "request");
        Check.notNull(response, "response");
        if (response.isOk())
            request.onSuccess(handler.apply(request, response));
        else
            request.onFailure(response);
    }

    /* Internal */

    private long getDeadline() {
        return deadline > 0 ?
            deadline :
            defaultTimeout > 0 ?
                System.currentTimeMillis() + defaultTimeout :
                0;
    }
}
