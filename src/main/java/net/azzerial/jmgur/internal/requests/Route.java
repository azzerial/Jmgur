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
 * https://github.com/DV8FromTheWorld/JDA/blob/master/src/main/java/net/dv8tion/jda/internal/requests/Route.java
 * All credit goes to the original authors.
 */

package net.azzerial.jmgur.internal.requests;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.azzerial.jmgur.internal.utils.Check;
import net.azzerial.jmgur.internal.utils.Helper;
import org.jetbrains.annotations.NotNull;

import static net.azzerial.jmgur.internal.requests.AuthorizationHeader.*;
import static net.azzerial.jmgur.internal.requests.Method.*;

public final class Route {

    /* API Endpoints */

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class AccountEndpoints {

        /* --- Profiles --- */

        public static final Route GET_USER_PROFILE = new Route(GET, BEARER, "3/account/{username}");
        public static final Route GET_USER_GALLERY_PROFILE = new Route(GET, BEARER, "3/account/{username}/gallery_profile");

        /* --- Blocks --- */

        public static final Route GET_SELF_BLOCK_STATUS = new Route(GET, BEARER, "account/v1/{username}/block");
        public static final Route GET_SELF_BLOCKS = new Route(GET, BEARER, "3/account/me/block");
        public static final Route POST_SELF_BLOCK_CREATE = new Route(POST, BEARER, "account/v1/{username}/block");
        public static final Route DELETE_SELF_BLOCK_CREATE = new Route(DELETE, BEARER, "account/v1/{username}/block");

        /* --- Resources --- */

        public static final Route GET_USER_GALLERY_FAVORITES = new Route(GET, BEARER, "3/account/{username}/gallery_favorites/(page)/(favorites_sort)");
        public static final Route GET_USER_FAVORITES = new Route(GET, BEARER, "3/account/{username}/favorites/(page)/(favorites_sort)");
        public static final Route GET_USER_SUBMISSIONS = new Route(GET, BEARER, "3/account/{username}/submissions/(page)");

        /* --- Avatars --- */

        public static final Route GET_USER_AVAILABLE_AVATARS = new Route(GET, BEARER, "3/account/{username}/available_avatars");
        public static final Route GET_USER_AVATAR = new Route(GET, BEARER, "3/account/{username}/avatar");

        /* --- Settings --- */

        public static final Route GET_SELF_SETTINGS = new Route(GET, BEARER, "3/account/me/settings");
        public static final Route POST_SELF_SETTINGS = new Route(POST, BEARER, "3/account/me/settings");

        /* --- Albums --- */

        public static final Route GET_USER_ALBUMS = new Route(GET, BEARER, "3/account/{username}/albums/(page)");
        public static final Route GET_USER_ALBUM = new Route(GET, BEARER, "3/account/{username}/album/{album_hash}");
        public static final Route GET_USER_ALBUM_IDS = new Route(GET, BEARER, "3/account/{username}/albums/ids/(page)");
        public static final Route GET_USER_ALBUM_COUNT = new Route(GET, BEARER, "3/account/{username}/albums/count");
        public static final Route DELETE_USER_ALBUM = new Route(DELETE, BEARER, "3/account/{username}/album/{delete_hash}");

        /* --- Comments --- */

        public static final Route GET_USER_COMMENTS = new Route(GET, BEARER, "3/account/{username}/comments/(comment_sort)/(page)");
        public static final Route GET_USER_COMMENT = new Route(GET, BEARER, "3/account/{username}/comment/{comment_id}");
        public static final Route GET_USER_COMMENT_IDS = new Route(GET, BEARER, "3/account/{username}/comments/ids/(comment_sort)/(page)");
        public static final Route GET_USER_COMMENT_COUNT = new Route(GET, BEARER, "3/account/{username}/comments/count");
        public static final Route DELETE_SELF_COMMENT = new Route(DELETE, BEARER, "3/account/{username}/comment/{comment_id}");

        /* --- Images --- */

        public static final Route GET_SELF_IMAGES = new Route(GET, BEARER, "3/account/{username}/images/(page)");
        public static final Route GET_USER_IMAGE = new Route(GET, BEARER, "3/account/{username}/image/{image_hash}");
        public static final Route GET_USER_IMAGE_IDS = new Route(GET, BEARER, "3/account/{username}/images/ids/(page)");
        public static final Route GET_USER_IMAGE_COUNT = new Route(GET, BEARER, "3/account/{username}/images/count");
        public static final Route DELETE_USER_IMAGE = new Route(DELETE, BEARER, "3/account/{username}/image/{delete_hash}");
    }

    private final Method method;
    private final AuthorizationHeader authHeader;
    private final String route;
    private final int majorParamCount;
    private final int optionalParamCount;

    /* Static Constructors */

