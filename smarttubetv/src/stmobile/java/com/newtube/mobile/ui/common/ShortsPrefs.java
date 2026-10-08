package com.newtube.mobile.ui.common;

import android.content.Context;

import com.liskovsoft.smartyoutubetv2.common.misc.PhoneUi;
import com.liskovsoft.youtubeapi.service.internal.MediaServiceData;

/** Phone-only preference controlling whether Shorts are filtered from app lists. */
public final class ShortsPrefs {
    private static final String PREFS_NAME = "mobile_shorts";
    private static final String KEY_SHOW_SHORTS = "show_shorts";

    private ShortsPrefs() {
    }

    public static boolean showShorts(Context context) {
        return context.getApplicationContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .getBoolean(KEY_SHOW_SHORTS, false);
    }

    public static void setShowShorts(Context context, boolean show) {
        context.getApplicationContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(KEY_SHOW_SHORTS, show)
                .apply();
        PhoneUi.setShowShortsEnabled(show);
        if (show) {
            MediaServiceData.instance().setContentHidden(MediaServiceData.CONTENT_SHORTS_ALL, false);
        }
    }
}