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

package net.azzerial.jmgur.api.exceptions;

import net.azzerial.jmgur.internal.utils.Check;
import org.jetbrains.annotations.NotNull;

public final class OAuth2Exception extends RuntimeException {

    private final boolean accessDenied;

    /* Constructors */

    public OAuth2Exception(@NotNull String message) {
        super(message);
        Check.notNull(message, "message");
        this.accessDenied = false;
    }

    public OAuth2Exception(boolean accessDenied) {
        super(accessDenied ? "The access was denied by the user" : "An error occurred during the OAuth2 authentication");
        this.accessDenied = accessDenied;
    }

    /* Getters & Setters */

    public boolean isAccessDenied() {
        return accessDenied;
    }
}
