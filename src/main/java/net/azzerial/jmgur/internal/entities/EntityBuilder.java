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

import net.azzerial.jmgur.api.Jmgur;
import net.azzerial.jmgur.api.entities.*;
import net.azzerial.jmgur.api.entities.dto.ImageInformationDTO;
import net.azzerial.jmgur.api.entities.subentities.AlbumLayout;
import net.azzerial.jmgur.api.entities.subentities.AlbumPrivacy;
import net.azzerial.jmgur.api.entities.subentities.ImagePrivacy;
import net.azzerial.jmgur.api.entities.subentities.Vote;
import net.azzerial.jmgur.api.utils.data.DataArray;
import net.azzerial.jmgur.api.utils.data.DataObject;
import net.azzerial.jmgur.api.utils.data.DataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static net.azzerial.jmgur.internal.utils.Helper.fromEpochSecond;

public final class EntityBuilder {

    private final Jmgur api;

    /* Constructors */

    public EntityBuilder(@NotNull Jmgur api) {
        this.api = api;
    }

    /* Getters & Setters */

    public Jmgur getApi() {
        return api;
    }

    /* Static Methods */

    public static AccountSettingsDTOImpl createAccountSettingsDTO() {
        return new AccountSettingsDTOImpl();
    }

    public static ImageInformationDTO createImageInformationDTO() {
        return new ImageInformationDTOImpl();
    }

    public static ImageUploadDTOImpl createImageUploadDTO() {
        return new ImageUploadDTOImpl();
    }

    /* Methods */

    public Avatar createAvatar(@NotNull String name, @NotNull String url) {
        final AvatarImpl avatar = new AvatarImpl(api);

        avatar.setName(name);
        avatar.setUrl(url);

        return avatar;
    }

    public Account createAccount(@NotNull DataObject obj) {
        final AccountImpl account = new AccountImpl(api);

        Avatar avatar = createAvatar(
            obj.getString("avatar_name", "default/default"),
            obj.getString("avatar")
        );
        Cover cover = createCover(
            obj.getString("cover_name"),
            obj.getString("cover")
        );

        account.setId(obj.getUnsignedLong("id"));
        account.setUrl(obj.getString("url"));
        account.setBio(obj.getString("bio", null));
        account.setAvatar(avatar);
        account.setCover(cover);
        account.setReputation(obj.getInt("reputation"));
        account.setReputationName(obj.getString("reputation_name"));
        account.setCreated(fromEpochSecond(obj.getUnsignedLong("created")));
        account.setBlocked(obj.getBoolean("is_blocked"));

        return account;
    }

    public AccountSettings createAccountSettings(@NotNull DataObject obj) {
        final AccountSettingsImpl accountSettings = new AccountSettingsImpl(api);

        accountSettings.setAccountUrl(obj.getString("account_url"));
        accountSettings.setEmail(obj.getString("email"));
        accountSettings.setAvatar(obj.getString("avatar"));
        accountSettings.setCover(obj.getString("cover"));
        accountSettings.setPublicImages(ImagePrivacy.fromKey(obj.getBoolean("public_images")));
        accountSettings.setAlbumPrivacy(AlbumPrivacy.fromKey(obj.getString("album_privacy")));
        accountSettings.setAcceptedGalleryTerms(obj.getBoolean("accepted_gallery_terms"));
        accountSettings.setMessagingEnabled(obj.getBoolean("messaging_enabled"));
        accountSettings.setShowMature(obj.getBoolean("show_mature"));
        accountSettings.setNewsletterSubscribed(obj.getBoolean("newsletter_subscribed"));

        return accountSettings;
    }

