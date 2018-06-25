package outlander.showcaseview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by aashish totla.
 */
public class ShowcaseViewBuilder extends View implements View.OnTouchListener {

    private Activity mActivity;
    private View mTargetView;
    private List<View> mCustomView = new ArrayList<>();
    private List<Float> mCustomViewLeftMargins = new ArrayList<>();
    private List<Float> mCustomViewTopMargins = new ArrayList<>();
    private List<Float> mCustomViewRightMargins = new ArrayList<>();
    private List<Float> mCustomViewBottomMargins = new ArrayList<>();
    private List<Integer> mCustomViewGravity = new ArrayList<>();
    private float mCenterX, mCenterY, mRadius;
    private Drawable mMarkerDrawable;
    private int mMarkerDrawableGravity;
    private int ringColor, backgroundOverlayColor;
    private int mCustomViewMargin, mShape = SHAPE_CIRCLE, mBgOverlayShape = FULL_SCREEN;
    private int mRoundRectCorner;
    private HashMap<Rect, Integer> idsRectMap = new HashMap<>();
    private HashMap<Integer, OnClickListener> idsClickListenerMap = new HashMap<>();
    private boolean mHideOnTouchOutside;
    private float mRingWidth = 10, mShowcaseMargin = 12, mRoundRectOffset = 170;
    private float mMarkerDrawableLeftMargin = 0, mMarkerDrawableRightMargin = 0,
            mMarkerDrawableTopMargin = 0, mMarkerDrawableBottomMargin = 0;
    private Canvas tempCanvas;
    private Paint backgroundPaint, transparentPaint, ringPaint;
    private Rect mTargetViewGlobalRect;
    private static final String TAG = "SHOWCASE_VIEW";
    //Showcase Shapes constants
    public static final int SHAPE_CIRCLE = 0;   //Default Shape
    public static final int SHAPE_SKEW = 1;
    //Bg Overlay Shapes constants
    public static final int FULL_SCREEN = 2;   //Default Shape
    public static final int ROUND_RECT = 3;
    //Round rect corner direction constants
    public static final int BOTTOM_LEFT = 4;
    public static final int BOTTOM_RIGHT = 5;
    public static final int TOP_LEFT = 6;
    public static final int TOP_RIGHT = 7;

    private ShowcaseViewBuilder(Context context) {
        super(context);
    }

    private ShowcaseViewBuilder(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static ShowcaseViewBuilder init(Activity activity) {
        ShowcaseViewBuilder showcaseViewBuilder = new ShowcaseViewBuilder(activity);
        showcaseViewBuilder.mActivity = activity;
        showcaseViewBuilder.setClickable(true);
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
            mRadius = 7 * (width) / 12;
        } else {
            mRadius = 7 * (height) / 12;
        }
    }

    public ShowcaseViewBuilder setHideOnTouchOutside(boolean value) {
        this.mHideOnTouchOutside = value;
        return this;
    }

    public ShowcaseViewBuilder setMarkerDrawable(Drawable drawable, int gravity) {
        this.mMarkerDrawable = drawable;
        this.mMarkerDrawableGravity = gravity;
        return this;
    }

    public ShowcaseViewBuilder setDrawableLeftMargin(float margin) {
        this.mMarkerDrawableLeftMargin = margin;
        return this;
    }

    public ShowcaseViewBuilder setRoundRectOffset(float roundRectOffset) {
        this.mRoundRectOffset = roundRectOffset;
        return this;
    }

    public ShowcaseViewBuilder setDrawableRightMargin(float margin) {
        this.mMarkerDrawableRightMargin = margin;
        return this;
    }

    public ShowcaseViewBuilder setDrawableTopMargin(float margin) {
        this.mMarkerDrawableTopMargin = margin;
        return this;
    }

    public ShowcaseViewBuilder setDrawableBottomMargin(float margin) {
        this.mMarkerDrawableBottomMargin = margin;
        return this;
    }

    public ShowcaseViewBuilder setShowcaseShape(int shape) {
        this.mShape = shape;
        return this;
    }

    public ShowcaseViewBuilder setBgOverlayShape(int bgOverlayShape) {
        this.mBgOverlayShape = bgOverlayShape;
        return this;
    }

    public ShowcaseViewBuilder setRoundRectCornerDirection(int roundRectCornerDirection) {
        this.mRoundRectCorner = roundRectCornerDirection;
        return this;
    }

