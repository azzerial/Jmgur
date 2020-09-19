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
import net.azzerial.jmgur.api.entities.subentities.Vote;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.OffsetDateTime;

public interface GalleryElement {

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

    @Nullable
    String getAuthorName();

    @Nullable
    default String getAuthorId() {
        return getAuthorIdLong() == 0 ? null : Long.toUnsignedString(getAuthorIdLong());
    }

    long getAuthorIdLong();

    int getViews();

    @NotNull
    String getUrl();

    int getUps();

    int getDowns();

    int getPoints();

    int getScore();

    @Nullable
    Vote getVote();

    boolean isFavorite();

    boolean isNSFW();

    @Nullable
    String getSection();

    boolean isInGallery();

    boolean isInMostViral();

    boolean isAlbum();
}
