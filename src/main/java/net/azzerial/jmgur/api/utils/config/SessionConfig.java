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
 * https://github.com/DV8FromTheWorld/JDA/blob/master/src/main/java/net/dv8tion/jda/internal/utils/config/SessionConfig.java
 */

package net.azzerial.jmgur.api.utils.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.azzerial.jmgur.api.OAuth2;
import net.azzerial.jmgur.api.utils.config.flags.ConfigFlag;
import net.azzerial.jmgur.internal.utils.Check;
import okhttp3.OkHttpClient;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public final class SessionConfig {

    private final OkHttpClient httpClient;
    private final ObjectMapper mapper;
    private final OAuth2 oauth;
    private final EnumSet<ConfigFlag> flags;

    /* Constructors */

    public SessionConfig(@NotNull OkHttpClient httpClient, @NotNull ObjectMapper mapper, @NotNull OAuth2 oauth, @NotNull EnumSet<ConfigFlag> flags) {
        Check.notNull(httpClient, "httpClient");
        Check.notNull(mapper, "mapper");
        Check.notNull(oauth, "oauth");
        Check.notNull(flags, "flags");
        this.httpClient = httpClient;
        this.mapper = mapper;
        this.oauth = oauth;
        this.flags = flags;
    }

    /* Getters & Setters */

    @NotNull
    public OkHttpClient getHttpClient() {
        return httpClient;
    }

    @NotNull
    public ObjectMapper getMapper() {
        return mapper;
    }

    @NotNull
    public OAuth2 getOAuth2() {
        return oauth;
    }

    @NotNull
    public EnumSet<ConfigFlag> getFlags() {
        return flags;
    }

    public boolean hasFlag(@NotNull ConfigFlag flag) {
        Check.notNull(flag, "flag");
        return flags.contains(flag);
    }

    @NotNull
    private SessionConfig setFlag(@NotNull ConfigFlag flag, boolean enable) {
        Check.notNull(flag, "flag");
        if (enable)
            this.flags.add(flag);
        else
            this.flags.remove(flag);
        return this;
    }
}
