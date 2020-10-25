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

package net.azzerial.jmgur.api.entities.dto;

import net.azzerial.jmgur.api.entities.subentities.AlbumPrivacy;
import net.azzerial.jmgur.internal.entities.EntityBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface AlbumInformationDTO {

    /* Static Constructors */

    @NotNull
    static AlbumInformationDTO create() {
        return EntityBuilder.createAlbumInformationDTO();
    }

    /* Getters & Setters */

    @NotNull
    AlbumInformationDTO addImage(@NotNull String hash);

    @NotNull
    AlbumInformationDTO addImages(@NotNull List<String> hashes);

    @NotNull
    AlbumInformationDTO setTitle(@Nullable String title);

    @NotNull
    AlbumInformationDTO setDescription(@Nullable String description);

    @NotNull
    AlbumInformationDTO setPrivacy(@NotNull AlbumPrivacy privacy);

    @NotNull
    AlbumInformationDTO setCover(@NotNull String hash);
}
