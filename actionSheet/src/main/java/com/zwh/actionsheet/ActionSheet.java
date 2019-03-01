package com.zwh.actionsheet;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class ActionSheet implements View.OnClickListener{

    public static class Builder {
        private Context context;
        private String mCancelItemTitle = "取消";
        private String[] actionItemsTitle;
        private boolean mCancelableOnTouchOutside = true;
        private ActionSheetListener mListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setCancelButtonTitle(String title) {
            mCancelItemTitle = title;
            return this;
        }

        public Builder setCancelButtonTitle(int strId) {
            return setCancelButtonTitle(context.getString(strId));
        }

        public Builder setActionItemTitles(String... titles) {
            actionItemsTitle = titles;
            return this;
        }

        public Builder setListener(ActionSheetListener listener) {
            this.mListener = listener;
            return this;
        }

        public Builder setCancelableOnTouchOutside(boolean cancelable) {
            mCancelableOnTouchOutside = cancelable;
            return this;
        }

        public ActionSheet show() {
            ActionSheet actionSheet = new ActionSheet(context,mCancelItemTitle,actionItemsTitle,mCancelableOnTouchOutside);
            actionSheet.setActionSheetListener(mListener);
            actionSheet.show();
            return actionSheet;
        }

    }

    public static Builder createBuilder(Context context){

        return new Builder(context);
    }

    private Context context;
    private Dialog dialog;
//    private TextView txt_title;
    private TextView     txt_cancel;
    private LinearLayout lLayout_content;
    private ScrollView sLayout_content;
    private boolean showTitle = false;

    private Display display;

    private static final int CANCEL_ITEM_ID = 100;
    private String[] sheetItems;
    private String cancelTitle= "取消";
    private ActionSheetListener sheetListener;
    private boolean isCancel = true;
    private boolean cancelableOnTouchOutside = true;
    private Attributes mAttrs;


    public ActionSheet(Context context,String cancelTitle,String[] sheetItems,boolean cancelableOnTouchOutside){
        this.context = context;
        this.cancelTitle = cancelTitle;
        this.cancelableOnTouchOutside = cancelableOnTouchOutside;
        this.sheetItems = sheetItems;
        builderView();
    }

    private void builderView() {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(R.layout.view_action_sheet_zwh, null);

        // 设置Dialog最小宽度为屏幕宽度
        view.setMinimumWidth(display.getWidth());

        // 获取自定义Dialog布局中的控件
        sLayout_content =  view.findViewById(R.id.sLayout_content);
        lLayout_content =  view.findViewById(R.id.lLayout_content);
//        txt_title =  view.findViewById(R.id.txt_title);
        txt_cancel =  view.findViewById(R.id.txt_cancel);
        txt_cancel.setText(cancelTitle);
        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        dialogWindow.setAttributes(lp);

        dialog.setCancelable(cancelableOnTouchOutside);
        dialog.setCanceledOnTouchOutside(cancelableOnTouchOutside);

        mAttrs = readAttribute();

        txt_cancel.setTextSize(TypedValue.COMPLEX_UNIT_PX,mAttrs.cancelItemTextSize);
        txt_cancel.setTextColor(mAttrs.cancelButtonTextColor);

//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
//        params.topMargin = mAttrs.cancelButtonMarginTop;

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) txt_cancel.getLayoutParams();
        layoutParams.topMargin = mAttrs.cancelButtonMarginTop;
        txt_cancel.setLayoutParams(layoutParams);

        view.setPadding(mAttrs.padding,mAttrs.padding,mAttrs.padding,mAttrs.padding);

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if(sheetListener != null){
                    sheetListener.onDismiss(isCancel);
                }
            }
        });

    }


    public void dismiss(){
        dialog.dismiss();
    }

    private void buildSheetItems(){
        if (sheetItems == null || sheetItems.length <= 0) {
            return;
        }
        int size = sheetItems.length;

        // TODO 高度控制，非最佳解决办法
        // 添加条目过多的时候控制高度
        if (size >= 7) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) sLayout_content.getLayoutParams();
            params.height = display.getHeight() / 2;
            sLayout_content.setLayoutParams(params);
        }

        // 循环添加条目
        for (int i = 1; i <= size; i++) {
            String item = sheetItems[i-1];
            int index = i - 1;
            TextView textView = new TextView(context);
            textView.setId(CANCEL_ITEM_ID + index + 1);
            textView.setText(item);
            textView.setTextSize(18);
            textView.setGravity(Gravity.CENTER);

            // 背景图片
            if (size == 1) {
                if (showTitle) {
                    textView.setBackgroundResource(R.drawable.as_item_bottom_selector);
                } else {
                    textView.setBackgroundResource(R.drawable.as_item_round_selector);
                }
            } else {
                if (showTitle) {
                    if (i >= 1 && i < size) {
                        textView.setBackgroundResource(R.drawable.as_item_middle_selector);
                    } else {
                        textView.setBackgroundResource(R.drawable.as_item_bottom_selector);
                    }
                } else {
                    if (i == 1) {
                        textView.setBackgroundResource(R.drawable.as_item_top_selector);
                    } else if (i < size) {
                        textView.setBackgroundResource(R.drawable.as_item_middle_selector);
                    } else {
                        textView.setBackgroundResource(R.drawable.as_item_bottom_selector);
                    }
                }
            }


            textView.setTextColor(mAttrs.actionItemTextColor);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,mAttrs.actionSheetTextSize);
            // 高度
            float scale = context.getResources().getDisplayMetrics().density;
            int height = (int) (45 * scale + 0.5f);
            textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height));

            // 点击事件
            textView.setOnClickListener(this);

            lLayout_content.addView(textView);
        }
    }

    public void show() {
        buildSheetItems();
        dialog.show();
    }


    @Override
    public void onClick(View v) {
//        if (v.getId() == ActionSheet.BG_VIEW_ID && !getCancelableOnTouchOutside()) {
//            return;
//        }
        dismiss();
        if (v.getId() != ActionSheet.CANCEL_ITEM_ID ) {
            if (sheetListener != null) {
                sheetListener.onActionButtonClick(this, v.getId() - CANCEL_ITEM_ID -1);
            }
            isCancel = false;
        }
    }

    public ActionSheet setActionSheetListener(ActionSheetListener sheetListener) {
        this.sheetListener = sheetListener;
        return this;
    }

    private Attributes readAttribute() {
        Attributes attrs = new Attributes(context);
        TypedArray a = context.getTheme().obtainStyledAttributes(null, R.styleable.ActionSheet, R.attr.actionSheetStyle, 0);
        Drawable background = a.getDrawable(R.styleable.ActionSheet_actionSheetBackground);
        if (background != null) {
            attrs.background = background;
        }
        Drawable cancelButtonBackground = a.getDrawable(R.styleable.ActionSheet_cancelButtonBackground);
        if (cancelButtonBackground != null) {
            attrs.cancelButtonBackground = cancelButtonBackground;
        }
        Drawable actionItemTopBackground = a.getDrawable(R.styleable.ActionSheet_actionItemTopBackground);
        if (actionItemTopBackground != null) {
            attrs.actionItemTopBackground = actionItemTopBackground;
        }
        Drawable actionItemMiddleBackground = a.getDrawable(R.styleable.ActionSheet_actionItemMiddleBackground);
        if (actionItemMiddleBackground != null) {
            attrs.actionItemMiddleBackground = actionItemMiddleBackground;
        }
        Drawable actionItemBottomBackground = a.getDrawable(R.styleable.ActionSheet_actionItemBottomBackground);
        if (actionItemBottomBackground != null) {
            attrs.actionItemBottomBackground = actionItemBottomBackground;
        }
        Drawable actionItemSingleBackground = a.getDrawable(R.styleable.ActionSheet_actionItemSingleBackground);
        if (actionItemSingleBackground != null) {
            attrs.actionItemSingleBackground = actionItemSingleBackground;
        }
        attrs.cancelButtonTextColor = a.getColor(R.styleable.ActionSheet_cancelItemTextColor,attrs.cancelButtonTextColor);
        attrs.actionItemTextColor = a.getColor(R.styleable.ActionSheet_actionItemTextColor,attrs.actionItemTextColor);
        attrs.padding = (int) a.getDimension(R.styleable.ActionSheet_actionSheetPadding, attrs.padding);
        attrs.actionItemSpacing = (int) a.getDimension(R.styleable.ActionSheet_actionItemSpacing,attrs.actionItemSpacing);
        attrs.cancelButtonMarginTop = (int) a.getDimension(R.styleable.ActionSheet_cancelButtonMarginTop,attrs.cancelButtonMarginTop);
        attrs.actionSheetTextSize = a.getDimensionPixelSize(R.styleable.ActionSheet_actionItemTextSize, (int) attrs.actionSheetTextSize);
        attrs.cancelItemTextSize = a.getDimensionPixelSize(R.styleable.ActionSheet_cancelItemTextSize, (int) attrs.actionSheetTextSize);

        a.recycle();
        return attrs;
    }


    private static class Attributes {
        private Context mContext;

        public Attributes(Context context) {
            mContext = context;
            this.background = new ColorDrawable(Color.TRANSPARENT);
            this.cancelButtonBackground = new ColorDrawable(Color.BLACK);
            ColorDrawable gray = new ColorDrawable(Color.GRAY);
            this.actionItemTopBackground = gray;
            this.actionItemMiddleBackground = gray;
            this.actionItemBottomBackground = gray;
            this.actionItemSingleBackground = gray;
            this.cancelButtonTextColor = Color.RED;
            this.actionItemTextColor = Color.BLACK;
            this.padding = dp2px(20);
            this.actionItemSpacing = dp2px(2);
            this.cancelButtonMarginTop = dp2px(10);
            this.actionSheetTextSize = dp2px(16);
            this.cancelItemTextSize = dp2px(16);
        }

        private int dp2px(int dp) {
            return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    dp, mContext.getResources().getDisplayMetrics());
        }

//        public Drawable getOtherButtonMiddleBackground() {
//            if (actionItemMiddleBackground instanceof StateListDrawable) {
//                TypedArray a = mContext.getTheme().obtainStyledAttributes(null,R.styleable.ActionSheet, R.attr.actionSheetStyle, 0);
//                actionItemMiddleBackground = a.getDrawable(R.styleable.ActionSheet_actionItemMiddleBackground);
//                a.recycle();
//            }
//            return actionItemMiddleBackground;
//        }

        Drawable background;
        Drawable cancelButtonBackground;
        Drawable actionItemTopBackground;
        Drawable actionItemMiddleBackground;
        Drawable actionItemBottomBackground;
        Drawable actionItemSingleBackground;
        int cancelButtonTextColor;
        int actionItemTextColor;
        int padding;
        int actionItemSpacing;
        int cancelButtonMarginTop;
        float actionSheetTextSize;
        float cancelItemTextSize;
    }

    public static interface ActionSheetListener {

        void onDismiss(boolean isCancel);

        void onActionButtonClick(ActionSheet actionSheet, int index);
    }
}
