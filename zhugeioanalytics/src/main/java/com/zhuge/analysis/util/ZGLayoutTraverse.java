package com.zhuge.analysis.util;

import android.view.View;
import android.view.ViewGroup;

public class ZGLayoutTraverse {

    private int index = 0;

    public interface Processor {
        void process(View view);

        void traverseEnd(ViewGroup root);
    }

    private final Processor processor;

    private ZGLayoutTraverse(Processor processor) {
        this.processor = processor;
    }

    public static ZGLayoutTraverse build(Processor processor) {
        return new ZGLayoutTraverse(processor);
    }

    public void traverse(ViewGroup root) {
        final int childCount = root.getChildCount();
        index++;
        for (int i = 0; i < childCount; ++i) {
            final View child = root.getChildAt(i);
            processor.process(child); // 节点处理的回调

            if (child instanceof ViewGroup) { //发现是 ViewGroup 就递归扫描
                traverse((ViewGroup) child);
            }
        }
        index--;
        if (index == 0) {
            processor.traverseEnd(root); //遍历结束回调
        }
    }
}
