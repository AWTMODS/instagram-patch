/*
 * Copyright (C) 2026 piko <https://github.com/crimera/piko>
 *
 * See the included NOTICE file for GPLv3 §7(b) terms that apply to this code.
 */

package app.morphe.extension.instagram.settings;

import static app.morphe.extension.instagram.utils.IgStr.str;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.WindowInsets;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.function.Supplier;

import app.morphe.extension.crimera.downloader.StorageUtils;
import app.morphe.extension.instagram.constants.Constants;
import app.morphe.extension.instagram.constants.UI;
import app.morphe.extension.instagram.settings.preference.Helper;
import app.morphe.extension.instagram.settings.preference.ScreenBuilder;
import app.morphe.extension.instagram.settings.preference.widgets.InstagramPreferenceStyle;
import app.morphe.extension.instagram.settings.preference.widgets.SwitchPref;
import app.morphe.extension.instagram.settings.SettingsStatus;
import app.morphe.extension.instagram.theme.MaterialYouTheme;

public class SettingsActivity extends Activity {

    private LinearLayout root;
    private LinearLayout toolbar;
    private LinearLayout content;
    private LinearLayout customContainer;
    private TextView titleTextView;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        String displayTitle = null;
        String fragmentName = null;

        // Extract both variables safely from the incoming intent bundle
        if (getIntent() != null && getIntent().getExtras() != null) {
            displayTitle = str(getIntent().getStringExtra(Constants.PIKO_FRAGMENT_TITLE));
            fragmentName = getIntent().getStringExtra(Constants.PIKO_FRAGMENT_NAME);
        }

        // Fallback to default localized string if no custom title was provided in the intent
        if (displayTitle == null || displayTitle.isEmpty()) {
            displayTitle = str("piko_title_settings");
        }

        boolean isRootSettings = fragmentName == null || Constants.PIKO_FRAGMENT_SETTINGS.equals(fragmentName);
        createLayout(displayTitle, isRootSettings);

