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

import net.azzerial.jmgur.internal.entities.EntityBuilder;
import org.jetbrains.annotations.NotNull;

public interface GalleryShareDTO {

    /* Static Constructors */

    @NotNull
    static GalleryShareDTO create() {
        return EntityBuilder.createGalleryShareDTO();
    }

    /* Getters & Setters */

    @NotNull
    GalleryShareDTO setTitle(@NotNull String title);

    @NotNull
    GalleryShareDTO setTopic(@NotNull String topic);

    @NotNull
    GalleryShareDTO acceptGalleryTerms(boolean accept);

    @NotNull
    GalleryShareDTO isNSFW(boolean enabled);
}
