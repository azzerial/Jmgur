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
 * https://github.com/DV8FromTheWorld/JDA/blob/master/src/main/java/net/dv8tion/jda/api/JDABuilder.java
 */

package net.azzerial.jmgur.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.azzerial.jmgur.internal.utils.Check;
import net.azzerial.jmgur.internal.utils.IOUtil;
import net.azzerial.jmgur.api.utils.config.AuthenticationConfig;
import net.azzerial.jmgur.api.utils.config.SessionConfig;
import net.azzerial.jmgur.api.utils.config.ThreadingConfig;
import net.azzerial.jmgur.api.utils.config.flags.ConfigFlag;
import okhttp3.OkHttpClient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.concurrent.ExecutorService;

public final class JmgurBuilder {

    private final String clientId;

    private OkHttpClient httpClient;
    private OkHttpClient.Builder httpClientBuilder;
    private ObjectMapper mapper;
    private OAuth2 oauth;
    private EnumSet<ConfigFlag> flags = ConfigFlag.getDefault();
    private ExecutorService callbackPool;
    private boolean shutdownCallbackPool = true;
    private ExecutorService requesterPool;
    private boolean shutdownRequesterPool = true;

    /* Static Constructors */

    @NotNull
    public static JmgurBuilder of(@NotNull String clientId) {
        return new JmgurBuilder(clientId);
    }

    /* Constructors */

    public JmgurBuilder(@NotNull String clientId) {
        Check.notBlank(clientId, "clientId");
        this.clientId = clientId;
    }

    /* Getters & Setters */

    @NotNull
    public JmgurBuilder setHttpClient(@Nullable OkHttpClient httpClient) {
        this.httpClient = httpClient;
        return this;
    }

    @NotNull
    public JmgurBuilder setHttpClientBuilder(@Nullable OkHttpClient.Builder builder) {
        this.httpClientBuilder = builder;
        return this;
    }

    @NotNull
    public JmgurBuilder setMapper(@Nullable ObjectMapper mapper) {
        this.mapper = mapper;
        return this;
    }

    @NotNull
    public JmgurBuilder setOAuth(@NotNull OAuth2 oauth) {
        Check.notNull(oauth, "oauth");
        this.oauth = oauth;
        return this;
    }

    @NotNull
    private JmgurBuilder setFlag(@NotNull ConfigFlag flag, boolean enable) {
        Check.notNull(flag, "flag");
        if (enable)
            this.flags.add(flag);
        else
            this.flags.remove(flag);
        return this;
    }

    @NotNull
    public JmgurBuilder setCallbackPool(@Nullable ExecutorService pool) {
        return setCallbackPool(pool, pool == null);
    }

    @NotNull
    public JmgurBuilder setCallbackPool(@Nullable ExecutorService pool, boolean automaticShutdown) {
        this.callbackPool = pool;
        this.shutdownCallbackPool = automaticShutdown;
        return this;
    }

    @NotNull
    public JmgurBuilder setRequesterPool(@Nullable ExecutorService pool) {
        return setRequesterPool(pool, pool == null);
    }

    @NotNull
    public JmgurBuilder setRequesterPool(@Nullable ExecutorService pool, boolean automaticShutdown) {
        this.requesterPool = pool;
        this.shutdownRequesterPool = automaticShutdown;
        return this;
    }

    /* Methods */

    public Jmgur build() {
        if (oauth == null)
            throw new NullPointerException("No OAuth2 token provided. Did you forget to call JmgurBuilder#setOAuth?");

        OkHttpClient httpClient = this.httpClient;
        if (httpClient == null) {
            if (this.httpClientBuilder == null)
                this.httpClientBuilder = IOUtil.newHttpClientBuilder();
            httpClient = this.httpClientBuilder.build();
        }

        ObjectMapper mapper = this.mapper;
        if (mapper == null)
            mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        final AuthenticationConfig authenticationConfig = new AuthenticationConfig(clientId, oauth::getAccessToken);
        final SessionConfig sessionConfig = new SessionConfig(httpClient, mapper, oauth, flags);
        final ThreadingConfig threadingConfig = new ThreadingConfig(JmgurInfo::getName);
        threadingConfig.setCallbackPool(callbackPool, shutdownCallbackPool);
        threadingConfig.setRequesterPool(requesterPool, shutdownRequesterPool);

        return new Jmgur(authenticationConfig, sessionConfig, threadingConfig);
    }
}
