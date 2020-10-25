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

package net.azzerial.jmgur.api;

import net.azzerial.jmgur.api.entities.*;
import net.azzerial.jmgur.api.entities.dto.AccountSettingsDTO;
import net.azzerial.jmgur.api.entities.subentities.CommentSort;
import net.azzerial.jmgur.api.entities.subentities.FavoriteSort;
import net.azzerial.jmgur.api.requests.restaction.PagedRestAction;
import net.azzerial.jmgur.api.requests.restaction.RestAction;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface AccountRepository {

    @NotNull
    Jmgur getApi();

    /* --- Profiles --- */

    @NotNull
    default RestAction<Account> getSelfAccount() {
        return getUserAccount("me");
    }

    @NotNull
    RestAction<Account> getUserAccount(@NotNull String name);

    @NotNull
    default RestAction<GalleryProfile> getSelfGalleryProfile() {
        return getUserGalleryProfile("me");
    }

    @NotNull
    RestAction<GalleryProfile> getUserGalleryProfile(@NotNull String name);

    /* --- Blocks --- */

    @NotNull
    RestAction<Boolean> isUserBlocked(@NotNull String name);

    @NotNull
    RestAction<List<String>> getBlockedUserNames();

    @NotNull
    RestAction<Boolean> blockUser(@NotNull String name);

    @NotNull
    RestAction<Boolean> unblockUser(@NotNull String name);

    /* --- Resources --- */

    @NotNull
    default PagedRestAction<List<GalleryElement>> getSelfGalleryFavorites() {
        return getUserGalleryFavorites("me");
    }

    @NotNull
    default PagedRestAction<List<GalleryElement>> getUserGalleryFavorites(@NotNull String name) {
        return getUserGalleryFavorites(name, FavoriteSort.NEWEST);
    }

    @NotNull
    default PagedRestAction<List<GalleryElement>> getSelfGalleryFavorites(@NotNull FavoriteSort sort) {
        return getUserGalleryFavorites("me", sort);
    }

    @NotNull
    PagedRestAction<List<GalleryElement>> getUserGalleryFavorites(@NotNull String name, @NotNull FavoriteSort sort);

    @NotNull
    default PagedRestAction<List<GalleryElement>> getSelfFavorites() {
        return getUserFavorites("me");
    }

    @NotNull
    default PagedRestAction<List<GalleryElement>> getUserFavorites(@NotNull String name) {
        return getUserGalleryFavorites(name, FavoriteSort.NEWEST);
    }

    @NotNull
    default PagedRestAction<List<GalleryElement>> getSelfFavorites(@NotNull FavoriteSort sort) {
        return getUserFavorites("me", sort);
    }

    @NotNull
    PagedRestAction<List<GalleryElement>> getUserFavorites(@NotNull String name, @NotNull FavoriteSort sort);

    @NotNull
    default PagedRestAction<List<GalleryElement>> getSelfSubmissions() {
        return getUserSubmissions("me");
    }

    @NotNull
    PagedRestAction<List<GalleryElement>> getUserSubmissions(@NotNull String name);

    /* --- Avatars --- */

    @NotNull
    default RestAction<List<Avatar>> getSelfAvailableAvatars() {
        return getUserAvailableAvatars("me");
    }

    @NotNull
    RestAction<List<Avatar>> getUserAvailableAvatars(@NotNull String name);

    @NotNull
    default RestAction<Avatar> getSelfAvatar() {
        return getUserAvatar("me");
    }

    @NotNull
    RestAction<Avatar> getUserAvatar(@NotNull String name);

    /* --- Settings --- */

    @NotNull
    RestAction<AccountSettings> getSelfAccountSettings();

    @NotNull
    RestAction<Boolean> updateSelfAccountSettings(@NotNull AccountSettingsDTO dto);

    /* --- Albums --- */

    @NotNull
    default PagedRestAction<List<Album>> getSelfAlbums() {
        return getUserAlbums("me");
    }

    @NotNull
    PagedRestAction<List<Album>> getUserAlbums(@NotNull String name);

    @NotNull
    default RestAction<Album> getSelfAlbum(@NotNull String hash) {
        return getUserAlbum("me", hash);
    }

    @NotNull
    RestAction<Album> getUserAlbum(@NotNull String name, @NotNull String hash);

    @NotNull
    default PagedRestAction<List<String>> getSelfAlbumIds() {
        return getUserAlbumIds("me");
    }

    @NotNull
    PagedRestAction<List<String>> getUserAlbumIds(@NotNull String name);

    @NotNull
    default RestAction<Integer> getSelfAlbumCount() {
        return getUserAlbumCount("me");
    }

    @NotNull
    RestAction<Integer> getUserAlbumCount(@NotNull String name);

    @NotNull
    default RestAction<Boolean> deleteSelfAlbum(@NotNull String deleteHash) {
        return deleteUserAlbum("me", deleteHash);
    }

    @NotNull
    RestAction<Boolean> deleteUserAlbum(@NotNull String name, @NotNull String deleteHash);

    /* --- Comments --- */

    @NotNull
    default PagedRestAction<List<Comment>> getSelfComments() {
        return getUserComments("me");
    }

    @NotNull
    default PagedRestAction<List<Comment>> getUserComments(@NotNull String name) {
        return getUserComments(name, CommentSort.NEWEST);
    }

    @NotNull
    default PagedRestAction<List<Comment>> getSelfComments(@NotNull CommentSort sort) {
        return getUserComments("me", sort);
    }

    @NotNull
    PagedRestAction<List<Comment>> getUserComments(@NotNull String name, @NotNull CommentSort sort);

    @NotNull
    default RestAction<Comment> getSelfComment(long id) {
        return getUserComment("me", id);
    }

    @NotNull
    RestAction<Comment> getUserComment(@NotNull String name, long id);

    @NotNull
    default PagedRestAction<List<Long>> getSelfCommentIds() {
        return getUserCommentIds("me");
    }

    @NotNull
    default PagedRestAction<List<Long>> getUserCommentIds(@NotNull String name) {
        return getUserCommentIds(name, CommentSort.NEWEST);
    }

    @NotNull
    default PagedRestAction<List<Long>> getSelfCommentIds(@NotNull CommentSort sort) {
        return getUserCommentIds("me", sort);
    }

    @NotNull
    PagedRestAction<List<Long>> getUserCommentIds(@NotNull String name, @NotNull CommentSort sort);

    @NotNull
    default RestAction<Integer> getSelfCommentCount() {
        return getUserCommentCount("me");
    }

    @NotNull
    RestAction<Integer> getUserCommentCount(@NotNull String name);

    @NotNull
    RestAction<Boolean> deleteSelfComment(long id);

    /* --- Images --- */

    @NotNull
    PagedRestAction<List<Image>> getSelfImages();

    @NotNull
    default RestAction<Image> getSelfImage(@NotNull String hash) {
        return getUserImage("me", hash);
    }

    @NotNull
    RestAction<Image> getUserImage(@NotNull String name, @NotNull String hash);

    @NotNull
    default PagedRestAction<List<String>> getSelfImageIds() {
        return getUserImageIds("me");
    }

    @NotNull
    PagedRestAction<List<String>> getUserImageIds(@NotNull String name);

    @NotNull
    default RestAction<Integer> getSelfImageCount() {
        return getUserImageCount("me");
    }

    @NotNull
    RestAction<Integer> getUserImageCount(@NotNull String name);

    @NotNull
    default RestAction<Boolean> deleteSelfImage(@NotNull String deleteHash) {
        return deleteUserImage("me", deleteHash);
    }

    @NotNull
    RestAction<Boolean> deleteUserImage(@NotNull String name, @NotNull String deleteHash);
}
