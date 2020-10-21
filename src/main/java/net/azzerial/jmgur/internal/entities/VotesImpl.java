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

package net.azzerial.jmgur.internal.entities;

import lombok.Setter;
import net.azzerial.jmgur.api.Jmgur;
import net.azzerial.jmgur.api.entities.Votes;
import org.jetbrains.annotations.NotNull;

@Setter
public final class VotesImpl implements Votes {

    private final transient Jmgur api;

    private int ups;
    private int downs;

    /* Constructors */

    VotesImpl(@NotNull Jmgur api) {
        this.api = api;
    }

    /* Getters & Setters */

    @NotNull
    @Override
    public Jmgur getApi() {
        return api;
    }

    @Override
    public int getUps() {
        return ups;
    }

    @Override
    public int getDowns() {
        return downs;
    }

    /* Methods */

    @Override
    public String toString() {
        return "Votes{" +
            "ups=" + ups +
            ", downs=" + downs +
            '}';
    }
}