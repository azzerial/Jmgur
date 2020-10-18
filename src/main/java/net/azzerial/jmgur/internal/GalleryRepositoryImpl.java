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

package net.azzerial.jmgur.internal;

import net.azzerial.jmgur.api.GalleryRepository;
import net.azzerial.jmgur.api.Jmgur;
import net.azzerial.jmgur.api.entities.GalleryAlbum;
import net.azzerial.jmgur.api.entities.GalleryElement;
import net.azzerial.jmgur.api.entities.GalleryImage;
import net.azzerial.jmgur.api.entities.Votes;
import net.azzerial.jmgur.api.entities.dto.GalleryDTO;
import net.azzerial.jmgur.api.entities.dto.GallerySearchDTO;
import net.azzerial.jmgur.api.entities.dto.GalleryShareDTO;
import net.azzerial.jmgur.api.entities.subentities.ReportReason;
import net.azzerial.jmgur.api.entities.subentities.Vote;
import net.azzerial.jmgur.api.requests.restaction.PagedRestAction;
import net.azzerial.jmgur.api.requests.restaction.RestAction;
import net.azzerial.jmgur.api.utils.data.DataArray;
import net.azzerial.jmgur.api.utils.data.DataObject;
import net.azzerial.jmgur.internal.entities.EntityBuilder;
import net.azzerial.jmgur.internal.entities.GalleryDTOImpl;
import net.azzerial.jmgur.internal.entities.GallerySearchDTOImpl;
import net.azzerial.jmgur.internal.entities.GalleryShareDTOImpl;
import net.azzerial.jmgur.internal.requests.Route;
import net.azzerial.jmgur.internal.requests.restaction.PagedRestActionImpl;
import net.azzerial.jmgur.internal.requests.restaction.RestActionImpl;
import net.azzerial.jmgur.internal.utils.Check;
import okhttp3.MultipartBody;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GalleryRepositoryImpl implements GalleryRepository {

    private final Jmgur api;

    /* Constructors */

    public GalleryRepositoryImpl(@NotNull Jmgur api) {
        this.api = api;
    }

    /* Methods */

    @NotNull
    @Override
    public Jmgur getApi() {
        return api;
    }

    /* --- Resources --- */

    @NotNull
    @Override
    public PagedRestAction<List<GalleryElement>> getGallery(@NotNull GalleryDTO dto) {
        Check.notNull(dto, "dto");
        final GalleryDTOImpl impl = (GalleryDTOImpl) dto;
        return new PagedRestActionImpl<>(
            api,
            Route.GalleryEndpoints.GET_GALLERY,
            new String[] {
                impl.getSection().getKey(),
                impl.getSort().getKey(),
                impl.getTimeWindow().getKey(),
                null
            },
            new String[] {
                "showViral", String.valueOf(impl.isViral()),
                "mature", String.valueOf(impl.isMature()),
                "album_previews", String.valueOf(impl.isAlbumPreviews())
            },
            (req, res) -> {
                final EntityBuilder builder = api.getEntityBuilder();
                final DataArray arr = res.getObject().getArray("data");
                final List<GalleryElement> galleryElements = new ArrayList<>();

                for (int i = 0; i < arr.length(); i += 1) {
                    final DataObject galleryElementObj = arr.getObject(i);
                    galleryElements.add(builder.createGalleryElement(galleryElementObj));
                }
                return galleryElements;
            }
        );
    }

    @NotNull
    @Override
    public PagedRestAction<List<GalleryElement>> searchGallery(@NotNull GallerySearchDTO dto) {
        Check.notNull(dto, "dto");
        final GallerySearchDTOImpl impl = (GallerySearchDTOImpl) dto;
        final List<String> queryParams = new LinkedList<String>() {{
            add(impl.getType().getKey());
            add(impl.getQuery());
        }};

        if (impl.getFileType() != null) {
            queryParams.add("q_type");
            queryParams.add(impl.getFileType().getKey());
        }
        if (impl.getImageSize() != null) {
            queryParams.add("q_size_px");
            queryParams.add(impl.getImageSize().getKey());
        }

        return new PagedRestActionImpl<>(
            api,
            Route.GalleryEndpoints.GET_GALLERY_SEARCH,
            new String[] {
                impl.getSort().getKey(),
                impl.getTimeWindow().getKey(),
                null
            },
            queryParams.toArray(new String[]{}),
            (req, res) -> {
                final EntityBuilder builder = api.getEntityBuilder();
                final DataArray arr = res.getObject().getArray("data");
                final List<GalleryElement> galleryElements = new ArrayList<>();

                for (int i = 0; i < arr.length(); i += 1) {
                    final DataObject galleryElementObj = arr.getObject(i);
                    galleryElements.add(builder.createGalleryElement(galleryElementObj));
                }
                return galleryElements;
            }
        );
    }

    @NotNull
    @Override
    public RestAction<GalleryAlbum> getGalleryAlbum(@NotNull String hash) {
        Check.notBlank(hash, "hash");
        return new RestActionImpl<>(
            api,
            Route.GalleryEndpoints.GET_GALLERY_ALBUM.compile(hash),
            (req, res) -> {
                final EntityBuilder builder = api.getEntityBuilder();
                final DataObject obj = res.getObject().getObject("data");
                return builder.createGalleryAlbum(obj);
            }
        );
    }

    @NotNull
    @Override
    public RestAction<GalleryImage> getGalleryImage(@NotNull String hash) {
        Check.notBlank(hash, "hash");
        return new RestActionImpl<>(
            api,
            Route.GalleryEndpoints.GET_GALLERY_IMAGE.compile(hash),
            (req, res) -> {
                final EntityBuilder builder = api.getEntityBuilder();
                final DataObject obj = res.getObject().getObject("data");
                return builder.createGalleryImage(obj);
            }
        );
    }

    /* Sharing */

    @NotNull
    @Override
    public RestAction<Boolean> shareImage(@NotNull String hash, @NotNull GalleryShareDTO dto) {
        Check.notBlank(hash, "hash");
        Check.notNull(dto, "dto");
        final GalleryShareDTOImpl impl = (GalleryShareDTOImpl) dto;
        final MultipartBody.Builder body = new MultipartBody.Builder().setType(MultipartBody.FORM);

        if (!impl.getMap().containsKey("title"))
            throw new IllegalArgumentException("no title provided");

        impl.getMap().forEach(body::addFormDataPart);

        return new RestActionImpl<>(
            api,
            Route.GalleryEndpoints.POST_SHARE_IMAGE.compile(hash),
            impl.isEmpty() ? null : body.build(),
            (req, res) -> {
                final DataObject obj = res.getObject();
                return obj.getBoolean("data");
            }
        );
    }

    @NotNull
    @Override
    public RestAction<Boolean> shareAlbum(@NotNull String hash, @NotNull GalleryShareDTO dto) {
        Check.notBlank(hash, "hash");
        Check.notNull(dto, "dto");
        final GalleryShareDTOImpl impl = (GalleryShareDTOImpl) dto;
        final MultipartBody.Builder body = new MultipartBody.Builder().setType(MultipartBody.FORM);

        if (!impl.getMap().containsKey("title"))
            throw new IllegalArgumentException("no title provided");

        impl.getMap().forEach(body::addFormDataPart);

        return new RestActionImpl<>(
            api,
            Route.GalleryEndpoints.POST_SHARE_ALBUM.compile(hash),
            impl.isEmpty() ? null : body.build(),
            (req, res) -> {
                final DataObject obj = res.getObject();
                return obj.getBoolean("data");
            }
        );
    }

    @NotNull
    @Override
    public RestAction<Boolean> removeFromGallery(@NotNull String hash) {
        Check.notBlank(hash, "hash");
        return new RestActionImpl<>(
            api,
            Route.GalleryEndpoints.DELETE_FROM_GALLERY.compile(hash),
            (req, res) -> {
                final DataObject obj = res.getObject();
                return obj.getBoolean("data");
            }
        );
    }

    /* Actions */

    @NotNull
    @Override
    public RestAction<Boolean> reportGalleryElement(@NotNull String hash, @Nullable ReportReason reason) {
        Check.notBlank(hash, "hash");
        Check.check(reason != ReportReason.UNKNOWN, "reason must not be UNKNOWN");
        final MultipartBody.Builder body = new MultipartBody.Builder().setType(MultipartBody.FORM);

        if (reason != null)
            body.addFormDataPart("reason", Integer.toUnsignedString(reason.getValue()));

        return new RestActionImpl<>(
            api,
            Route.GalleryEndpoints.POST_IMAGE_REPORTING.compile(hash),
            reason == null ? null : body.build(),
            (req, res) -> {
                final DataObject obj = res.getObject();
                return obj.getBoolean("data");
            }
        );
    }

    @NotNull
    @Override
    public RestAction<Votes> getGalleryElementVotes(@NotNull String hash) {
        Check.notBlank(hash, "hash");
        return new RestActionImpl<>(
            api,
            Route.GalleryEndpoints.GET_GALLERY_ELEMENT_VOTES.compile(hash),
            (req, res) -> {
                final EntityBuilder builder = api.getEntityBuilder();
                final DataObject obj = res.getObject().getObject("data");
                return builder.createVotes(obj);
            }
        );
    }

    @NotNull
    @Override
    public RestAction<Boolean> voteForGalleryElement(@NotNull String hash, @NotNull Vote vote) {
        Check.notBlank(hash, "hash");
        Check.notNull(vote, "vote");
        Check.check(vote != Vote.UNKNOWN, "vote must not be UNKNOWN");
        return new RestActionImpl<>(
            api,
            Route.GalleryEndpoints.POST_GALLERY_ELEMENT_VOTE.compile(hash, vote.getKey()),
            (req, res) -> {
                final DataObject obj = res.getObject();
                return obj.getBoolean("data");
            }
        );
    }
}
