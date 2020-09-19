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

import lombok.Setter;
import net.azzerial.jmgur.api.Jmgur;
import net.azzerial.jmgur.api.entities.AccountSettings;
import net.azzerial.jmgur.api.entities.subentities.AlbumPrivacy;
import net.azzerial.jmgur.api.entities.subentities.ImagePrivacy;
import org.jetbrains.annotations.NotNull;

import static net.azzerial.jmgur.internal.utils.Helper.print;

@Setter
public final class AccountSettingsImpl implements AccountSettings {

    private final Jmgur api;

    private String accountUrl;
    private String email;
    private String avatar;
    private String cover;
    private ImagePrivacy publicImages;
    private AlbumPrivacy albumPrivacy;
    private boolean acceptedGalleryTerms;
    private boolean messagingEnabled;
    private boolean showMature;
    private boolean newsletterSubscribed;

    /* Constructors */

    AccountSettingsImpl(@NotNull Jmgur api) {
        this.api = api;
    }

    /* Getters & Setters */

    @NotNull
    @Override
    public Jmgur getApi() {
        return api;
    }

    @NotNull
    @Override
    public String getName() {
        return accountUrl;
    }

    @NotNull
    @Override
    public String getEmail() {
        return email;
    }

    @NotNull
    @Override
    public String getAvatarName() {
        return avatar;
    }

    @NotNull
    @Override
    public String getCoverName() {
        return cover;
    }

    @NotNull
    @Override
    public ImagePrivacy getImagePrivacy() {
        return publicImages;
    }

    @NotNull
    @Override
    public AlbumPrivacy getAlbumPrivacy() {
        return albumPrivacy;
    }

    @Override
    public boolean hasAcceptedGalleryTerms() {
        return acceptedGalleryTerms;
    }

    @Override
    public boolean isMessagingEnabled() {
        return messagingEnabled;
    }

    @Override
    public boolean showNSFW() {
        return showMature;
    }

    @Override
    public boolean isNewsletterSubscribed() {
        return newsletterSubscribed;
    }

    /* Methods */

    @Override
    public String toString() {
        return "AccountSettings{" +
            "name=" + print(accountUrl) +
            ", email=" + print(email) +
            ", avatarName=" + print(avatar) +
            ", coverName=" + print(cover) +
            ", imagePrivacy=" + publicImages +
            ", albumPrivacy=" + albumPrivacy +
            ", acceptedGalleryTerms=" + acceptedGalleryTerms +
            ", messagingEnabled=" + messagingEnabled +
            ", showNSFW=" + showMature +
            ", newsletterSubscribed=" + newsletterSubscribed +
            '}';
    }
}
