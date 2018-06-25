package showcaseview.outlander.showcaseviewdemo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import outlander.showcaseview.RippleBackground;
import outlander.showcaseview.ShowcaseViewBuilder;

public class MainActivity extends AppCompatActivity {

    public ShowcaseViewBuilder showcaseViewBuilder;

    private FloatingActionButton fab;
    private TextView textView;
    private ImageView imageView;
    private Button button;
    private RippleBackground fabHighlighter, tvHighlighter, btnHighlighter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textView = (TextView) findViewById(R.id.textView);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showcaseTextView();
            }
        });

        imageView = (ImageView) findViewById(R.id.imgBtn);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showcaseImage();
            }
        });

        button = (Button) findViewById(R.id.magic_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showcaseButton();
            }
        });

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showcaseFab();
            }
        });

        fabHighlighter = (RippleBackground) findViewById(R.id.fab_highlighter);
        tvHighlighter = (RippleBackground) findViewById(R.id.tv_highlighter);
        btnHighlighter = (RippleBackground) findViewById(R.id.btn_highlighter);

        showcaseViewBuilder = ShowcaseViewBuilder.init(this);

        showcaseFab();

    }

    private void showcaseFab() {
        if (!fabHighlighter.isRippleAnimationRunning()) {
            fabHighlighter.startRippleAnimation();
        } else {
            fabHighlighter.stopRippleAnimation();
            showcaseViewBuilder.setTargetView(fab)
                    .setBackgroundOverlayColor(0xcc000000)
                    .setBgOverlayShape(ShowcaseViewBuilder.ROUND_RECT)
                    .setRoundRectCornerDirection(ShowcaseViewBuilder.TOP_LEFT)
                    .setRoundRectOffset(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 170, getResources().getDisplayMetrics()))
                    .setRingColor(0xcc8e8e8e)
                    .setShowcaseShape(ShowcaseViewBuilder.SHAPE_CIRCLE)
                    .setRingWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()))
                    .setMarkerDrawable(getResources().getDrawable(R.drawable.arrow_up), Gravity.LEFT)
                    .addCustomView(R.layout.fab_description_view, Gravity.LEFT, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources().getDisplayMetrics()), TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, -228, getResources().getDisplayMetrics()), TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, -80, getResources().getDisplayMetrics()) ,0);
//                    .addCustomView(R.layout.fab_description_view, Gravity.CENTER);

            showcaseViewBuilder.show();

            showcaseViewBuilder.setClickListenerOnView(R.id.btn, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showcaseViewBuilder.hide();
                }
            });
        }
    }

    private void showcaseTextView() {
        showcaseViewBuilder.setTargetView(textView)
                .setBackgroundOverlayColor(0xcc000000)
                .setBgOverlayShape(ShowcaseViewBuilder.ROUND_RECT)
                .setRoundRectCornerDirection(ShowcaseViewBuilder.BOTTOM_RIGHT)
                .setRoundRectOffset(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 170, getResources().getDisplayMetrics()))
                .setRingColor(0xccb9e797)
                .setShowcaseShape(ShowcaseViewBuilder.SHAPE_SKEW)
                .setHideOnTouchOutside(true)
                .setRingWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, getResources().getDisplayMetrics()))
                .setShowcaseMargin(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics()))
                .setMarkerDrawable(getResources().getDrawable(android.R.drawable.arrow_down_float), Gravity.BOTTOM)
                .setDrawableLeftMargin(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()))
                .addCustomView(R.layout.textview_description_view, Gravity.BOTTOM, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources().getDisplayMetrics()), TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics()), TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources().getDisplayMetrics()) ,0)
                .addCustomView(R.layout.skip_layout);

        showcaseViewBuilder.show();

        showcaseViewBuilder.setClickListenerOnView(R.id.skip_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showcaseViewBuilder.hide();
            }
        });
    }

    private void showcaseButton() {
        showcaseViewBuilder.setTargetView(button)
                .setBackgroundOverlayColor(0xcc000000)
                .setBgOverlayShape(ShowcaseViewBuilder.FULL_SCREEN)
                .setRingColor(0xcc8e8e8e)
                .setShowcaseShape(ShowcaseViewBuilder.SHAPE_SKEW)
                .setRingWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, getResources().getDisplayMetrics()))
                .setShowcaseMargin(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics()))
                .setMarkerDrawable(getResources().getDrawable(android.R.drawable.arrow_up_float), Gravity.BOTTOM)
                .setDrawableLeftMargin(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()))
                .addCustomView(R.layout.button_description_view_bottom, Gravity.BOTTOM, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()), TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics()), TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources().getDisplayMetrics()) ,0)
                .addCustomView(R.layout.skip_layout)
                .addCustomView(R.layout.button_description_view_top, Gravity.TOP, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()), TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, -30, getResources().getDisplayMetrics()), TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources().getDisplayMetrics()) ,0)
                .setCustomViewMargin((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics()));

        showcaseViewBuilder.show();

        showcaseViewBuilder.setClickListenerOnView(R.id.skip_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showcaseViewBuilder.hide();
            }
        });
//        if (!btnHighlighter.isRippleAnimationRunning()) {
//            btnHighlighter.startRippleAnimation();
//        } else {
//            btnHighlighter.stopRippleAnimation();
//        }
    }

    private void showcaseImage() {
        View view = LayoutInflater.from(this).inflate(R.layout.image_description_view, null, false);
        view.findViewById(R.id.tv_test).startAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_down));
        showcaseViewBuilder.setTargetView(imageView)
                .setBackgroundOverlayColor(0xee4d4d4d)
                .setRingColor(0xcc8e8e8e)
                .setRingWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()))
                .setMarkerDrawable(getResources().getDrawable(android.R.drawable.arrow_down_float), Gravity.BOTTOM)
                .setDrawableLeftMargin(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()))
                .addCustomView(view, Gravity.BOTTOM)
                .setHideOnTouchOutside(true)
                .setCustomViewMargin((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));

        showcaseViewBuilder.show();
    }
}
