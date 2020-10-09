/*
 * Copyright 2015-2020 Austin Keener, Michael Ritter, Florian Spieß, and the JDA contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * This class was taken (and modified) from DV8FromTheWorld's project JDA.
 * https://github.com/DV8FromTheWorld/JDA/blob/master/src/main/java/net/dv8tion/jda/internal/utils/config/ThreadingConfig.java
 * All credit goes to the original authors.
 */

package net.azzerial.jmgur.api.utils.config;

import net.azzerial.jmgur.api.JmgurInfo;
import net.azzerial.jmgur.internal.utils.Check;
import net.azzerial.jmgur.internal.utils.concurrent.CountingThreadFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.*;
import java.util.function.Supplier;

public final class ThreadingConfig {

    private final Supplier<String> identifier;

    private ExecutorService callbackPool;
    private ExecutorService requesterPool;

    private boolean shutdownCallbackPool;
    private boolean shutdownRequesterPool;

    /* Constructors */

    public ThreadingConfig(@NotNull Supplier<String> identifier) {
        this.identifier = identifier;
        this.callbackPool = ForkJoinPool.commonPool();
        this.requesterPool = defaultRequester();
        this.shutdownCallbackPool = false;
        this.shutdownRequesterPool = true;
    }

    /* Getters & Setters */

    @NotNull
    public ExecutorService getCallbackPool() {
        return callbackPool;
    }

    public void setCallbackPool(@Nullable ExecutorService executor, boolean shutdown) {
        this.callbackPool = executor == null ? ForkJoinPool.commonPool() : executor;
        this.shutdownCallbackPool = executor != null && shutdown;
    }

    public boolean isShutdownCallbackPool() {
        return shutdownCallbackPool;
    }

    @NotNull
    public ExecutorService getRequesterPool() {
        return requesterPool;
    }

    public void setRequesterPool(@Nullable ExecutorService executor, boolean shutdown) {
        this.requesterPool = executor == null ? defaultRequester() : executor;
        this.shutdownRequesterPool = executor == null || shutdown;
    }

    public boolean isShutdownRequesterPool() {
        return shutdownRequesterPool;
    }

    /* Methods */

    @NotNull
    public static ThreadPoolExecutor newExecutor(int coreSize, long keepAliveSeconds, int queueSize, @NotNull Supplier<String> identifier, @NotNull String baseName) {
        return newExecutor(coreSize, keepAliveSeconds, queueSize, identifier, baseName, true);
    }

    @NotNull
    public static ThreadPoolExecutor newExecutor(int coreSize, long keepAliveSeconds, int queueSize, @NotNull Supplier<String> identifier, @NotNull String baseName, boolean daemon) {
        Check.notNull(identifier, "identifier");
        Check.notBlank(identifier.get(), "identifier#get");
        Check.notBlank(baseName, "baseName");
        return new ThreadPoolExecutor(coreSize, coreSize * 2, keepAliveSeconds, TimeUnit.SECONDS, new ArrayBlockingQueue<>(queueSize), new CountingThreadFactory(identifier, baseName, daemon));
    }

    @NotNull
    public static ThreadingConfig getDefault() {
        return new ThreadingConfig(JmgurInfo::getName);
    }

    public void shutdown() {
        if (shutdownCallbackPool)
            callbackPool.shutdown();
        if (shutdownRequesterPool)
            requesterPool.shutdown();
    }

    public void shutdownNow() {
        if (shutdownCallbackPool)
            callbackPool.shutdownNow();
        if (shutdownRequesterPool)
            requesterPool.shutdownNow();
    }

    /* Internal */

    @NotNull
    private ThreadPoolExecutor defaultRequester() {
        return newExecutor(5, 15L, 10, identifier, "Requester", false);
    }
}
