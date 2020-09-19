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

import net.azzerial.jmgur.api.AccountRepository;
import net.azzerial.jmgur.api.Jmgur;
import net.azzerial.jmgur.api.entities.*;
import net.azzerial.jmgur.api.entities.dto.AccountSettingsDTO;
import net.azzerial.jmgur.api.entities.subentities.CommentSort;
import net.azzerial.jmgur.api.entities.subentities.FavoriteSort;
import net.azzerial.jmgur.api.requests.restaction.PagedRestAction;
import net.azzerial.jmgur.api.requests.restaction.RestAction;
import net.azzerial.jmgur.api.utils.data.DataArray;
import net.azzerial.jmgur.api.utils.data.DataObject;
import net.azzerial.jmgur.api.utils.data.DataType;
import net.azzerial.jmgur.internal.entities.AccountSettingsDTOImpl;
import net.azzerial.jmgur.internal.entities.EntityBuilder;
import net.azzerial.jmgur.internal.requests.Route;
import net.azzerial.jmgur.internal.requests.restaction.PagedRestActionImpl;
import net.azzerial.jmgur.internal.requests.restaction.RestActionImpl;
import net.azzerial.jmgur.internal.utils.Check;
import net.azzerial.jmgur.internal.utils.IOUtil;
import okhttp3.MultipartBody;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class AccountRepositoryImpl implements AccountRepository {

    private final Jmgur api;

    /* Constructors */

    public AccountRepositoryImpl(@NotNull Jmgur api) {
        this.api = api;
    }

    /* Methods */

    @NotNull
    @Override
    public Jmgur getApi() {
        return api;
    }

    /* --- Profiles --- */

    @NotNull
    @Override
    public RestAction<Account> getUserAccount(@NotNull String name) {
        Check.notBlank(name, "name");
        return new RestActionImpl<>(
            api,
            Route.AccountEndpoint.GET_USER_PROFILE.compile(name),
            (req, res) -> {
                final EntityBuilder builder = api.getEntityBuilder();
                final DataObject obj = res.getObject().getObject("data");
                return builder.createAccount(obj);
            }
        );
    }

    @NotNull
    @Override
    public RestAction<GalleryProfile> getUserGalleryProfile(@NotNull String name) {
        Check.notBlank(name, "name");
        return new RestActionImpl<>(
            api,
            Route.AccountEndpoint.GET_USER_GALLERY_PROFILE.compile(name),
            (req, res) -> {
                final EntityBuilder builder = api.getEntityBuilder();
                final DataObject obj = res.getObject().getObject("data");
                return builder.createGalleryProfile(obj);
            }
        );
    }

    /* --- Blocks --- */

    @NotNull
    @Override
    public RestAction<Boolean> isUserBlocked(@NotNull String name) {
        Check.notBlank(name, "name");
        return new RestActionImpl<>(
            api,
            Route.AccountEndpoint.GET_SELF_BLOCK_STATUS.compile(name),
            (req, res) -> {
                final DataObject obj = res.getObject().getObject("data");
                return obj.getBoolean("blocked");
            }
        );
    }

    @NotNull
    @Override
    public RestAction<List<String>> getBlockedUserNames() {
        return getBlockedUsersNames(null);
    }

    @NotNull
    @Override
    public RestAction<Boolean> blockUser(@NotNull String name) {
        Check.notBlank(name, "name");
        return new RestActionImpl<>(
            api,
            Route.AccountEndpoint.POST_SELF_BLOCK_CREATE.compile(name),
            (req, res) -> res.isOk()
        );
    }

    @NotNull
    @Override
    public RestAction<Boolean> unblockUser(@NotNull String name) {
        Check.notBlank(name, "name");
        return new RestActionImpl<>(
            api,
            Route.AccountEndpoint.DELETE_SELF_BLOCK_CREATE.compile(name),
            (req, res) -> res.isOk()
        );
    }

    /* --- Resources --- */

    @NotNull
    @Override
    public PagedRestAction<List<GalleryElement>> getUserGalleryFavorites(@NotNull String name, @NotNull FavoriteSort sort) {
        Check.notBlank(name, "name");
        Check.notNull(sort, "sort");
        Check.check(sort != FavoriteSort.UNKNOWN, "sort must not be UNKNOWN");
        return new PagedRestActionImpl<>(
            api,
            Route.AccountEndpoint.GET_USER_GALLERY_FAVORITES,
            new String[] {name, null, sort.getKey()},
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
    public PagedRestAction<List<GalleryElement>> getUserFavorites(@NotNull String name, @NotNull FavoriteSort sort) {
        Check.notBlank(name, "name");
        Check.notNull(sort, "sort");
        Check.check(sort != FavoriteSort.UNKNOWN, "sort must not be UNKNOWN");
        return new PagedRestActionImpl<>(
            api,
            Route.AccountEndpoint.GET_USER_FAVORITES,
            new String[] {name, null, sort.getKey()},
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
    public PagedRestAction<List<GalleryElement>> getUserSubmissions(@NotNull String name) {
        Check.notBlank(name, "name");
        return new PagedRestActionImpl<>(
            api,
            Route.AccountEndpoint.GET_USER_SUBMISSIONS,
            new String[] {name, null},
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

    /* --- Avatars --- */

    @NotNull
    @Override
    public RestAction<List<Avatar>> getUserAvailableAvatars(@NotNull String name) {
        Check.notBlank(name, "name");
        return new RestActionImpl<>(
            api,
            Route.AccountEndpoint.GET_USER_AVAILABLE_AVATARS.compile(name),
            (req, res) -> {
                final EntityBuilder builder = api.getEntityBuilder();
                final DataObject obj = res.getObject().getObject("data");
                final DataArray avatarArr = obj.getArray("available_avatars");
                final List<Avatar> avatars = new ArrayList<>();

                for (int i = 0; i < avatarArr.length(); i += 1) {
                    final DataObject avatarObj = avatarArr.getObject(i);
                    avatars.add(builder.createAvatar(
                        avatarObj.getString("name"),
                        avatarObj.getString("location")
                    ));
                }
                return avatars;
            }
        );
    }

    @NotNull
    @Override
    public RestAction<Avatar> getUserAvatar(@NotNull String name) {
        Check.notBlank(name, "name");
        return new RestActionImpl<>(
            api,
            Route.AccountEndpoint.GET_USER_AVATAR.compile(name),
            (req, res) -> {
                final EntityBuilder builder = api.getEntityBuilder();
                final DataObject obj = res.getObject().getObject("data");
                return builder.createAvatar(
                    obj.getString("avatar_name"),
                    obj.getString("avatar")
                );
            }
        );
    }

    /* --- Settings --- */

    @NotNull
    @Override
    public RestAction<AccountSettings> getSelfAccountSettings() {
        return new RestActionImpl<>(
            api,
            Route.AccountEndpoint.GET_SELF_SETTINGS.compile(),
            (req, res) -> {
                final EntityBuilder builder = api.getEntityBuilder();
                final DataObject obj = res.getObject().getObject("data");
                return builder.createAccountSettings(obj);
            }
        );
    }

    @NotNull
    @Override
    public RestAction<Boolean> updateSelfAccountSettings(@NotNull AccountSettingsDTO settings) {
        final AccountSettingsDTOImpl dto = (AccountSettingsDTOImpl) settings;
        final MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        dto.getMap().forEach(builder::addFormDataPart);

        return new RestActionImpl<>(
            api,
            Route.AccountEndpoint.POST_SELF_SETTINGS.compile(),
            dto.getMap().isEmpty() ? null : builder.build(),
            (req, res) -> {
                final DataObject obj = res.getObject();
                return obj.getBoolean("data");
            }
        );
    }

    /* --- Albums --- */

    @NotNull
    @Override
    public PagedRestAction<List<Album>> getUserAlbums(@NotNull String name) {
        Check.notBlank(name, "name");
        return new PagedRestActionImpl<>(
            api,
            Route.AccountEndpoint.GET_USER_ALBUMS,
            new String[] {name, null},
            (req, res) -> {
                final EntityBuilder builder = api.getEntityBuilder();
                final DataArray arr = res.getObject().getArray("data");
                final List<Album> albums = new ArrayList<>();

                for (int i = 0; i < arr.length(); i += 1) {
                    final DataObject albumObj = arr.getObject(i);
                    albums.add(builder.createAlbum(albumObj));
                }
                return albums;
            }
        );
    }

    @NotNull
    @Override
    public RestAction<Album> getUserAlbum(@NotNull String name, @NotNull String hash) {
        Check.notBlank(name, "name");
        Check.notBlank(hash, "hash");
        return new RestActionImpl<>(
            api,
            Route.AccountEndpoint.GET_USER_ALBUM.compile(name, hash),
            (req, res) -> {
                final EntityBuilder builder = api.getEntityBuilder();
                final DataObject obj = res.getObject().getObject("data");
                return builder.createAlbum(obj);
            }
        );
    }

    @NotNull
    @Override
    public PagedRestAction<List<String>> getUserAlbumIds(@NotNull String name) {
        Check.notBlank(name, "name");
        return new PagedRestActionImpl<>(
            api,
            Route.AccountEndpoint.GET_USER_ALBUM_IDS,
            new String[] {name, null},
            (req, res) -> {
                final DataArray arr = res.getObject().getArray("data");
                final List<String> albumIds = new ArrayList<>();

                for (int i = 0; i < arr.length(); i += 1) {
                    albumIds.add(arr.getString(i));
                }
                return albumIds;
            }
        );
    }

    @NotNull
    @Override
    public RestAction<Integer> getUserAlbumCount(@NotNull String name) {
        Check.notBlank(name, "name");
        return new RestActionImpl<>(
            api,
            Route.AccountEndpoint.GET_USER_ALBUM_COUNT.compile(name),
            (req, res) -> {
                final DataObject obj = res.getObject();
                return obj.getUnsignedInt("data");
            }
        );
    }

    @NotNull
    @Override
    public RestAction<Boolean> deleteUserAlbum(@NotNull String name, @NotNull String deleteHash) {
        Check.notBlank(name, "name");
        Check.notBlank(deleteHash, "deleteHash");
        return new RestActionImpl<>(
            api,
            Route.AccountEndpoint.DELETE_USER_ALBUM.compile(name, deleteHash),
            (req, res) -> {
                final DataObject obj = res.getObject();
                return obj.hasKey("data") && obj.isType("data", DataType.BOOLEAN);
            }
        );
    }

    /* --- Comments --- */

    @NotNull
    @Override
    public PagedRestAction<List<Comment>> getUserComments(@NotNull String name, @NotNull CommentSort sort) {
        Check.notBlank(name, "name");
        Check.notNull(sort, "sort");
        Check.check(sort != CommentSort.UNKNOWN, "sort must not be UNKNOWN");
        return new PagedRestActionImpl<>(
            api,
            Route.AccountEndpoint.GET_USER_COMMENTS,
            new String[] {name, sort.getKey(), null},
            (req, res) -> {
                final EntityBuilder builder = api.getEntityBuilder();
                final DataArray arr = res.getObject().getArray("data");
                final List<Comment> comments = new ArrayList<>();

                for (int i = 0; i < arr.length(); i += 1) {
                    final DataObject commentObj = arr.getObject(i);
                    comments.add(builder.createComment(commentObj));
                }
                return comments;
            }
        );
    }

    @NotNull
    @Override
    public RestAction<Comment> getUserComment(@NotNull String name, long id) {
        Check.notBlank(name, "name");
        Check.positive(id, "id");
        return new RestActionImpl<>(
            api,
            Route.AccountEndpoint.GET_USER_COMMENT.compile(name, Long.toUnsignedString(id)),
            (req, res) -> {
                final EntityBuilder builder = api.getEntityBuilder();
                final DataObject obj = res.getObject().getObject("data");
                return builder.createComment(obj);
            }
        );
    }

    @NotNull
    @Override
    public PagedRestAction<List<Long>> getUserCommentIds(@NotNull String name, @NotNull CommentSort sort) {
        Check.notBlank(name, "name");
        Check.notNull(sort, "sort");
        Check.check(sort != CommentSort.UNKNOWN, "sort must not be UNKNOWN");
        return new PagedRestActionImpl<>(
            api,
            Route.AccountEndpoint.GET_USER_COMMENT_IDS,
            new String[] {name, sort.getKey(), null},
            (req, res) -> {
                final DataArray arr = res.getObject().getArray("data");
                final List<Long> commentIds = new ArrayList<>();

                for (int i = 0; i < arr.length(); i += 1) {
                    commentIds.add(arr.getUnsignedLong(i));
                }
                return commentIds;
            }
        );
    }

    @NotNull
    @Override
    public RestAction<Integer> getUserCommentCount(@NotNull String name) {
        Check.notBlank(name, "name");
        return new RestActionImpl<>(
            api,
            Route.AccountEndpoint.GET_USER_COMMENT_COUNT.compile(name),
            (req, res) -> {
                final DataObject obj = res.getObject();
                return obj.getUnsignedInt("data");
            }
        );
    }

    @NotNull
    @Override
    public RestAction<Boolean> deleteSelfComment(long id) {
        Check.positive(id, "id");
        return new RestActionImpl<>(
            api,
            Route.AccountEndpoint.DELETE_SELF_COMMENT.compile("me", Long.toUnsignedString(id)),
            (req, res) -> {
                final DataObject obj = res.getObject();
                return obj.hasKey("data") && obj.isType("data", DataType.BOOLEAN);
            }
        );
    }

    /* --- Images --- */

    @NotNull
    @Override
    public PagedRestAction<List<Image>> getSelfImages() {
        return new PagedRestActionImpl<>(
            api,
            Route.AccountEndpoint.GET_SELF_IMAGES,
            new String[] {"me", null},
            (req, res) -> {
                final EntityBuilder builder = api.getEntityBuilder();
                final DataArray arr = res.getObject().getArray("data");
                final List<Image> images = new ArrayList<>();

                for (int i = 0; i < arr.length(); i += 1) {
                    final DataObject imageObj = arr.getObject(i);
                    images.add(builder.createImage(imageObj));
                }
                return images;
            }
        );
    }

    @NotNull
    @Override
    public RestAction<Image> getUserImage(@NotNull String name, @NotNull String hash) {
        Check.notBlank(name, "name");
        Check.notBlank(hash, "hash");
        return new RestActionImpl<>(
            api,
            Route.AccountEndpoint.GET_USER_IMAGE.compile(name, hash),
            (req, res) -> {
                final EntityBuilder builder = api.getEntityBuilder();
                final DataObject obj = res.getObject().getObject("data");
                return builder.createImage(obj);
            }
        );
    }

    @NotNull
    @Override
    public PagedRestAction<List<String>> getUserImageIds(@NotNull String name) {
        Check.notBlank(name, "name");
        return new PagedRestActionImpl<>(
            api,
            Route.AccountEndpoint.GET_USER_IMAGE_IDS,
            new String[] {name, null},
            (req, res) -> {
                final DataArray arr = res.getObject().getArray("data");
                final List<String> imageIds = new ArrayList<>();

                for (int i = 0; i < arr.length(); i += 1) {
                    imageIds.add(arr.getString(i));
                }
                return imageIds;
            }
        );
    }

    @NotNull
    @Override
    public RestAction<Integer> getUserImageCount(@NotNull String name) {
        Check.notBlank(name, "name");
        return new RestActionImpl<>(
            api,
            Route.AccountEndpoint.GET_USER_IMAGE_COUNT.compile(name),
            (req, res) -> {
                final DataObject obj = res.getObject();
                return obj.getUnsignedInt("data");
            }
        );
    }

    @NotNull
    @Override
    public RestAction<Boolean> deleteUserImage(@NotNull String name, @NotNull String deleteHash) {
        Check.notBlank(name, "name");
        Check.notBlank(deleteHash, "deleteHash");
        return new RestActionImpl<>(
            api,
            Route.AccountEndpoint.DELETE_USER_IMAGE.compile(name, deleteHash),
            (req, res) -> {
                final DataObject obj = res.getObject();
                return obj.hasKey("data") && obj.isType("data", DataType.BOOLEAN);
            }
        );
    }

    /* Internal */

    @NotNull
    private RestAction<List<String>> getBlockedUsersNames(String next) {
        Map<String, String> params = Collections.emptyMap();

        if (next != null)
            params = IOUtil.getQueryParams(next);

        return new RestActionImpl<>(
            api,
            !params.containsKey("last_seen_id") ?
                Route.AccountEndpoint.GET_SELF_BLOCKS.compile() :
                Route.AccountEndpoint.GET_SELF_BLOCKS.compile().addQueryParams("last_seen_id", params.get("last_seen_id"))
            ,
            (req, res) -> {
                final DataObject obj = res.getObject().getObject("data");
                final DataArray nameArr = obj.getArray("items");
                final List<String> names = new ArrayList<>();

                for (int i = 0; i < nameArr.length(); i += 1) {
                    final DataObject nameObj = nameArr.getObject(i);
                    names.add(nameObj.getString("url"));
                }
                if (!obj.isNull("next"))
                    names.addAll(Objects.requireNonNull(getBlockedUsersNames(obj.getString("next")).complete()));
                return names;
            }
        );
    }
}
