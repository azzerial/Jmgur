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

package net.azzerial.jmgur.internal.entities;

import lombok.Setter;
import net.azzerial.jmgur.api.Jmgur;
import net.azzerial.jmgur.api.entities.GalleryProfile;
import net.azzerial.jmgur.api.entities.Trophy;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Setter
public final class GalleryProfileImpl implements GalleryProfile {

    private final Jmgur api;

    private List<Trophy> trophies;
    private int totalGalleryComments;
    private int totalGalleryFavorites;
    private int totalGallerySubmissions;

    /* Constructors */

    GalleryProfileImpl(@NotNull Jmgur api) {
        this.api = api;
    }

    /* Getters & Setters */

    @NotNull
    @Override
    public Jmgur getApi() {
        return api;
    }

    @NotNull
    @Override
    public List<Trophy> getTrophies() {
        return trophies;
    }

    @Override
    public int getTotalGalleryComments() {
        return totalGalleryComments;
    }

    @Override
    public int getTotalGalleryFavorites() {
        return totalGalleryFavorites;
    }

    @Override
    public int getTotalGallerySubmissions() {
        return totalGallerySubmissions;
    }

    /* Methods */

    @Override
    public String toString() {
        return "GalleryProfile{" +
            "trophies=" + trophies +
            ", totalGalleryComments=" + totalGalleryComments +
            ", totalGalleryFavorites=" + totalGalleryFavorites +
            ", totalGallerySubmissions=" + totalGallerySubmissions +
            '}';
    }
}
