# ActionSheet[![](https://jitpack.io/v/Haozi0456/ActionSheet.svg)](https://jitpack.io/#Haozi0456/ActionSheet)
仿ios底部弹出选择器

#效果图
<p>
   <img src="https://github.com/Haozi0456/ActionSheet/blob/master/Screenshot_20181102-185157.jpg" width="320"  height="640" alt="Screenshot"/>

# 使用

### 添加依赖

```groovy
dependencies {
     implementation 'com.github.Haozi0456:ActionSheet:1.0'
}
```

### 创建

```java
ActionSheet.createBuilder(context)
                        .setActionItemTitles(new String[]{"照相","从相册选择"})
                        .setCancelableOnTouchOutside(true)
                        .setCancelButtonTitle("取消")
                        .setListener(this)
                        .show();
```

### 包含方法

* `setCancelButtonTitle()` 设置取消按钮文字
* `setActionItemTitles()` 选项集合
* `setCancelableOnTouchOutside()` 触摸控件外消失
* `setListener()` 设置监听返回事件
* `show()` 显示控件

### 事件监听

实现 `ActionSheetListener` 接口.
* `onActionButtonClick()` 选项返回,index
* `onDismiss()` 关闭返回

```java
   	@Override
	public void onActionButtonClick(ActionSheet actionSheet, int index) {
		
	}

	@Override
	public void onDismiss(boolean isCancle) {
		
	}
```

### 样式

你可以自定义样式:

```xml
<!-- Application theme. -->
    <style name="AppTheme" parent="AppBaseTheme">
        <item name="actionSheetStyle">@style/ActionSheetStyle</item>
    </style>
```

自定义样式设置

```xml
 <!-- ActionSheetStyle -->
 <style name="ActionSheetStyle">
        <item name="actionSheetBackground">@android:color/transparent</item>
        <item name="cancelButtonBackground">@drawable/as_item_cancel_selector</item>
        <item name="actionItemTopBackground">@drawable/as_item_top_selector</item>
        <item name="actionItemMiddleBackground">@drawable/as_item_middle_selector</item>
        <item name="actionItemBottomBackground">@drawable/as_item_bottom_selector</item>
        <item name="actionItemSingleBackground">@drawable/as_item_cancel_selector</item>
        <item name="cancelItemTextColor">#FD4A2E</item>
        <item name="actionItemTextColor">#1E82FF</item>
        <item name="actionSheetPadding">15dp</item>
        <item name="actionItemSpacing">0dp</item>
        <item name="cancelButtonMarginTop">15dp</item>
        <item name="actionItemTextSize">18sp</item>
        <item name="cancelItemTextSize">18sp</item>
    </style>
```

