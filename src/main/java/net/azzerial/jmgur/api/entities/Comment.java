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

public interface Comment {

    @NotNull
    Jmgur getApi();

    @NotNull
    default String getId() {
        return Long.toUnsignedString(getIdLong());
    }

    long getIdLong();

    @NotNull
    String getPostHash();

    @NotNull
    String getContent();

    @NotNull
    String getAuthorName();

    @NotNull
    default String getAuthorId() {
        return Long.toUnsignedString(getAuthorIdLong());
    }

    long getAuthorIdLong();

    boolean isAlbumComment();

    @Nullable
    String getAlbumCoverHash();

    int getUps();

    int getDowns();

    int getPoints();

    @NotNull
    OffsetDateTime getCreationDate();

    @Nullable
    default String getParentId() {
        return getParentIdLong() == 0 ? null : Long.toUnsignedString(getParentIdLong());
    }

    long getParentIdLong();

    boolean isDeleted();

    @Nullable
    Vote getVote();
}
