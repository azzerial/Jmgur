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
import net.azzerial.jmgur.api.entities.dto.CommentInformationDTO;
import net.azzerial.jmgur.internal.utils.Check;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@Getter
public final class CommentInformationDTOImpl implements CommentInformationDTO {

    private final Map<String, String> map;

    /* Constructors */

    CommentInformationDTOImpl() {
        this.map = new HashMap<>();
    }

    /* Getters & Setters */

    @NotNull
    @Override
    public CommentInformationDTO setPostHash(@NotNull String hash) {
        Check.notBlank(hash, "hash");
        this.map.put("image_id", hash);
        return this;
    }

    @NotNull
    @Override
    public CommentInformationDTO setContent(@NotNull String content) {
        Check.notBlank(content, "content");
        this.map.put("comment", content);
        return this;
    }

    @NotNull
    @Override
    public CommentInformationDTO setParentId(long id) {
        Check.positive(id, "id");
        this.map.put("parent_id", Long.toUnsignedString(id));
        return this;
    }

    /* Methods */

    public boolean isEmpty() {
        return map.isEmpty();
    }
}
