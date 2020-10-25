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

import net.azzerial.jmgur.api.entities.Album;
import net.azzerial.jmgur.api.entities.Image;
import net.azzerial.jmgur.api.entities.dto.AlbumInformationDTO;
import net.azzerial.jmgur.api.requests.restaction.RestAction;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface AlbumRepository {

    @NotNull
    Jmgur getApi();

    /* --- Core --- */

    @NotNull
    RestAction<Album> getAlbum(@NotNull String hash);

    @NotNull
    RestAction<List<Image>> getAlbumImages(@NotNull String hash);

    @NotNull
    RestAction<Image> getAlbumImage(@NotNull String albumHash, @NotNull String imageHash);

    @NotNull
    RestAction<String> createAlbum(@NotNull AlbumInformationDTO dto);

    @NotNull
    RestAction<Boolean> updateAlbum(@NotNull String hash, @NotNull AlbumInformationDTO dto);

    @NotNull
    RestAction<Boolean> deleteAlbum(@NotNull String hash);

    @NotNull
    RestAction<Boolean> favoriteAlbum(@NotNull String hash);

    @NotNull
    RestAction<Boolean> setAlbumImages(@NotNull String albumHash, @NotNull List<String> imagesHash);

    @NotNull
    RestAction<Boolean> addAlbumImages(@NotNull String albumHash, @NotNull List<String> imagesHash);
}
