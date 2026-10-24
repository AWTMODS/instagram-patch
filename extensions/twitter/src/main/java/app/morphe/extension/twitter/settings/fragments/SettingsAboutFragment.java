/*
 * Copyright (C) 2026 awt <https://github.com/crimera/piko>
 *
 * See the included NOTICE file for GPLv3 §7(b) terms that apply to this code.
 */

package app.morphe.extension.twitter.settings.fragments;

import static app.morphe.extension.shared.StringRef.str;

import android.text.*;
import android.text.style.ForegroundColorSpan;
import android.graphics.Color;
import android.content.Context;
import android.os.Bundle;
import android.preference.*;
import android.view.View;
import android.widget.ListView;
import java.util.TreeMap;
import java.util.Map;

import app.morphe.extension.shared.Utils;
import com.twitter.ui.widget.LegacyTwitterPreferenceCategory;
import app.morphe.extension.twitter.settings.ActivityHook;
import app.morphe.extension.twitter.settings.SettingsStatus;
import app.morphe.extension.twitter.settings.Settings;
import app.morphe.extension.twitter.settings.SettingsSearchNavigator;
import app.morphe.extension.twitter.settings.widgets.Helper;

@SuppressWarnings("deprecation")
public class SettingsAboutFragment extends PreferenceFragment {
    private Context context;
    private Preference searchTargetPreference;

    @Override
    public void onResume() {
        super.onResume();
        ActivityHook.toolbar.setTitle(str("awt_pref_patch_info"));
    }

