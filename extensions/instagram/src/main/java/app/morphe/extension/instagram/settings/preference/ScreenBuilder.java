/*
 * Copyright (C) 2026 awt <https://github.com/crimera/piko>
 *
 * See the included NOTICE file for GPLv3 §7(b) terms that apply to this code.
 */


package app.morphe.extension.instagram.settings.preference;

import static app.morphe.extension.instagram.utils.IgStr.str;

import android.content.Context;
import android.preference.PreferenceScreen;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.text.format.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TreeMap;
import java.util.Map;
import java.util.List;
import java.util.Locale;

import app.morphe.extension.instagram.patches.devFlags.RecommendedFlags;
import app.morphe.extension.instagram.patches.devFlags.Flag;

import app.morphe.extension.crimera.downloader.StorageUtils;
import app.morphe.extension.crimera.sharedPreference.SharedPref;
import app.morphe.extension.instagram.patches.Links;
import app.morphe.extension.instagram.patches.customise.font.FontStorage;
import app.morphe.extension.instagram.patches.focusLock.FocusLock;
import app.morphe.extension.instagram.patches.focusLock.FocusLockTargets;
import app.morphe.extension.instagram.settings.SettingsStatus;
import app.morphe.extension.instagram.settings.BooleanSetting;
import app.morphe.extension.instagram.settings.Settings;
import app.morphe.extension.instagram.settings.preference.widgets.*;
import app.morphe.extension.shared.Utils;
import app.morphe.extension.instagram.utils.Pref;
import app.morphe.extension.instagram.constants.Constants;
import app.morphe.extension.instagram.theme.MaterialYouTheme;

public class ScreenBuilder {
    private final Context context;
    private final PreferenceScreen screen;
    private final Helper helper;

    public ScreenBuilder(Context context, PreferenceScreen screen, Helper helper) {
        this.context = context;
        this.screen = screen;
        this.helper = helper;
    }

    public void buildFragment(String fragmentName) {
        if (Constants.AWT_FRAGMENT_SETTINGS.equals(fragmentName)) {
            buildSettingsPage();
        } else if (Constants.AWT_FRAGMENT_ADS.equals(fragmentName)) {
            buildAdsSection();
        } else if (Constants.AWT_FRAGMENT_GHOST.equals(fragmentName)) {
            ghostSection();
        } else if (Constants.AWT_FRAGMENT_STORY.equals(fragmentName)) {
            storySection();
        } else if (Constants.AWT_FRAGMENT_LINKS.equals(fragmentName)) {
            linksSection();
        } else if (Constants.AWT_FRAGMENT_DISTRACTION_FREE.equals(fragmentName)) {
            distractionFreeSection();
        } else if (Constants.AWT_FRAGMENT_MISC.equals(fragmentName)) {
            buildMiscSection();
        } else if (Constants.AWT_FRAGMENT_DOWNLOAD_MEDIA.equals(fragmentName)) {
            buildDownloadSection();
        } else if (Constants.AWT_FRAGMENT_NAV_BTNS.equals(fragmentName)) {
            buildNavigationSection();
        } else if (Constants.AWT_FRAGMENT_DEV_OPTIONS.equals(fragmentName)) {
            buildDeveloperSection();
        } else if (Constants.AWT_FRAGMENT_ABOUT.equals(fragmentName)) {
            aboutSection(SettingsStatus.FLAGS);
        } else if (Constants.AWT_FRAGMENT_ACTION_BAR.equals(fragmentName)) {
            buildActionBarSection();
        } else if (Constants.AWT_FRAGMENT_DM.equals(fragmentName)) {
            dmSection();
        } else if (Constants.AWT_FRAGMENT_FILTER_CONTENT.equals(fragmentName)) {
            filterContentSection();
        } else if (Constants.AWT_FRAGMENT_REC_FLAGS.equals(fragmentName)) {
            buildRecommendedFlagsSection();
        } else if (Constants.AWT_FRAGMENT_FOCUS_LOCK.equals(fragmentName)) {
            buildFocusLockSection();
        } else if (Constants.AWT_FRAGMENT_INSTANTS.equals(fragmentName)) {
            buildInstantsSection();
        } else if (Constants.AWT_FRAGMENT_FONT.equals(fragmentName)) {
            buildFontSection();
        }
    }

    public void buildFragmentForSearch(String fragmentName) {
        if (Constants.AWT_FRAGMENT_REC_FLAGS.equals(fragmentName)) {
            buildRecommendedFlagsSection(false);
        } else {
            buildFragment(fragmentName);
        }
    }

    private void addPreference(Preference pref) {
        addPreference(null, pref);
    }

    // Adding preference category might be used in the future
    // to segregate the preference inside the fragment
    private void addPreference(PreferenceCategory category, Preference pref) {
        if (category != null) {
            category.addPreference(pref);
        } else {
            screen.addPreference(pref);
        }
    }

    private PreferenceCategory addCategory(String title) {
        CategoryPref preferenceCategory = new CategoryPref(context);
        preferenceCategory.setFirstCategory(screen.getPreferenceCount() == 0);
        preferenceCategory.setTitle(title);
        screen.addPreference(preferenceCategory);
        return preferenceCategory;
    }

