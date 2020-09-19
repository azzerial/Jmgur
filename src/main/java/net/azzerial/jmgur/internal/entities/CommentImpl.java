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
import net.azzerial.jmgur.api.entities.Comment;
import net.azzerial.jmgur.api.entities.subentities.Vote;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.OffsetDateTime;

import static net.azzerial.jmgur.internal.utils.Helper.print;

@Setter
public final class CommentImpl implements Comment {

    private final Jmgur api;

    private long id;
    private String imageId;
    private String comment;
    private String author;
    private long authorId;
    private boolean onAlbum;
    private String albumCover;
    private int ups;
    private int downs;
    private int points;
    private OffsetDateTime datetime;
    private long parentId;
    private boolean deleted;
    private Vote vote;

    /* Constructors */

    CommentImpl(@NotNull Jmgur api) {
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
    public String getPostHash() {
        return imageId;
    }

    @NotNull
    @Override
    public String getContent() {
        return comment;
    }

    @NotNull
    @Override
    public String getAuthorName() {
        return author;
    }

    @Override
    public long getAuthorIdLong() {
        return authorId;
    }

    @Override
    public boolean isAlbumComment() {
        return onAlbum;
    }

    @Nullable
    @Override
    public String getAlbumCoverHash() {
        return albumCover;
    }

    @Override
    public int getUps() {
        return ups;
    }

    @Override
    public int getDowns() {
        return downs;
    }

    @Override
    public int getPoints() {
        return points;
    }

    @NotNull
    @Override
    public OffsetDateTime getCreationDate() {
        return datetime;
    }

    @Override
    public long getParentIdLong() {
        return parentId;
    }

    @Override
    public boolean isDeleted() {
        return deleted;
    }

    @Nullable
    @Override
    public Vote getVote() {
        return vote;
    }

    /* Methods */

    @Override
    public String toString() {
        return "Comment{" +
            "id=" + id +
            ", postId=" + print(imageId) +
            ", content=" + print(comment) +
            ", authorName=" + print(author) +
            ", authorId=" + authorId +
            ", albumComment=" + onAlbum +
            ", albumCoverHash=" + print(albumCover) +
            ", ups=" + ups +
            ", downs=" + downs +
            ", points=" + points +
            ", creationDate=" + datetime +
            ", parentId=" + parentId +
            ", deleted=" + deleted +
            ", vote=" + vote +
            '}';
    }
}
