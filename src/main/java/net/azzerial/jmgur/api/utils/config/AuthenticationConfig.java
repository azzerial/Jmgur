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

/*
 * This class has major inspiration from DV8FromTheWorld's project JDA.
 * https://github.com/DV8FromTheWorld/JDA/blob/master/src/main/java/net/dv8tion/jda/internal/utils/config/AuthorizationConfig.java
 */

package net.azzerial.jmgur.api.utils.config;

import net.azzerial.jmgur.internal.utils.Check;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public final class AuthenticationConfig {

    private final String clientId;
    private final Supplier<String> accessToken;

    /* Constructors */

    public AuthenticationConfig(@NotNull String clientId, @NotNull Supplier<String> accessToken) {
        Check.notBlank(clientId, "clientId");
        Check.notNull(accessToken, "accessToken");
        Check.notBlank(accessToken.get(), "accessToken.get()");
        this.clientId = clientId;
        this.accessToken = accessToken;
    }

    /* Getters & Setters */

    @NotNull
    public String getClientId() {
        return clientId;
    }

    @NotNull
    public String getAccessToken() {
        return accessToken.get();
    }
}