        if (bundle == null) {
            SettingsFragment fragment = new SettingsFragment();
            if (getIntent() != null && getIntent().getExtras() != null) {
                fragment.setArguments(getIntent().getExtras());
            }

            getFragmentManager().beginTransaction().replace(1001, fragment).commit();
        }
    }

    public static CharSequence formatStyledTitle(String text) {
        if (text == null) return "";
        int lastSpace = text.lastIndexOf(' ');
        if (lastSpace > 0) {
            SpannableStringBuilder span = new SpannableStringBuilder(text);
            span.setSpan(new ForegroundColorSpan(Color.WHITE), 0, lastSpace, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            span.setSpan(new ForegroundColorSpan(Color.parseColor("#4EE49B")), lastSpace + 1, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return span;
        }
        return text;
    }

    private static Drawable createAuroraBackground() {
        return new Drawable() {
            private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

            @Override
            public void draw(Canvas canvas) {
                int width = canvas.getWidth();
                int height = canvas.getHeight();

                canvas.drawColor(Color.parseColor("#080B09"));

                float centerX = width * 0.95f;
                float centerY = height * 0.05f;
                float radius = Math.max(width, height) * 0.55f;

                int[] colors = new int[]{
                        Color.parseColor("#384EE49B"),
                        Color.parseColor("#1834D399"),
                        Color.parseColor("#08154D32"),
                        Color.TRANSPARENT
                };
                float[] stops = new float[]{0f, 0.35f, 0.65f, 1f};

                paint.setShader(new RadialGradient(
                        centerX, centerY, radius,
                        colors, stops,
                        Shader.TileMode.CLAMP
                ));
                canvas.drawRect(0, 0, width, height, paint);
            }

            @Override
            public void setAlpha(int alpha) {
                paint.setAlpha(alpha);
            }

            @Override
            public void setColorFilter(ColorFilter colorFilter) {
                paint.setColorFilter(colorFilter);
            }

            @Override
            public int getOpacity() {
                return PixelFormat.TRANSLUCENT;
            }
        };
    }

    @SuppressLint("ResourceType")
    private void createLayout(String displayTitle, boolean isRootSettings) {
        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackground(createAuroraBackground());

        InstagramPreferenceStyle.applySystemBarStyle(this);

        // ---------- Toolbar ----------
        toolbar = new LinearLayout(this);
        toolbar.setBackgroundColor(Color.TRANSPARENT);

        ImageView back = new ImageView(this);
        UI.setThemedIcon(back, UI.DRAWABLE_ARROW_BACK);
        back.setColorFilter(new PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        titleTextView = new TextView(this);
        titleTextView.setText(formatStyledTitle(displayTitle));
        InstagramPreferenceStyle.applyToolbarLayout(
                this, toolbar, back, titleTextView, isRootSettings);
        if (!(titleTextView.getText() instanceof Spanned)) {
            titleTextView.setTextColor(Color.WHITE);
        }

        toolbar.addView(back);
        toolbar.addView(titleTextView);

        // ---------- Custom Container ----------
        customContainer = new LinearLayout(this);
        customContainer.setOrientation(LinearLayout.VERTICAL);
        customContainer.setBackgroundColor(Color.TRANSPARENT);

        root.addView(toolbar);
        root.addView(customContainer, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        // ---------- Content ----------
        content = new LinearLayout(this);
        content.setId(1001);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setBackgroundColor(Color.TRANSPARENT);

        LinearLayout.LayoutParams contentParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                1
        );
        contentParams.setMargins(
                InstagramPreferenceStyle.dp(this, 16),
                InstagramPreferenceStyle.dp(this, 6),
                InstagramPreferenceStyle.dp(this, 16),
                InstagramPreferenceStyle.dp(this, 16)
        );
        root.addView(content, contentParams);

        root.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
                int topInset = insets.getSystemWindowInsetTop();
                int bottomInset = insets.getSystemWindowInsetBottom();

                v.setPadding(0, topInset, 0, 0);

                content.setPadding(
                        content.getPaddingLeft(),
                        content.getPaddingTop(),
                        content.getPaddingRight(),
                        bottomInset
                );

                return insets;
            }
        });

        setContentView(root);
    }

    public LinearLayout getCustomContainer() {
        return customContainer;
    }

    // (Keep the nested static SettingsFragment class unchanged)
    public static class SettingsFragment extends PreferenceFragment {

        Context context;

        private void refreshPreferenceSummary(
                String key,
                Supplier<CharSequence> summaryProvider
        ) {
            Preference preference = findPreference(key);
            if (preference != null) {
                preference.setSummary(summaryProvider.get());
            }
        }

        @Override
        public void onResume() {
            super.onResume();
            refreshPreferenceSummary(
                    "piko_download_set_path",
                    StorageUtils::getCustomPathForDisplay
            );
            Preference preference = findPreference(Settings.AMOLED_THEME.key);
            if (preference instanceof SwitchPref) {
                SwitchPref amoledPreference = (SwitchPref) preference;
                amoledPreference.setChecked(MaterialYouTheme.isAmoledEnabled());
                amoledPreference.setSwitchInteractionEnabled(
                        MaterialYouTheme.canEnableAmoled(getActivity())
                );
            }
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            context = getActivity();
            PreferenceManager preferenceManager = getPreferenceManager();
            PreferenceScreen screen = preferenceManager.createPreferenceScreen(context);
            preferenceManager.setSharedPreferencesName(Constants.SHARED_PREF_NAME);

            Helper helper = new Helper(context);
            ScreenBuilder screenBuilder = new ScreenBuilder(context, screen, helper);

            String fragment_name = Constants.PIKO_FRAGMENT_SETTINGS;
            if (getArguments() != null) {
                fragment_name = getArguments().getString(Constants.PIKO_FRAGMENT_NAME, Constants.PIKO_FRAGMENT_SETTINGS);
            }
            if(fragment_name.equals(Constants.PIKO_FRAGMENT_SETTINGS)) {
                screenBuilder.buildSettingsPage();
            } else if(fragment_name.equals(Constants.PIKO_FRAGMENT_ADS)) {
                screenBuilder.buildAdsSection();
            } else if(fragment_name.equals(Constants.PIKO_FRAGMENT_GHOST)) {
                screenBuilder.ghostSection();
            } else if(fragment_name.equals(Constants.PIKO_FRAGMENT_STORY)) {
                screenBuilder.storySection();
            } else if(fragment_name.equals(Constants.PIKO_FRAGMENT_LINKS)) {
                screenBuilder.linksSection();
            } else if(fragment_name.equals(Constants.PIKO_FRAGMENT_DISTRACTION_FREE)) {
                screenBuilder.distractionFreeSection();
            } else if(fragment_name.equals(Constants.PIKO_FRAGMENT_FOCUS_LOCK)) {
                screenBuilder.buildFocusLockSection();
            } else if(fragment_name.equals(Constants.PIKO_FRAGMENT_MISC)) {
                screenBuilder.buildMiscSection();
            } else if(fragment_name.equals(Constants.PIKO_FRAGMENT_DOWNLOAD_MEDIA)) {
                screenBuilder.buildDownloadSection();
            } else if(fragment_name.equals(Constants.PIKO_FRAGMENT_NAV_BTNS)) {
                screenBuilder.buildNavigationSection();
            } else if(fragment_name.equals(Constants.PIKO_FRAGMENT_DEV_OPTIONS)) {
                screenBuilder.buildDeveloperSection();
            } else if(fragment_name.equals(Constants.PIKO_FRAGMENT_ABOUT)) {
                screenBuilder.aboutSection(SettingsStatus.FLAGS);
            } else if (fragment_name.equals(Constants.PIKO_FRAGMENT_ACTION_BAR)) {
                screenBuilder.buildActionBarSection();
            } else if (fragment_name.equals(Constants.PIKO_FRAGMENT_DM)) {
                screenBuilder.dmSection();
            } else if (fragment_name.equals(Constants.PIKO_FRAGMENT_FILTER_CONTENT)) {
                screenBuilder.filterContentSection();
            } else if (fragment_name.equals(Constants.PIKO_FRAGMENT_REC_FLAGS)) {
                preferenceManager.setSharedPreferencesName(Constants.REC_FLAGS);
                screenBuilder.buildRecommendedFlagsSection();
            } else if (fragment_name.equals(Constants.PIKO_FRAGMENT_INSTANTS)) {
                screenBuilder.buildInstantsSection();
            } else if (fragment_name.equals(Constants.PIKO_FRAGMENT_FONT)) {
                screenBuilder.buildFontSection();
            }

            setPreferenceScreen(screen);
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            View rootView = getView();
            ListView listView = null;
            if (rootView != null) {
                View list = rootView.findViewById(android.R.id.list);
                if (list instanceof ListView) {
                    listView = (ListView) list;
                }
            }
            if (listView != null) {
                Context ctx = listView.getContext();
                GradientDrawable cardBg = new GradientDrawable();
                cardBg.setShape(GradientDrawable.RECTANGLE);
                cardBg.setCornerRadius(InstagramPreferenceStyle.dp(ctx, 22));
                cardBg.setColor(Color.parseColor("#101513"));
                cardBg.setStroke(InstagramPreferenceStyle.dp(ctx, 1), Color.parseColor("#1B2620"));

                listView.setBackground(cardBg);
                listView.setClipToOutline(true);
                listView.setOutlineProvider(new ViewOutlineProvider() {
                    @Override
                    public void getOutline(View view, Outline outline) {
                        outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), InstagramPreferenceStyle.dp(ctx, 22));
                    }
                });

                ShapeDrawable divider = new ShapeDrawable();
                divider.setIntrinsicHeight(InstagramPreferenceStyle.dp(ctx, 1));
                divider.getPaint().setColor(Color.parseColor("#18221D"));
                listView.setDivider(divider);
                listView.setDividerHeight(InstagramPreferenceStyle.dp(ctx, 1));
                listView.setFooterDividersEnabled(false);
                listView.setHeaderDividersEnabled(false);

                listView.setPadding(0, InstagramPreferenceStyle.dp(ctx, 4), 0, InstagramPreferenceStyle.dp(ctx, 4));
                listView.setClipToPadding(true);
                listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
                listView.setCacheColorHint(Color.TRANSPARENT);
            }

            if (rootView != null) {
                rootView.setBackgroundColor(Color.TRANSPARENT);
            }
        }
    }
}