    public Album createAlbum(@NotNull DataObject obj) {
        final AlbumImpl album = new AlbumImpl(api);

        List<Image> images = new ArrayList<>();
        if (obj.hasKey("images")) {
            DataArray imagesArr = obj.getArray("images");
            for (int i = 0; i < imagesArr.length(); i += 1) {
                DataObject imageObj = imagesArr.getObject(i);
                images.add(createImage(imageObj));
            }
        }

        album.setId(obj.getString("id"));
        album.setTitle(obj.getString("title", null));
        album.setDescription(obj.getString("description", null));
        album.setDatetime(fromEpochSecond(obj.getUnsignedLong("datetime")));
        album.setCover(obj.getString("cover"));
        album.setCoverEdited(obj.getUnsignedInt("cover_edited", 0));
        album.setCoverWidth(obj.getUnsignedInt("cover_width", 0));
        album.setCoverHeight(obj.getUnsignedInt("cover_height", 0));
        album.setAccountUrl(obj.getString("account_url", null));
        album.setAccountId(obj.getUnsignedLong("account_id", 0L));
        album.setPrivacy(AlbumPrivacy.fromKey(obj.getString("privacy")));
        album.setLayout(AlbumLayout.fromKey(obj.getString("layout")));
        album.setViews(obj.getUnsignedInt("views"));
        album.setLink(obj.getString("link"));
        album.setFavorite(obj.getBoolean("favorite"));
        album.setNsfw(obj.getBoolean("nsfw"));
        album.setSection(obj.getString("section", null));
        album.setImagesCount(obj.getUnsignedInt("images_count"));
        album.setInGallery(obj.getBoolean("in_gallery"));
        album.setAlbum(obj.getBoolean("is_album"));
        album.setDeleteHash(obj.getString("deletehash", null));
        album.setImages(images);

        return album;
    }

    public Comment createComment(@NotNull DataObject obj) {
        final CommentImpl comment = new CommentImpl(api);

        comment.setId(obj.getUnsignedLong("id"));
        comment.setImageId(obj.getString("image_id"));
        comment.setComment(obj.getString("comment"));
        comment.setAuthor(obj.getString("author"));
        comment.setAuthorId(obj.getUnsignedLong("author_id"));
        comment.setOnAlbum(obj.getBoolean("on_album"));
        comment.setAlbumCover(obj.getString("album_cover", null));
        comment.setUps(obj.getUnsignedInt("ups"));
        comment.setDowns(obj.getUnsignedInt("downs"));
        comment.setPoints(obj.getInt("points"));
        comment.setDatetime(fromEpochSecond(obj.getUnsignedLong("datetime")));
        comment.setParentId(obj.getUnsignedLong("parent_id"));
        comment.setDeleted(obj.getBoolean("deleted"));
        comment.setVote(Vote.fromKey(obj.getString("vote", null)));

        return comment;
    }

    public Cover createCover(@NotNull String name, @NotNull String url) {
        final CoverImpl cover = new CoverImpl(api);

        cover.setName(name);
        cover.setUrl(url);

        return cover;
    }

