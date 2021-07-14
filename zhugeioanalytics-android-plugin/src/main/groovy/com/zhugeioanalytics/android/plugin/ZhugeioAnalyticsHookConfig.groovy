package com.zhugeioanalytics.android.plugin

import org.objectweb.asm.Opcodes

class ZhugeioAnalyticsHookConfig {
    public static final String ZHUGEIO_ANALYTICS_API = "com/zhuge/analysis/util/AutoTrackHelper"
    public final static HashMap<String, ZhugeioAnalyticsMethodCell> INTERFACE_METHODS = new HashMap<>()
    public final static HashMap<String, ZhugeioAnalyticsMethodCell> CLASS_METHODS = new HashMap<>()

//    static {
//        addInterfaceMethod(new ZhugeioAnalyticsMethodCell(
//                'onCheckedChanged',
//                '(Landroid/widget/CompoundButton;Z)V',
//                'android/widget/CompoundButton$OnCheckedChangeListener',
//                'trackViewOnClick',
//                '(Landroid/view/View;)V',
//                1, 1,
//                [Opcodes.ALOAD]))
//        addInterfaceMethod(new ZhugeioAnalyticsMethodCell(
//                'onRatingChanged',
//                '(Landroid/widget/RatingBar;FZ)V',
//                'android/widget/RatingBar$OnRatingBarChangeListener',
//                'trackViewOnClick',
//                '(Landroid/view/View;)V',
//                1, 1,
//                [Opcodes.ALOAD]))
//        addInterfaceMethod(new ZhugeioAnalyticsMethodCell(
//                'onStopTrackingTouch',
//                '(Landroid/widget/SeekBar;)V',
//                'android/widget/SeekBar$OnSeekBarChangeListener',
//                'trackViewOnClick',
//                '(Landroid/view/View;)V',
//                1, 1,
//                [Opcodes.ALOAD]))
//        addInterfaceMethod(new ZhugeioAnalyticsMethodCell(
//                'onCheckedChanged',
//                '(Landroid/widget/RadioGroup;I)V',
//                'android/widget/RadioGroup$OnCheckedChangeListener',
//                'trackRadioGroup',
//                '(Landroid/widget/RadioGroup;I)V',
//                1, 2,
//                [Opcodes.ALOAD, Opcodes.ILOAD]))
//        addInterfaceMethod(new ZhugeioAnalyticsMethodCell(
//                'onClick',
//                '(Landroid/content/DialogInterface;I)V',
//                'android/content/DialogInterface$OnClickListener',
//                'trackDialog',
//                '(Landroid/content/DialogInterface;I)V',
//                1, 2,
//                [Opcodes.ALOAD, Opcodes.ILOAD]))
//        addInterfaceMethod(new ZhugeioAnalyticsMethodCell(
//                'onItemSelected',
//                '(Landroid/widget/AdapterView;Landroid/view/View;IJ)V',
//                'android/widget/AdapterView$OnItemSelectedListener',
//                'trackListView',
//                '(Landroid/widget/AdapterView;Landroid/view/View;I)V',
//                1, 3,
//                [Opcodes.ALOAD, Opcodes.ALOAD, Opcodes.ILOAD]))
//        addInterfaceMethod(new ZhugeioAnalyticsMethodCell(
//                'onGroupClick',
//                '(Landroid/widget/ExpandableListView;Landroid/view/View;IJ)Z',
//                'android/widget/ExpandableListView$OnGroupClickListener',
//                'trackExpandableListViewOnGroupClick',
//                '(Landroid/widget/ExpandableListView;Landroid/view/View;I)V',
//                1, 3,
//                [Opcodes.ALOAD, Opcodes.ALOAD, Opcodes.ILOAD]))
//        addInterfaceMethod(new ZhugeioAnalyticsMethodCell(
//                'onChildClick',
//                '(Landroid/widget/ExpandableListView;Landroid/view/View;IIJ)Z',
//                'android/widget/ExpandableListView$OnChildClickListener',
//                'trackExpandableListViewOnChildClick',
//                '(Landroid/widget/ExpandableListView;Landroid/view/View;II)V',
//                1, 4,
//                [Opcodes.ALOAD, Opcodes.ALOAD, Opcodes.ILOAD, Opcodes.ILOAD]))
//        addInterfaceMethod(new ZhugeioAnalyticsMethodCell(
//                'onTabChanged',
//                '(Ljava/lang/String;)V',
//                'android/widget/TabHost$OnTabChangeListener',
//                'trackTabHost',
//                '(Ljava/lang/String;)V',
//                1, 1,
//                [Opcodes.ALOAD]))
//        addInterfaceMethod(new ZhugeioAnalyticsMethodCell(
//                'onTabSelected',
//                '(Landroid/support/design/widget/TabLayout$Tab;)V',
//                'android/support/design/widget/TabLayout$OnTabSelectedListener',
//                'trackTabLayoutSelected',
//                '(Ljava/lang/Object;Ljava/lang/Object;)V',
//                0, 2,
//                [Opcodes.ALOAD, Opcodes.ALOAD]))
//        addInterfaceMethod(new ZhugeioAnalyticsMethodCell(
//                'onTabSelected',
//                '(Lcom/google/android/material/tabs/TabLayout$Tab;)V',
//                'com/google/android/material/tabs/TabLayout$OnTabSelectedListener',
//                'trackTabLayoutSelected',
//                '(Ljava/lang/Object;Ljava/lang/Object;)V',
//                0, 2,
//                [Opcodes.ALOAD, Opcodes.ALOAD]))
//        addInterfaceMethod(new ZhugeioAnalyticsMethodCell(
//                'onMenuItemClick',
//                '(Landroid/view/MenuItem;)Z',
//                'android/widget/Toolbar$OnMenuItemClickListener',
//                'trackMenuItem',
//                '(Landroid/view/MenuItem;)V',
//                1, 1,
//                [Opcodes.ALOAD]))
//        addInterfaceMethod(new ZhugeioAnalyticsMethodCell(
//                'onMenuItemClick',
//                '(Landroid/view/MenuItem;)Z',
//                'android/support/v7/widget/Toolbar$OnMenuItemClickListener',
//                'trackMenuItem',
//                '(Landroid/view/MenuItem;)V',
//                1, 1,
//                [Opcodes.ALOAD]))
//        addInterfaceMethod(new ZhugeioAnalyticsMethodCell(
//                'onMenuItemClick',
//                '(Landroid/view/MenuItem;)Z',
//                'androidx/appcompat/widget/Toolbar$OnMenuItemClickListener',
//                'trackMenuItem',
//                '(Landroid/view/MenuItem;)V',
//                1, 1,
//                [Opcodes.ALOAD]))
//        addInterfaceMethod(new ZhugeioAnalyticsMethodCell(
//                'onClick',
//                '(Landroid/content/DialogInterface;IZ)V',
//                'android/content/DialogInterface$OnMultiChoiceClickListener',
//                'trackDialog',
//                '(Landroid/content/DialogInterface;I)V',
//                1, 2,
//                [Opcodes.ALOAD, Opcodes.ILOAD]))
//        addInterfaceMethod(new ZhugeioAnalyticsMethodCell(
//                'onMenuItemClick',
//                '(Landroid/view/MenuItem;)Z',
//                'android/widget/PopupMenu$OnMenuItemClickListener',
//                'trackMenuItem',
//                '(Landroid/view/MenuItem;)V',
//                1, 1,
//                [Opcodes.ALOAD]))
//        addInterfaceMethod(new ZhugeioAnalyticsMethodCell(
//                'onMenuItemClick',
//                '(Landroid/view/MenuItem;)Z',
//                'androidx/appcompat/widget/PopupMenu$OnMenuItemClickListener',
//                'trackMenuItem',
//                '(Landroid/view/MenuItem;)V',
//                1, 1,
//                [Opcodes.ALOAD]))
//        addInterfaceMethod(new ZhugeioAnalyticsMethodCell(
//                'onMenuItemClick',
//                '(Landroid/view/MenuItem;)Z',
//                'android/support/v7/widget/PopupMenu$OnMenuItemClickListener',
//                'trackMenuItem',
//                '(Landroid/view/MenuItem;)V',
//                1, 1,
//                [Opcodes.ALOAD]))
//        addInterfaceMethod(new ZhugeioAnalyticsMethodCell(
//                'onNavigationItemSelected',
//                '(Landroid/view/MenuItem;)Z',
//                'com/google/android/material/navigation/NavigationView$OnNavigationItemSelectedListener',
//                'trackMenuItem',
//                '(Landroid/view/MenuItem;)V',
//                1, 1,
//                [Opcodes.ALOAD]))
//        addInterfaceMethod(new ZhugeioAnalyticsMethodCell(
//                'onNavigationItemSelected',
//                '(Landroid/view/MenuItem;)Z',
//                'android/support/design/widget/NavigationView$OnNavigationItemSelectedListener',
//                'trackMenuItem',
//                '(Landroid/view/MenuItem;)V',
//                1, 1,
//                [Opcodes.ALOAD]))
//        addInterfaceMethod(new ZhugeioAnalyticsMethodCell(
//                'onNavigationItemSelected',
//                '(Landroid/view/MenuItem;)Z',
//                'android/support/design/widget/BottomNavigationView$OnNavigationItemSelectedListener',
//                'trackMenuItem',
//                '(Landroid/view/MenuItem;)V',
//                1, 1,
//                [Opcodes.ALOAD]))
//        addInterfaceMethod(new ZhugeioAnalyticsMethodCell(
//                'onNavigationItemSelected',
//                '(Landroid/view/MenuItem;)Z',
//                'com/google/android/material/bottomnavigation/BottomNavigationView$OnNavigationItemSelectedListener',
//                'trackMenuItem',
//                '(Landroid/view/MenuItem;)V',
//                1, 1,
//                [Opcodes.ALOAD]))
//    }
//
//    static {
//        addClassMethod(new ZhugeioAnalyticsMethodCell(
//                'performClick',
//                '()Z',
//                'androidx/appcompat/widget/ActionMenuPresenter$OverflowMenuButton',
//                'trackViewOnClick',
//                '(Landroid/view/View;)V',
//                0, 1,
//                [Opcodes.ALOAD]))
//
//        addClassMethod(new ZhugeioAnalyticsMethodCell(
//                'performClick',
//                '()Z',
//                'android/support/v7/widget/ActionMenuPresenter$OverflowMenuButton',
//                'trackViewOnClick',
//                '(Landroid/view/View;)V',
//                0, 1,
//                [Opcodes.ALOAD]))
//
//        addClassMethod(new ZhugeioAnalyticsMethodCell(
//                'performClick',
//                '()Z',
//                'android/widget/ActionMenuPresenter$OverflowMenuButton',
//                'trackViewOnClick',
//                '(Landroid/view/View;)V',
//                0, 1,
//                [Opcodes.ALOAD]))
//    }

