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

package net.azzerial.jmgur.internal;

import net.azzerial.jmgur.api.CommentRepository;
import net.azzerial.jmgur.api.Jmgur;
import net.azzerial.jmgur.api.entities.Comment;
import net.azzerial.jmgur.api.entities.dto.CommentInformationDTO;
import net.azzerial.jmgur.api.requests.restaction.RestAction;
import net.azzerial.jmgur.api.utils.data.DataObject;
import net.azzerial.jmgur.internal.entities.CommentInformationDTOImpl;
import net.azzerial.jmgur.internal.entities.EntityBuilder;
import net.azzerial.jmgur.internal.requests.Route;
import net.azzerial.jmgur.internal.requests.restaction.RestActionImpl;
import net.azzerial.jmgur.internal.utils.Check;
import okhttp3.MultipartBody;
import org.jetbrains.annotations.NotNull;

public class CommentRepositoryImpl implements CommentRepository {

    private final Jmgur api;

    /* Constructors */

    public CommentRepositoryImpl(@NotNull Jmgur api) {
        this.api = api;
    }

    /* Methods */

    @NotNull
    @Override
    public Jmgur getApi() {
        return api;
    }

    /* --- Core --- */

    @NotNull
    @Override
    public RestAction<Comment> getComment(long id) {
        Check.positive(id, "id");
        return new RestActionImpl<>(
            api,
            Route.CommentEndpoints.GET_COMMENT.compile(Long.toUnsignedString(id)),
            (req, res) -> {
                final EntityBuilder builder = api.getEntityBuilder();
                final DataObject obj = res.getObject().getObject("data");
                return builder.createComment(obj);
            }
        );
    }

    @NotNull
    @Override
    public RestAction<Long> postComment(@NotNull CommentInformationDTO dto) {
        Check.notNull(dto, "dto");
        final CommentInformationDTOImpl impl = (CommentInformationDTOImpl) dto;
        final MultipartBody.Builder body = new MultipartBody.Builder().setType(MultipartBody.FORM);

        if (!impl.getMap().containsKey("image_id"))
            throw new IllegalArgumentException("no post hash provided");
        if (!impl.getMap().containsKey("comment"))
            throw new IllegalArgumentException("no content provided");

        impl.getMap().forEach(body::addFormDataPart);

        return new RestActionImpl<>(
            api,
            Route.CommentEndpoints.POST_COMMENT_CREATION.compile(),
            impl.isEmpty() ? null : body.build(),
            (req, res) -> {
                final DataObject obj = res.getObject().getObject("data");
                return obj.getUnsignedLong("id", 0L);
            }
        );
    }

    @NotNull
    @Override
    public RestAction<Boolean> deleteComment(long id) {
        Check.positive(id, "id");
        return new RestActionImpl<>(
            api,
            Route.CommentEndpoints.DELETE_COMMENT.compile(Long.toUnsignedString(id)),
            (req, res) -> {
                final DataObject obj = res.getObject();
                return obj.getBoolean("data");
            }
        );
    }

    @NotNull
    @Override
    public RestAction<Comment> getCommentWithReplies(long id) {
        Check.positive(id, "id");
        return new RestActionImpl<>(
            api,
            Route.CommentEndpoints.GET_COMMENT_REPLIES.compile(Long.toUnsignedString(id)),
            (req, res) -> {
                final EntityBuilder builder = api.getEntityBuilder();
                final DataObject obj = res.getObject().getObject("data");
                return builder.createComment(obj);
            }
        );
    }

    @NotNull
    @Override
    public RestAction<Long> replyToComment(@NotNull CommentInformationDTO dto) {
        Check.notNull(dto, "dto");
        final CommentInformationDTOImpl impl = (CommentInformationDTOImpl) dto;
        final MultipartBody.Builder body = new MultipartBody.Builder().setType(MultipartBody.FORM);

        if (!impl.getMap().containsKey("image_id"))
            throw new IllegalArgumentException("no post hash provided");
        if (!impl.getMap().containsKey("comment"))
            throw new IllegalArgumentException("no content provided");
        if (!impl.getMap().containsKey("parent_id"))
            throw new IllegalArgumentException("no parent id provided");

        body.addFormDataPart("image_id", impl.getMap().get("image_id"));
        body.addFormDataPart("comment", impl.getMap().get("comment"));

        return new RestActionImpl<>(
            api,
            Route.CommentEndpoints.POST_COMMENT_REPLY.compile(impl.getMap().get("parent_id")),
            impl.isEmpty() ? null : body.build(),
            (req, res) -> {
                final DataObject obj = res.getObject().getObject("data");
                return obj.getUnsignedLong("id", 0L);
            }
        );
    }
}
