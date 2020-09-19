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

import net.azzerial.jmgur.api.entities.subentities.AlbumPrivacy;
import net.azzerial.jmgur.api.entities.subentities.ImagePrivacy;
import net.azzerial.jmgur.internal.entities.EntityBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface AccountSettingsDTO {

    /* Static Constructors */

    @NotNull
    static AccountSettingsDTO create() {
        return EntityBuilder.createAccountSettingsDTO();
    }

    /* Getters & Setters */

    @NotNull
    AccountSettingsDTO setName(@NotNull String name);

    @NotNull
    AccountSettingsDTO setBio(@Nullable String bio);

    @NotNull
    AccountSettingsDTO setImagePrivacy(@NotNull ImagePrivacy privacy);

    @NotNull
    AccountSettingsDTO setAlbumPrivacy(@NotNull AlbumPrivacy privacy);

    @NotNull
    AccountSettingsDTO enableMessaging(boolean enabled);

    @NotNull
    AccountSettingsDTO acceptedGalleryTerms(boolean accepted);

    @NotNull
    AccountSettingsDTO showNSFW(boolean show);

    @NotNull
    AccountSettingsDTO subscribeToNewsletter(boolean subscribe);
}
