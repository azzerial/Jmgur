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
import net.azzerial.jmgur.api.entities.dto.ImageUploadDTO;
import net.azzerial.jmgur.api.entities.subentities.FileType;
import net.azzerial.jmgur.internal.utils.Check;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Getter
public final class ImageUploadDTOImpl implements ImageUploadDTO {

    private final Map<String, String> map;

    private String data;
    private File file;
    private FileType type;
    private boolean isFileVideo;

    /* Constructors */

    ImageUploadDTOImpl() {
        this.map = new HashMap<>();
        this.data = null;
        this.file = null;
        this.type = null;
        this.isFileVideo = false;
    }

    /* Getters & Setters */

    @NotNull
    @Override
    public ImageUploadDTO of(@NotNull FileType type, @NotNull Object data) {
        Check.notNull(type, "type");
        Check.check(type != FileType.UNKNOWN, "section must not be UNKNOWN");
        if (!(data instanceof CharSequence) && !(data instanceof File))
            throw new IllegalArgumentException("data may only be a String or a File");
        if (data instanceof CharSequence && (type == FileType.BASE64 || type == FileType.URL)) {
            Check.notBlank((CharSequence) data, "data");
            this.data = ((CharSequence) data).toString();
            this.file = null;
        } else if (data instanceof File && type == FileType.BINARY_FILE) {
            Check.notNull(data, "data");
            this.data = null;
            this.file = (File) data;
        } else
            throw new IllegalArgumentException("provided data is from incorrect FileType");
        this.type = type;
        return this;
    }

    @NotNull
    @Override
    public ImageUploadDTO addToAlbum(@NotNull String hash) {
        Check.notBlank(hash, hash);
        this.map.put("album", hash);
        return this;
    }

    @NotNull
    @Override
    public ImageUploadDTO setName(@NotNull String name) {
        Check.notBlank(name, name);
        this.map.put("name", name);
        return this;
    }

    @NotNull
    @Override
    public ImageUploadDTO setTitle(@NotNull String title) {
        Check.notBlank(title, title);
        this.map.put("title", title);
        return this;
    }

    @NotNull
    @Override
    public ImageUploadDTO setDescription(@NotNull String description) {
        Check.notBlank(description, description);
        this.map.put("description", description);
        return this;
    }

    @NotNull
    @Override
    public ImageUploadDTO disableAudio(boolean enabled) {
        this.map.put("disable_audio", enabled ? "1" : "0");
        return this;
    }

    @NotNull
    @Override
    public ImageUploadDTO isVideo(boolean video) {
        this.isFileVideo = video;
        return this;
    }

    /* Methods */

    public boolean isEmpty() {
        return type == null && map.isEmpty();
    }
}