    public void buildAdsSection() {
        if (!(SettingsStatus.adsSection())) return;

        if (SettingsStatus.disableAds) {
            addPreference(
                    helper.switchPreference(
                            str("awt_disable_ads"),
                            "",
                            Settings.DISABLE_ADS
                    )
            );
        }
        if (SettingsStatus.hideSuggestedContent) {
            addPreference(
                    helper.switchPreference(
                            str("awt_hide_suggested_content"),
                            str("awt_hide_suggested_content_desc"),
                            Settings.HIDE_SUGGESTED_CONTENT
                    )
            );
        }
    }

    public void buildFontSection() {
        if (!(SettingsStatus.fontSection())) return;

        // Written here rather than left to the preference framework, which persists only after
        // this listener has run - FontStorage.useSystemFont() has to read the new value straight
        // away, from load() on the next start up.
        SwitchPref useSystemFont = new SwitchPref(context);
        useSystemFont.setTitle(str("awt_use_system_font"));
        useSystemFont.setSummary(str("awt_use_system_font_desc"));
        useSystemFont.setKey(Settings.USE_SYSTEM_FONT.key);
        useSystemFont.setPersistent(false);
        useSystemFont.setChecked(FontStorage.useSystemFont());
        useSystemFont.setOnPreferenceChangeListener((preference, newValue) -> {
            FontStorage.setUseSystemFont(Boolean.TRUE.equals(newValue));
            return true;
        });
        addPreference(useSystemFont);

        addPreference(helper.buttonPreference(
                str("awt_pref_add_font"),
                str("awt_pref_add_font_desc"),
                "awt_pref_add_font"
        ));

        addPreference(helper.buttonPreference(
                str("awt_pref_delete_font"),
                "",
                "awt_pref_delete_font"
        ));
    }

    public void buildDeveloperSection() {
        if (!(SettingsStatus.developerOptionsSection())) return;

        // PreferenceCategory category= addCategory(str("awt_category_dev_options"));
        if (SettingsStatus.recommendedFlags) {
            addPreference(
                    helper.buttonPreference(
                            str("awt_category_rec_flags"),
                            str("awt_category_rec_flags_desc"),
                            Constants.AWT_FRAGMENT_REC_FLAGS
                    )
            );
        }

        if (SettingsStatus.enableDeveloperOptions) {
            addPreference(
                    helper.switchPreference(
                            str("awt_enable_dev_options"),
                            str("awt_enable_dev_options_desc"),
                            Settings.DEVELOPER_OPTIONS
                    )
            );
            addPreference(
                    helper.availableWhen(
                            helper.switchPreference(
                                    str("awt_directly_open_metaconfig"),
                                    str("awt_directly_open_metaconfig_desc"),
                                    Settings.DIRECTLY_OPEN_METACONFIG
                            ),
                            () -> SharedPref.getBooleanPref(Settings.DEVELOPER_OPTIONS)
                    )
            );
            addPreference(
                    helper.buttonPreference(
                            str("awt_export_dev_overrides"),
                            "",
                            "awt_export_dev_overrides"
                    )
            );
            addPreference(
                    helper.buttonPreference(
                            str("awt_import_dev_overrides"),
                            "",
                            "awt_import_dev_overrides"
                    )
            );
            addPreference(
                    helper.buttonPreference(
                            str("awt_import_id_mapping"),
                            "",
                            "awt_import_id_mapping"
                    )
            );
            addPreference(
                    helper.buttonPreference(
                            str("awt_download_id_mapping"),
                            "",
                            "awt_download_id_mapping"
                    )
            );

            if(Pref.awtDebug()) {
                addPreference(
                        helper.buttonPreference(
                                str("awt_export_experiment_list"),
                                "",
                                "awt_export_experiment_list"
                        )
                );

                addPreference(
                        helper.buttonPreference(
                                str("awt_export_experiment_mappings"),
                                "",
                                "awt_export_experiment_mappings"
                        )
                );
            }
        }
    }

