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
 * https://github.com/DV8FromTheWorld/JDA/blob/master/src/main/java/net/dv8tion/jda/api/requests/RestAction.java
 * All credit goes to the original authors.
 */

package net.azzerial.jmgur.api.requests.restaction;

import net.azzerial.jmgur.api.Jmgur;
import net.azzerial.jmgur.internal.requests.restaction.RestActionImpl;
import net.azzerial.jmgur.internal.utils.Check;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public interface RestAction<T> {

    /* Static Getters & Setters */

    @NotNull
    static Consumer<? super Throwable> getDefaultFailure() {
        return RestActionImpl.getDefaultFailure();
    }

    static void setDefaultFailure(@Nullable final Consumer<? super Throwable> callback) {
        RestActionImpl.setDefaultFailure(callback);
    }

    @NotNull
    static Consumer<Object> getDefaultSuccess() {
        return RestActionImpl.getDefaultSuccess();
    }

    static void setDefaultSuccess(@Nullable final Consumer<Object> callback) {
        RestActionImpl.setDefaultSuccess(callback);
    }

    static long getDefaultTimeout() {
        return RestActionImpl.getDefaultTimeout();
    }

    static void setDefaultTimeout(long timeout, @NotNull TimeUnit unit) {
        RestActionImpl.setDefaultTimeout(timeout, unit);
    }

    /* Getters & Setters */

    @NotNull
    Jmgur getApi();

    /* Methods */

    default void queue() {
        queue(null, null);
    }

    default void queue(@Nullable Consumer<? super T> success) {
        queue(success, null);
    }

    void queue(@Nullable Consumer<? super T> success, @Nullable Consumer<? super Throwable> failure);

    @Nullable
    T complete();

    @NotNull
    CompletableFuture<T> submit();

    @NotNull
    default RestAction<T> timeout(long timeout, @NotNull TimeUnit unit) {
        Check.notNull(unit, "unit");
        return deadline(timeout <= 0 ? 0 : System.currentTimeMillis() + unit.toMillis(timeout));
    }

    @NotNull
    default RestAction<T> deadline(long timestamp) {
        Check.notNegative(timestamp, "timestamp");
        throw new UnsupportedOperationException();
    }
}
