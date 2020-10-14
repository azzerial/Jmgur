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

import net.azzerial.jmgur.api.entities.subentities.UploadFileType;
import net.azzerial.jmgur.internal.entities.EntityBuilder;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public interface ImageUploadDTO {

    /* Static Constructors */

    @NotNull
    static ImageUploadDTO create() {
        return EntityBuilder.createImageUploadDTO();
    }

    /* Getters & Setters */

    @NotNull
    ImageUploadDTO of(@NotNull UploadFileType fileType, @NotNull Object data);

    @NotNull
    default ImageUploadDTO binaryFile(@NotNull File file) {
        return of(UploadFileType.BINARY_FILE, file);
    }

    @NotNull
    default ImageUploadDTO base64(@NotNull String base64) {
        return of(UploadFileType.BASE64, base64);
    }

    @NotNull
    default ImageUploadDTO url(@NotNull String url) {
        return of(UploadFileType.URL, url);
    }

    @NotNull
    ImageUploadDTO addToAlbum(@NotNull String hash);

    @NotNull
    ImageUploadDTO setName(@NotNull String name);

    @NotNull
    ImageUploadDTO setTitle(@NotNull String title);

    @NotNull
    ImageUploadDTO setDescription(@NotNull String description);

    @NotNull
    ImageUploadDTO disableAudio(boolean enabled);

    @NotNull
    ImageUploadDTO isVideo(boolean video);
}