    public GalleryAlbum createGalleryAlbum(@NotNull DataObject obj) {
        final GalleryAlbumImpl galleryAlbum = new GalleryAlbumImpl(api);

        List<GalleryImage> galleryImages = new ArrayList<>();
        if (!obj.isNull("images")) {
            DataArray galleryImagesArr = obj.getArray("images");
            for (int i = 0; i < galleryImagesArr.length(); i += 1) {
                DataObject galleryImageObj = galleryImagesArr.getObject(i);
                galleryImages.add(createGalleryImage(galleryImageObj));
            }
        }

        galleryAlbum.setId(obj.getString("id"));
        galleryAlbum.setTitle(obj.getString("title", null));
        galleryAlbum.setDescription(obj.getString("description", null));
        galleryAlbum.setDatetime(fromEpochSecond(obj.getUnsignedLong("datetime")));
        galleryAlbum.setCover(obj.getString("cover"));
        galleryAlbum.setCoverWidth(obj.getUnsignedInt("cover_width", 0));
        galleryAlbum.setCoverHeight(obj.getUnsignedInt("cover_height", 0));
        galleryAlbum.setAccountUrl(obj.getString("account_url", null));
        galleryAlbum.setAccountId(obj.getUnsignedLong("account_id", 0L));
        galleryAlbum.setPrivacy(AlbumPrivacy.fromKey(obj.getString("privacy", null)));
        galleryAlbum.setLayout(AlbumLayout.fromKey(obj.getString("layout", null)));
        galleryAlbum.setViews(obj.getUnsignedInt("views"));
        galleryAlbum.setLink(obj.getString("link"));
        galleryAlbum.setUps(obj.getUnsignedInt("ups"));
        galleryAlbum.setDowns(obj.getUnsignedInt("downs"));
        galleryAlbum.setPoints(obj.getUnsignedInt("points"));
        galleryAlbum.setScore(obj.getUnsignedInt("score", 0));
        galleryAlbum.setAlbum(obj.getBoolean("is_album"));
        galleryAlbum.setVote(Vote.fromKey(obj.getString("vote", null)));
        galleryAlbum.setFavorite(obj.getBoolean("favorite"));
        galleryAlbum.setNsfw(obj.getBoolean("nsfw"));
        galleryAlbum.setSection(obj.getString("section", null));
        galleryAlbum.setCommentCount(obj.getUnsignedInt("comment_count"));
        galleryAlbum.setFavoriteCount(obj.getUnsignedInt("favorite_count"));
        galleryAlbum.setImagesCount(obj.getUnsignedInt("images_count"));
        galleryAlbum.setInGallery(obj.getBoolean("in_gallery"));
        galleryAlbum.setInMostViral(obj.isType("in_most_viral", DataType.INT) ? obj.getUnsignedInt("in_most_viral") == 1 : obj.getBoolean("in_most_viral"));
        galleryAlbum.setImages(galleryImages);

        return galleryAlbum;
    }

    public GalleryElement createGalleryElement(@NotNull DataObject obj) {
        return obj.getBoolean("is_album") ?
            createGalleryAlbum(obj) :
            createGalleryImage(obj);
    }

    public GalleryImage createGalleryImage(@NotNull DataObject obj) {
        final GalleryImageImpl galleryImage = new GalleryImageImpl(api);

        galleryImage.setId(obj.getString("id"));
        galleryImage.setTitle(obj.getString("title", null));
        galleryImage.setDescription(obj.getString("description", null));
        galleryImage.setDatetime(fromEpochSecond(obj.getUnsignedLong("datetime")));
        galleryImage.setType(obj.getString("type"));
        galleryImage.setAnimated(obj.getBoolean("animated"));
        galleryImage.setWidth(obj.getUnsignedInt("width"));
        galleryImage.setHeight(obj.getUnsignedInt("height"));
        galleryImage.setSize(obj.getUnsignedInt("size"));
        galleryImage.setViews(obj.getUnsignedInt("views"));
        galleryImage.setBandwidth(obj.getUnsignedInt("bandwidth", 0));
        galleryImage.setVote(Vote.fromKey(obj.getString("vote", null)));
        galleryImage.setFavorite(obj.getBoolean("favorite"));
        galleryImage.setNsfw(obj.getBoolean("nsfw"));
        galleryImage.setSection(obj.getString("section", null));
        galleryImage.setAccountUrl(obj.getString("account_url", null));
        galleryImage.setAccountId(obj.getUnsignedLong("account_id", 0L));
        galleryImage.setInMostViral(obj.isType("in_most_viral", DataType.INT) ? obj.getUnsignedInt("in_most_viral") == 1 : obj.getBoolean("in_most_viral"));
        galleryImage.setHasSound(obj.getBoolean("has_sound"));
        galleryImage.setEdited(obj.getUnsignedInt("edited", 0));
        galleryImage.setInGallery(obj.getBoolean("in_gallery"));
        galleryImage.setLink(obj.getString("link", null));
        galleryImage.setMp4(obj.getString("mp4", null));
        galleryImage.setGifv(obj.getString("gifv", null));
        galleryImage.setHls(obj.getString("hls", null));
        galleryImage.setMp4Size(obj.getUnsignedInt("mp4_size", 0));
        galleryImage.setLooping(obj.getBoolean("looping", false));
        galleryImage.setCommentCount(obj.getUnsignedInt("comment_count", 0));
        galleryImage.setFavoriteCount(obj.getUnsignedInt("favorite_count", 0));
        galleryImage.setUps(obj.getUnsignedInt("ups", 0));
        galleryImage.setDowns(obj.getUnsignedInt("downs", 0));
        galleryImage.setPoints(obj.getUnsignedInt("points", 0));
        galleryImage.setScore(obj.getUnsignedInt("score", 0));
        galleryImage.setAlbum(obj.getBoolean("is_album"));

        return galleryImage;
    }