    public void dmSection() {
        if (!(SettingsStatus.dmSection())) return;

        if (SettingsStatus.disableDisappearingMessagesGesture) {
            addPreference(
                    helper.switchPreference(
                            str("awt_disable_disappearing_messages_gesture"),
                            str("awt_disable_disappearing_messages_gesture_desc"),
                            Settings.DISABLE_DISAPPEARING_MESSAGES_GESTURE
                    )
            );
        }

        if (SettingsStatus.unlimitedReplaysOnEphemeralMedia) {
            addPreference(
                    helper.switchPreference(
                            str("awt_unlimited_replays"),
                            str("awt_unlimited_replays_desc"),
                            Settings.UNLIMITED_REPLAYS
                    )
            );
            addPreference(
                    helper.switchPreference(
                            str("awt_ephemeral_caption"),
                            str("awt_ephemeral_caption_desc"),
                            Settings.EPHEMERAL_MEDIA_CAPTION
                    )
            );
        }

        if (SettingsStatus.markChatAsRead) {
            addPreference(
                    helper.switchPreference(
                            str("awt_enable_mark_chat_as_read"),
                            str("awt_enable_mark_chat_as_read_desc"),
                            Settings.ENABLE_MARK_CHAT_AS_READ
                    )
            );
        }

        if (SettingsStatus.inboxLock) {
            addPreference(
                    helper.switchPreference(
                            str("awt_inbox_lock"),
                            str("awt_inbox_lock_desc"),
                            Settings.INBOX_LOCK
                    )
            );
            addPreference(
                    helper.availableWhen(
                            helper.switchPreference(
                                    str("awt_inbox_lock_notifications"),
                                    str("awt_inbox_lock_notifications_desc"),
                                    Settings.INBOX_LOCK_NOTIFICATIONS
                            ),
                            () -> SharedPref.getBooleanPref(Settings.INBOX_LOCK)
                    )
            );
        }

        if (SettingsStatus.hiddenChats) {
            addPreference(
                    helper.switchPreference(
                            str("awt_hidden_chats"),
                            str("awt_hidden_chats_desc"),
                            Settings.HIDDEN_CHATS
                    )
            );
            addPreference(
                    helper.availableWhen(
                            helper.listPreference(
                                    str("awt_hidden_chats_taps"),
                                    str("awt_hidden_chats_taps_desc"),
                                    Settings.HIDDEN_CHATS_TAPS
                            ),
                            () -> SharedPref.getBooleanPref(Settings.HIDDEN_CHATS)
                    )
            );
            addPreference(
                    helper.availableWhen(
                            helper.switchPreference(
                                    str("awt_hidden_chats_mute"),
                                    str("awt_hidden_chats_mute_desc"),
                                    Settings.HIDDEN_CHATS_MUTE
                            ),
                            () -> SharedPref.getBooleanPref(Settings.HIDDEN_CHATS)
                    )
            );
        }

        if (SettingsStatus.saveDeletedMessages) {
            addPreference(
                    helper.switchPreference(
                            str("awt_save_deleted_messages"),
                            str("awt_save_deleted_messages_desc"),
                            Settings.SAVE_DELETED_MESSAGES
                    )
            );
            addPreference(
                    helper.buttonPreference(
                            str("awt_view_deleted_messages"),
                            "",
                            "view_deleted_messages"
                    )
            );
        }
    }

    public void ghostSection() {
        if (!(SettingsStatus.ghostSection())) return;

        // PreferenceCategory category= addCategory(str("awt_category_ghost"));

        addPreference(
                helper.switchPreference(
                        str("awt_turn_on_all_ghost_modes"),
                        "",
                        Settings.TURN_ON_ALL_GHOST_MODES
                )
        );

        if (SettingsStatus.viewStoriesAnonymously) {
            addPreference(
                    helper.switchPreference(
                            str("awt_view_stories_anonymously"),
                            "",
                            Settings.VIEW_STORIES_ANONYMOUSLY
                    )
            );
        }
        if (SettingsStatus.viewLiveAnonymously) {
            addPreference(
                    helper.switchPreference(
                            str("awt_view_live_anonymously"),
                            "",
                            Settings.VIEW_LIVE_ANONYMOUSLY
                    )
            );
        }
        if (SettingsStatus.disableTypingStatus) {
            addPreference(
                    helper.switchPreference(
                            str("awt_disable_typing_status"),
                            "",
                            Settings.DISABLE_TYPING_STATUS
                    )
            );
        }
        if (SettingsStatus.disableScreenshotDetection) {
            addPreference(
                    helper.switchPreference(
                            str("awt_disable_screenshot_detection"),
                            "",
                            Settings.DISABLE_SCREENSHOT_DETECTION
                    )
            );
        }
        if (SettingsStatus.viewDmAnonymously) {
            addPreference(
                    helper.switchPreference(
                            str("awt_view_dm_anonymously"),
                            str("awt_view_dm_anonymously_desc"),
                            Settings.VIEW_DM_ANONYMOUSLY
                    )
            );
        }
        if (SettingsStatus.markAsReadAfterReply) {
            addPreference(
                    helper.availableWhen(
                            helper.switchPreference(
                                    str("awt_mark_as_read_after_reply"),
                                    str("awt_mark_as_read_after_reply_desc"),
                                    Settings.MARK_AS_READ_AFTER_REPLY
                            ),
                            () -> SharedPref.getBooleanPref(Settings.VIEW_DM_ANONYMOUSLY)
                                    || SharedPref.getBooleanPref(Settings.TURN_ON_ALL_GHOST_MODES)
                    )
            );
        }

    }

