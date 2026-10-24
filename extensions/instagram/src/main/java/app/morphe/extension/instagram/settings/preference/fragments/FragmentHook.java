/*
 * Copyright (C) 2026 awt <https://github.com/crimera/piko>
 *
 * See the included NOTICE file for GPLv3 §7(b) terms that apply to this code.
*/

package app.morphe.extension.instagram.settings.preference.fragments;


import android.content.Context;

import app.morphe.extension.crimera.AwtUtils;
import app.morphe.extension.instagram.settings.ActivityHook;
import app.morphe.extension.instagram.constants.Constants;

@SuppressWarnings("deprecation")
public class FragmentHook {

    public static void startFragment(String key){
        String actionBarTitleKey = null;

        if(key.equals(Constants.AWT_FRAGMENT_ADS)){
            actionBarTitleKey = "awt_category_ads";
        }else if(key.equals(Constants.AWT_FRAGMENT_GHOST)){
            actionBarTitleKey = "awt_category_ghost";
        }else if(key.equals(Constants.AWT_FRAGMENT_STORY)){
            actionBarTitleKey = "awt_category_story";
        }else if(key.equals(Constants.AWT_FRAGMENT_LINKS)){
            actionBarTitleKey = "awt_category_links";
        }else if(key.equals(Constants.AWT_FRAGMENT_DISTRACTION_FREE)){
            actionBarTitleKey = "awt_category_distraction_free";
        }else if(key.equals(Constants.AWT_FRAGMENT_FOCUS_LOCK)){
            actionBarTitleKey = "awt_focus_lock";
        }else if(key.equals(Constants.AWT_FRAGMENT_MISC)){
            actionBarTitleKey = "awt_category_misc";
        }else if(key.equals(Constants.AWT_FRAGMENT_DOWNLOAD_MEDIA)){
            actionBarTitleKey = "awt_category_downloads";
        }else if(key.equals(Constants.AWT_FRAGMENT_NAV_BTNS)){
            actionBarTitleKey = "awt_category_navigation_tabs";
        }else if(key.equals(Constants.AWT_FRAGMENT_DEV_OPTIONS)){
            actionBarTitleKey = "awt_category_dev_options";
        }else if(key.equals(Constants.AWT_FRAGMENT_ABOUT)){
            actionBarTitleKey = "awt_category_about";
        }else if(key.equals(Constants.AWT_FRAGMENT_ACTION_BAR)){
            actionBarTitleKey = "awt_category_action_bar";
        }else if(key.equals(Constants.AWT_FRAGMENT_DM)){
            actionBarTitleKey = "awt_category_dm";
        }else if(key.equals(Constants.AWT_FRAGMENT_FILTER_CONTENT)){
            actionBarTitleKey = "awt_category_filter_content";
        }else if(key.equals(Constants.AWT_FRAGMENT_REC_FLAGS)){
            actionBarTitleKey = "awt_category_rec_flags";
        }else if(key.equals(Constants.AWT_FRAGMENT_INSTANTS)){
            actionBarTitleKey = "awt_instants_title";
        }else if(key.equals(Constants.AWT_FRAGMENT_FONT)){
            actionBarTitleKey = "awt_category_font";
        }

        if(actionBarTitleKey!=null){
            ActivityHook.startAwtActivity(key,actionBarTitleKey);
        }
    }

    public static void startSettings(){
        ActivityHook.startAwtActivity(Constants.AWT_FRAGMENT_SETTINGS,"awt_title_settings");
    }

}
