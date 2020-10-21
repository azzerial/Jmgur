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

package net.azzerial.jmgur.api.entities;

import net.azzerial.jmgur.api.Jmgur;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.time.OffsetDateTime;

public interface Trophy extends Serializable {

    @NotNull
    Jmgur getApi();

    @NotNull
    default String getId() {
        return Long.toUnsignedString(getIdLong());
    }

    long getIdLong();

    @NotNull
    String getName();

    @NotNull
    String getDisplayName();

    @NotNull
    String getDescription();

    @Nullable
    String getData();

    @Nullable
    String getDataUrl();

    @Nullable
    default String getDataFullUrl() {
        return getDataUrl() == null ? null : String.format("https://imgur.com/%s", getDataUrl());
    }

    @NotNull
    OffsetDateTime getAchievementDate();

    @NotNull
    String getImageUrl();

    int getImageWidth();

    int getImageHeight();
}
