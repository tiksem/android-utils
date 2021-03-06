package com.utilsframework.android.view;

import java.util.*;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.*;

import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.utils.framework.CollectionUtils;
import com.utils.framework.Predicate;
import com.utils.framework.predicates.InstanceOfPredicate;
import com.utilsframework.android.BuildConfig;
import com.utilsframework.android.ListenerRemover;
import com.utilsframework.android.UiLoopEvent;
import com.utilsframework.android.WeakUiLoopEvent;
import com.utilsframework.android.system.Intents;
import com.utilsframework.android.threading.OnFinish;
import com.utilsframework.android.threading.ResultLoop;

/**
 * Created by Tikhonenko.S on 19.09.13.
 */
public class GuiUtilities {

	private static final String TAG = "GuiUtilities";

    public static List<View> getAllChildrenRecursive(View view){
        if(!(view instanceof ViewGroup)){
            return Collections.emptyList();
        }

        List<View> result = new ArrayList<View>();
        ViewGroup viewGroup = (ViewGroup)view;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            result.add(child);
            result.addAll(getAllChildrenRecursive(child));
        }

        return result;
    }

    public static <T extends View> List<T> getAllChildrenRecursiveInSubView(View view,
                                                                            int subViewId,
                                                                            Class<T> aClass){
        View subView = view.findViewById(subViewId);
        if (subView == null) {
            throw new NullPointerException("subView not found");
        }

        return getAllChildrenRecursive(subView, aClass);
    }

    public static List<View> getAllChildrenRecursive(View view, Predicate<View> predicate){
        List<View> views = getAllChildrenRecursive(view);
        return CollectionUtils.findAll(views, predicate);
    }

    public static <T extends View> List<T> getAllChildrenRecursive(View view, Class<T> aClass){
        InstanceOfPredicate<View> predicate = new InstanceOfPredicate<View>(aClass);
        return (List<T>) getAllChildrenRecursive(view, predicate);
    }

    public static <T extends View> T getFirstChildWithTypeRecursive(View view, Class<T> aClass){
        List<T> all = getAllChildrenRecursive(view, aClass);
        if(all.isEmpty()){
            return null;
        }

        if(all.size() > 1){
            throw new RuntimeException("all.size() > 1");
        }

        return all.get(0);
    }

    public static List<View> getAllChildrenRecursive(Activity activity){
        return getAllChildrenRecursive(activity.getWindow().getDecorView().getRootView());
    }

    public static List<View> getAllChildrenRecursive(Activity activity, Predicate<View> predicate){
        List<View> views = getAllChildrenRecursive(activity);
        return CollectionUtils.findAll(views, predicate);
    }

    public static <T extends View> List<T> getAllChildrenRecursive(Activity activity, Class<T> aClass){
        InstanceOfPredicate<View> predicate = new InstanceOfPredicate<View>(aClass);
        return (List<T>) getAllChildrenRecursive(activity, predicate);
    }

    public static List<View> getNonViewGroupChildrenRecursive(View view){
        List<View> children = getAllChildrenRecursive(view);
        CollectionUtils.removeAllWithType(children, ViewGroup.class);
        return children;
    }

    public static void setVisibility(Iterable<View> views, int visibility){
        for(View view : views){
            view.setVisibility(visibility);
        }
    }

    public static void setVisibility(View[] views, int visibility){
        setVisibility(Arrays.asList(views), visibility);
    }

    public static void setVisibility(int visibility, View... views){
        setVisibility(Arrays.asList(views), visibility);
    }

    public static View getContentView(Activity activity){
        return activity.getWindow().getDecorView().getRootView();
    }

    public static View[] resourceIdArrayToViewArray(Activity activity, int... ides) {
        View[] result = new View[ides.length];

        int index = 0;
        for(int id : ides){
            result[index++] = activity.findViewById(id);
        }

        return result;
    }

    @SuppressLint("NewApi")
	public static Point getViewCenter(View view) {
        float x = view.getX();
        float y = view.getY();
        int width = view.getMeasuredWidth();
        int height = view.getMeasuredHeight();

        return new Point(Math.round(x + width / 2), Math.round(y + height / 2));
    }

    public static void lockOrientation(Activity context) {
    	if (Build.VERSION.SDK_INT >= 18) {
    		context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
		} else {
            int orientation;
            int rotation = ((WindowManager) context.getSystemService(
                    Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
            switch (rotation) {
                case Surface.ROTATION_0:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
                case Surface.ROTATION_90:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
                case Surface.ROTATION_180:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                    break;
                default:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                    break;
            }

            context.setRequestedOrientation(orientation);
    	}

    }

    public static void unlockOrientation(Activity context) {
        context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    }

    public static void removeView(View view) {
        ViewParent parent = view.getParent();
        if (parent != null) {
            ((ViewGroup)parent).removeView(view);
        }
    }

    public static void removeAllViews(Iterable<? extends View> views) {
        for(View view : views){
            removeView(view);
        }
    }

    public static Iterator<View> getChildrenIterator(final ViewGroup viewGroup) {
        return new Iterator<View>() {
            int index = 0;

            @Override
            public boolean hasNext() {
                return index < viewGroup.getChildCount();
            }

            @Override
            public View next() {
                return viewGroup.getChildAt(index++);
            }

            @Override
            public void remove() {
                viewGroup.removeViewAt(index - 1);
            }
        };
    }

    public static Iterable<View> getChildren(final ViewGroup viewGroup) {
        return new Iterable<View>() {
            @Override
            public Iterator<View> iterator() {
                return getChildrenIterator(viewGroup);
            }
        };
    }

    public static List<View> getChildrenAsList(final ViewGroup viewGroup) {
        int childCount = viewGroup.getChildCount();
        List<View> result = new ArrayList<View>(childCount);
        for (int i = 0; i < childCount; i++) {
            result.add(viewGroup.getChildAt(i));
        }

        return result;
    }

    public static List<View> getChildrenAsListNonCopy(final ViewGroup viewGroup) {
        return new AbstractList<View>() {
            @Override
            public View get(int location) {
                return viewGroup.getChildAt(location);
            }

            @Override
            public int size() {
                return viewGroup.getChildCount();
            }

            @Override
            public boolean add(View object) {
                viewGroup.addView(object);
                return true;
            }
        };
    }

    public static <T extends View> List<T> getChildrenAsList(final ViewGroup viewGroup, Class<T> aClass) {
        int childCount = viewGroup.getChildCount();
        List<T> result = new ArrayList<T>(childCount);
        for (int i = 0; i < childCount; i++) {
            View view = viewGroup.getChildAt(i);
            if (aClass.isAssignableFrom(view.getClass())) {
                result.add((T) view);
            }
        }

        return result;
    }

    public static List<View> findChildren(ViewGroup viewGroup, Predicate<View> predicate) {
        return CollectionUtils.findAll(getChildren(viewGroup), predicate);
    }

    public static Bitmap createBitmapFromView(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void removeGlobalOnLayoutListener(View view,
                                                    ViewTreeObserver.OnGlobalLayoutListener listener) {
        try {
            view.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
        } catch (NoSuchMethodError e) {
            view.getViewTreeObserver().removeGlobalOnLayoutListener(listener);
        }
    }

    public static void executeWhenViewMeasuredUsingLoop(final View view, final Runnable runnable) {
        final WeakUiLoopEvent<View> uiLoopEvent = new WeakUiLoopEvent<>(view);
        final long time = System.currentTimeMillis();
        uiLoopEvent.run(new Runnable() {
            @Override
            public void run() {
                View view = uiLoopEvent.get();

                if(view.getMeasuredHeight() != 0 || view.getMeasuredWidth() != 0){
                    runnable.run();
                    uiLoopEvent.stop();
                    return;
                }

                if(System.currentTimeMillis() - time > 1000){
                    uiLoopEvent.stop();
                }
            }
        });
    }

    /* Might not be called, when passed view is a child or sub-child of GridView, use
    executeWhenViewMeasuredUsingLoop instead */
    public static void executeWhenViewMeasured(final View view, final Runnable runnable) {
        if(view.getMeasuredHeight() == 0 || view.getMeasuredWidth() == 0){
            view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    runnable.run();
                    removeGlobalOnLayoutListener(view, this);
                }
            });
        } else {
            runnable.run();
        }
    }

    public static void createBitmapFromViewAsync(final View view, final OnFinish<Bitmap> onFinish) {
        final int measuredWidth = view.getMeasuredWidth();
        final int measuredHeight = view.getMeasuredHeight();
        final AsyncTask<Void, Void, Bitmap> asyncTask = new AsyncTask<Void, Void, Bitmap>(){
            @Override
            protected Bitmap doInBackground(Void... params) {
                return Bitmap.createBitmap(measuredWidth,
                        measuredHeight, Bitmap.Config.ARGB_8888);
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                Canvas canvas = new Canvas(bitmap);
                view.draw(canvas);

                if(onFinish != null){
                    onFinish.onFinish(bitmap);
                }
            }
        };

        if(view.getMeasuredHeight() == 0 || view.getMeasuredWidth() == 0){
            view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onGlobalLayout() {
                    asyncTask.execute();
                    removeGlobalOnLayoutListener(view, this);
                }
            });
        } else {
            asyncTask.execute();
        }
    }

    public interface OnImageViewCreated {
        void onFinish(Bitmap bitmap, ImageView imageView);
    }

    // This method creates an ImageView representation of the given view, converting the view into bitmap,
    // and places the result ImageView in the same position with same width and height on the top
    // level of the current activity
    // *
    // Use GuiUtilities.removeView to delete this view from activity.
    public static void createImageViewCloneOnView(final View view, final OnImageViewCreated onFinish){
        createBitmapFromViewAsync(view, new OnFinish<Bitmap>() {
            @Override
            public void onFinish(final Bitmap bitmap) {
                FrameLayout frameLayout = (FrameLayout) view.getParent();

                final ImageView imageView = new ImageView(view.getContext());
                imageView.setImageBitmap(bitmap);

                frameLayout.addView(imageView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));

                imageView.setX(view.getX());
                imageView.setY(view.getY());

                imageView.getViewTreeObserver().
                        addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                onFinish.onFinish(bitmap, imageView);
                                removeGlobalOnLayoutListener(imageView, this);
                            }
                        });
            }
        });
    }

    public interface OnViewCreated {
        void onViewCreated(View view);
    }

    public static void insertBefore(View viewToInsert, View view) {
        ViewGroup viewGroup = (ViewGroup) view.getParent();
        int index = viewGroup.indexOfChild(view);
        viewGroup.addView(viewToInsert, index);
    }

    public static enum Orientation {
        SQUARE,
        LANDSCAPE,
        PORTRAIT
    }

    public static void getViewOrientation(final View view, final OnFinish<Orientation> onFinish) {
        executeWhenViewMeasured(view, new Runnable() {
            @Override
            public void run() {
                int width = view.getMeasuredWidth();
                int height = view.getMeasuredHeight();

                if(width == height){
                    onFinish.onFinish(Orientation.SQUARE);
                    return;
                }

                Orientation result = Orientation.SQUARE;

                float floatWidth = width;
                float floatHeight = height;

                if (floatWidth / floatHeight > 1.0f) {
                    onFinish.onFinish(Orientation.LANDSCAPE);
                } else {
                    onFinish.onFinish(Orientation.PORTRAIT);
                }
            }
        });
    }

    public static Orientation getScreenOrientation(Activity context)
    {
        Display display = context.getWindowManager().getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);

        Orientation orientation;

        if (size.y == size.x) {
            orientation = Orientation.SQUARE;
        } else {
            if(size.x > size.y){
                orientation = Orientation.LANDSCAPE;
            } else {
                orientation = Orientation.PORTRAIT;
            }
        }

        return orientation;
    }

    public static void startActivityForResult(Activity activity, Fragment fragment, Intent intent, int requestCode) {
        if (activity != null && fragment != null) {
            throw new IllegalArgumentException();
        }

        if (activity == null && fragment == null) {
            throw new IllegalArgumentException();
        }

        if(activity != null){
            activity.startActivityForResult(intent, requestCode);
        } else {
            fragment.startActivityForResult(intent, requestCode);
        }
    }

    public static void setChildrenVisibility(SubMenu subMenu, boolean visibility) {
        for(int i = 0; i < subMenu.size(); i++){
            subMenu.getItem(i).setVisible(visibility);
        }
    }

    public static int getChildrenHeight(ViewGroup viewGroup) {
        if (viewGroup.getChildCount() > 0) {
            View first = viewGroup.getChildAt(0);
            View last = getLastChild(viewGroup);

            return last.getBottom() - first.getTop();
        } else {
            return 0;
        }
    }

    private static View getLastChild(ViewGroup viewGroup) {
        return viewGroup.getChildAt(viewGroup.getChildCount() - 1);
    }

    public static void setClickListenerToMenuItems(Menu menu, MenuItem.OnMenuItemClickListener clickListener) {
        for(int i = 0; i < menu.size(); i++){
            MenuItem menuItem = menu.getItem(i);
            menuItem.setOnMenuItemClickListener(clickListener);
        }
    }

    public static void setClickListenersToChildren(View.OnClickListener clickListener,
                                                   ViewGroup viewGroup) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            viewGroup.getChildAt(i).setOnClickListener(clickListener);
        }
    }

    public interface EditTextFocusListener {
        void onFocusEnter();
        void onFocusLeave(boolean textChanged, String textBefore);
    }

    public static void setEditTextFocusChangedListener(final EditText editText, final EditTextFocusListener focusListener) {
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            String textBefore;

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    focusListener.onFocusEnter();
                    textBefore = editText.getText().toString();
                } else {
                    boolean textChanged = !textBefore.equals(editText.getText().toString());
                    focusListener.onFocusLeave(textChanged, textBefore);
                }
            }
        });
    }

    public static void swapVisibilities(View a, View b) {
        int aVisibility = a.getVisibility();
        int bVisibility = b.getVisibility();
        a.setVisibility(bVisibility);
        b.setVisibility(aVisibility);
    }

    public static String getApplicationName(Context context) {
        int stringId = context.getApplicationInfo().labelRes;
        return context.getString(stringId);
    }

    public static void setEnabledForChildren(ViewGroup viewGroup, boolean enabled) {
        for (View child : getAllChildrenRecursive(viewGroup)) {
            child.setEnabled(enabled);
        }
    }

    public static void replaceView(View currentView, View newView) {
        ViewGroup parent = (ViewGroup) currentView.getParent();
        int indexOfChild = parent.indexOfChild(currentView);
        parent.removeView(currentView);
        GuiUtilities.removeView(newView);
        parent.addView(newView, indexOfChild);
    }

    public static void setProgressBarTintColor(ProgressBar progressBar, int color) {
        Drawable progressDrawable = progressBar.getProgressDrawable();
        progressDrawable.mutate().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        progressBar.setProgressDrawable(progressDrawable);

        Drawable indeterminateDrawable = progressBar.getIndeterminateDrawable();
        indeterminateDrawable.mutate().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        progressBar.setIndeterminateDrawable(indeterminateDrawable);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void setSeekBarTintColor(SeekBar seekBar, int color) {
        setProgressBarTintColor(seekBar, color);
        Drawable thumb = seekBar.getThumb();
        thumb.mutate().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        seekBar.setThumb(thumb);
    }

    public static void setSeekBarTintColor(ExtendedSeekBar seekBar, int color) {
        setProgressBarTintColor(seekBar, color);
        Drawable thumb = seekBar.getSeekBarThumb();
        thumb.mutate().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        seekBar.setThumb(thumb);
    }

    public static void setButtonTintColors(Button button, int colorResourceId) {
        Drawable background = button.getBackground();
        background = DrawableCompat.wrap(background);
        DrawableCompat.setTintList(background, button.getContext().getResources().
                getColorStateList(colorResourceId));
        button.setBackgroundDrawable(background);
    }

    public static boolean isPointInView(float x, float y, View view) {
        float viewX = view.getX();
        float viewY = view.getY();

        float width = view.getMeasuredWidth();
        float height = view.getMeasuredHeight();

        float xDiff = x - viewX;
        if (xDiff < 0 || xDiff >= width) {
            return false;
        }

        float yDiff = y - viewY;
        if (yDiff < 0 || yDiff >= height) {
            return false;
        }

        return true;
    }

    public static void setTopPadding(View view, int topPadding) {
        view.setPadding(view.getPaddingLeft(), topPadding, view.getPaddingRight(),
                view.getPaddingBottom());
    }

    public static void setLeftPadding(View view, int leftPadding) {
        view.setPadding(leftPadding, view.getPaddingTop(), view.getPaddingRight(),
                view.getPaddingBottom());
    }

    public static void toggleVisibilityGoneAndVisible(View view) {
        if (view.getVisibility() == View.GONE) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    public static void setLeftDrawable(TextView textView, Drawable drawable) {
        Drawable[] compoundDrawables = textView.getCompoundDrawables();
        textView.setCompoundDrawables(drawable, compoundDrawables[1],
                compoundDrawables[2], compoundDrawables[3]);
    }

    public static void setupLinkButton(View root,
                                       int buttonId, final String link) {
        View linkButton = root.findViewById(buttonId);
        if (link != null) {
            linkButton.setVisibility(View.VISIBLE);
            linkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intents.openUrl(v.getContext(), link);
                }
            });
        } else {
            linkButton.setVisibility(View.GONE);
        }
    }

    public static void setupLinkButton(final Activity activity,
                                       int buttonId, final String link) {
        View linkButton = activity.findViewById(buttonId);
        if (link != null) {
            linkButton.setVisibility(View.VISIBLE);
            linkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intents.openUrl(activity, link);
                }
            });
        } else {
            linkButton.setVisibility(View.GONE);
        }
    }
}