    public GalleryProfile createGalleryProfile(@NotNull DataObject obj) {
        final GalleryProfileImpl galleryProfile = new GalleryProfileImpl(api);

        DataArray trophiesArr = obj.getArray("trophies");
        List<Trophy> trophies = new ArrayList<>();
        for (int i = 0; i < trophiesArr.length(); i += 1) {
            DataObject trophyObj = trophiesArr.getObject(i);
            trophies.add(createTrophy(trophyObj));
        }

        galleryProfile.setTrophies(trophies);
        galleryProfile.setTotalGalleryComments(obj.getUnsignedInt("total_gallery_comments"));
        galleryProfile.setTotalGalleryFavorites(obj.getUnsignedInt("total_gallery_favorites"));
        galleryProfile.setTotalGallerySubmissions(obj.getUnsignedInt("total_gallery_submissions"));

        return galleryProfile;
    }

    public Image createImage(@NotNull DataObject obj) {
        final ImageImpl image = new ImageImpl(api);

        image.setId(obj.getString("id"));
        image.setTitle(obj.getString("title", null));
        image.setDescription(obj.getString("description", null));
        image.setDatetime(fromEpochSecond(obj.getUnsignedLong("datetime")));
        image.setType(obj.getString("type"));
        image.setAnimated(obj.getBoolean("animated"));
        image.setWidth(obj.getUnsignedInt("width"));
        image.setHeight(obj.getUnsignedInt("height"));
        image.setSize(obj.getUnsignedInt("size"));
        image.setViews(obj.getUnsignedInt("views"));
        image.setBandwidth(obj.getUnsignedInt("bandwidth"));
        image.setVote(Vote.fromKey(obj.getString("vote", null)));
        image.setFavorite(obj.getBoolean("favorite"));
        image.setNsfw(obj.getBoolean("nsfw"));
        image.setSection(obj.getString("section", null));
        image.setAccountUrl(obj.getString("account_url", null));
        image.setAccountId(obj.getUnsignedLong("account_id", 0L));
        image.setInMostViral(obj.getBoolean("in_most_viral"));
        image.setHasSound(obj.getBoolean("has_sound"));
        image.setEdited(obj.getUnsignedInt("edited", 0));
        image.setInGallery(obj.getBoolean("in_gallery"));
        image.setDeleteHash(obj.getString("deletehash", null));
        image.setName(obj.getString("name", null));
        image.setLink(obj.getString("link"));

        return image;
    }

    public Trophy createTrophy(@NotNull DataObject obj) {
        final TrophyImpl trophy = new TrophyImpl(api);

        trophy.setId(obj.getUnsignedLong("id"));
        trophy.setName(obj.getString("name"));
        trophy.setNameClean(obj.getString("name_clean"));
        trophy.setDescription(obj.getString("description"));
        trophy.setData(obj.getString("data", null));
        trophy.setDataLink(obj.getString("data_link", null));
        trophy.setDatetime(fromEpochSecond(obj.getLong("datetime")));
        trophy.setImage(obj.getString("image"));
        trophy.setImageWidth(obj.getUnsignedInt("image_width"));
        trophy.setImageHeight(obj.getUnsignedInt("image_height"));

        return trophy;
    }
}
