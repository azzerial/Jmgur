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
 * https://github.com/DV8FromTheWorld/JDA/blob/master/src/main/java/net/dv8tion/jda/internal/utils/Helpers.java
 * All credit goes to the original authors.
 */

package net.azzerial.jmgur.internal.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Helper {

    /* --- String Utils --- */

    public static boolean isEmpty(final CharSequence seq) {
        return seq == null || seq.length() == 0;
    }

    public static boolean isBlank(final CharSequence seq) {
        if (isEmpty(seq))
            return true;
        for (int i = 0; i < seq.length(); i += 1) {
            if (!Character.isWhitespace(seq.charAt(i)))
                return false;
        }
        return true;
    }

    public static boolean isNumeric(final String input) {
        if (isEmpty(input))
            return false;
        for (char c : input.toCharArray()) {
            if (!Character.isDigit(c))
                return false;
        }
        return true;
    }

    public static boolean containsWhitespace(final CharSequence seq) {
        if (isEmpty(seq))
            return false;
        for (int i = 0; i < seq.length(); i += 1) {
            if (Character.isWhitespace(seq.charAt(i)))
                return true;
        }
        return false;
    }

    public static int countMatches(final CharSequence seq, final char c) {
        if (isEmpty(seq))
            return 0;
        int count = 0;
        for (int i = 0; i < seq.length(); i += 1) {
            if (seq.charAt(i) == c)
                count += 1;
        }
        return count;
    }

    public static String print(final String s) {
        if (s == null)
            return "null";
        else
            return '\'' + s.replaceAll("\n", "\\\\n") + '\'';
    }

    /* --- Time Utils --- */

    public static OffsetDateTime fromEpochSecond(long epoch) {
        return OffsetDateTime.of(LocalDateTime.ofEpochSecond(epoch, 0, ZoneOffset.UTC), ZoneOffset.UTC);
    }
}
