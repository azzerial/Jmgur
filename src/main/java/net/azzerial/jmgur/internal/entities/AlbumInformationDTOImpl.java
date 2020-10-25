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
import net.azzerial.jmgur.api.entities.dto.AlbumInformationDTO;
import net.azzerial.jmgur.api.entities.subentities.AlbumPrivacy;
import net.azzerial.jmgur.internal.utils.Check;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@Getter
public final class AlbumInformationDTOImpl implements AlbumInformationDTO {

    private final Set<String> images;
    private final Map<String, String> map;

    /* Constructors */

    AlbumInformationDTOImpl() {
        this.images = new LinkedHashSet<>();
        this.map = new HashMap<>();
    }

    /* Getters & Setters */

    @NotNull
    @Override
    public AlbumInformationDTO addImage(@NotNull String hash) {
        Check.notBlank(hash, "hash");
        this.images.add(hash);
        return this;
    }

    @NotNull
    @Override
    public AlbumInformationDTO addImages(@NotNull List<String> hashes) {
        Check.noneBlank(hashes, "hashes");
        this.images.addAll(hashes);
        return this;
    }

    @NotNull
    @Override
    public AlbumInformationDTO setTitle(@Nullable String title) {
        this.map.put("title", title == null ? "" : title);
        return this;
    }

    @NotNull
    @Override
    public AlbumInformationDTO setDescription(@Nullable String description) {
        this.map.put("description", description == null ? "" : description);
        return this;
    }

    @NotNull
    @Override
    public AlbumInformationDTO setPrivacy(@NotNull AlbumPrivacy privacy) {
        Check.notNull(privacy, "privacy");
        Check.check(privacy != AlbumPrivacy.UNKNOWN, "privacy must not be UNKNOWN");
        this.map.put("privacy", privacy.getKey());
        return this;
    }

    @NotNull
    @Override
    public AlbumInformationDTO setCover(@NotNull String hash) {
        Check.notNull(hash, "hash");
        this.map.put("cover", hash);
        return this;
    }

    /* Methods */

    public boolean isEmpty() {
        return map.isEmpty() && images.isEmpty();
    }
}
