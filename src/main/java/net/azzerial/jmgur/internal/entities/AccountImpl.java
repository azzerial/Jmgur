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
import net.azzerial.jmgur.api.entities.Account;
import net.azzerial.jmgur.api.entities.Avatar;
import net.azzerial.jmgur.api.entities.Cover;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.OffsetDateTime;

import static net.azzerial.jmgur.internal.utils.Helper.print;

@Setter
public final class AccountImpl implements Account {

    private final Jmgur api;

    private long id;
    private String url;
    private String bio;
    private Avatar avatar;
    private Cover cover;
    private int reputation;
    private String reputationName;
    private OffsetDateTime created;
    private boolean isBlocked;

    /* Constructors */

    AccountImpl(@NotNull Jmgur api) {
        this.api = api;
    }

    /* Getters & Setters */

    @NotNull
    @Override
    public Jmgur getApi() {
        return api;
    }

    @Override
    public long getIdLong() {
        return id;
    }

    @NotNull
    @Override
    public String getName() {
        return url;
    }

    @Nullable
    @Override
    public String getBio() {
        return bio;
    }

    @NotNull
    @Override
    public Avatar getAvatar() {
        return avatar;
    }

    @NotNull
    @Override
    public Cover getCover() {
        return cover;
    }

    @Override
    public int getReputationScore() {
        return reputation;
    }

    @NotNull
    @Override
    public String getReputationName() {
        return reputationName;
    }

    @NotNull
    @Override
    public OffsetDateTime getCreationDate() {
        return created;
    }

    @Override
    public boolean isBlocked() {
        return isBlocked;
    }

    /* Methods */

    @Override
    public String toString() {
        return "Account{" +
            "id=" + id +
            ", name=" + print(url) +
            ", bio=" + print(bio) +
            ", avatar=" + avatar +
            ", cover=" + cover +
            ", reputation=" + reputation +
            ", reputationName=" + print(reputationName) +
            ", creationDate=" + created +
            ", blocked=" + isBlocked +
            '}';
    }
}
