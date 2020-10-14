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
import net.azzerial.jmgur.api.entities.dto.AccountSettingsDTO;
import net.azzerial.jmgur.api.entities.subentities.AlbumPrivacy;
import net.azzerial.jmgur.api.entities.subentities.ImagePrivacy;
import net.azzerial.jmgur.internal.utils.Check;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

@Getter
public final class AccountSettingsDTOImpl implements AccountSettingsDTO {

    private final Map<String, String> map;

    /* Constructors */

    AccountSettingsDTOImpl() {
        this.map = new HashMap<>();
    }

    /* Getters & Setters */

    @NotNull
    @Override
    public AccountSettingsDTO setName(@NotNull String name) {
        Check.check(name.length() > 3 && name.length() < 64, "name must be 4 to 63 characters long");
        Check.check(name.matches("[A-Za-z0-9]*"), "name must be alphanumeric");
        Check.notBlank(name, name);
        this.map.put("username", name);
        return this;
    }

    @NotNull
    @Override
    public AccountSettingsDTO setBio(@Nullable String bio) {
        this.map.put("bio", bio == null ? "" : bio);
        return this;
    }

    @NotNull
    @Override
    public AccountSettingsDTO setAvatar(@NotNull String avatar) {
        Check.notBlank(avatar, "avatar");
        this.map.put("avatar", avatar);
        return this;
    }

    @NotNull
    @Override
    public AccountSettingsDTO setImagePrivacy(@NotNull ImagePrivacy privacy) {
        Check.notNull(privacy, "privacy");
        Check.check(privacy != ImagePrivacy.UNKNOWN, "privacy must not be UNKNOWN");
        this.map.put("public_images", String.valueOf(privacy.getKey()));
        return this;
    }

    @NotNull
    @Override
    public AccountSettingsDTO setAlbumPrivacy(@NotNull AlbumPrivacy privacy) {
        Check.notNull(privacy, "privacy");
        Check.check(privacy != AlbumPrivacy.UNKNOWN, "privacy must not be UNKNOWN");
        this.map.put("album_privacy", privacy.getKey());
        return this;
    }

    @NotNull
    @Override
    public AccountSettingsDTO enableMessaging(boolean enabled) {
        this.map.put("messaging_enabled", String.valueOf(enabled));
        return this;
    }

    @NotNull
    @Override
    public AccountSettingsDTO acceptedGalleryTerms(boolean accepted) {
        this.map.put("accepted_gallery_terms", String.valueOf(accepted));
        return this;
    }

    @NotNull
    @Override
    public AccountSettingsDTO showNSFW(boolean show) {
        this.map.put("show_mature", String.valueOf(show));
        return this;
    }

    @NotNull
    @Override
    public AccountSettingsDTO subscribeToNewsletter(boolean subscribe) {
        this.map.put("newsletter_subscribed", String.valueOf(subscribe));
        return this;
    }

    /* Methods */

    public boolean isEmpty() {
        return map.isEmpty();
    }
}
