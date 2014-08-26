package com.utilsframework.android.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import com.utilsframework.android.R;

/**
 * Created with IntelliJ IDEA.
 * User: CM
 * Date: 02.11.12
 * Time: 19:21
 * To change this template use File | Settings | File Templates.
 */
public class LayoutRadioButtonGroup extends LinearLayout {
    private int selectedItemIndex = -1;
    private OnSelectedChanged onSelectedChangedListener;

    public LayoutRadioButtonGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs,R.styleable.LayoutRadioButtonGroup);
        selectedItemIndex = array.getInt(R.styleable.LayoutRadioButtonGroup_selected_index,0);
        array.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        internalSetSelectedItemIndex();
    }

    public int getSelectedItemIndex() {
        return selectedItemIndex;
    }

    private void internalSetSelectedItemIndex(){
        //check for out of range
        for(int i = 0; i < getChildCount(); i++) {
            LayoutRadioButton child = (LayoutRadioButton) getChildAt(i);
            child.isSelected = false;
        }
        LayoutRadioButton childToSelect = (LayoutRadioButton)getChildAt(selectedItemIndex);
        childToSelect.isSelected = true;
    }

    private boolean selectFirst = true;

    private void setSelectedItemIndex(int selectedItemIndex,boolean fromUser) {
        if(selectedItemIndex != this.selectedItemIndex){
            LayoutRadioButton old = getSelectedItem();
            LayoutRadioButton item = (LayoutRadioButton)getChildAt(selectedItemIndex);

            if(onSelectedChangedListener != null){
                onSelectedChangedListener.onSelectedChanged(fromUser,item,old);
            }
            if(item.onSelectedChangedListener != null){
                item.onSelectedChangedListener.onSelectedChanged(fromUser,item,old);
            }

            this.selectedItemIndex = selectedItemIndex;
            internalSetSelectedItemIndex();

            old.refreshDrawableState();
            item.refreshDrawableState();
        }
    }

    public OnSelectedChanged getOnSelectedChangedListener() {
        return onSelectedChangedListener;
    }

    public void setOnSelectedChangedListener(OnSelectedChanged onSelectedChangedListener) {
        this.onSelectedChangedListener = onSelectedChangedListener;
        if(selectFirst){
            onSelectedChangedListener.onSelectedChanged(false, getSelectedItem(), null);
            selectFirst = false;
        }
    }

    public class ItemNotFoundException extends Throwable{

    }

    public int getItemIndex(LayoutRadioButton item) throws ItemNotFoundException{
        for(int i = 0; i < getChildCount(); i++){
            View child = getChildAt(i);
            if(child == item){
                return i;
            }
        }
        throw new ItemNotFoundException();
    }

    public LayoutRadioButton getSelectedItem(){
        return (LayoutRadioButton)getChildAt(selectedItemIndex);
    }

    public int getSelectedItemId(){
        LayoutRadioButton selectedItem = getSelectedItem();
        return selectedItem.getId();
    }

    private void setSelectedItem(LayoutRadioButton item,boolean fromUser) throws ItemNotFoundException{
        int index = getItemIndex(item);
        setSelectedItemIndex(index,fromUser);
    }

    public void setSelectedItem(LayoutRadioButton item) throws ItemNotFoundException{
        setSelectedItem(item,false);
    }

    private static final int[] IS_SELECTED = {R.attr.is_selected};

    public interface OnSelectedChanged{
        void onSelectedChanged(boolean fromUser, LayoutRadioButton item, LayoutRadioButton old);
    }

    public static class LayoutRadioButton extends LinearLayout {
        private boolean isSelected = false;
        private OnSelectedChanged onSelectedChangedListener;

        public boolean isSelected() {
            return isSelected;
        }

        public LayoutRadioButtonGroup getLayoutRadioButtonGroup(){
            return (LayoutRadioButtonGroup)getParent();
        }

        public int getIndex(){
            try {
                return getLayoutRadioButtonGroup().getItemIndex(this);
            }
            catch (ItemNotFoundException e){
                throw new RuntimeException(e);
            }
        }

        private void select(boolean fromUser){
            try{
                getLayoutRadioButtonGroup().setSelectedItem(this,fromUser);
            }
            catch (ItemNotFoundException e){
                throw new RuntimeException(e);
            }
        }

        public OnSelectedChanged getOnSelectedChangedListener() {
            return onSelectedChangedListener;
        }

        public void setOnSelectedChangedListener(OnSelectedChanged onSelectedChangedListener) {
            this.onSelectedChangedListener = onSelectedChangedListener;
            LayoutRadioButtonGroup parent = getLayoutRadioButtonGroup();
            if(parent.selectFirst){
                LayoutRadioButton selected = parent.getSelectedItem();
                if(selected == this){
                    this.onSelectedChangedListener.onSelectedChanged(false,selected,null);
                    OnSelectedChanged parentListener = parent.getOnSelectedChangedListener();
                    if(parentListener != null){
                        parentListener.onSelectedChanged(false,selected,null);
                    }
                }
                parent.selectFirst = false;
            }
        }

        public void select(){
            select(false);
        }

        public LayoutRadioButton(Context ctx, AttributeSet attrs) {
            super(ctx, attrs);
            TypedArray array = ctx.obtainStyledAttributes(attrs,R.styleable.LayoutRadioButton);
            array.recycle();
        }

        @Override
        protected int[] onCreateDrawableState(int extraSpace) {
            int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
            if(isSelected){
                drawableState = mergeDrawableStates(drawableState,IS_SELECTED);
            }
            return drawableState;
        }

        @Override
        public boolean performClick() {
            select(true);
            return super.performClick();
        }
    }
}
