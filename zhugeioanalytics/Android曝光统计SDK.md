### 曝光采集

#### 打开曝光采集

曝光采集功能默认关闭，需要在SDK调用Init之前进行开启

```
ZhugeSDK.getInstance().enableExpTrack();

```

#### 标记需要曝光采集的元素

一般而言，在view初始化之后调用如下方法进行标记
```
ZhugeSDK.getInstance().viewExpTrack(ViewExposeData data)

```

关于参数```ViewExposeData```

参数 | 传参方式 | 是否必须 | 说明
---|---|---|---
view | 构造函数传参 | 必须 | 需要曝光采集的view
eventName | 构造函数传参 | 必须 | 事件名称
prop | setProp() | 非必须 | 曝光时对应的事件属性

代码示例:

```
  View view = findViewById(R.id.view);
  ViewExposeData exposeData = new ViewExposeData(view, "view曝光");
  JSONObject prop = new JSONObject();
  prop.put("prop","value");
  exposeData.setProp(prop);
  ZhugeSDK.getInstance().viewExpTrack(exposeData);
```

#### 常见问题

1.View 是否是完全可见的时候，SDK 才会发送事件？

  默认 View 任意可见像素则自动触发埋点事件。

2.当元素内容发生改变，会发送事件么？

例如 TextView，当元素内容发生变化，不会再次发送事件。  