/*
 * Copyright (C) 2026 awt <https://github.com/crimera/piko>
 *
 * See the included NOTICE file for GPLv3 §7(b) terms that apply to this code.
 */

package app.morphe.extension.instagram.patches.devFlags;

import static app.morphe.extension.instagram.utils.IgStr.str;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import app.morphe.extension.crimera.AwtUtils;
import app.morphe.extension.instagram.constants.Constants;
import app.morphe.extension.instagram.utils.InstaUtils;

public class RecommendedFlags {
    private static final Set<Runnable> changeListeners = new CopyOnWriteArraySet<>();

    public static void addChangeListener(Runnable listener) {
        changeListeners.add(listener);
    }

    public static void removeChangeListener(Runnable listener) {
        changeListeners.remove(listener);
    }

    private static File getRecommendedFlagsFile() {
        Context context = AwtUtils.getContext();
        return new File(context.getFilesDir(), Constants.REC_FLAGS + ".json");
    }

    public static long getLastModified() {
        return getRecommendedFlagsFile().lastModified();
    }

    private static JSONArray readRecommendedFlags(boolean notifyErrors) {
        try {
            File recFlagsFile = getRecommendedFlagsFile();
            if (!recFlagsFile.exists()) {
                if (notifyErrors) AwtUtils.toast(str("awt_rec_flags_no_file"));
            } else {
                String data = AwtUtils.readFile(recFlagsFile);
                return new JSONArray(data);
            }
        } catch (Exception e) {
            AwtUtils.logger(e);
            if (notifyErrors) AwtUtils.toast(str("awt_rec_flags_read_error"));
        }
        return new JSONArray();
    }

    public static List<Flag> getFlags() {
        return getFlags(true);
    }

    public static List<Flag> getFlags(boolean notifyErrors) {
        JSONArray arr = readRecommendedFlags(notifyErrors);
        List<Flag> flags = new ArrayList<>();
        try {
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                flags.add(new Flag(obj));
            }
        } catch (Exception e) {
            AwtUtils.logger(e);
            if (notifyErrors) AwtUtils.toast("getFlags failed");
        }
        return flags;
    }

    public static void downloadRecommendedFlagsFile(Runnable onComplete) {
        String filename = Constants.REC_FLAGS + ".json";
        String host = Constants.AWT_MAPPINGS_PATH;
        InstaUtils.downloadFile(host, filename, getRecommendedFlagsFile(), () -> {
            for (Runnable listener : changeListeners) {
                listener.run();
            }
            if (onComplete != null) {
                onComplete.run();
            }
        });
    }
}
