package app.morphe.extension.instagram.settings.preference.widgets;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.preference.Preference;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AboutPreference extends Preference {

    private final Context context;

    public AboutPreference(Context context) {
        super(context);
        this.context = context;
        setSelectable(false);
    }

    public AboutPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setSelectable(false);
    }

    public AboutPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        setSelectable(false);
    }

    @Override
    protected View onCreateView(ViewGroup parent) {
        LinearLayout root = new LinearLayout(context);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER_HORIZONTAL);
        root.setPadding(
                InstagramPreferenceStyle.dp(context, 16),
                InstagramPreferenceStyle.dp(context, 12),
                InstagramPreferenceStyle.dp(context, 16),
                InstagramPreferenceStyle.dp(context, 32)
        );

        // ---------- 1. Glowing Circular AWT Logo Badge ----------
        AwtLogoBadgeView logoBadge = new AwtLogoBadgeView(context);
        int badgeSize = InstagramPreferenceStyle.dp(context, 120);
        LinearLayout.LayoutParams badgeParams = new LinearLayout.LayoutParams(badgeSize, badgeSize);
        badgeParams.gravity = Gravity.CENTER_HORIZONTAL;
        badgeParams.bottomMargin = InstagramPreferenceStyle.dp(context, 14);
        root.addView(logoBadge, badgeParams);

        // ---------- 2. Title: AWT Mods ----------
        TextView titleView = new TextView(context);
        SpannableStringBuilder titleSpan = new SpannableStringBuilder("AWT Mods");
        titleSpan.setSpan(new ForegroundColorSpan(Color.WHITE), 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        titleSpan.setSpan(new StyleSpan(Typeface.BOLD), 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        titleSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#4EE49B")), 4, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        titleSpan.setSpan(new StyleSpan(Typeface.BOLD), 4, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        titleView.setText(titleSpan);
        titleView.setTextSize(24);
        titleView.setGravity(Gravity.CENTER);
        root.addView(titleView);

        // ---------- 3. Subtitle: Information about this mod. ----------
        TextView subtitleView = new TextView(context);
        subtitleView.setText("Information about this mod.");
        subtitleView.setTextColor(Color.parseColor("#9E9E9E"));
        subtitleView.setTextSize(14);
        subtitleView.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams subParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        subParams.topMargin = InstagramPreferenceStyle.dp(context, 4);
        subParams.bottomMargin = InstagramPreferenceStyle.dp(context, 10);
        root.addView(subtitleView, subParams);

        // ---------- 4. Version Pill Badge: v4.0.1 ----------
        TextView versionPill = new TextView(context);
        versionPill.setText("v4.0.1");
        versionPill.setTextColor(Color.parseColor("#4EE49B"));
        versionPill.setTextSize(12);
        versionPill.setTypeface(Typeface.DEFAULT_BOLD);
        versionPill.setGravity(Gravity.CENTER);

        GradientDrawable pillBg = new GradientDrawable();
        pillBg.setShape(GradientDrawable.RECTANGLE);
        pillBg.setCornerRadius(InstagramPreferenceStyle.dp(context, 50));
        pillBg.setColor(Color.parseColor("#091811"));
        pillBg.setStroke(InstagramPreferenceStyle.dp(context, 1), Color.parseColor("#4EE49B"));
        versionPill.setBackground(pillBg);
        versionPill.setPadding(
                InstagramPreferenceStyle.dp(context, 18),
                InstagramPreferenceStyle.dp(context, 4),
                InstagramPreferenceStyle.dp(context, 18),
                InstagramPreferenceStyle.dp(context, 4)
        );

        LinearLayout.LayoutParams pillParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        pillParams.bottomMargin = InstagramPreferenceStyle.dp(context, 20);
        root.addView(versionPill, pillParams);

        // ---------- 5. Three Cards ----------
        // Card 1: Thanks
        root.addView(createCard(
                "♥",
                "Thanks",
                "Special thanks to contributors.",
                v -> showThanksDialog()
        ));

        // Card 2: Donate
        root.addView(createCard(
                "$",
                "Donate",
                "Support the developer.",
                v -> showDonateDialog()
        ));

        // Card 3: About mod
        root.addView(createCard(
                "ℹ",
                "About mod",
                "View mod version, developer info and social links.",
                v -> showAboutModDialog()
        ));

        // ---------- 6. Footer Divider with Centered Heart ----------
        LinearLayout dividerLayout = new LinearLayout(context);
        dividerLayout.setOrientation(LinearLayout.HORIZONTAL);
        dividerLayout.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams divParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        divParams.topMargin = InstagramPreferenceStyle.dp(context, 24);
        divParams.bottomMargin = InstagramPreferenceStyle.dp(context, 12);
        dividerLayout.setLayoutParams(divParams);

        View leftLine = new View(context);
        leftLine.setBackgroundColor(Color.parseColor("#20382D"));
        LinearLayout.LayoutParams lineParams1 = new LinearLayout.LayoutParams(
                InstagramPreferenceStyle.dp(context, 90),
                InstagramPreferenceStyle.dp(context, 1)
        );
        dividerLayout.addView(leftLine, lineParams1);

        TextView heartIcon = new TextView(context);
        heartIcon.setText("♥");
        heartIcon.setTextColor(Color.parseColor("#4EE49B"));
        heartIcon.setTextSize(14);
        heartIcon.setPadding(
                InstagramPreferenceStyle.dp(context, 12),
                0,
                InstagramPreferenceStyle.dp(context, 12),
                0
        );
        dividerLayout.addView(heartIcon);

        View rightLine = new View(context);
        rightLine.setBackgroundColor(Color.parseColor("#20382D"));
        LinearLayout.LayoutParams lineParams2 = new LinearLayout.LayoutParams(
                InstagramPreferenceStyle.dp(context, 90),
                InstagramPreferenceStyle.dp(context, 1)
        );
        dividerLayout.addView(rightLine, lineParams2);

        root.addView(dividerLayout);

        // ---------- 7. Footer Texts ----------
        TextView footer1 = new TextView(context);
        footer1.setText("Thank you for using AWT Mods.");
        footer1.setTextColor(Color.parseColor("#8E9E96"));
        footer1.setTextSize(13);
        footer1.setGravity(Gravity.CENTER);
        root.addView(footer1);

        TextView footer2 = new TextView(context);
        footer2.setText("Your support means a lot!");
        footer2.setTextColor(Color.parseColor("#5A6F65"));
        footer2.setTextSize(12);
        footer2.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams f2Params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        f2Params.topMargin = InstagramPreferenceStyle.dp(context, 3);
        root.addView(footer2, f2Params);

        return root;
    }

    private View createCard(String symbol, String title, String subtitle, View.OnClickListener onClickListener) {
        LinearLayout card = new LinearLayout(context);
        card.setOrientation(LinearLayout.HORIZONTAL);
        card.setGravity(Gravity.CENTER_VERTICAL);
        card.setClickable(true);
        card.setFocusable(true);

        // Background with border and pressed effect
        GradientDrawable normalBg = new GradientDrawable();
        normalBg.setShape(GradientDrawable.RECTANGLE);
        normalBg.setCornerRadius(InstagramPreferenceStyle.dp(context, 18));
        normalBg.setColor(Color.parseColor("#09140F"));
        normalBg.setStroke(InstagramPreferenceStyle.dp(context, 1), Color.parseColor("#1B3B2B"));

        GradientDrawable pressedBg = new GradientDrawable();
        pressedBg.setShape(GradientDrawable.RECTANGLE);
        pressedBg.setCornerRadius(InstagramPreferenceStyle.dp(context, 18));
        pressedBg.setColor(Color.parseColor("#152C21"));
        pressedBg.setStroke(InstagramPreferenceStyle.dp(context, 1), Color.parseColor("#4EE49B"));

        StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{android.R.attr.state_pressed}, pressedBg);
        states.addState(new int[]{}, normalBg);
        card.setBackground(states);

        card.setPadding(
                InstagramPreferenceStyle.dp(context, 16),
                InstagramPreferenceStyle.dp(context, 14),
                InstagramPreferenceStyle.dp(context, 16),
                InstagramPreferenceStyle.dp(context, 14)
        );

        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardParams.bottomMargin = InstagramPreferenceStyle.dp(context, 10);
        card.setLayoutParams(cardParams);

        // Left Icon Badge (circle)
        TextView iconView = new TextView(context);
        iconView.setText(symbol);
        iconView.setTextColor(Color.parseColor("#4EE49B"));
        iconView.setTextSize(18);
        iconView.setTypeface(Typeface.DEFAULT_BOLD);
        iconView.setGravity(Gravity.CENTER);

        int iconSize = InstagramPreferenceStyle.dp(context, 44);
        GradientDrawable iconBg = new GradientDrawable();
        iconBg.setShape(GradientDrawable.OVAL);
        iconBg.setColor(Color.parseColor("#0D271D"));
        iconBg.setStroke(InstagramPreferenceStyle.dp(context, 1.5f), Color.parseColor("#4EE49B"));
        iconView.setBackground(iconBg);

        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(iconSize, iconSize);
        card.addView(iconView, iconParams);

        // Center Content (Title & Subtitle)
        LinearLayout textCol = new LinearLayout(context);
        textCol.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        textParams.leftMargin = InstagramPreferenceStyle.dp(context, 14);
        textParams.rightMargin = InstagramPreferenceStyle.dp(context, 8);
        textCol.setLayoutParams(textParams);

        TextView titleView = new TextView(context);
        titleView.setText(title);
        titleView.setTextColor(Color.WHITE);
        titleView.setTextSize(16);
        titleView.setTypeface(Typeface.DEFAULT_BOLD);
        textCol.addView(titleView);

        TextView subtitleView = new TextView(context);
        subtitleView.setText(subtitle);
        subtitleView.setTextColor(Color.parseColor("#8E9E96"));
        subtitleView.setTextSize(12);
        LinearLayout.LayoutParams subParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        subParams.topMargin = InstagramPreferenceStyle.dp(context, 2);
        textCol.addView(subtitleView, subParams);

        card.addView(textCol);

        // Right Chevron
        TextView chevron = new TextView(context);
        chevron.setText("›");
        chevron.setTextColor(Color.parseColor("#8E9E96"));
        chevron.setTextSize(22);
        chevron.setTypeface(Typeface.DEFAULT_BOLD);
        chevron.setGravity(Gravity.CENTER);
        card.addView(chevron);

        if (onClickListener != null) {
            card.setOnClickListener(onClickListener);
        }

        return card;
    }

    private void showThanksDialog() {
        new AlertDialog.Builder(context)
                .setTitle("Special Thanks")
                .setMessage("Special thanks to all contributors, developers, and testers of AWT Mods and Morphe!\n\nYour passion makes this mod possible.")
                .setPositiveButton("GitHub", (dialog, which) -> openUrl("https://github.com/AWTMODS/instagram-patch"))
                .setNegativeButton("Close", null)
                .show();
    }

    private void showDonateDialog() {
        new AlertDialog.Builder(context)
                .setTitle("Donate & Support")
                .setMessage("Thank you for supporting AWT Mods!\n\nYour support helps maintain features, compatibility updates, and future enhancements.")
                .setPositiveButton("Support on GitHub", (dialog, which) -> openUrl("https://github.com/AWTMODS/instagram-patch"))
                .setNegativeButton("Close", null)
                .show();
    }

    private void showAboutModDialog() {
        new AlertDialog.Builder(context)
                .setTitle("AWT Mods")
                .setMessage("Version: 4.0.1\n\nFeatures:\n• Modern AMOLED Aurora Glow UI\n• Fix for Instagram v447.0.0.55.81\n• Privacy, Ghost Mode, and Media Downloader\n\nDeveloped with ♥ by AWTMODS")
                .setPositiveButton("Visit Repository", (dialog, which) -> openUrl("https://github.com/AWTMODS/instagram-patch"))
                .setNegativeButton("Close", null)
                .show();
    }

    private void openUrl(String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(intent);
        } catch (Exception ignored) {
        }
    }

    /**
     * Custom View that paints the neon circular double-ring AWT logo on canvas.
     */
    public static class AwtLogoBadgeView extends View {
        private final Paint bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final Paint outerRingPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final Paint innerRingPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        public AwtLogoBadgeView(Context context) {
            super(context);
            bgPaint.setColor(Color.parseColor("#0C1612"));
            bgPaint.setStyle(Paint.Style.FILL);

            outerRingPaint.setColor(Color.parseColor("#4EE49B"));
            outerRingPaint.setStyle(Paint.Style.STROKE);
            outerRingPaint.setStrokeWidth(InstagramPreferenceStyle.dp(context, 2.8f));

            innerRingPaint.setColor(Color.parseColor("#268758"));
            innerRingPaint.setStyle(Paint.Style.STROKE);
            innerRingPaint.setStrokeWidth(InstagramPreferenceStyle.dp(context, 1.2f));

            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize(InstagramPreferenceStyle.dp(context, 34));
            textPaint.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD_ITALIC));
            textPaint.setTextAlign(Paint.Align.CENTER);
            textPaint.setFakeBoldText(true);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            float cx = getWidth() / 2f;
            float cy = getHeight() / 2f;
            float r = Math.min(cx, cy) - InstagramPreferenceStyle.dp(getContext(), 4);

            // Background circle
            canvas.drawCircle(cx, cy, r, bgPaint);
            // Outer glowing mint ring
            canvas.drawCircle(cx, cy, r - InstagramPreferenceStyle.dp(getContext(), 1), outerRingPaint);
            // Inner subtle mint ring
            canvas.drawCircle(cx, cy, r - InstagramPreferenceStyle.dp(getContext(), 6), innerRingPaint);

            // Centered "AWT" text
            Paint.FontMetrics fm = textPaint.getFontMetrics();
            float baseline = cy - (fm.ascent + fm.descent) / 2f;
            canvas.drawText("AWT", cx, baseline, textPaint);
        }
    }
}
