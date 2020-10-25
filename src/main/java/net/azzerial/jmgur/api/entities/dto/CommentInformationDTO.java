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

public interface CommentInformationDTO {

    /* Static Constructors */

    @NotNull
    static CommentInformationDTO create() {
        return EntityBuilder.createCommentInformationDTO();
    }

    /* Getters & Setters */

    @NotNull
    CommentInformationDTO setPostHash(@NotNull String hash);

    @NotNull
    CommentInformationDTO setContent(@NotNull String content);

    @NotNull
    CommentInformationDTO setParentId(long id);
}
