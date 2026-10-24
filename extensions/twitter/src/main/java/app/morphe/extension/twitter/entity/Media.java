/*
 * Copyright (C) 2026 awt <https://github.com/crimera/piko>
 *
 * See the included NOTICE file for GPLv3 §7(b) terms that apply to this code.
 */

package app.morphe.extension.twitter.entity;

import app.morphe.extension.crimera.AwtUtils;

public class Media {
    // 0-img, 1-video
    public final int type;
    public final String url;
    public final String ext;
    public final String resolution;

    public Media(int type, String url, String ext, String resolution) {
        this.type = type;
        this.url = url;
        this.ext = ext;
        this.resolution = resolution;
    }

    @Override
    public String toString() {

        try {
            return "Media [type=" + this.type + ", url=" + this.url + ", ext=" + this.ext + "]";
        } catch (Exception e) {
            AwtUtils.logger(e);
            return e.getMessage();
        }
    }

}