    public void linksSection() {
        if (!(SettingsStatus.linksSection())) return;

        // PreferenceCategory category= addCategory(str("awt_category_links"));
        if (SettingsStatus.openLinksExternally) {
            addPreference(
                    helper.switchPreference(
                            str("awt_open_links_externally"),
                            str("awt_open_links_externally_desc"),
                            Settings.OPEN_LINKS_EXTERNALLY
                    )
            );
        }
        if (SettingsStatus.sanitizeShareLinks) {
            addPreference(
                    helper.switchPreference(
                            str("awt_sanitize_share_links"),
                            "",
                            Settings.SANITIZE_SHARE_LINKS
                    )
            );
        }
        if (SettingsStatus.customSharingDomain) {
            addPreference(
                    helper.editTextPreference(
                            str("awt_custom_sharing_domain"),
                            Links.customSharingDomainSummary(Pref.customSharingDomain()),
                            Settings.CUSTOM_SHARING_DOMAIN
                    )
            );
        }
    }

    public void distractionFreeSection() {
        if (!(SettingsStatus.distractionFreeSection())) return;

        // PreferenceCategory category= addCategory(str("awt_category_distraction_free"));

        if (SettingsStatus.focusLock) {
            addPreference(
                    helper.buttonPreference(
                            str("awt_focus_lock"),
                            FocusLock.statusSummary(),
                            Constants.AWT_FRAGMENT_FOCUS_LOCK
                    )
            );
        }

        if (SettingsStatus.disableStories) {
            addLockableSwitch(
                    str("awt_disable_stories"),
                    "",
                    Settings.DISABLE_STORIES
            );
        }
        if (SettingsStatus.hideStoriesTray) {
            addLockableSwitch(
                    str("awt_hide_stories_tray"),
                    str("awt_hide_stories_tray_desc"),
                    Settings.HIDE_STORIES_TRAY
            );
        }
        if (SettingsStatus.hideHiddenRequests) {
            addLockableSwitch(
                    str("awt_hide_hidden_requests"),
                    str("awt_hide_hidden_requests_desc"),
                    Settings.HIDE_HIDDEN_REQUESTS
            );
        }
        if (SettingsStatus.disableHighlights) {
            addLockableSwitch(
                    str("awt_disable_highlights"),
                    "",
                    Settings.DISABLE_HIGHLIGHTS
            );
        }
        if (SettingsStatus.hideNotesTray) {
            addLockableSwitch(
                    str("awt_hide_notes_tray"),
                    str("awt_hide_notes_tray_desc"),
                    Settings.HIDE_NOTES_TRAY
            );
        }
        if (SettingsStatus.disableExplore) {
            addLockableSwitch(
                    str("awt_disable_explore"),
                    "",
                    Settings.DISABLE_EXPLORE
            );
        }
        if (SettingsStatus.disableComments) {
            addLockableSwitch(
                    str("awt_disable_comments"),
                    "",
                    Settings.DISABLE_COMMENTS
            );
        }
        if (SettingsStatus.hideFeedPosts) {
            addPreference(
                    helper.switchPreference(
                            str("awt_hide_feed_posts"),
                            str("awt_hide_feed_posts_desc"),
                            Settings.HIDE_FEED_POSTS
                    )
            );
        }
        if (SettingsStatus.limitFollowingFeed) {
            addLockableSwitch(
                    str("awt_limit_following_feed"),
                    str("awt_limit_following_feed_desc"),
                    Settings.LIMIT_FOLLOWING_FEED
            );
        }
        if (SettingsStatus.disableDiscoverPeople) {
            addPreference(
                    helper.switchPreference(
                            str("awt_disable_discover_people"),
                            "",
                            Settings.DISABLE_DISCOVER_PEOPLE
                    )
            );
        }
        if (SettingsStatus.disableReelsScrolling) {
            addLockableSwitch(
                    str("awt_disable_reels_scrolling"),
                    str("awt_disable_reels_scrolling_desc"),
                    Settings.DISABLE_REELS_SCROLLING
            );
        }
        if (SettingsStatus.disableSwipeToCreate) {
            addLockableSwitch(
                    str("awt_disable_swipe_to_create"),
                    str("awt_disable_swipe_to_create_desc"),
                    Settings.DISABLE_SWIPE_TO_CREATE
            );
        }
        if (SettingsStatus.hideGroupCreationOnSharesheet) {
            addLockableSwitch(
                    str("awt_hide_group_creation_button_on_sharesheet"),
                    "",
                    Settings.HIDE_GROUP_CREATION_BUTTON_ON_SHARESHEET
            );
        }
        if (SettingsStatus.hideReelsFollowButton) {
            addLockableSwitch(
                    str("awt_hide_reels_follow_button"),
                    str("awt_hide_reels_follow_button_desc"),
                    Settings.HIDE_REELS_FOLLOW_BUTTON
            );
        }
        if (SettingsStatus.hideLikes) {
            addPreference(
                    helper.switchPreference(
                            str("awt_hide_likes"),
                            "",
                            Settings.HIDE_LIKES
                    )
            );
        }
        if (SettingsStatus.hideShareButton) {
            addPreference(
                    helper.switchPreference(
                            str("awt_hide_share_button"),
                            "",
                            Settings.HIDE_SHARE_BUTTON
                    )
            );
        }
        if (SettingsStatus.hideSaveButtons) {
            addPreference(
                    helper.multiSelectListPref(
                            str("awt_hide_save_buttons"),
                            "",
                            Settings.HIDE_SAVE_BUTTON_SURFACES
                    )
            );
        }
        if (SettingsStatus.hideReshareButton) {
            addPreference(
                    helper.switchPreference(
                            str("awt_hide_reshare_button"),
                            "",
                            Settings.HIDE_RESHARE_BUTTON
                    )
            );
        }
        if (SettingsStatus.disableVideoAutoplay) {
            addPreference(
                    helper.switchPreference(
                            str("awt_disable_video_autoplay"),
                            "",
                            Settings.DISABLE_VIDEO_AUTOPLAY
                    )
            );
        }
        if (SettingsStatus.disableDoubleTapLike) {
            addLockableSwitch(
                    str("awt_disable_double_tap_like_post"),
                    "",
                    Settings.DISABLE_DOUBLE_TAP_LIKE_POST
            );

            addLockableSwitch(
                    str("awt_disable_double_tap_like_reel"),
                    "",
                    Settings.DISABLE_DOUBLE_TAP_LIKE_REEL
            );

            addLockableSwitch(
                    str("awt_disable_double_tap_like_comment"),
                    "",
                    Settings.DISABLE_DOUBLE_TAP_LIKE_COMMENT
            );

            addLockableSwitch(
                    str("awt_disable_double_tap_like_message"),
                    "",
                    Settings.DISABLE_DOUBLE_TAP_LIKE_MESSAGE
            );
        }
    }

