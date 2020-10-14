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
 * https://github.com/DV8FromTheWorld/JDA/blob/master/src/main/java/net/dv8tion/jda/internal/JDAImpl.java
 */

package net.azzerial.jmgur.api;

import net.azzerial.jmgur.api.utils.config.AuthenticationConfig;
import net.azzerial.jmgur.api.utils.config.SessionConfig;
import net.azzerial.jmgur.api.utils.config.ThreadingConfig;
import net.azzerial.jmgur.internal.AccountRepositoryImpl;
import net.azzerial.jmgur.internal.GalleryRepositoryImpl;
import net.azzerial.jmgur.internal.ImageRepositoryImpl;
import net.azzerial.jmgur.internal.entities.EntityBuilder;
import net.azzerial.jmgur.internal.requests.Requester;
import org.jetbrains.annotations.NotNull;

public final class Jmgur {

    private final AuthenticationConfig authenticationConfig;
    private final SessionConfig sessionConfig;
    private final ThreadingConfig threadingConfig;
    private final Requester requester;
    private final EntityBuilder entityBuilder;

    public final AccountRepository ACCOUNT;
    public final GalleryRepository GALLERY;
    public final ImageRepository IMAGE;

    /* Constructors */

    Jmgur(AuthenticationConfig authenticationConfig, SessionConfig sessionConfig, ThreadingConfig threadingConfig) {
        this.authenticationConfig = authenticationConfig;
        this.sessionConfig = sessionConfig;
        this.threadingConfig = threadingConfig;
        this.requester = new Requester(this);
        this.entityBuilder = new EntityBuilder(this);

        // api repositories
        this.ACCOUNT = new AccountRepositoryImpl(this);
        this.GALLERY = new GalleryRepositoryImpl(this);
        this.IMAGE = new ImageRepositoryImpl(this);
    }

    /* Getters & Setters */

    @NotNull
    public AuthenticationConfig getAuthenticationConfig() {
        return authenticationConfig;
    }

    @NotNull
    public SessionConfig getSessionConfig() {
        return sessionConfig;
    }

    @NotNull
    public ThreadingConfig getThreadingConfig() {
        return threadingConfig;
    }

    @NotNull
    public Requester getRequester() {
        return requester;
    }

    @NotNull
    public EntityBuilder getEntityBuilder() {
        return entityBuilder;
    }

    /* Methods */

    public synchronized void shutdown() {
        threadingConfig.shutdown();
    }

    public synchronized void shutdownNow() {
        shutdown();
        threadingConfig.shutdownNow();
    }
}
