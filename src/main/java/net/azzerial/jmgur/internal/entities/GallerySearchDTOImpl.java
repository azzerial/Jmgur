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

import lombok.Getter;
import net.azzerial.jmgur.api.entities.dto.GallerySearchDTO;
import net.azzerial.jmgur.api.entities.subentities.*;
import net.azzerial.jmgur.internal.utils.Check;
import org.jetbrains.annotations.NotNull;

@Getter
public final class GallerySearchDTOImpl implements GallerySearchDTO {

    private String query;
    private SearchType type;
    private GallerySort sort;
    private GalleryTimeWindow timeWindow;
    private SearchFileType fileType;
    private SearchImageSize imageSize;

    /* Constructors */

    GallerySearchDTOImpl() {
        this.query = "";
        this.type = SearchType.QUERY;
        this.sort = GallerySort.TIME;
        this.timeWindow = GalleryTimeWindow.ALL;
        this.fileType = null;
        this.imageSize = null;
    }

    /* Getters & Setters */

    @NotNull
    @Override
    public GallerySearchDTO search(@NotNull SearchType type, @NotNull String query) {
        Check.notNull(type, "type");
        Check.check(type != SearchType.UNKNOWN, "type must not be UNKNOWN");
        Check.notNull(query, "query");
        this.query = query;
        this.type = type;
        return this;
    }

    @NotNull
    @Override
    public GallerySearchDTO setSort(@NotNull GallerySort sort) {
        Check.notNull(sort, "sort");
        Check.check(sort != GallerySort.RISING, "sort must not be RISING");
        Check.check(sort != GallerySort.UNKNOWN, "sort must not be UNKNOWN");
        this.sort = sort;
        return this;
    }

    @NotNull
    @Override
    public GallerySearchDTO setTimeWindow(@NotNull GalleryTimeWindow timeWindow) {
        Check.notNull(timeWindow, "timeWindow");
        Check.check(timeWindow != GalleryTimeWindow.UNKNOWN, "timeWindow must not be UNKNOWN");
        this.timeWindow = timeWindow;
        return this;
    }

    @NotNull
    @Override
    public GallerySearchDTO setFileType(@NotNull SearchFileType fileType) {
        Check.notNull(fileType, "fileType");
        Check.check(fileType != SearchFileType.UNKNOWN, "fileType must not be UNKNOWN");
        this.fileType = fileType;
        return this;
    }

    @NotNull
    @Override
    public GallerySearchDTO setImageSize(@NotNull SearchImageSize imageSize) {
        Check.notNull(imageSize, "imageSize");
        Check.check(imageSize != SearchImageSize.UNKNOWN, "imageSize must not be UNKNOWN");
        this.imageSize = imageSize;
        return this;
    }
}