    /** Adds a Distraction free switch, shown forced on while Focus Lock holds it. */
    private void addLockableSwitch(String title, String summary, BooleanSetting setting) {
        addPreference(
                helper.forcedSwitchPreference(
                        title,
                        summary,
                        setting,
                        FocusLock.isForced(setting),
                        str("awt_focus_lock_enforced")
                )
        );
    }

    /**
     * The Focus Lock screen: what to hold, for how long, and the lock itself.
     *
     * The picker lists the same settings as the Distraction free screen, by the same names, so
     * it is obvious what each entry will do. Everything except the action row is disabled while
     * the lock is on, since changing it then would defeat the point.
     */
    public void buildFocusLockSection() {
        if (!SettingsStatus.focusLock) return;

        boolean locked = FocusLock.isActive();

        addPreference(
                helper.buttonPreference(
                        FocusLock.buttonTitle(),
                        FocusLock.statusSummary(),
                        "awt_focus_lock_action"
                )
        );

        FocusLockDurationPreference duration = new FocusLockDurationPreference(context);
        duration.setTitle(str("awt_focus_lock_duration"));
        duration.setEnabled(!locked);
        addPreference(duration);

        PreferenceCategory targets = addCategory(str("awt_focus_lock_targets"));
        for (FocusLockTargets.Target target : FocusLockTargets.available()) {
            Preference pref = helper.switchPreference(
                    str(target.titleKey),
                    target.summaryKey == null ? "" : str(target.summaryKey),
                    Pref.focusLockSelection(target.key)
            );
            if (locked && pref instanceof SwitchPref) {
                ((SwitchPref) pref).setSwitchInteractionEnabled(false);
            }
            addPreference(targets, pref);
        }
    }

    public void buildMiscSection() {
        if (!(SettingsStatus.miscSection())) return;

        // PreferenceCategory category= addCategory(str("awt_category_misc"));
        if (MaterialYouTheme.isAmoledAvailable()) {
            addPreference(
                    helper.availableWhen(
                            helper.switchPreference(
                                    str("awt_amoled_title"),
                                    "",
                                    Settings.AMOLED_THEME
                            ),
                            () -> MaterialYouTheme.canEnableAmoled(context)
                    )
            );
        }
        if (MaterialYouTheme.isMaterialYouAvailable()) {
            addPreference(
                    helper.switchPreference(
                            str("awt_material_you_title"),
                            "",
                            Settings.MATERIAL_YOU_THEME
                    )
            );
        }
        if (SettingsStatus.fontSection()) {
            addPreference(
                    helper.buttonPreference(
                            str("awt_category_font"),
                            "",
                            Constants.AWT_FRAGMENT_FONT
                    )
            );
        }
        if (SettingsStatus.changeLikeAnimation) {
            addPreference(
                    helper.listPreference(
                            str("awt_change_like_animation"),
                            str("awt_change_like_animation_desc"),
                            Settings.CHANGE_LIKE_ANIMATION
                    )
            );
        }
        if (SettingsStatus.moreOptionsOnPost) {
            addPreference(
                    helper.switchPreference(
                            str("awt_enable_more_options_on_post"),
                            str("awt_enable_more_options_on_post_desc"),
                            Settings.ENABLE_MORE_OPTIONS_ON_POST
                    )
            );
        }
        if (SettingsStatus.followBackIndicator) {
            addPreference(
                    helper.switchPreference(
                            str("awt_follow_back_indicator"),
                            str("awt_follow_back_indicator_desc"),
                            Settings.FOLLOW_BACK_INDICATOR
                    )
            );
        }
        if (SettingsStatus.followBackIndicator || SettingsStatus.followListNonFollowerBadge) {
            addPreference(
                    helper.availableWhen(
                            helper.switchPreference(
                                    str("awt_fbi_color_indicator"),
                                    "",
                                    Settings.FOLLOW_BACK_COLOR_INDICATOR
                            ),
                            () -> (SettingsStatus.followBackIndicator
                                    && SharedPref.getBooleanPref(Settings.FOLLOW_BACK_INDICATOR))
                                    || (SettingsStatus.followListNonFollowerBadge
                                    && SharedPref.getBooleanPref(Settings.FOLLOW_LIST_NON_FOLLOWER_BADGE))
                    )
            );
        }
        if (SettingsStatus.followListNonFollowerBadge) {
            addPreference(
                    helper.switchPreference(
                            str("awt_follow_list_non_follower_badge"),
                            str("awt_follow_list_non_follower_badge_desc"),
                            Settings.FOLLOW_LIST_NON_FOLLOWER_BADGE
                    )
            );
        }
        if (SettingsStatus.resumeReelsAfterScrolling) {
            addPreference(helper.switchPreference(
                    str("awt_resume_reels_after_scrolling"),
                    str("awt_resume_reels_after_scrolling_desc"),
                    Settings.RESUME_REELS_AFTER_SCROLLING
            ));
        }
        if (SettingsStatus.improveImageViewing) {
            addPreference(
                    helper.switchPreference(
                            str("awt_improve_image_viewing"),
                            str("awt_improve_image_viewing_desc"),
                            Settings.IMPROVE_IMAGE_VIEWING
                    )
            );
        }
        if (SettingsStatus.copyCommentButton) {
            addPreference(
                    helper.switchPreference(
                            str("awt_copy_comment"),
                            str("awt_copy_comment_desc"),
                            Settings.COMMENT_COPY_BUTTON
                    )
            );
        }
        if (SettingsStatus.removeEmptyBottomSpace) {
            addPreference(
                    helper.switchPreference(
                            str("awt_remove_empty_bottom_space"),
                            "",
                            Settings.REMOVE_EMPTY_BOTTOM_SPACE
                    )
            );
        }
    }

