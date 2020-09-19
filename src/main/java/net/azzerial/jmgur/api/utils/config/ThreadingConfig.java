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

    private ExecutorService callbackPool;
    private ScheduledExecutorService requesterPool;

    private boolean shutdownCallbackPool;
    private boolean shutdownRequesterPool;

    /* Constructors */

    public ThreadingConfig() {
        this.callbackPool = ForkJoinPool.commonPool();
        this.shutdownRequesterPool = true;
        this.shutdownCallbackPool = false;
    }

    /* Getters & Setters */

    @NotNull
    public ExecutorService getCallbackPool() {
        return callbackPool;
    }

    public void setCallbackPool(@Nullable ExecutorService executor, boolean shutdown) {
        this.callbackPool = executor == null ? ForkJoinPool.commonPool() : executor;
        this.shutdownCallbackPool = shutdown;
    }

    public boolean isShutdownCallbackPool() {
        return shutdownCallbackPool;
    }

    @NotNull
    public ScheduledExecutorService getRequesterPool() {
        return requesterPool;
    }

    public void setRequesterPool(@Nullable ScheduledExecutorService executor, boolean shutdown) {
        this.requesterPool = executor == null ? newScheduler(5, JmgurInfo::getName, "Requester", false) : executor;
        this.shutdownRequesterPool = shutdown;
    }

    public boolean isShutdownRequesterPool() {
        return shutdownRequesterPool;
    }

    /* Methods */

    public void init(@NotNull Supplier<String> identifier) {
        if (this.requesterPool == null)
            this.requesterPool = newScheduler(5, identifier, "Requester", false);
    }

    @NotNull
    public static ScheduledThreadPoolExecutor newScheduler(int coreSize, @NotNull Supplier<String> identifier, @NotNull String baseName) {
        return newScheduler(coreSize, identifier, baseName, true);
    }

    @NotNull
    public static ScheduledThreadPoolExecutor newScheduler(int coreSize, @NotNull Supplier<String> identifier, @NotNull String baseName, boolean daemon) {
        Check.notNull(identifier, "identifier");
        Check.notBlank(identifier.get(), "identifier#get");
        Check.notBlank(baseName, "baseName");
        return new ScheduledThreadPoolExecutor(coreSize, new CountingThreadFactory(identifier, baseName, daemon));
    }

    @NotNull
    public static ThreadingConfig getDefault() {
        return new ThreadingConfig();
    }

    public void shutdown() {
        if (shutdownCallbackPool)
            callbackPool.shutdown();
        if (shutdownRequesterPool) {
            if (requesterPool instanceof ScheduledThreadPoolExecutor) {
                final ScheduledThreadPoolExecutor executor = (ScheduledThreadPoolExecutor) requesterPool;

                executor.setKeepAliveTime(5L, TimeUnit.SECONDS);
                executor.allowCoreThreadTimeOut(true);
            } else
                requesterPool.shutdown();
        }
    }

    public void shutdownRequester() {
        if (shutdownRequesterPool)
            requesterPool.shutdown();
    }

    public void shutdownNow() {
        if (shutdownCallbackPool)
            callbackPool.shutdownNow();
        if (shutdownRequesterPool)
            requesterPool.shutdownNow();
    }
}