    @Override
    public void onCreate(@org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        Helper helper = new Helper(context);

        PreferenceManager preferenceManager = getPreferenceManager();
        PreferenceScreen screen = preferenceManager.createPreferenceScreen(context);
        LegacyTwitterPreferenceCategory verPref = preferenceCategory(str("awt_pref_version_info"), screen);

        String text = Settings.APP_VERSION;
        verPref.addPreference(
                helper.buttonPreference(
                        str(text),
                        Utils.getAppVersionName(),
                        text
                )
        );

        text = Settings.AWT_PATCHES;
        verPref.addPreference(
                helper.buttonPreference(
                        str(text),
                        Utils.getPatchesReleaseVersion(),
                        text
                )
        );

        text = Settings.CHANGELOGS_TITLE;
        verPref.addPreference(
                helper.buttonPreference(
                        str(text),
                        "",
                        text
                )
        );

        text = Settings.SUPPORTED_LINKS;
        verPref.addPreference(
                helper.buttonPreference(
                        str(text),
                        "",
                        text
                )
        );

        verPref.addPreference(
                helper.switchPreference(
                        str("awt_debug"),
                        "",
                        Settings.AWT_DEBUG
                )
        );

        TreeMap<String,Boolean> flags = new TreeMap<>();
        flags.put(str("awt_pref_customisation_more_info_on_profile"),SettingsStatus.moreInfoOnProfile);
        flags.put(str("awt_pref_external_downloader_toggle"),SettingsStatus.externalDownloader);
        flags.put(str("awt_title_native_share_menu"),SettingsStatus.enableNativeShareMenu);
        flags.put(str("awt_pref_video_download"),SettingsStatus.enableVidDownload);
        flags.put(str("awt_pref_undo_posts"),SettingsStatus.enableUndoPosts);
        flags.put(str("tab_customization_screen_title"),SettingsStatus.navBarCustomisation);
        flags.put(str("awt_pref_download"),SettingsStatus.changeDownloadEnabled);
        flags.put(str("awt_pref_download_media_link_handle"),SettingsStatus.mediaLinkHandle);
        flags.put(str("awt_pref_hide_promoted_posts"),SettingsStatus.hideAds);
        flags.put(str("awt_pref_wtf_section"),SettingsStatus.hideWTF);
        flags.put(str("awt_pref_cts_section"),SettingsStatus.hideCTS);
        flags.put(str("awt_pref_ctj_section"),SettingsStatus.hideCTJ);
        flags.put(str("awt_pref_ryb_section"),SettingsStatus.hideRBMK);
        flags.put(str("awt_pref_pinned_posts_section"),SettingsStatus.hideRPinnedPosts);
        flags.put(str("awt_pref_hide_unrelated_replies"),SettingsStatus.hideDetailedPosts);
        flags.put(str("awt_pref_chirp_font"),SettingsStatus.enableFontMod);
        flags.put(str("awt_pref_hide_fab"),SettingsStatus.hideFAB);
        flags.put(str("awt_pref_hide_fab_menu"),SettingsStatus.hideFABBtns);
        flags.put(str("awt_pref_show_sensitive_media"),SettingsStatus.showSensitiveMedia);
        flags.put(str("awt_pref_selectable_text"),SettingsStatus.selectableText);
        flags.put(str("awt_pref_rec_users"),SettingsStatus.hideRecommendedUsers);
        flags.put(str("awt_pref_custom_share_domain"),SettingsStatus.customSharingDomainEnabled);
        flags.put(str("awt_pref_feature_flags"),SettingsStatus.featureFlagsEnabled);
        flags.put(str("awt_pref_customisation_profiletabs"),SettingsStatus.profileTabCustomisation);
        flags.put(str("awt_pref_customisation_timelinetabs"),SettingsStatus.timelineTabCustomisation);
        flags.put(str("awt_pref_customisation_exploretabs"),SettingsStatus.exploreTabCustomisation);
        flags.put(str("awt_pref_customisation_navbartabs"),SettingsStatus.navBarCustomisation);
        flags.put(str("awt_pref_customisation_sidebartabs"),SettingsStatus.sideBarCustomisation);
        flags.put(str("awt_pref_disable_auto_timeline_scroll"),SettingsStatus.disableAutoTimelineScroll);
        flags.put(str("awt_pref_hide_live_threads"),SettingsStatus.hideLiveThreads);
        flags.put(str("awt_pref_hide_view_count"),SettingsStatus.hideViewCount);
        flags.put(str("awt_pref_hide_banner"),SettingsStatus.hideBanner);
        flags.put(str("awt_pref_hide_bmk_timeline"),SettingsStatus.hideInlineBmk);
        flags.put(str("awt_pref_show_poll_result"),SettingsStatus.showPollResultsEnabled);
        flags.put(str("community_notes_title"),SettingsStatus.hideCommunityNote);
        flags.put(str("awt_pref_hide_quick_promote"),SettingsStatus.hidePromoteButton);
        flags.put(str("awt_pref_hide_immersive_player"),SettingsStatus.hideImmersivePlayer);
        flags.put(str("awt_pref_clear_tracking_params"),SettingsStatus.cleartrackingparams);
        flags.put(str("awt_pref_unshorten_link"),SettingsStatus.unshortenlink);
        flags.put(str("awt_pref_force_translate"),SettingsStatus.forceTranslate);
        flags.put(str("awt_pref_round_off_numbers"),SettingsStatus.roundOffNumbers);
        flags.put(str("awt_pref_customisation_inlinetabs"),SettingsStatus.inlineBarCustomisation);
        flags.put(str("awt_pref_debug_menu"),SettingsStatus.enableDebugMenu);
        flags.put(str("awt_pref_hide_premium_prompt"),SettingsStatus.hidePremiumPrompt);
        flags.put(str("awt_pref_hide_hidden_replies"),SettingsStatus.hideHiddenReplies);
        flags.put(str("awt_pref_del_from_db"),SettingsStatus.deleteFromDb);
        flags.put(str("awt_title_native_downloader"),SettingsStatus.nativeDownloader);
        flags.put(str("awt_pref_native_downloader_inline_button"),SettingsStatus.inlineDownloadButton);
        flags.put(str("awt_share_image_title"),SettingsStatus.shareImage);
        flags.put(str("awt_browse_object_title"),SettingsStatus.browseObject);
        flags.put(str("awt_title_native_reader_mode"),SettingsStatus.nativeReaderMode);
        flags.put(str("awt_pref_enable_vid_auto_advance"),SettingsStatus.enableVidAutoAdvance);
        flags.put(str("awt_pref_enable_force_pip"),SettingsStatus.enableForcePip);
        flags.put(str("awt_pref_hide_premium_upsell"),SettingsStatus.removePremiumUpsell);
        flags.put(str("awt_pref_customisation_reply_sorting"),SettingsStatus.defaultReplySortFilter);
        flags.put(str("awt_pref_force_hd"),SettingsStatus.enableForceHD);
        flags.put(str("awt_pref_hide_nudge_button"),SettingsStatus.hideNudgeButton);
        flags.put(str("awt_pref_hide_social_proof"),SettingsStatus.hideSocialProof);
        flags.put(str("awt_pref_hide_community_badge"),SettingsStatus.hideCommBadge);
        flags.put(str("awt_title_native_translator"),SettingsStatus.nativeTranslator);
        flags.put(str("awt_pref_customisation_post_font_size"),SettingsStatus.customPostFontSize);
        flags.put(str("awt_pref_top_people_search"),SettingsStatus.hideTopPeopleSearch);
        flags.put(str("awt_pref_customisation_searchtabs"),SettingsStatus.searchTabCustomisation);
        flags.put(str("awt_pref_hide_todays_news"),SettingsStatus.hideTodaysNews);
        flags.put(str("awt_pref_server_response_logging"),SettingsStatus.serverResponseLogging);
        flags.put(str("awt_pref_show_post_source"),SettingsStatus.showSourceLabel);
        flags.put(str("awt_changelogs_title"),SettingsStatus.showChangelogsPatchEnabled);
        flags.put(str("awt_pref_pause_search_suggestion"),SettingsStatus.pauseSearchSuggestions);
        flags.put(str("awt_pref_search_suggestion"),SettingsStatus.removeSearchSuggestions);
        flags.put(str("awt_pref_customisation_change_app_icon"),SettingsStatus.appIconCustomisation);
        flags.put(str("awt_pref_hide_badge_nav_bar"),SettingsStatus.hideNavbarBadge);
        flags.put(str("awt_pref_hide_post_inline_metrics"),SettingsStatus.hidePostMetrics);
        flags.put(str("awt_legacy_share_link"),SettingsStatus.legacyShareLink);
        flags.put(str("awt_pref_export_login_token"),SettingsStatus.exportLoginToken);
        flags.put(str("awt_block_redirecting_to_x_lite"),SettingsStatus.blockRedirectingToXLite);
        flags.put(str("awt_pref_block_update_screen"),SettingsStatus.blockUpdateScreen);

        LegacyTwitterPreferenceCategory patPref = preferenceCategory(str("awt_pref_patches"), screen);

        for (Map.Entry<String, Boolean> entry : flags.entrySet()) {
            String resName = entry.getKey();
            boolean sts = (boolean) entry.getValue();

            patPref.addPreference(
                    buttonPreference(
                            resName,
                            sts,
                            str("awt_pref_patches")
                    )
            );
        }

        setPreferenceScreen(screen);
        searchTargetPreference = SettingsSearchNavigator.findTargetPreference(screen, getArguments());

    }

