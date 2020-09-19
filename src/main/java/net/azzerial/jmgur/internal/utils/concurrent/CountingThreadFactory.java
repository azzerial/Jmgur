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
 * https://github.com/DV8FromTheWorld/JDA/blob/master/src/main/java/net/dv8tion/jda/internal/utils/concurrent/CountingThreadFactory.java
 * All credit goes to the original authors.
 */

package net.azzerial.jmgur.internal.utils.concurrent;

import net.azzerial.jmgur.internal.utils.Check;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

public final class CountingThreadFactory implements ThreadFactory {

    private final Supplier<String> identifier;
    private final AtomicLong count = new AtomicLong(1);
    private final boolean daemon;

    /* Constructors */

    public CountingThreadFactory(@NotNull Supplier<String> identifier, @NotNull String specifier) {
        this(identifier, specifier, true);
    }

    public CountingThreadFactory(@NotNull Supplier<String> identifier, @NotNull String specifier, boolean daemon) {
        Check.notNull(identifier, "identifier");
        Check.notBlank(identifier.get(), "identifier#get");
        Check.notBlank(specifier, "specifier");
        this.identifier = () -> identifier.get() + " " + specifier;
        this.daemon = daemon;
    }

    /* Methods */

    @NotNull
    @Override
    public Thread newThread(@NotNull Runnable runnable) {
        Check.notNull(runnable, "runnable");
        final Thread thread = new Thread(runnable, identifier.get() + "-Worker " + count.getAndIncrement());

        thread.setDaemon(daemon);
        return thread;
    }
}
