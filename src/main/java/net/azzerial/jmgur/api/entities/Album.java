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
import net.azzerial.jmgur.api.entities.subentities.AlbumLayout;
import net.azzerial.jmgur.api.entities.subentities.AlbumPrivacy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;

public interface Album extends Serializable {

    @NotNull
    Jmgur getApi();

    @NotNull
    String getHash();

    @Nullable
    String getTitle();

    @Nullable
    String getDescription();

    @NotNull
    OffsetDateTime getCreationDate();

    @NotNull
    String getCoverHash();

    default boolean isCoverEdited() {
        return getCoverEditCount() > 0;
    }

    int getCoverEditCount();

    int getCoverWidth();

    int getCoverHeight();

    @Nullable
    String getAuthorName();

    @Nullable
    default String getAuthorId() {
        return getAuthorIdLong() == 0 ? null : Long.toUnsignedString(getAuthorIdLong());
    }

    long getAuthorIdLong();

    @Nullable
    AlbumPrivacy getPrivacy();

    @Nullable
    AlbumLayout getLayout();

    int getViews();

    @NotNull
    String getUrl();

    boolean isFavorite();

    boolean isNSFW();

    @Nullable
    String getSection();

    int getSize();

    boolean isInGallery();

    boolean isAlbum();

    @Nullable
    String getDeleteHash();

    @NotNull
    List<Image> getImages();
}
