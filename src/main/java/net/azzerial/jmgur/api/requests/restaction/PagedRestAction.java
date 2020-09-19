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

package net.azzerial.jmgur.api.requests.restaction;

import net.azzerial.jmgur.api.Jmgur;
import org.jetbrains.annotations.NotNull;

public interface PagedRestAction<T> {

    /* Getters & Setters*/

    @NotNull
    Jmgur getApi();

    @NotNull
    RestAction<T> get();

    @NotNull
    RestAction<T> get(int page);

    @NotNull
    RestAction<T> next();

    int page();

    /* Methods */

    @NotNull
    PagedRestAction<T> reset();

    @NotNull
    PagedRestAction<T> skip(int pages);

    @NotNull
    PagedRestAction<T> skipTo(int page);
}
