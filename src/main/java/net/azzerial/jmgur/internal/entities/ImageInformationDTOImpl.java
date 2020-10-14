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
import net.azzerial.jmgur.api.entities.dto.ImageInformationDTO;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

@Getter
public final class ImageInformationDTOImpl implements ImageInformationDTO {

    private final Map<String, String> map;

    /* Constructors */

    ImageInformationDTOImpl() {
        this.map = new HashMap<>();
    }

    /* Getters & Setters */

    @NotNull
    @Override
    public ImageInformationDTO setTitle(@Nullable String title) {
        this.map.put("title", title == null ? "" : title);
        return this;
    }

    @NotNull
    @Override
    public ImageInformationDTO setDescription(@Nullable String description) {
        this.map.put("description", description == null ? "" : description);
        return this;
    }

    /* Methods */

    public boolean isEmpty() {
        return map.isEmpty();
    }
}
