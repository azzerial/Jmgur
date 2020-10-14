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
import net.azzerial.jmgur.api.entities.dto.GalleryDTO;
import net.azzerial.jmgur.api.entities.subentities.GallerySection;
import net.azzerial.jmgur.api.entities.subentities.GallerySort;
import net.azzerial.jmgur.api.entities.subentities.GalleryTimeWindow;
import net.azzerial.jmgur.internal.utils.Check;
import org.jetbrains.annotations.NotNull;

@Getter
public final class GalleryDTOImpl implements GalleryDTO {

    private GallerySection section;
    private GallerySort sort;
    private GalleryTimeWindow timeWindow;
    private boolean viral;
    private boolean mature;
    private boolean albumPreviews;

    /* Constructors */

    GalleryDTOImpl() {
        this.section = GallerySection.HOT;
        this.sort = GallerySort.VIRAL;
        this.timeWindow = GalleryTimeWindow.DAY;
        this.viral = true;
        this.mature = false;
        this.albumPreviews = true;
    }

    /* Getters & Setters */

    @NotNull
    @Override
    public GalleryDTO setSection(@NotNull GallerySection section) {
        Check.notNull(section, "section");
        Check.check(section != GallerySection.UNKNOWN, "section must not be UNKNOWN");
        this.section = section;
        return this;
    }

    @NotNull
    @Override
    public GalleryDTO setSort(@NotNull GallerySort sort) {
        Check.notNull(sort, "sort");
        Check.check(sort != GallerySort.UNKNOWN, "sort must not be UNKNOWN");
        this.sort = sort;
        return this;
    }

    @NotNull
    @Override
    public GalleryDTO setTimeWindow(@NotNull GalleryTimeWindow timeWindow) {
        Check.notNull(timeWindow, "timeWindow");
        Check.check(timeWindow != GalleryTimeWindow.UNKNOWN, "timeWindow must not be UNKNOWN");
        this.timeWindow = timeWindow;
        return this;
    }

    @NotNull
    @Override
    public GalleryDTO showViral(boolean enabled) {
        this.viral = enabled;
        return this;
    }

    @NotNull
    @Override
    public GalleryDTO showNSFW(boolean enabled) {
        this.mature = enabled;
        return this;
    }

    @NotNull
    @Override
    public GalleryDTO includeImages(boolean enabled) {
        this.albumPreviews = enabled;
        return this;
    }
}
