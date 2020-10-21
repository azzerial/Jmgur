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
import net.azzerial.jmgur.api.entities.Trophy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.OffsetDateTime;

import static net.azzerial.jmgur.internal.utils.Helper.print;

@Setter
public final class TrophyImpl implements Trophy {

    private final transient Jmgur api;

    private long id;
    private String name;
    private String nameClean;
    private String description;
    private String data;
    private String dataLink;
    private OffsetDateTime datetime;
    private String image;
    private int imageWidth;
    private int imageHeight;

    /* Constructors */

    TrophyImpl(@NotNull Jmgur api) {
        this.api = api;
    }

    /* Getters & Setters */

    @NotNull
    @Override
    public Jmgur getApi() {
        return api;
    }

    @Override
    public long getIdLong() {
        return id;
    }

    @NotNull
    @Override
    public String getName() {
        return name;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return nameClean;
    }

    @NotNull
    @Override
    public String getDescription() {
        return description;
    }

    @Nullable
    @Override
    public String getData() {
        return data;
    }

    @Nullable
    @Override
    public String getDataUrl() {
        return dataLink;
    }

    @NotNull
    @Override
    public OffsetDateTime getAchievementDate() {
        return datetime;
    }

    @NotNull
    @Override
    public String getImageUrl() {
        return image;
    }

    @Override
    public int getImageWidth() {
        return imageWidth;
    }

    @Override
    public int getImageHeight() {
        return imageHeight;
    }

    /* Methods */

    @Override
    public String toString() {
        return "Trophy{" +
            "id=" + id +
            ", name=" + print(name) +
            ", displayName=" + print(nameClean) +
            ", description=" + print(description) +
            ", data=" + print(data) +
            ", dataUrl=" + print(dataLink) +
            ", achievementDate=" + datetime +
            ", imageUrl=" + print(image) +
            ", imageWidth=" + imageWidth +
            ", imageHeight=" + imageHeight +
            '}';
    }
}