    static void addInterfaceMethod(ZhugeioAnalyticsMethodCell zhugeioAnalyticsMethodCell) {
        if (zhugeioAnalyticsMethodCell != null) {
            INTERFACE_METHODS.put(zhugeioAnalyticsMethodCell.parent + zhugeioAnalyticsMethodCell.name + zhugeioAnalyticsMethodCell.desc, zhugeioAnalyticsMethodCell)
        }
    }

    static void addClassMethod(ZhugeioAnalyticsMethodCell zhugeioAnalyticsMethodCell) {
        if (zhugeioAnalyticsMethodCell != null) {
            CLASS_METHODS.put(zhugeioAnalyticsMethodCell.parent + zhugeioAnalyticsMethodCell.name + zhugeioAnalyticsMethodCell.desc, zhugeioAnalyticsMethodCell)
        }
    }

    /**
     * Fragment中的方法
     */
    public final static HashMap<String, ZhugeioAnalyticsMethodCell> FRAGMENT_METHODS = new HashMap<>()

//    static {
//        FRAGMENT_METHODS.put('onResume()V', new ZhugeioAnalyticsMethodCell(
//                'onResume',
//                '()V',
//                '',// parent省略，均为 android/app/Fragment 或 android/support/v4/app/Fragment
//                'trackFragmentResume',
//                '(Ljava/lang/Object;)V',
//                0, 1,
//                [Opcodes.ALOAD]))
//        FRAGMENT_METHODS.put('setUserVisibleHint(Z)V', new ZhugeioAnalyticsMethodCell(
//                'setUserVisibleHint',
//                '(Z)V',
//                '',// parent省略，均为 android/app/Fragment 或 android/support/v4/app/Fragment
//                'trackFragmentSetUserVisibleHint',
//                '(Ljava/lang/Object;Z)V',
//                0, 2,
//                [Opcodes.ALOAD, Opcodes.ILOAD]))
//        FRAGMENT_METHODS.put('onHiddenChanged(Z)V', new ZhugeioAnalyticsMethodCell(
//                'onHiddenChanged',
//                '(Z)V',
//                '',
//                'trackOnHiddenChanged',
//                '(Ljava/lang/Object;Z)V',
//                0, 2,
//                [Opcodes.ALOAD, Opcodes.ILOAD]))
//        FRAGMENT_METHODS.put('onViewCreated(Landroid/view/View;Landroid/os/Bundle;)V', new ZhugeioAnalyticsMethodCell(
//                'onViewCreated',
//                '(Landroid/view/View;Landroid/os/Bundle;)V',
//                '',
//                'onFragmentViewCreated',
//                '(Ljava/lang/Object;Landroid/view/View;Landroid/os/Bundle;)V',
//                0, 3,
//                [Opcodes.ALOAD, Opcodes.ALOAD, Opcodes.ALOAD]))
//    }

    /**
     * android.gradle 3.2.1 版本中，针对 Lambda 表达式处理
     */

    public final static HashMap<String, ZhugeioAnalyticsMethodCell> LAMBDA_METHODS = new HashMap<>()
    //lambda 参数优化取样
    public final static ArrayList<ZhugeioAnalyticsMethodCell> SAMPLING_LAMBDA_METHODS = new ArrayList<>()
//

    static void addLambdaMethod(ZhugeioAnalyticsMethodCell zhugeioAnalyticsMethodCell) {
        if (zhugeioAnalyticsMethodCell != null) {
            LAMBDA_METHODS.put(zhugeioAnalyticsMethodCell.parent + zhugeioAnalyticsMethodCell.name + zhugeioAnalyticsMethodCell.desc, zhugeioAnalyticsMethodCell)
        }
    }
}