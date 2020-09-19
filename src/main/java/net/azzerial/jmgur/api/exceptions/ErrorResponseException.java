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
 * https://github.com/DV8FromTheWorld/JDA/blob/master/src/main/java/net/dv8tion/jda/api/exceptions/ErrorResponseException.java
 * All credit goes to the original authors.
 */

package net.azzerial.jmgur.api.exceptions;

import net.azzerial.jmgur.api.requests.Response;
import net.azzerial.jmgur.api.utils.data.DataObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public final class ErrorResponseException extends RuntimeException {

    private final Response response;
    private final int code;
    private final String message;

    /* Constructors */

    private ErrorResponseException(@Nullable Response response, int code, @Nullable String message) {
        super(code + ": " + message);
        this.response = response;
        if (response != null && response.getException() != null)
            initCause(response.getException());
        this.code = code;
        this.message = message;
    }

    /* Getters & Setters */

    @Nullable
    public Response getResponse() {
        return response;
    }

    public int getCode() {
        return code;
    }

    @Nullable
    public String getMessage() {
        return message;
    }

    public boolean isOk() {
        return code > 199 && code < 300;
    }

    public boolean isError() {
        return code > 399 || code == Response.ERROR_CODE;
    }

    public boolean isClientError() {
        return code > 399 && code < 500;
    }

    public boolean isServerError() {
        return code >= 500;
    }

    public boolean isRequesterError() {
        return code == Response.ERROR_CODE;
    }

    /* Static Methods */

    @NotNull
    public static ErrorResponseException create(@Nullable Response response) {
        if (response == null)
            return new ErrorResponseException(null, Response.ERROR_CODE, Response.ERROR_MESSAGE);

        final Optional<DataObject> optObj = response.optObject();
        final int code = response.getCode();
        String message = response.getMessage();

        if (response.getException() != null)
            message = response.getException().getClass().getName();
        else if (optObj.isPresent()) {
            DataObject obj = optObj.get();
            message = obj.toString();
        } else if (message.isEmpty())
            message = response.getString();

        return new ErrorResponseException(response, code, message);
    }
}
