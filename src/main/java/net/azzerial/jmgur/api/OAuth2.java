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

package net.azzerial.jmgur.api;

import net.azzerial.jmgur.api.exceptions.OAuth2Exception;
import net.azzerial.jmgur.internal.utils.Check;
import net.azzerial.jmgur.internal.utils.IOUtil;
import org.jetbrains.annotations.NotNull;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

public final class OAuth2 {

    private final OffsetDateTime createdAt;
    private final String accessToken;
    private final String refreshToken;
    private final long expiresIn;
    private final OffsetDateTime expiresAt;
    private final String accountUsername;
    private final long accountId;

    /* Static Constructors */

    @NotNull
    public static OAuth2 fromData(@NotNull String accessToken, @NotNull String refreshToken, long expiresIn, @NotNull String accountUsername, long accountId) {
        return fromData(OffsetDateTime.now(), accessToken, refreshToken, expiresIn, accountUsername, accountId);
    }

    @NotNull
    public static OAuth2 fromData(@NotNull OffsetDateTime createdAt, @NotNull String accessToken, @NotNull String refreshToken, long expiresIn, @NotNull String accountUsername, long accountId) {
        return new OAuth2(createdAt, accessToken, refreshToken, expiresIn, accountUsername, accountId);
    }

    @NotNull
    public static OAuth2 fromUrl(@NotNull String url) throws OAuth2Exception {
        return fromUrl(OffsetDateTime.now(), url);
    }

    @NotNull
    public static OAuth2 fromUrl(@NotNull OffsetDateTime createdAt, @NotNull String url) throws OAuth2Exception {
        Check.notBlank(url, "url");
        return decodeCallbackUrl(createdAt, url);
    }

    /* Constructors */

    private OAuth2(OffsetDateTime createdAt, String accessToken, String refreshToken, long expiresIn, String accountUsername, long accountId) {
        Check.notNull(createdAt, "createdAt");
        Check.notBlank(accessToken, "accessToken");
        Check.notBlank(refreshToken, "refreshToken");
        Check.positive(expiresIn, "expiresIn");
        Check.notBlank(accountUsername, "accountUsername");
        Check.positive(accountId, "accountId");
        this.createdAt = createdAt;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
        this.expiresAt = createdAt.plus(expiresIn, ChronoUnit.MILLIS);
        this.accountUsername = accountUsername;
        this.accountId = accountId;
    }

    /* Getters & Setters */

    @NotNull
    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    @NotNull
    public String getAccessToken() {
        return accessToken;
    }

    @NotNull
    public String getRefreshToken() {
        return refreshToken;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    @NotNull
    public OffsetDateTime getExpiresAt() {
        return expiresAt;
    }

    @NotNull
    public String getAccountUsername() {
        return accountUsername;
    }

    public long getAccountId() {
        return accountId;
    }

    /* Internal */

    private static OAuth2 decodeCallbackUrl(OffsetDateTime createdAt, String url) throws OAuth2Exception {
        final Map<String, String> params = IOUtil.getQueryParams(url);

        if (params.containsKey("error")) {
            if (params.get("error").equals("access_denied"))
                throw new OAuth2Exception(true);
            throw new OAuth2Exception(false);
        }

        if (!params.containsKey("access_token"))
            throw new OAuth2Exception("Missing parameter: access_token");
        if (!params.containsKey("refresh_token"))
            throw new OAuth2Exception("Missing parameter: refresh_token");
        if (!params.containsKey("expires_in"))
            throw new OAuth2Exception("Missing parameter: expires_in");
        if (!params.containsKey("account_username"))
            throw new OAuth2Exception("Missing parameter: account_username");
        if (!params.containsKey("account_id"))
            throw new OAuth2Exception("Missing parameter: account_id");

        return new OAuth2(
            createdAt,
            params.get("access_token"),
            params.get("refresh_token"),
            Long.parseUnsignedLong(params.get("expires_in")),
            params.get("account_username"),
            Long.parseUnsignedLong(params.get("account_id"))
        );
    }
}