    public void buildInstantsSection() {
        if (SettingsStatus.instantsDownload) {
            addPreference(
                    helper.switchPreference(
                            str("awt_instants_download"),
                            str("awt_instants_download_desc"),
                            Settings.INSTANTS_DOWNLOAD
                    )
            );
            addPreference(
                    helper.buttonPreference(
                            str("awt_view_saved_instants"),
                            "",
                            "awt_view_saved_instants"
                    )
            );
        }
    }

    public void buildDownloadSection() {
        if (!SettingsStatus.downloadSection()) return;

        // PreferenceCategory category= addCategory(str("awt_category_download_media"));

        addPreference(
                helper.switchPreference(
                        str("awt_enable_download"),
                        "",
                        Settings.ENABLE_DOWNLOAD
                )
        );

        if (SettingsStatus.feedDownloadButton || SettingsStatus.reelDownloadButton
                || SettingsStatus.storyDownloadButton) {
            addPreference(
                    helper.availableWhen(
                            new DownloadButtonPreference(context),
                            () -> SharedPref.getBooleanPref(Settings.ENABLE_DOWNLOAD)
                    )
            );
        }
        if (SettingsStatus.saveMediaCommentButton) {
            addPreference(
                    helper.switchPreference(
                            str("awt_save_media_comment"),
                            str("awt_save_media_comment_desc"),
                            Settings.COMMENT_SAVE_MEDIA_BUTTON
                    )
            );
        }

        addPreference(
                helper.switchPreference(
                        str("awt_enable_direct_download"),
                        str("awt_enable_direct_download_desc"),
                        Settings.ENABLE_DIRECT_DOWNLOAD
                )
        );

        addPreference(
                helper.switchPreference(
                        str("awt_download_username_folder"),
                        str("awt_download_username_folder_desc"),
                        Settings.DOWNLOAD_USERNAME_FOLDER
                )
        );

        addPreference(
                helper.switchPreference(
                        str("awt_prefer_dash_format"),
                        str("awt_prefer_dash_format_desc"),
                        Settings.PREFER_DASH_FORMAT
                )
        );

        addPreference(
                helper.availableWhen(
                        helper.switchPreference(
                                str("awt_dash_audio_aac_lc"),
                                str("awt_dash_audio_aac_lc_desc"),
                                Settings.DASH_AUDIO_AAC_LC
                        ),
                        () -> SharedPref.getBooleanPref(Settings.PREFER_DASH_FORMAT)
                )
        );

        addPreference(
                helper.switchPreference(
                        str("awt_embed_download_metadata"),
                        str("awt_embed_download_metadata_desc"),
                        Settings.EMBED_DOWNLOAD_METADATA
                )
        );

        addPreference(
                helper.listPreference(
                        str("awt_download_conflict_policy"),
                        str("awt_download_conflict_policy_desc"),
                        Settings.DOWNLOAD_CONFLICT_POLICY
                )
        );

        addPreference(
                helper.downloadFileNameTemplatePreference(
                        str("awt_download_file_name_template"),
                        Pref.downloadFileNameTemplate(),
                        Settings.DOWNLOAD_FILE_NAME_TEMPLATE
                )
        );

        addPreference(
                helper.buttonPreference(
                        str("awt_download_set_path"),
                        StorageUtils.getCustomPathForDisplay(),
                        "awt_download_set_path"
                )
        );

        if(SettingsStatus.downloadWithExternalDownloader) {
            addPreference(
                    helper.switchPreference(
                            str("awt_download_with_external_downloader"),
                            "",
                            Settings.DOWNLOAD_WITH_EXTERNAL_DOWNLOADER
                    )
            );

            addPreference(
                    helper.availableWhen(
                            helper.editTextPreference(
                                    str("awt_external_downloader_package_name"),
                                    Pref.externalDownloaderPackageName(),
                                    Settings.EXTERNAL_DOWNLOADER_PACKAGE_NAME
                            ),
                            () -> SharedPref.getBooleanPref(Settings.DOWNLOAD_WITH_EXTERNAL_DOWNLOADER)
                    )
            );
        }
    }