    public ShowcaseViewBuilder addCustomView(int layoutId, int gravity) {
        View view = LayoutInflater.from(mActivity).inflate(layoutId, null);
        LinearLayout linearLayout = new LinearLayout(mActivity);
        linearLayout.addView(view);
        linearLayout.setGravity(Gravity.CENTER);

        DisplayMetrics metrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        Rect rect = new Rect();
        rect.set(0, 0, metrics.widthPixels, metrics.heightPixels);

        int widthSpec = View.MeasureSpec.makeMeasureSpec(rect.width(), MeasureSpec.EXACTLY);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(rect.height(), MeasureSpec.EXACTLY);

        linearLayout.measure(widthSpec, heightSpec);
        mCustomView.add(linearLayout);
        mCustomViewGravity.add(gravity);
        mCustomViewLeftMargins.add(0f);
        mCustomViewTopMargins.add(0f);
        mCustomViewRightMargins.add(0f);
        mCustomViewBottomMargins.add(0f);
        return this;
    }

    public ShowcaseViewBuilder addCustomView(View view, int gravity) {
        LinearLayout linearLayout = new LinearLayout(mActivity);
        linearLayout.addView(view);
        linearLayout.setGravity(Gravity.CENTER);

        DisplayMetrics metrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        Rect rect = new Rect();
        rect.set(0, 0, metrics.widthPixels, metrics.heightPixels);

        int widthSpec = View.MeasureSpec.makeMeasureSpec(rect.width(), MeasureSpec.EXACTLY);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(rect.height(), MeasureSpec.EXACTLY);

        linearLayout.measure(widthSpec, heightSpec);
        mCustomView.add(linearLayout);
        mCustomViewGravity.add(gravity);
        mCustomViewLeftMargins.add(0f);
        mCustomViewTopMargins.add(0f);
        mCustomViewRightMargins.add(0f);
        mCustomViewBottomMargins.add(0f);
        return this;
    }

    public ShowcaseViewBuilder addCustomView(int layoutId, int gravity, float leftMargin, float topMargin, float rightMargin, float bottomMargin) {
        View view = LayoutInflater.from(mActivity).inflate(layoutId, null);
        LinearLayout linearLayout = new LinearLayout(mActivity);
        linearLayout.addView(view);
        linearLayout.setGravity(Gravity.CENTER);

        DisplayMetrics metrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        Rect rect = new Rect();
        rect.set(0, 0, metrics.widthPixels, metrics.heightPixels);

        int widthSpec = View.MeasureSpec.makeMeasureSpec(rect.width(), MeasureSpec.EXACTLY);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(rect.height(), MeasureSpec.EXACTLY);

        linearLayout.measure(widthSpec, heightSpec);
        mCustomView.add(linearLayout);
        mCustomViewGravity.add(gravity);
        mCustomViewLeftMargins.add(leftMargin);
        mCustomViewTopMargins.add(topMargin);
        mCustomViewRightMargins.add(rightMargin);
        mCustomViewBottomMargins.add(bottomMargin);
        return this;
    }

    public ShowcaseViewBuilder addCustomView(View view, int gravity, float leftMargin, float topMargin, float rightMargin, float bottomMargin) {
        LinearLayout linearLayout = new LinearLayout(mActivity);
        linearLayout.addView(view);
        linearLayout.setGravity(Gravity.CENTER);

        DisplayMetrics metrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        Rect rect = new Rect();
        rect.set(0, 0, metrics.widthPixels, metrics.heightPixels);

        int widthSpec = View.MeasureSpec.makeMeasureSpec(rect.width(), MeasureSpec.EXACTLY);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(rect.height(), MeasureSpec.EXACTLY);

        linearLayout.measure(widthSpec, heightSpec);
        mCustomView.add(linearLayout);
        mCustomViewGravity.add(gravity);
        mCustomViewLeftMargins.add(leftMargin);
        mCustomViewTopMargins.add(topMargin);
        mCustomViewRightMargins.add(rightMargin);
        mCustomViewBottomMargins.add(bottomMargin);
        return this;
    }

    public ShowcaseViewBuilder addCustomView(View view) {
        DisplayMetrics metrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        Rect rect = new Rect();
        rect.set(0, 0, metrics.widthPixels, metrics.heightPixels);

        int widthSpec = View.MeasureSpec.makeMeasureSpec(rect.width(), MeasureSpec.EXACTLY);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(rect.height(), MeasureSpec.EXACTLY);

        view.measure(widthSpec, heightSpec);
        mCustomView.add(view);
        mCustomViewGravity.add(0);
        mCustomViewLeftMargins.add(0f);
        mCustomViewTopMargins.add(0f);
        mCustomViewRightMargins.add(0f);
        mCustomViewBottomMargins.add(0f);
        return this;
    }

