/*
 * Copyright (C) 2026 awt <https://github.com/crimera/piko>
 *
 * See the included NOTICE file for GPLv3 §7(b) terms that apply to this code.
 */


package app.morphe.extension.instagram.constants;

import static app.morphe.extension.instagram.utils.IgStr.str;

import app.morphe.extension.crimera.constants.ExtensionStrings;

public class Constants {
    public static final String AWT = "awt";
    public static final String AWT_SETTINGS = AWT + "_settings";
    public static final String SHARED_PREF_NAME = AWT_SETTINGS;
    public static final String DEFAULT_AWT_FOLDER = "Awt-Instagram";
    public static final String DEFAULT_AUDIO_FOLDER = "Audio";
    public static final String DEFAULT_DM_FOLDER = "DM";
    public static final String DEFAULT_GIF_FOLDER = "Gif";
    public static final String INSTAGRAM_SHARE_LINK = "https://www.instagram.com/%s/%s/";
    // https://www.instagram.com/p/<short code>/
    // https://www.instagram.com/reel/<short code>/
    // https://www.instagram.com/stories/<user name>/<post id>
    public static final String REC_FLAGS = AWT + "_recommended_flags_v3";
    public static final String AWT_MAPPINGS_PATH = "https://github.com/crimera/piko/raw/refs/heads/dev/docs/mappings/";

    public static final String AB_SETTINGS_ICON = "SETTINGS_ICON";
    public static final String AB_CREATE = "CREATE";
    public static final String AB_NOTIFICATIONS = "NOTIFICATIONS";
    public static final String AB_GHOST_MODE_ICON = "GHOST_MODE_ICON";
    public static final String AB_PROFILE_INFO_ICON = "PROFILE_INFO_ICON";

    public static final String AWT_FRAGMENT_TITLE = "awt_frag_title";
    public static final String AWT_FRAGMENT_NAME = "awt_frag_name";

    public static final String AWT_FRAGMENT_SETTINGS = "awt_frag_settings";
    public static final String AWT_FRAGMENT_ADS = "awt_frag_ads";
    public static final String AWT_FRAGMENT_GHOST = "awt_frag_ghost";
    public static final String AWT_FRAGMENT_STORY = "awt_frag_story";
    public static final String AWT_FRAGMENT_LINKS = "awt_frag_links";
    public static final String AWT_FRAGMENT_DISTRACTION_FREE = "awt_frag_distraction_free";
    public static final String AWT_FRAGMENT_FOCUS_LOCK = "awt_frag_focus_lock";
    public static final String AWT_FRAGMENT_MISC = "awt_frag_misc";
    public static final String AWT_FRAGMENT_DOWNLOAD_MEDIA = "awt_frag_download_media";
    public static final String AWT_FRAGMENT_NAV_BTNS = "awt_frag_nav_btns";
    public static final String AWT_FRAGMENT_DEV_OPTIONS = "awt_frag_dev_options";
    public static final String AWT_FRAGMENT_ABOUT = "awt_frag_about";
    public static final String AWT_FRAGMENT_ACTION_BAR = "awt_frag_action_bar";
    public static final String AWT_FRAGMENT_DM = "awt_frag_dm";
    public static final String AWT_FRAGMENT_FILTER_CONTENT = "awt_frag_filter_content";
    public static final String AWT_FRAGMENT_REC_FLAGS = "awt_frag_rec_flags";
    public static final String AWT_FRAGMENT_INSTANTS = "awt_frag_instants";
    public static final String AWT_FRAGMENT_FONT = "awt_frag_font";

    public static void load() {
        ExtensionStrings.setDefaultAwtFolder(Constants.DEFAULT_AWT_FOLDER);
        ExtensionStrings.setDownloadOngoing(str("awt_downloading_media"));
        ExtensionStrings.setDownloadCompleted(str("awt_downloaded_media"));
        ExtensionStrings.setDownloadError(str("awt_download_failed_media"));
        ExtensionStrings.setDownloadSetPathFailed(str("awt_download_set_path_failed"));
        ExtensionStrings.setDownloadSetPathSuccess(str("awt_download_set_path_success"));
        ExtensionStrings.setDownloadGrantPermission(str("awt_download_choose_folder"));
        ExtensionStrings.setDownloadGrantPermissionFailed(str("awt_download_choose_folder_failed"));
    }
}