    @NotNull
    public static Route delete(@NotNull AuthorizationHeader authHeader, @NotNull String route) {
        return custom(DELETE, authHeader, route);
    }

    @NotNull
    public static Route get(@NotNull AuthorizationHeader authHeader, @NotNull String route) {
        return custom(GET, authHeader, route);
    }

    @NotNull
    public static Route post(@NotNull AuthorizationHeader authHeader, @NotNull String route) {
        return custom(POST, authHeader, route);
    }

    @NotNull
    public static Route put(@NotNull AuthorizationHeader authHeader, @NotNull String route) {
        return custom(PUT, authHeader, route);
    }

    @NotNull
    public static Route custom(@NotNull Method method, @NotNull AuthorizationHeader authHeader, @NotNull String route) {
        return new Route(method, authHeader, route);
    }

    /* Constructors */

    private Route(@NotNull Method method, @NotNull AuthorizationHeader authHeader, @NotNull String route) {
        Check.notNull(method, "method");
        Check.notNull(authHeader, "authHeader");
        Check.notEmpty(route, "route");
        Check.noWhitespace(route, "route");
        this.method = method;
        this.authHeader = authHeader;
        this.route = route;
        this.majorParamCount = Helper.countMatches(route, '{');
        this.optionalParamCount = Helper.countMatches(route, '(');

        Check.check(
            majorParamCount == Helper.countMatches(route, '}'),
            "An argument does not have both {}'s for route: %s %s",
            method, route
        );
        Check.check(
            optionalParamCount == Helper.countMatches(route, ')'),
            "An argument does not have both ()'s for route: %s %s",
            method, route
        );
    }

    /* Getters & Setters */

    @NotNull
    public Method getMethod() {
        return method;
    }

    @NotNull
    public AuthorizationHeader getAuthHeader() {
        return authHeader;
    }

    @NotNull
    public String getRoute() {
        return route;
    }

    public int getMajorParamCount() {
        return majorParamCount;
    }

    public int getOptionalParamCount() {
        return optionalParamCount;
    }

    /* Methods */

    @NotNull
    public CompiledRoute compile(@NotNull String... params) {
        Check.check(
            params.length >= majorParamCount,
            "Error Compiling Route: [%s], incorrect amount of parameters provided. Expected: %d (+ %d), Provided: %d",
            route, majorParamCount, optionalParamCount, params.length
        );
        Check.check(
            params.length <= majorParamCount + optionalParamCount,
            "Error Compiling Route: [%s], incorrect amount of parameters provided. Expected: %d (+ %d), Provided: %d",
            route, majorParamCount, optionalParamCount, params.length
        );
        final StringBuilder compiledRoute = new StringBuilder(route);

        for (final String param : params) {
            int paramStart = compiledRoute.indexOf("{");
            int paramEnd;

            if (paramStart != -1)
                paramEnd = compiledRoute.indexOf("}");
            else {
                paramStart = compiledRoute.indexOf("(");
                paramEnd = compiledRoute.indexOf(")");
            }
            compiledRoute.replace(paramStart, paramEnd + 1, param);
        }
        return new CompiledRoute(this, compiledRoute.toString().replaceAll("/\\(.+?\\)", ""));
    }

    /* Inner Classes */

    public static final class CompiledRoute {

        private final Route baseRoute;
        private final String compiledRoute;
        private final boolean hasQueryParams;

        /* Constructors */

        private CompiledRoute(@NotNull Route baseRoute, @NotNull String compiledRoute) {
            this(baseRoute, compiledRoute, false);
        }

        private CompiledRoute(@NotNull Route baseRoute, @NotNull String compiledRoute, boolean hasQueryParams) {
            Check.notNull(baseRoute, "baseRoute");
            Check.notNull(compiledRoute, "compiledRoute");
            this.baseRoute = baseRoute;
            this.compiledRoute = compiledRoute;
            this.hasQueryParams = hasQueryParams;
        }

        /* Getters & Setters */

        @NotNull
        public Route getBaseRoute() {
            return baseRoute;
        }

        @NotNull
        public Method getMethod() {
            return baseRoute.method;
        }

        @NotNull
        public AuthorizationHeader getAuthHeader() {
            return baseRoute.authHeader;
        }

        @NotNull
        public String getCompiledRoute() {
            return compiledRoute;
        }

        /* Methods */

        @NotNull
        public CompiledRoute addQueryParams(@NotNull String... params) {
            Check.check(params.length >= 2, "params length must be at least 2");
            Check.check(params.length % 2 == 0, "params length must be a multiple of 2");
            final StringBuilder newRoute = new StringBuilder(compiledRoute);

            for (int i = 0; i < params.length; i++)
                newRoute.append(!hasQueryParams && i == 0 ? '?' : '&').append(params[i]).append('=').append(params[++i]);
            return new CompiledRoute(baseRoute, newRoute.toString(), true);
        }
    }
}
