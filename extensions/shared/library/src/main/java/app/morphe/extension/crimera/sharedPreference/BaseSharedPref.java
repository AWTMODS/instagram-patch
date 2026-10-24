/*
 * Copyright (C) 2026 awt <https://github.com/crimera/piko>
 *
 * See the included NOTICE file for GPLv3 §7(b) terms that apply to this code.
*/


package app.morphe.extension.crimera.sharedPreference;

import android.content.Context;
import java.util.Set;
import java.util.HashSet;
import org.json.JSONObject;

import app.morphe.extension.shared.Utils;
import app.morphe.extension.crimera.settings.BooleanSetting;
import app.morphe.extension.crimera.settings.StringSetting;
import app.morphe.extension.shared.settings.preference.AwtSharedPrefCategory;
import app.morphe.extension.crimera.constants.ExtensionStrings;

public abstract class BaseSharedPref {

    protected volatile AwtSharedPrefCategory sp;
    private final String sharedPrefName;

    protected BaseSharedPref(Context context, String sharedPrefName) {
        this.sharedPrefName = sharedPrefName;
        this.sp = (context != null)
                ? new AwtSharedPrefCategory(context, sharedPrefName)
                : null;
    }

    protected BaseSharedPref(String sharedPrefName) {
        this(null, sharedPrefName);
    }

    private AwtSharedPrefCategory preferences() {
        AwtSharedPrefCategory current = sp;
        if (current != null) {
            return current;
        }

        Context context = Utils.getContext();
        if (context == null) {
            return null;
        }

        synchronized (this) {
            if (sp == null) {
                sp = new AwtSharedPrefCategory(context, sharedPrefName);
            }
            return sp;
        }
    }

    public Boolean getBoolean(BooleanSetting setting) {
        Boolean defaultValue = setting.defaultValue;
        AwtSharedPrefCategory preferences = preferences();
        if (preferences != null) {
            return preferences.getBoolean(setting.key, defaultValue);
        }
        return defaultValue;
    }

    public Boolean setBoolean(String key, Boolean val) {
        try {
            AwtSharedPrefCategory preferences = preferences();
            if (preferences != null) {
                preferences.saveBoolean(key, val);
                return true;
            }
        } catch (Exception ex) {
            Utils.showToastShort(ex.toString());
        }
        return false;
    }

    public Boolean setString(String key, String val) {
        try {
            AwtSharedPrefCategory preferences = preferences();
            if (preferences != null) {
                preferences.saveString(key, val);
                return true;
            }
        } catch (Exception ex) {
            Utils.showToastShort(ex.toString());
        }
        return false;
    }

    public String getString(String key, String defaultValue) {
        AwtSharedPrefCategory preferences = preferences();
        if (preferences == null)
            return defaultValue;

        String value = preferences.getString(key, defaultValue);
        if (value.isBlank())
            return defaultValue;
        return value;
    }

    public String getString(StringSetting setting) {
        return SharedPref.getStringPref(setting.key, setting.defaultValue);
    }

    public Set<String> getSet(StringSetting stringSetting) {
        Set<String> defVal = new HashSet();
        AwtSharedPrefCategory preferences = preferences();
        if (preferences != null) {
            return preferences.getSet(stringSetting.key, defVal);
        }
        return defVal;
    }

    public Boolean setSet(String key, Set<String> value) {
        try {
            AwtSharedPrefCategory preferences = preferences();
            if (preferences != null) {
                preferences.saveSet(key, value);
                return true;
            }
        } catch (Exception ex) {
            Utils.showToastShort(ex.toString());
        }
        return false;
    }

    public boolean clear() {
        try {
            AwtSharedPrefCategory preferences = preferences();
            if (preferences != null) {
                preferences.clearAll();
                return true;
            }
        } catch (Exception ex) {
            Utils.showToastShort(ex.toString());
        }
        return false;
    }

    protected boolean flushPreferences() {
        try {
            AwtSharedPrefCategory preferences = preferences();
            if (preferences != null) {
                return preferences.preferences.edit().commit();
            }
        } catch (Exception ex) {
            Utils.showToastShort(ex.toString());
        }
        return false;
    }

    public JSONObject all() {
        try {
            AwtSharedPrefCategory preferences = preferences();
            if (preferences != null) {
                return preferences.getAll();
            }
        } catch (Exception ex) {
            Utils.showToastShort(ex.toString());
        }
        return null;
    }

}
