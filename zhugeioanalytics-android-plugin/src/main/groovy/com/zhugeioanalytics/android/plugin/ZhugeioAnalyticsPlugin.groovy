package com.zhugeioanalytics.android.plugin

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.internal.reflect.Instantiator
import org.gradle.invocation.DefaultGradle

class ZhugeioAnalyticsPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        Instantiator ins = ((DefaultGradle) project.getGradle()).getServices().get(Instantiator)
        def args = [ins] as Object[]
        ZhugeioAnalyticsExtension extension = project.extensions.create("zhugeioAnalytics", ZhugeioAnalyticsExtension, args)

        boolean disableZhugeioAnalyticsPlugin = false
        boolean disableZhugeioAnalyticsMultiThreadBuild = false
        boolean disableZhugeioAnalyticsIncrementalBuild = false
        boolean isHookOnMethodEnter = false
        Properties properties = new Properties()
        if (project.rootProject.file('gradle.properties').exists()) {
            properties.load(project.rootProject.file('gradle.properties').newDataInputStream())
            disableZhugeioAnalyticsPlugin = Boolean.parseBoolean(properties.getProperty("zhugeioAnalytics.disablePlugin", "false")) ||
                    Boolean.parseBoolean(properties.getProperty("disableZhugeioAnalyticsPlugin", "false"))
            disableZhugeioAnalyticsMultiThreadBuild = Boolean.parseBoolean(properties.getProperty("zhugeioAnalytics.disableMultiThreadBuild", "false"))
            disableZhugeioAnalyticsIncrementalBuild = Boolean.parseBoolean(properties.getProperty("zhugeioAnalytics.disableIncrementalBuild", "false"))
            isHookOnMethodEnter = Boolean.parseBoolean(properties.getProperty("zhugeioAnalytics.isHookOnMethodEnter", "false"))
        }
        if (!disableZhugeioAnalyticsPlugin) {
            AppExtension appExtension = project.extensions.findByType(AppExtension.class)
            ZhugeioAnalyticsTransformHelper transformHelper = new ZhugeioAnalyticsTransformHelper(extension, appExtension)
            transformHelper.disableZhugeioAnalyticsMultiThread = disableZhugeioAnalyticsMultiThreadBuild
            transformHelper.disableZhugeioAnalyticsIncremental = disableZhugeioAnalyticsIncrementalBuild
            transformHelper.isHookOnMethodEnter = isHookOnMethodEnter
            appExtension.registerTransform(new ZhugeioAnalyticsTransform(transformHelper))
        } else {
            Logger.error("------------您已关闭了诸葛 io 插件--------------")
        }

    }
}