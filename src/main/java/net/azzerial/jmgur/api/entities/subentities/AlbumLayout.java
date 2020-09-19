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

package net.azzerial.jmgur.api.entities.subentities;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum AlbumLayout {
    BLOG("blog"),
    HORIZONTAL("horizontal"),
    UNKNOWN("unknown");

    private final String key;

    /* Constructors */

    AlbumLayout(@NotNull String key) {
        this.key = key;
    }

    /* Getters & Setters */

    @NotNull
    public String getKey() {
        return key;
    }

    /* Static Methods */

    @Nullable
    public static AlbumLayout fromKey(@Nullable String key) {
        if (key == null || key.isEmpty())
            return null;
        for (AlbumLayout al : values()) {
            if (al.key.equalsIgnoreCase(key))
                return al;
        }
        return UNKNOWN;
    }
}
