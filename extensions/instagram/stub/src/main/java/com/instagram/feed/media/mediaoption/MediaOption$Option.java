package com.instagram.feed.media.mediaoption;

public class MediaOption$Option {
    public final int iconDrawable;

    public MediaOption$Option(String str, int i, int i2) {
        this.iconDrawable = i2;
    }

    public static final MediaOption$Option AWT_DOWNLOAD = new MediaOption$Option("", 0, 0);
    public static final MediaOption$Option AWT_MORE_POST_OPTION = new MediaOption$Option("", 0, 0);
    public static final MediaOption$Option AWT_DEBUG = new MediaOption$Option("", 0, 0);
    public static final MediaOption$Option AWT_EXTERNAL_DOWNLOADER = new MediaOption$Option("", 0, 0);

    public static final MediaOption$Option PIKO_DOWNLOAD = AWT_DOWNLOAD;
    public static final MediaOption$Option PIKO_MORE_POST_OPTION = AWT_MORE_POST_OPTION;
    public static final MediaOption$Option PIKO_DEBUG = AWT_DEBUG;
    public static final MediaOption$Option PIKO_EXTERNAL_DOWNLOADER = AWT_EXTERNAL_DOWNLOADER;

    public static final MediaOption$Option[] $values(){
        return null;
    }
}