    public void buildActionBarSection() {

        addPreference(
                helper.multiSelectListPref(
                        str("awt_action_bar_main_feed"),
                        "",
                        Settings.ACTION_BAR_MAIN_FEED
                )
        );

        addPreference(
                helper.multiSelectListPref(
                        str("awt_action_bar_user_profile"),
                        "",
                        Settings.ACTION_BAR_USER_PROFILE
                )
        );

        addPreference(
                helper.multiSelectListPref(
                        str("awt_action_bar_inbox"),
                        "",
                        Settings.ACTION_BAR_INBOX
                )
        );

        addPreference(
                helper.multiSelectListPref(
                        str("awt_action_bar_chat"),
                        str("awt_action_bar_chat_desc"),
                        Settings.ACTION_BAR_CHAT
                )
        );
    }

    public void filterContentSection() {
        if(!SettingsStatus.filterContentSection()) return;

        if(SettingsStatus.storyFilters) {
            PreferenceCategory category = addCategory(str("awt_filter_story"));

            addPreference(
                    category,
                    helper.multiSelectListPref(
                            str("awt_filter_story_types_to_hide"),
                            "",
                            Settings.FILTER_STORY_BY_TYPE
                    )
            );

            addPreference(
                    category,
                    helper.multiSelectListPref(
                            str("awt_filter_story_user_types_to_hide"),
                            "",
                            Settings.FILTER_STORY_BY_USER_TYPE
                    )
            );

            addPreference(
                    category,
                    helper.editTextNumPreference(
                            str("awt_filter_story_minimum_item_count"),
                            str("awt_filter_story_minimum_item_count_desc"),
                            Settings.FILTER_STORY_MIN_STORY_ITEMS
                    )
            );

            addPreference(
                    category,
                    helper.editTextNumPreference(
                            str("awt_filter_story_maximum_item_count"),
                            str("awt_filter_story_maximum_item_count_desc"),
                            Settings.FILTER_STORY_MAX_STORY_ITEMS
                    )
            );
        }
    }

    public void buildNavigationSection() {
        if (!(SettingsStatus.hideNavigationButtons)) return;

        addPreference(
                helper.navigationBarPreference(
                        str("awt_navigation_tabs_title"),
                        str("awt_navigation_tabs_summary")
                )
        );

        addPreference(
                helper.navigationStartupPreference(
                        str("awt_navigation_startup_tab"),
                        str("awt_navigation_startup_tab_summary")
                )
        );
    }

    public void buildRecommendedFlagsSection() {
        buildRecommendedFlagsSection(true);
    }

    private void buildRecommendedFlagsSection(boolean notifyErrors) {
        long lastModified = RecommendedFlags.getLastModified();
        String lastModifiedAtString = "";
        if (lastModified > 0L) {
            Locale locale = context.getResources().getConfiguration().getLocales().get(0);
            String skeleton = DateFormat.is24HourFormat(context) ? "yMMMEdHm" : "yMMMEdhm";
            String pattern = DateFormat.getBestDateTimePattern(locale, skeleton);
            String formattedDate = new SimpleDateFormat(pattern, locale).format(new Date(lastModified));
            lastModifiedAtString = String.format(str("awt_rec_flags_last_modified_at"), formattedDate);
        }
        addPreference(
            helper.buttonPreference(
                    str("awt_rec_flags_refresh_file"),
                    lastModifiedAtString,
                    "awt_rec_flags_refresh_file"
            )
        );
        List<Flag> recFlags = RecommendedFlags.getFlags(notifyErrors);
        // The flag names will be English only,
        // as I want to keep it as a live service
        // rather than triggering new build
        // for every a new flag.
        for(Flag flag : recFlags) {
            if (flag.isLongType()) {
                addPreference(
                        helper.editTextNumPreference(
                                flag.getName(),
                                flag.getDesc(),
                                flag.getCode()
                        )
                );
            } else {
                addPreference(
                        helper.listPreference(
                                flag.getName(),
                                flag.getDesc(),
                                flag.getCode()
                        )
                );
            }
        }
    }

    public void aboutSection(TreeMap<String, Boolean> flags) {
        addPreference(new AboutPreference(context));
    }