    public ShowcaseViewBuilder addCustomView(int layoutId) {
        final View view = LayoutInflater.from(mActivity).inflate(layoutId, null);

        DisplayMetrics metrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        Rect rect = new Rect();
        rect.set(0, 0, metrics.widthPixels, metrics.heightPixels);

        int widthSpec = View.MeasureSpec.makeMeasureSpec(rect.width(), MeasureSpec.EXACTLY);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(rect.height(), MeasureSpec.EXACTLY);

        view.measure(widthSpec, heightSpec);
        mCustomView.add(view);
        mCustomViewGravity.add(0);
        mCustomViewLeftMargins.add(0f);
        mCustomViewTopMargins.add(0f);
        mCustomViewRightMargins.add(0f);
        mCustomViewBottomMargins.add(0f);
        return this;
    }

    /**
     * Deprecated. Use @addCustomView(int layoutId, int gravity, int leftMargin, int topMargin, int rightMargin, int bottomMargin) instead
     *
     * @param margin
     * @return
     */
    public ShowcaseViewBuilder setCustomViewMargin(int margin) {
        this.mCustomViewMargin = margin;
        return this;
    }

    public ShowcaseViewBuilder setRingColor(int color) {
        this.ringColor = color;
        return this;
    }

    public ShowcaseViewBuilder setRingWidth(float ringWidth) {
        this.mRingWidth = ringWidth;
        return this;
    }

