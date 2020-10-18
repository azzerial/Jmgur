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

package net.azzerial.jmgur.api.entities.subentities;

import org.jetbrains.annotations.NotNull;

public enum ReportReason {
    DOESN_T_BELONG_TO_IMGUR(1),
    SPAM(2),
    ABUSIVE(3),
    MATURE_CONTENT_NOT_MARKED_AS_MATURE(4),
    PORNOGRAPHY(5),
    UNKNOWN(-1);

    private final int value;

    /* Constructors */

    ReportReason(int value) {
        this.value = value;
    }

    /* Getters & Setters */

    public int getValue() {
        return value;
    }

    /* Static Methods */

    @NotNull
    public static ReportReason fromValue(int value) {
        for (ReportReason rr : values()) {
            if (rr.value == value)
                return rr;
        }
        return UNKNOWN;
    }
}
