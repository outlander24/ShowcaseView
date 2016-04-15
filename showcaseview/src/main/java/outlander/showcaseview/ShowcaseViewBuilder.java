package outlander.showcaseview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

/**
 * Created by ashish on 13/04/16.
 */
public class ShowcaseViewBuilder extends View {

    private Activity mActivity;
    private View mTargetView, mCustomView;
    private float mCenterX, mCenterY, mRadius;
    private Drawable mMarkerDrawable;
    private int mMarkerDrawableGravity, mCustomViewGravity;
    private int ringColor, backgroundOverlayColor;
    private int mCustomViewMargin;

    private Canvas tempCanvas;
    private Paint backgroundPaint, transparentPaint, ringPaint;

    private static final String TAG = "SHOWCASE_VIEW";

    public ShowcaseViewBuilder(Context context) {
        super(context);
    }

    public ShowcaseViewBuilder(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static ShowcaseViewBuilder init(Activity activity) {
        ShowcaseViewBuilder showcaseViewBuilder = new ShowcaseViewBuilder(activity);
        showcaseViewBuilder.mActivity = activity;
        return showcaseViewBuilder;
    }

    public ShowcaseViewBuilder setTargetView(View view) {
        mTargetView = view;
        return this;
    }

    private void calculateRadiusAndCenter() {
        int width = mTargetView.getMeasuredWidth();
        int height = mTargetView.getMeasuredHeight();

        int[] xy = {0, 0};
        mTargetView.getLocationInWindow(xy);

        mCenterX = xy[0] + (width / 2);
        mCenterY = xy[1] + (height / 2);

        if (width > height) {
            mRadius = (width) / 2;
        } else {
            mRadius = (height) / 2;
        }
    }

    public ShowcaseViewBuilder setMarkerDrawable(Drawable drawable) {
        this.mMarkerDrawable = drawable;
        return this;
    }

    public ShowcaseViewBuilder setMarkerDrawableGravity(int gravity) {
        this.mMarkerDrawableGravity = gravity;
        return this;
    }

    public ShowcaseViewBuilder setCustomView(int layoutId) {
        View view = LayoutInflater.from(mActivity).inflate(layoutId, null);
        LinearLayout linearLayout = new LinearLayout(mActivity);
        linearLayout.addView(view);
        linearLayout.setGravity(Gravity.CENTER);
        mCustomView = linearLayout;

        DisplayMetrics metrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        Rect rect = new Rect();
        rect.set(0, 0, metrics.widthPixels, metrics.heightPixels);

        int widthSpec = View.MeasureSpec.makeMeasureSpec(rect.width(), MeasureSpec.EXACTLY);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(rect.height(), MeasureSpec.EXACTLY);

        this.mCustomView.measure(widthSpec, heightSpec);
        return this;
    }

    public ShowcaseViewBuilder setCustomViewGravity(int gravity) {
        this.mCustomViewGravity = gravity;
        return this;
    }

    public ShowcaseViewBuilder setCustomViewMargin(int margin) {
        this.mCustomViewMargin = margin;
        return this;
    }

    public ShowcaseViewBuilder setRingColor(int color) {
        this.ringColor = color;
        return this;
    }

    public ShowcaseViewBuilder setBackgroundOverlayColor(int color) {
        this.backgroundOverlayColor = color;
        return this;
    }

    public void show() {
        transparentPaint = new Paint();
        ringPaint = new Paint();
        backgroundPaint = new Paint();
        mTargetView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                showShowcaseView();
                invalidate();
                mTargetView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
    }

    public void showShowcaseView() {
        ((ViewGroup) mActivity.getWindow().getDecorView()).addView(this);
        this.setVisibility(VISIBLE);

    }

    public void hideShowcaseView() {
        ((ViewGroup) mActivity.getWindow().getDecorView()).removeView(this);
        this.setVisibility(GONE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mTargetView != null) {
            setShowcase(canvas);
            drawMarkerDrawable(canvas);
            addCustomView(canvas);
        }
        super.onDraw(canvas);
    }

    private void drawMarkerDrawable(Canvas canvas) {
        if (mMarkerDrawable != null) {
            switch (mMarkerDrawableGravity) {
                case Gravity.LEFT:
                    mMarkerDrawable.setBounds((int) (mCenterX - mRadius - mMarkerDrawable.getMinimumWidth() - 20),
                            (int) (mCenterY - mMarkerDrawable.getMinimumHeight() / 2),
                            (int) (mCenterX - mRadius - 20), (int) (mCenterY + mMarkerDrawable.getMinimumHeight() / 2));
                    break;

                case Gravity.TOP:
                    mMarkerDrawable.setBounds((int) (mCenterX - mMarkerDrawable.getMinimumWidth() / 2),
                            (int) (mCenterY - mRadius - mMarkerDrawable.getMinimumHeight() - 20),
                            (int) (mCenterX + mMarkerDrawable.getMinimumWidth() / 2), (int) (mCenterY - mRadius - 20));
                    break;

                case Gravity.RIGHT:
                    mMarkerDrawable.setBounds((int) (mCenterX + mRadius + 20),
                            (int) (mCenterY - mMarkerDrawable.getMinimumHeight() / 2),
                            (int) (mCenterX + mRadius + mMarkerDrawable.getMinimumWidth() + 20),
                            (int) (mCenterY + mMarkerDrawable.getMinimumHeight() / 2));
                    break;

                case Gravity.BOTTOM:
                    mMarkerDrawable.setBounds((int) (mCenterX - mMarkerDrawable.getMinimumWidth() / 2),
                            (int) (mCenterY + mRadius + 20), (int) (mCenterX + mMarkerDrawable.getMinimumWidth() / 2),
                            (int) (mCenterY + mRadius + mMarkerDrawable.getMinimumHeight() + 20));
                    break;
            }

            mMarkerDrawable.draw(canvas);
        } else {
            Log.d(TAG, "No marker drawable defined");
        }
    }

    private void addCustomView(Canvas canvas) {
        if (mCustomView != null) {
            float cy = mCustomView.getMeasuredHeight() / 2, cx = mCustomView.getMeasuredWidth() / 2;
            float diffY, diffX;
            float marginY, marginX;
            switch (mCustomViewGravity) {
                case Gravity.LEFT:
                    diffY = mCenterY - cy;
                    mCustomView.layout(0, 0, (int)(mCenterX - 2 * mRadius - 2 * mCustomViewMargin),
                            (int) (mCustomView.getMeasuredHeight() + 2 * diffY));
                    break;

                case Gravity.TOP:
                    cy = mCustomView.getMeasuredHeight() / 2;
                    diffY = mCenterY - cy;
                    marginY = diffY - (2 * (mRadius + mCustomViewMargin));
                    mCustomView.layout(0, 0, mCustomView.getMeasuredWidth(),
                            (int) (mCustomView.getMeasuredHeight() + 2 * marginY));

                    break;

                case Gravity.RIGHT:
                    diffY = mCenterY - cy;
                    mCustomView.layout(0, 0, (int)(mCustomView.getMeasuredWidth() + mCenterX + 2 * mRadius + 2 * mCustomViewMargin),
                            (int) (mCustomView.getMeasuredHeight() + 2 * diffY));
                    break;

                case Gravity.BOTTOM:
                    cy = mCustomView.getMeasuredHeight() / 2;
                    diffY = cy - mCenterY;
                    marginY = diffY - 2 * (mRadius + mCustomViewMargin);
                    mCustomView.layout(0, 0, mCustomView.getMeasuredWidth(),
                            (int) (mCustomView.getMeasuredHeight() - 2 * marginY));
                    break;
            }
            mCustomView.draw(canvas);
        } else {
            Log.d(TAG, "No Custom View defined");
        }
    }

    private void setShowcase(Canvas canvas) {

        calculateRadiusAndCenter();
        Bitmap bitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
        tempCanvas = new Canvas(bitmap);

        backgroundPaint.setColor(backgroundOverlayColor);

        transparentPaint.setColor(getResources().getColor(android.R.color.transparent));
        transparentPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        ringPaint.setColor(ringColor);
        ringPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.ADD));

        tempCanvas.drawRect(0, 0, tempCanvas.getWidth(), tempCanvas.getHeight(), backgroundPaint);
        tempCanvas.drawCircle(mCenterX, mCenterY, mRadius + 10, ringPaint);
        tempCanvas.drawCircle(mCenterX, mCenterY, mRadius, transparentPaint);

        canvas.drawBitmap(bitmap, 0, 0, new Paint());
    }
}