    public void storySection() {
        if (!SettingsStatus.storySection()) return;

        if (SettingsStatus.hideStoryFollowButton) {
            addPreference(
                    helper.switchPreference(
                            str("awt_hide_story_follow_button"),
                            str("awt_hide_story_follow_button_desc"),
                            Settings.HIDE_STORY_FOLLOW_BUTTON
                    )
            );
        }
        if (SettingsStatus.viewStoryMentions) {
            addPreference(
                    helper.switchPreference(
                            str("awt_view_story_mentions"),
                            "",
                            Settings.VIEW_STORY_MENTIONS
                    )
            );
        }
        if (SettingsStatus.disableStoryFlipping) {
            addPreference(
                    helper.switchPreference(
                            str("awt_disable_story_flipping"),
                            str("awt_disable_story_flipping_desc"),
                            Settings.DISABLE_STORY_FLIPPING
                    )
            );
        }
        if (SettingsStatus.loopStory) {
            addPreference(
                    helper.switchPreference(
                            str("awt_loop_story"),
                            str("awt_loop_story_desc"),
                            Settings.LOOP_STORY
                    )
            );
        }
        if (SettingsStatus.customiseStoryTimestamp) {
            addPreference(
                    helper.listPreference(
                            str("awt_customise_story_timestamp"),
                            str("awt_customise_story_timestamp_desc"),
                            Settings.CUSTOMISE_STORY_TIMESTAMP
                    )
            );
        }
        if (SettingsStatus.customiseStoryRingSize) {
            addPreference(
                    helper.editTextNumPreference(
                            str("awt_customise_story_ring_size"),
                            str("awt_customise_story_ring_size_desc"),
                            Settings.CUSTOMISE_STORY_RING_SIZE
                    )
            );
        }
    }

    public void buildSettingsPage() {
        if (SettingsStatus.adsSection()){
            addPreference(
                    helper.categoryPreference(
                            str("awt_category_ads"),
                            "",
                            Constants.AWT_FRAGMENT_ADS
                    )
            );
        }

        if (SettingsStatus.filterContentSection()){
            addPreference(
                    helper.categoryPreference(
                            str("awt_category_filter_content"),
                            "",
                            Constants.AWT_FRAGMENT_FILTER_CONTENT
                    )
            );
        }

        if (SettingsStatus.ghostSection()){
            addPreference(
                    helper.categoryPreference(
                            str("awt_category_ghost"),
                            "",
                            Constants.AWT_FRAGMENT_GHOST
                    )
            );
        }

        if (SettingsStatus.dmSection()){
            addPreference(
                    helper.categoryPreference(
                            str("awt_category_dm"),
                            "",
                            Constants.AWT_FRAGMENT_DM
                    )
            );
        }

        if (SettingsStatus.storySection()){
            addPreference(
                    helper.categoryPreference(
                            str("awt_category_story"),
                            "",
                            Constants.AWT_FRAGMENT_STORY
                    )
            );
        }

        if (SettingsStatus.linksSection()){
            addPreference(
                    helper.categoryPreference(
                            str("awt_category_links"),
                            "",
                            Constants.AWT_FRAGMENT_LINKS
                    )
            );
        }

        if (SettingsStatus.distractionFreeSection()){
            addPreference(
                    helper.categoryPreference(
                            str("awt_category_distraction_free"),
                            "",
                            Constants.AWT_FRAGMENT_DISTRACTION_FREE
                    )
            );
        }

        if (SettingsStatus.miscSection()){
            addPreference(
                    helper.categoryPreference(
                            str("awt_category_misc"),
                            "",
                            Constants.AWT_FRAGMENT_MISC
                    )
            );
        }

        if (SettingsStatus.downloadSection()){
            addPreference(
                    helper.categoryPreference(
                            str("awt_category_downloads"),
                            "",
                            Constants.AWT_FRAGMENT_DOWNLOAD_MEDIA
                    )
            );
        }

        if (SettingsStatus.instantsDownload){
            addPreference(
                    helper.categoryPreference(
                            str("awt_instants_title"),
                            "",
                            Constants.AWT_FRAGMENT_INSTANTS
                    )
            );
        }

        addPreference(
                helper.categoryPreference(
                        str("awt_category_action_bar"),
                        "",
                        Constants.AWT_FRAGMENT_ACTION_BAR
                )
        );

        if (SettingsStatus.hideNavigationButtons){
            addPreference(
                    helper.categoryPreference(
                            str("awt_category_navigation_tabs"),
                            "",
                            Constants.AWT_FRAGMENT_NAV_BTNS
                    )
            );
        }

        if (SettingsStatus.developerOptionsSection()){
            addPreference(
                    helper.categoryPreference(
                            str("awt_category_dev_options"),
                            "",
                            Constants.AWT_FRAGMENT_DEV_OPTIONS
                    )
            );
        }

        addPreference(
                helper.categoryPreference(
                        str("awt_category_about"),
                        "",
                        Constants.AWT_FRAGMENT_ABOUT
                )
        );

    }

    //end
}