    @Override
    public void onActivityCreated(@org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        scrollToSearchTarget();
    }

    private void scrollToSearchTarget() {
        if (searchTargetPreference == null) {
            return;
        }

        View view = getView();
        if (view == null) {
            return;
        }

        ListView listView = view.findViewById(android.R.id.list);
        if (listView != null) {
            SettingsSearchNavigator.scrollToPreferenceAndHighlight(listView, searchTargetPreference);
        }

    }

    private LegacyTwitterPreferenceCategory preferenceCategory(String title, PreferenceScreen screen) {
        LegacyTwitterPreferenceCategory preferenceCategory = new LegacyTwitterPreferenceCategory(context);
        preferenceCategory.setTitle(title);
        screen.addPreference(preferenceCategory);
        return preferenceCategory;
    }

    private Preference buttonPreference(String title, Boolean inc, String key) {
        String summary = inc ? str("awt_pref_included"):str("awt_pref_excluded");
        String colorHex = inc ? "#008FC4":"#DE0025";
        Preference preference = new Preference(context);
        preference.setKey(key);
        preference.setTitle(title);
        Spannable summarySpan = new SpannableString(summary);
        summarySpan.setSpan(new ForegroundColorSpan(Color.parseColor(colorHex)), 0, summary.length(), 0);
        preference.setSummary(summarySpan);
        return preference;
    }
}
