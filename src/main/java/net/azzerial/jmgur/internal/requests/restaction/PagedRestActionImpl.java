/*
 * Copyright 2020 Robin Mercier
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.azzerial.jmgur.internal.requests.restaction;

import net.azzerial.jmgur.api.Jmgur;
import net.azzerial.jmgur.api.requests.restaction.PagedRestAction;
import net.azzerial.jmgur.api.requests.Request;
import net.azzerial.jmgur.api.requests.Response;
import net.azzerial.jmgur.api.requests.restaction.RestAction;
import net.azzerial.jmgur.internal.requests.Route;
import net.azzerial.jmgur.internal.utils.Check;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

public final class PagedRestActionImpl<T> implements PagedRestAction<T> {

    private final Jmgur api;
    private final Map<Integer, RestAction<T>> map;
    private final Route route;
    private final String[] params;
    private final BiFunction<Request<T>, Response, T> handler;

    private int page;

    /* Constructors */

    public PagedRestActionImpl(@NotNull Jmgur api, @NotNull Route route, @Nullable String[] params, @NotNull BiFunction<Request<T>, Response, T> handler) {
        this(api, route, params, handler, 0);
    }

    public PagedRestActionImpl(@NotNull Jmgur api, @NotNull Route route, @Nullable String[] params, @NotNull BiFunction<Request<T>, Response, T> handler, int page) {
        Check.notNull(api, "api");
        Check.notNull(route, "route");
        Check.notNull(handler, "handler");
        Check.notNegative(page, "page");
        this.api = api;
        this.map = new ConcurrentHashMap<>();
        this.route = route;
        this.params = params;
        this.handler = handler;
        this.page = page;
    }

    /* Getters & Setters */

    @NotNull
    @Override
    public Jmgur getApi() {
        return api;
    }

    /* Methods */

    @NotNull
    @Override
    public RestAction<T> get() {
        buildPage(page);
        return map.get(page);
    }

    @NotNull
    @Override
    public RestAction<T> get(int page) {
        Check.notNegative(page, "page");
        buildPage(page);
        return map.get(page);
    }

    @NotNull
    @Override
    public RestAction<T> next() {
        buildPage(page);
        return map.get(page++);
    }

    @Override
    public int page() {
        return page;
    }

    @NotNull
    @Override
    public PagedRestAction<T> reset() {
        this.page = 0;
        return this;
    }

    @NotNull
    @Override
    public PagedRestAction<T> skip(int pages) {
        Check.notNegative(pages, "pages");
        this.page += pages;
        return this;
    }

    @NotNull
    @Override
    public PagedRestAction<T> skipTo(int page) {
        Check.notNegative(page, "page");
        this.page = page;
        return this;
    }

    /* Internal */

    private void buildPage(int page) {
        if (!map.containsKey(page))
            map.put(page, buildRestAction(page));
    }

    @NotNull
    private RestAction<T> buildRestAction(int page) {
        return new RestActionImpl<>(api, route.compile(buildParams(page)), handler);
    }

    @NotNull
    private String[] buildParams(int page) {
        final String[] params = this.params.clone();

        if (params == null)
            return new String[0];
        for (int i = 0; i < params.length; i += 1) {
            if (params[i] == null)
                params[i] = Integer.toUnsignedString(page);
        }
        return params;
    }
}
