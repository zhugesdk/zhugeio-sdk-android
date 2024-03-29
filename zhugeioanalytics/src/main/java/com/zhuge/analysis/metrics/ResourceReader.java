package com.zhuge.analysis.metrics;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is for internal use in the Zhuge library, and should not be imported into
 * client code.
 */
public abstract class ResourceReader implements ResourceIds {

    public static class Ids extends ResourceReader {
        public Ids(String resourcePackageName, Context context) {
            super(context);
            mResourcePackageName = resourcePackageName;
            initialize();
        }

        @Override
        protected Class<?> getSystemClass() {
            return android.R.id.class;
        }

        @Override
        protected String getLocalClassName(Context context) {
            return mResourcePackageName + ".R$id";
        }

        private final String mResourcePackageName;
    }

    public static class Drawables extends ResourceReader {
        protected Drawables(String resourcePackageName, Context context) {
            super(context);
            mResourcePackageName = resourcePackageName;
            initialize();
        }

        @Override
        protected Class<?> getSystemClass() {
            return android.R.drawable.class;
        }

        @Override
        protected String getLocalClassName(Context context) {
            return mResourcePackageName + ".R$drawable";
        }

        private final String mResourcePackageName;
    }

    protected ResourceReader(Context context) {
        mContext = context;
        mIdNameToId = new HashMap<String, Integer>();
        mIdToIdName = new SparseArray<String>();
    }

    @Override
    public boolean knownIdName(String name) {
        return mIdNameToId.containsKey(name);
    }

    @Override
    public int idFromName(String name) {
        return mIdNameToId.get(name);
    }

    @Override
    public String nameForId(int id) {
        return mIdToIdName.get(id);
    }


    /**
     *
     * @param platformIdClass android.r.class
     * @param namespace       android
     * @param namesToIds      map
     */
    private static void readClassIds(Class<?> platformIdClass, String namespace, Map<String, Integer> namesToIds) {
        try {
            final Field[] fields = platformIdClass.getFields();
            for (int i = 0; i < fields.length; i++) {
                final Field field = fields[i];
                final int modifiers = field.getModifiers();
                if (Modifier.isStatic(modifiers)) {
                    final Class fieldType = field.getType();
                    if (fieldType == int.class) {
                        final String name = field.getName();
                        final int value = field.getInt(null);
                        final String namespacedName;
                        if (null == namespace) {
                            namespacedName = name;
                        } else {
                            namespacedName = namespace + ":" + name;
                        }
                        namesToIds.put(namespacedName, value);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            Log.e(LOGTAG, "Can't read built-in id names from " + platformIdClass.getName(), e);
        }
    }

    protected abstract Class<?> getSystemClass();

    protected abstract String getLocalClassName(Context context);

    protected void initialize() {
        mIdNameToId.clear();
        mIdToIdName.clear();

        final Class<?> sysIdClass = getSystemClass();
        readClassIds(sysIdClass, "android", mIdNameToId);

        final String localClassName = getLocalClassName(mContext);
        try {
            final Class<?> rIdClass = Class.forName(localClassName);
            readClassIds(rIdClass, null, mIdNameToId);
        } catch (ClassNotFoundException e) {
            Log.w(LOGTAG, "Can't load names for Android view ids from '" + localClassName + "', ids by name will not be available in the events editor.");

        }

        for (Map.Entry<String, Integer> idMapping : mIdNameToId.entrySet()) {
            mIdToIdName.put(idMapping.getValue(), idMapping.getKey());
        }
    }

    private final Context mContext;
    private final Map<String, Integer> mIdNameToId;
    private final SparseArray<String> mIdToIdName;

    @SuppressWarnings("unused")
    private static final String LOGTAG = "Zhuge.RsrcReader";
}