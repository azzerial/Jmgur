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
import net.azzerial.jmgur.api.entities.GalleryAlbum;
import net.azzerial.jmgur.api.entities.GalleryImage;
import net.azzerial.jmgur.api.entities.subentities.AlbumLayout;
import net.azzerial.jmgur.api.entities.subentities.AlbumPrivacy;
import net.azzerial.jmgur.api.entities.subentities.Vote;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.OffsetDateTime;
import java.util.List;

import static net.azzerial.jmgur.internal.utils.Helper.print;

@Setter
public final class GalleryAlbumImpl implements GalleryAlbum {

    private final transient Jmgur api;

    private String id;
    private String title;
    private String description;
    private OffsetDateTime datetime;
    private String cover;
    private int coverWidth;
    private int coverHeight;
    private String accountUrl;
    private long accountId;
    private AlbumPrivacy privacy;
    private AlbumLayout layout;
    private int views;
    private String link;
    private int ups;
    private int downs;
    private int points;
    private int score;
    private boolean isAlbum;
    private Vote vote;
    private boolean favorite;
    private boolean nsfw;
    private String section;
    private int commentCount;
    private int favoriteCount;
    private int imagesCount;
    private boolean inGallery;
    private boolean inMostViral;
    private List<GalleryImage> images;

    /* Constructors */

    GalleryAlbumImpl(@NotNull Jmgur api) {
        this.api = api;
    }

    /* Getters & Setters */

    @NotNull
    @Override
    public Jmgur getApi() {
        return api;
    }

    @NotNull
    @Override
    public String getHash() {
        return id;
    }

    @Nullable
    @Override
    public String getTitle() {
        return title;
    }

    @Nullable
    @Override
    public String getDescription() {
        return description;
    }

    @NotNull
    @Override
    public OffsetDateTime getCreationDate() {
        return datetime;
    }

    @NotNull
    @Override
    public String getCoverHash() {
        return cover;
    }

    @Override
    public int getCoverWidth() {
        return coverWidth;
    }

    @Override
    public int getCoverHeight() {
        return coverHeight;
    }

    @Nullable
    @Override
    public String getAuthorName() {
        return accountUrl;
    }

    @Override
    public long getAuthorIdLong() {
        return accountId;
    }

    @NotNull
    @Override
    public AlbumPrivacy getPrivacy() {
        return privacy;
    }

    @NotNull
    @Override
    public AlbumLayout getLayout() {
        return layout;
    }

    @Override
    public int getViews() {
        return views;
    }

    @NotNull
    @Override
    public String getUrl() {
        return link;
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

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public boolean isAlbum() {
        return isAlbum;
    }

    @Nullable
    @Override
    public Vote getVote() {
        return vote;
    }

    @Override
    public boolean isFavorite() {
        return favorite;
    }

    @Override
    public boolean isNSFW() {
        return nsfw;
    }

    @Nullable
    @Override
    public String getSection() {
        return section;
    }

    @Override
    public int getCommentCount() {
        return commentCount;
    }

    @Override
    public int getFavoriteCount() {
        return favoriteCount;
    }

    @Override
    public int getSize() {
        return imagesCount;
    }

    @Override
    public boolean isInGallery() {
        return inGallery;
    }

    @Override
    public boolean isInMostViral() {
        return inMostViral;
    }

    @NotNull
    @Override
    public List<GalleryImage> getImages() {
        return images;
    }

    /* Methods */

    @Override
    public String toString() {
        return "GalleryAlbum{" +
            "hash=" + print(id) +
            ", title=" + print(title) +
            ", description=" + print(description) +
            ", creationDate=" + datetime +
            ", coverHash=" + print(cover) +
            ", coverWidth=" + coverWidth +
            ", coverHeight=" + coverHeight +
            ", authorName=" + print(accountUrl) +
            ", authorId=" + accountId +
            ", privacy=" + privacy +
            ", layout=" + layout +
            ", views=" + views +
            ", url=" + print(link) +
            ", ups=" + ups +
            ", downs=" + downs +
            ", points=" + points +
            ", score=" + score +
            ", isAlbum=" + isAlbum +
            ", vote=" + vote +
            ", favorite=" + favorite +
            ", nsfw=" + nsfw +
            ", section=" + print(section) +
            ", commentCount=" + commentCount +
            ", favoriteCount=" + favoriteCount +
            ", size=" + imagesCount +
            ", inGallery=" + inGallery +
            ", inMostViral=" + inMostViral +
            ", images=" + images +
            '}';
    }
}
