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

import net.azzerial.jmgur.api.entities.subentities.*;
import net.azzerial.jmgur.internal.entities.EntityBuilder;
import org.jetbrains.annotations.NotNull;

public interface GallerySearchDTO {

    /* Static Constructors */

    @NotNull
    static GallerySearchDTO create() {
        return EntityBuilder.createGallerySearchDTO();
    }

    /* Getters & Setters */

    @NotNull
    GallerySearchDTO search(@NotNull SearchType type, @NotNull String query);

    @NotNull
    default GallerySearchDTO query(@NotNull String query) {
        return search(SearchType.QUERY, query);
    }

    @NotNull
    default GallerySearchDTO queryAll(@NotNull String query) {
        return search(SearchType.QUERY_ALL, query);
    }

    @NotNull
    default GallerySearchDTO queryAny(@NotNull String query) {
        return search(SearchType.QUERY_ANY, query);
    }

    @NotNull
    default GallerySearchDTO queryExactly(@NotNull String query) {
        return search(SearchType.QUERY_EXACTLY, query);
    }

    @NotNull
    default GallerySearchDTO queryNot(@NotNull String query) {
        return search(SearchType.QUERY_NOT, query);
    }

    @NotNull
    GallerySearchDTO setSort(@NotNull GallerySort sort);

    @NotNull
    GallerySearchDTO setTimeWindow(@NotNull GalleryTimeWindow timeWindow);

    @NotNull
    GallerySearchDTO setFileType(@NotNull SearchFileType fileType);

    @NotNull
    GallerySearchDTO setImageSize(@NotNull SearchImageSize imageSize);
}
