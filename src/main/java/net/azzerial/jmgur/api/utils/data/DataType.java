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
 * https://github.com/DV8FromTheWorld/JDA/blob/master/src/main/java/net/dv8tion/jda/api/utils/data/DataType.java
 * All credit goes to the original authors.
 */

package net.azzerial.jmgur.api.utils.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public enum DataType {
    INT, FLOAT, STRING, OBJECT, ARRAY, BOOLEAN, NULL, UNKNOWN;

    @NotNull
    public static DataType getType(@Nullable Object value) {
        for (DataType type : values()) {
            if (type.isType(value))
                return type;
        }
        return UNKNOWN;
    }

    public boolean isType(@Nullable Object value) {
        switch (this) {
            case INT:
                return value instanceof Integer || value instanceof Long || value instanceof Short || value instanceof Byte;
            case FLOAT:
                return value instanceof Double || value instanceof Float;
            case STRING:
                return value instanceof String;
            case BOOLEAN:
                return value instanceof Boolean;
            case ARRAY:
                return value instanceof List;
            case OBJECT:
                return value instanceof Map;
            case NULL:
                return value == null;
            default:
                return false;
        }
    }
}