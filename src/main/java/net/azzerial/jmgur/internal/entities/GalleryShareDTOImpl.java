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
import net.azzerial.jmgur.api.entities.dto.GalleryShareDTO;
import net.azzerial.jmgur.internal.utils.Check;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@Getter
public final class GalleryShareDTOImpl implements GalleryShareDTO {

    private final Map<String, String> map;

    /* Constructors */

    GalleryShareDTOImpl() {
        this.map = new HashMap<>();
    }

    /* Getters & Setters */

    @NotNull
    @Override
    public GalleryShareDTO setTitle(@NotNull String title) {
        Check.notBlank(title, "title");
        this.map.put("title", title);
        return this;
    }

    @NotNull
    @Override
    public GalleryShareDTO setTopic(@NotNull String topic) {
        Check.notBlank(topic, "topic");
        this.map.put("topic", topic);
        return this;
    }

    @NotNull
    @Override
    public GalleryShareDTO acceptGalleryTerms(boolean accept) {
        this.map.put("terms", accept ? "1" : "0");
        return this;
    }

    @NotNull
    @Override
    public GalleryShareDTO isNSFW(boolean enabled) {
        this.map.put("mature", enabled ? "1" : "0");
        return this;
    }

    /* Methods */

    public boolean isEmpty() {
        return map.isEmpty();
    }
}
