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
 * https://github.com/DV8FromTheWorld/JDA/blob/master/src/main/java/net/dv8tion/jda/api/requests/Request.java
 * All credit goes to the original authors.
 */

package net.azzerial.jmgur.api.requests;

import net.azzerial.jmgur.api.Jmgur;
import net.azzerial.jmgur.api.exceptions.ErrorResponseException;
import net.azzerial.jmgur.internal.requests.Route;
import net.azzerial.jmgur.internal.requests.restaction.RestActionImpl;
import net.azzerial.jmgur.internal.utils.Check;
import okhttp3.RequestBody;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CancellationException;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

public final class Request<T> {

    private final Jmgur api;
    private final RestActionImpl<T> restAction;
    private final Consumer<? super T> onSuccess;
    private final Consumer<? super Throwable> onFailure;
    private final Route.CompiledRoute route;
    private final RequestBody body;
    private final long deadline;

    private boolean done = false;
    private boolean isCancelled = false;

    /* Constructors */

    public Request(
        @NotNull RestActionImpl<T> restAction,
        @NotNull Consumer<? super T> onSuccess, @NotNull Consumer<? super Throwable> onFailure,
        @Nullable RequestBody body, long deadline, @NotNull Route.CompiledRoute route
    ) {
        Check.notNull(restAction, "restAction");
        Check.notNull(onSuccess, "onSuccess");
        Check.notNull(onFailure, "onFailure");
        Check.notNegative(deadline, "deadline");
        Check.notNull(route, "route");
        this.api = restAction.getApi();
        this.restAction = restAction;
        this.onSuccess = onSuccess;
        this.onFailure = onFailure;
        this.body = body;
        this.deadline = deadline;
        this.route = route;
    }

    /* Getters & Setters */

    @NotNull
    public Jmgur getApi() {
        return api;
    }

    @NotNull
    public RestActionImpl<T> getRestAction() {
        return restAction;
    }

    @NotNull
    public Consumer<? super T> getOnSuccess() {
        return onSuccess;
    }

    @NotNull
    public Consumer<? super Throwable> getOnFailure() {
        return onFailure;
    }

    @Nullable
    public RequestBody getBody() {
        return body;
    }

    @NotNull
    public Route.CompiledRoute getRoute() {
        return route;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public boolean isSkipped() {
        if (isTimeout()) {
            onTimeout();
            return true;
        }
        final boolean skip = isCancelled();
        if (skip)
            onCancelled();
        return skip;
    }

    /* Methods */

    public void cancel() {
        this.isCancelled = true;
    }

    public void onSuccess(@Nullable T successObj) {
        if (done)
            return;
        done = true;
        api.getThreadingConfig().getCallbackPool().execute(() -> {
            try {
                onSuccess.accept(successObj);
            } catch (Throwable t) {
                RestActionImpl.LOG.error("Encountered error while processing success consumer", t);
                if (t instanceof Error)
                    throw (Error) t;
            }
        });
    }

    public void onFailure(@Nullable Response response) {
        onFailure(ErrorResponseException.create(response));
    }

    public void onFailure(@Nullable Throwable failException) {
        if (done)
            return;
        done = true;
        api.getThreadingConfig().getCallbackPool().execute(() -> {
            try {
                onFailure.accept(failException);
            } catch (Throwable t) {
                RestActionImpl.LOG.error("Encountered error while processing failure consumer", t);
                if (t instanceof Error)
                    throw (Error) t;
            }
        });
    }

    public void onCancelled() {
        onFailure(new CancellationException("RestAction has been cancelled"));
    }

    public void onTimeout() {
        onFailure(new TimeoutException("RestAction has timed out"));
    }

    public void handleResponse(@NotNull Response response) {
        restAction.handleResponse(this, response);
    }

    /* Internal */

    private boolean isTimeout() {
        return deadline > 0 && deadline < System.currentTimeMillis();
    }
}