    public ShowcaseViewBuilder setShowcaseMargin(float showcaseMargin) {
        this.mShowcaseMargin = showcaseMargin;
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
        if (mTargetView != null) {
            if (mTargetView.getWidth() == 0 || mTargetView.getHeight() == 0) {
                mTargetView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        invalidate();
                        addShowcaseView();
                        mTargetView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                });
            } else {
                invalidate();
                addShowcaseView();
            }
        }
        setOnTouchListener(this);
    }

    private void addShowcaseView() {
        ((ViewGroup) mActivity.getWindow().getDecorView()).addView(this);
    }

    public void hide() {
        mCustomView.clear();
        mCustomViewGravity.clear();
        mCustomViewLeftMargins.clear();
        mCustomViewRightMargins.clear();
        mCustomViewTopMargins.clear();
        mCustomViewBottomMargins.clear();
        idsClickListenerMap.clear();
        idsRectMap.clear();
        mHideOnTouchOutside = false;
        ((ViewGroup) mActivity.getWindow().getDecorView()).removeView(this);
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
                case Gravity.START:
                    mMarkerDrawable.setBounds((int) (mCenterX + mMarkerDrawableLeftMargin - mRadius - mMarkerDrawable.getMinimumWidth() - mRingWidth - 10),
                            (int) (mCenterY + mMarkerDrawableTopMargin - mMarkerDrawable.getMinimumHeight()),
                            (int) (mCenterX + mMarkerDrawableLeftMargin - mRadius - mRingWidth - 10), (int) (mCenterY + mMarkerDrawableTopMargin));
                    break;

                case Gravity.TOP:
                    mMarkerDrawable.setBounds((int) (mCenterX + mMarkerDrawableLeftMargin - mMarkerDrawable.getMinimumWidth()),
                            (int) (mCenterY + mMarkerDrawableTopMargin - mRadius - mMarkerDrawable.getMinimumHeight() - mRingWidth - 10),
                            (int) (mCenterX + mMarkerDrawableLeftMargin), (int) (mCenterY + mMarkerDrawableTopMargin - mRadius - mRingWidth - 10));
                    break;

                case Gravity.RIGHT:
                case Gravity.END:
                    mMarkerDrawable.setBounds((int) (mCenterX + mMarkerDrawableLeftMargin + mRadius + mRingWidth + 10),
                            (int) (mCenterY + mMarkerDrawableTopMargin - mMarkerDrawable.getMinimumHeight()),
                            (int) (mCenterX + mMarkerDrawableLeftMargin + mRadius + mMarkerDrawable.getMinimumWidth() + mRingWidth + 10),
                            (int) (mCenterY + mMarkerDrawableTopMargin));
                    break;

                case Gravity.BOTTOM:
                    mMarkerDrawable.setBounds((int) (mCenterX + mMarkerDrawableLeftMargin - mMarkerDrawable.getMinimumWidth()),
                            (int) (mCenterY + mMarkerDrawableTopMargin + mRadius + mRingWidth + 10), (int) (mCenterX + mMarkerDrawableLeftMargin),
                            (int) (mCenterY + mMarkerDrawableTopMargin + mRadius + mMarkerDrawable.getMinimumHeight() + mRingWidth + 10));
                    break;
            }

            mMarkerDrawable.draw(canvas);
        } else {
            Log.d(TAG, "No marker drawable defined");
        }
    }

    private void addCustomView(Canvas canvas) {
        if (mCustomView.size() != 0) {
            for (int i = 0; i < mCustomView.size(); i++) {
                float cy = mCustomView.get(i).getMeasuredHeight() / 2, cx = mCustomView.get(i).getMeasuredWidth() / 2;
                float diffY, diffX;
                float marginTop = mCustomViewTopMargins.get(i);
                float marginLeft = mCustomViewLeftMargins.get(i);
                float marginRight = mCustomViewRightMargins.get(i);
                float marginBottom = mCustomViewBottomMargins.get(i);
                mTargetViewGlobalRect = new Rect();
                mTargetView.getGlobalVisibleRect(mTargetViewGlobalRect);
                View view = mCustomView.get(i);
                switch (mCustomViewGravity.get(i)) {
                    case Gravity.START:
                    case Gravity.LEFT:
                        diffY = mCenterY - cy;
                        diffX = mCenterX - cx;
                        if (diffX < 0) {
                            view.layout(0, 0, (int) (mCenterX - view.getMeasuredWidth() - 2 * marginRight),
                                    (int) (mCustomView.get(i).getMeasuredHeight() + 2 * (diffY + marginTop)));
                        } else {
                            view.layout((int) diffX, 0, (int) (view.getMeasuredWidth() - diffX - 2 * marginRight),
                                    (int) (mCustomView.get(i).getMeasuredHeight() + 2 * (diffY + marginTop)));
                        }
                        break;

                    case Gravity.TOP:
                        diffY = mCenterY - cy - 2 * mTargetView.getMeasuredHeight();
                        view.layout((int) (-marginLeft), 0, (int) (view.getMeasuredWidth() + marginLeft),
                                (int) (mCustomView.get(i).getMeasuredHeight() + 2 * (diffY + marginTop)));
                        break;

                    case Gravity.END:
                    case Gravity.RIGHT:
                        diffY = mCenterY - cy;
                        view.layout(-2 * mTargetViewGlobalRect.right, 0,
                                (int) (view.getMeasuredWidth() + 4 * marginLeft), (int) (mCustomView.get(i).getMeasuredHeight() + 2 * (diffY + marginTop)));
                        break;

                    case Gravity.BOTTOM:
                        diffY = mCenterY - cy + 2 * mTargetView.getMeasuredHeight();
                        view.layout((int) (-marginLeft), 0, (int) (view.getMeasuredWidth() + marginLeft),
                                (int) (mCustomView.get(i).getMeasuredHeight() + 2 * (diffY + marginTop)));
                        break;

                    default:
                        mCustomView.get(i).layout(0, 0, mCustomView.get(i).getMeasuredWidth(), mCustomView.get(i).getMeasuredHeight());
                }
                mCustomView.get(i).draw(canvas);
            }
        } else {
            Log.d(TAG, "No Custom View defined");
        }
    }

    private ArrayList<View> getAllChildren(View v) {
        if (!(v instanceof ViewGroup)) {
            ArrayList<View> viewArrayList = new ArrayList<>();
            viewArrayList.add(v);
            return viewArrayList;
        }

        ArrayList<View> result = new ArrayList<>();

        ViewGroup viewGroup = (ViewGroup) v;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            ArrayList<View> viewArrayList = new ArrayList<>();
            viewArrayList.add(v);
            viewArrayList.addAll(getAllChildren(child));

            result.addAll(viewArrayList);
        }
        return result;
    }

    private void setShowcase(Canvas canvas) {
        calculateRadiusAndCenter();
        Bitmap bitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
        tempCanvas = new Canvas(bitmap);

        backgroundPaint.setColor(backgroundOverlayColor);
        backgroundPaint.setAntiAlias(true);

        transparentPaint.setColor(getResources().getColor(android.R.color.transparent));
        transparentPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        transparentPaint.setAntiAlias(true);

        ringPaint.setColor(ringColor);
        ringPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.ADD));
        ringPaint.setAntiAlias(true);

        if (mBgOverlayShape == ROUND_RECT) {
            RectF oval = new RectF();
            switch (mRoundRectCorner) {
                case BOTTOM_LEFT:
                    oval.set(-mRoundRectOffset, -tempCanvas.getHeight(), 2 * tempCanvas.getWidth() + mRoundRectOffset, tempCanvas.getHeight());
                    tempCanvas.drawArc(oval, 90F, 90F, true, backgroundPaint);
                    break;

                case BOTTOM_RIGHT:
                    oval.set(-tempCanvas.getWidth() - mRoundRectOffset, -tempCanvas.getHeight(), tempCanvas.getWidth() + mRoundRectOffset, tempCanvas.getHeight());
                    tempCanvas.drawArc(oval, 360F, 90F, true, backgroundPaint);
                    break;

                case TOP_LEFT:
                    oval.set(-mRoundRectOffset, 0, 2 * tempCanvas.getWidth() + mRoundRectOffset, 2 * tempCanvas.getHeight());
                    tempCanvas.drawArc(oval, 180F, 90F, true, backgroundPaint);
                    break;

                case TOP_RIGHT:
                    oval.set(-tempCanvas.getWidth() - mRoundRectOffset, 0, tempCanvas.getWidth() + mRoundRectOffset, 2 * tempCanvas.getHeight());
                    tempCanvas.drawArc(oval, 270F, 90F, true, backgroundPaint);
                    break;

                default:
                    oval.set(-mRoundRectOffset, -tempCanvas.getHeight(), 2 * tempCanvas.getWidth() + mRoundRectOffset, tempCanvas.getHeight());
                    tempCanvas.drawArc(oval, 90F, 90F, true, backgroundPaint);
                    break;
            }
        } else {
            tempCanvas.drawRect(0, 0, tempCanvas.getWidth(), tempCanvas.getHeight(), backgroundPaint);
        }

        if (mShape == SHAPE_SKEW) {
            Rect r = new Rect();
            Rect ring = new Rect();
            mTargetView.getGlobalVisibleRect(r);
            mTargetView.getGlobalVisibleRect(ring);
            //Showcase rect
            r.top -= mShowcaseMargin;
            r.left -= mShowcaseMargin;
            r.right += mShowcaseMargin;
            r.bottom += mShowcaseMargin;
            //Showcase ring rect
            ring.top -= mShowcaseMargin + mRingWidth;
            ring.left -= mShowcaseMargin + mRingWidth;
            ring.right += mShowcaseMargin + mRingWidth;
            ring.bottom += mShowcaseMargin + mRingWidth;
            tempCanvas.drawRect(ring, ringPaint);
            tempCanvas.drawRect(r, transparentPaint);
        } else {
            tempCanvas.drawCircle(mCenterX, mCenterY, mRadius + mRingWidth, ringPaint);
            tempCanvas.drawCircle(mCenterX, mCenterY, mRadius, transparentPaint);
        }

        canvas.drawBitmap(bitmap, 0, 0, new Paint());
    }

    public void setClickListenerOnView(int id, final View.OnClickListener clickListener) {
        idsClickListenerMap.put(id, clickListener);
    }

    private int getAbsoluteLeft(View myView) {
        if (myView == null) {
            return 0;
        }
        if (myView.getParent() == myView.getRootView())
            return myView.getLeft();
        else
            return myView.getLeft() + getAbsoluteLeft((View) myView.getParent());
    }

    private int getAbsoluteTop(View myView) {
        if (myView == null) {
            return 0;
        }
        if (myView.getParent() == myView.getRootView())
            return myView.getTop();
        else
            return myView.getTop() + getAbsoluteTop((View) myView.getParent());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (idsRectMap.isEmpty()) {
            for (View parentView : mCustomView) {
                List<View> childrenViews = getAllChildren(parentView);
                for (final View view : childrenViews) {
                    Rect rect = new Rect();
                    rect.set(getAbsoluteLeft(view), getAbsoluteTop(view),
                            getAbsoluteLeft(view) + view.getMeasuredWidth(), getAbsoluteTop(view) + view.getMeasuredHeight());
                    if (view.getId() > 0) {
                        idsRectMap.put(rect, view.getId());
                    }
                }
            }
        }

        if (event.getAction() == MotionEvent.ACTION_UP) {
            float X = event.getX();
            float Y = event.getY();
            Object[] keys = idsRectMap.keySet().toArray();
            for (int i = 0; i < idsRectMap.size(); i++) {
                Rect r = (Rect) keys[i];
                if (r.contains((int) X, (int) Y)) {
                    int id = idsRectMap.get(r);
                    if (idsClickListenerMap.get(id) != null) {
                        idsClickListenerMap.get(id).onClick(v);
                        return true;
                    }
                }
            }

            if (mHideOnTouchOutside) {
                hide();
                return true;
            }
        }
        return false;
    }
}

