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
public class ShowcaseViewBuilder extends View implements View.OnTouchListener{

    private Activity mActivity;
    private View mTargetView;
    private List<View> mCustomView = new ArrayList<>();
    private List<Integer> mCustomViewGravity = new ArrayList<>();
    private float mCenterX, mCenterY, mRadius;
    private Drawable mMarkerDrawable;
    private int mMarkerDrawableGravity;
    private int ringColor, backgroundOverlayColor;
    private int mCustomViewMargin;
    private HashMap<Rect, Integer> idsRectMap = new HashMap<>();
    private HashMap<Integer, OnClickListener> idsClickListenerMap = new HashMap<>();
    private boolean mHideOnTouchOutside;

    private Canvas tempCanvas;
    private Paint backgroundPaint, transparentPaint, ringPaint;

    private static final String TAG = "SHOWCASE_VIEW";

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
            mRadius = (width) / 2;
        } else {
            mRadius = (height) / 2;
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
                invalidate();
                addShowcaseView();
                mTargetView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
        setOnTouchListener(this);
    }

    private void addShowcaseView() {
        ((ViewGroup) mActivity.getWindow().getDecorView()).addView(this);
    }

    public void hide() {
        mCustomView.clear();
        mCustomViewGravity.clear();
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
        if (mCustomView.size() != 0) {
            for (int i = 0; i < mCustomView.size(); i++) {
                float cy = mCustomView.get(i).getMeasuredHeight() / 2, cx = mCustomView.get(i).getMeasuredWidth() / 2;
                float diffY, diffX;
                float marginY, marginX;
                switch (mCustomViewGravity.get(i)) {
                    case Gravity.LEFT:
                        diffY = mCenterY - cy;
                        mCustomView.get(i).layout(0, 0, (int) (mCenterX - 2 * mRadius - 2 * mCustomViewMargin),
                                (int) (mCustomView.get(i).getMeasuredHeight() + 2 * diffY));
                        break;

                    case Gravity.TOP:
                        cy = mCustomView.get(i).getMeasuredHeight() / 2;
                        diffY = mCenterY - cy;
                        marginY = diffY - (2 * (mRadius + mCustomViewMargin));
                        mCustomView.get(i).layout(0, 0, mCustomView.get(i).getMeasuredWidth(),
                                (int) (mCustomView.get(i).getMeasuredHeight() + 2 * marginY));
                        break;

                    case Gravity.RIGHT:
                        diffY = mCenterY - cy;
                        mCustomView.get(i).layout(0, 0, (int) (mCustomView.get(i).getMeasuredWidth() + mCenterX + 2 * mRadius + 2 * mCustomViewMargin),
                                (int) (mCustomView.get(i).getMeasuredHeight() + 2 * diffY));
                        break;

                    case Gravity.BOTTOM:
                        cy = mCustomView.get(i).getMeasuredHeight() / 2;
                        diffY = cy - mCenterY;
                        marginY = diffY - 2 * (mRadius + mCustomViewMargin);
                        mCustomView.get(i).layout(0, 0, mCustomView.get(i).getMeasuredWidth(),
                                (int) (mCustomView.get(i).getMeasuredHeight() - 2 * marginY));
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

        transparentPaint.setColor(getResources().getColor(android.R.color.transparent));
        transparentPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        ringPaint.setColor(ringColor);
        ringPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.ADD));

        tempCanvas.drawRect(0, 0, tempCanvas.getWidth(), tempCanvas.getHeight(), backgroundPaint);
        tempCanvas.drawCircle(mCenterX, mCenterY, mRadius + 10, ringPaint);
        tempCanvas.drawCircle(mCenterX, mCenterY, mRadius, transparentPaint);

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
                if (r.contains((int)X, (int)Y)) {
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

