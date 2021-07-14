package com.zhuge.analysis.metrics;

/**
 * A Tweak allows you to alter values in your user's applications through the Zhuge UI.
 * Use Tweaks to expose parameters you can adjust in A/B tests, to determine what application
 * settings result in the best experiences for your users and which are best for achieving
 * your goals.

 * You can declare tweaks with
 */
public interface Tweak<T> {
    /**
     * @return a value for this tweak, either the default value or a value set as part of a Zhuge A/B test.
     */
    T get();
}
