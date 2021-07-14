package com.zhuge.analysis.stat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Base64;
import android.util.Pair;

import com.zhuge.analysis.util.ZGLogger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.Iterator;

/**
 * SQL Lite Database Adapter for Zhuge.
 * Created by Omen on 15/11/17.
 */
/*package*/ class ZhugeDbAdapter {

    private static final String TAG = "Zhuge.Database";
    private static final String TABLE_NAME = "events";
    private static final String TABLE_SEE = "see";

    public static final String KEY_DATA = "data";
    public static final String KEY_CREATED_AT = "created_at";
    public static final String KEY_SESSION_ID = "session";
    private static final String DATABASE_NAME = "zhuge";
    private static final int DATABASE_VERSION = 2;


    private static final String CREATE_EVENTS_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_DATA + " TEXT NOT NULL, " +
                    KEY_CREATED_AT + " INTEGER NOT NULL);";
    private static final String CREATE_SEE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_SEE + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    KEY_SESSION_ID + " INTEGER NOT NULL, " +
                    KEY_DATA + " TEXT NOT NULL, " +
                    KEY_CREATED_AT + " INTEGER NOT NULL);";
    private static final String EVENTS_TIME_INDEX =
            "CREATE INDEX IF NOT EXISTS time_idx ON " + TABLE_NAME +
                    " (" + KEY_CREATED_AT + ");";
    private static final String SEE_TIME_INDEX =
            "CREATE INDEX IF NOT EXISTS time_idx ON " + TABLE_SEE +
                    " (" + KEY_CREATED_AT + ");";



    private final ZhugeDbHelper mDbHelper;


    /*package*/ ZhugeDbAdapter(Context context){
        mDbHelper = new ZhugeDbHelper(context,DATABASE_NAME);
    }



    /*package*/ int addEventToSee(boolean appSeeEnable,JSONObject data,long sessionID){
        int count=-1;
        Cursor cursor = null;
        if (!appSeeEnable){
            int sessionCount = getSessionCountFromSee();
            if (sessionCount > Constants.MAX_SEE_SESSION){//本地存储会话超过限制
                //删除最早的会话事件
                ZGLogger.logMessage(TAG," 本地存储会话超限，开始删除最早的数据");
                long firstSession = getFirstSession();
                deleteDataFromSeeWithSession(firstSession);
            }
        }
        try {
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(KEY_DATA,data.toString());

//            String dataString = new String(Base64.encode(data.toString().getBytes(),Base64.DEFAULT));
//            contentValues.put(KEY_DATA,dataString);

            long create = System.currentTimeMillis();
            contentValues.put(KEY_CREATED_AT, create);
            contentValues.put(KEY_SESSION_ID,sessionID);
            long insert = db.insert(TABLE_SEE, null, contentValues);

            cursor = db.rawQuery("SELECT COUNT(*) FROM "+TABLE_SEE,null);
            cursor.moveToFirst();
            count = cursor.getInt(0);

        }catch (Exception  e){
            ZGLogger.handleException(TAG,"向zhuge数据库中写入数据时出错，重新初始化zhuge数据库。",e);
            mDbHelper.deleteDatabase();
        }finally {
            if (cursor != null){
                cursor.close();
            }
            mDbHelper.close();
        }
        return count;
    }
    /**
     * 获取zgSee data，最多获取60条
     * @param sessionID
     * @return 第一个string对象为最后一条数据的id，第二个对象是数据组
     */
    /*package*/ Pair<String,JSONArray> getDataFromSee(long sessionID){
        Cursor cursor = null;
        String id = null;
        Pair<String,JSONArray> pair = null;
        try {
            SQLiteDatabase db = mDbHelper.getReadableDatabase();
            JSONArray array = new JSONArray();
            String sql = "SELECT _id,data FROM "+TABLE_SEE+" LIMIT 60";
            cursor = db.rawQuery(sql,null);
//            int count = cursor.getCount();
//            ZGLogger.logMessage(TAG,"get data with  count "+count);
            while (cursor.moveToNext()){
                if (cursor.isLast()){
                    id = cursor.getString(cursor.getColumnIndex("_id"));
                }
                String s = cursor.getString(cursor.getColumnIndex(KEY_DATA));
                JSONObject jsonObject = new JSONObject(s);
                array.put(jsonObject);

//                String encodeEvent = cursor.getString(cursor.getColumnIndex(KEY_DATA));
//                if (encodeEvent != null) {
//                    String jsonString = new String(Base64.decode(encodeEvent,Base64.DEFAULT));
//                    JSONObject jsonObject = new JSONObject(jsonString);
//                    array.put(jsonObject);
//                }

            }
            if (id  != null){
                pair = new Pair<>(id,array);
            }
        }catch (Exception e){
            ZGLogger.handleException(TAG,"get data  error.",e);
        }finally {
            if (cursor!=null)
                cursor.close();
            mDbHelper.close();
        }
        return pair;
    }

    private void deleteDataFromSeeWithSession(long firstSession) {
        try {
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            int delete = db.delete(TABLE_SEE, KEY_SESSION_ID + " = " + firstSession, null);
            ZGLogger.logMessage(TAG," delete session "+firstSession+" :"+delete);
        }catch (Exception e){
            ZGLogger.handleException(TAG,"delete error from see table.",e);
        }finally {
            mDbHelper.close();
        }
    }

    private long getFirstSession(){
        Cursor cursor = null;
        long session = 0;
        try {
            SQLiteDatabase db = mDbHelper.getReadableDatabase();
            cursor = db.rawQuery("SELECT * FROM "+TABLE_SEE+" LIMIT 1",null);
            int count = cursor.getCount();
            if (count != 0){
                cursor.moveToFirst();
                session =  cursor.getLong(cursor.getColumnIndex(KEY_SESSION_ID));
            }
        }catch (Exception e){
            ZGLogger.handleException(TAG," get first session error",e);
        }finally {
            if (cursor!=null){
                cursor.close();
            }
            mDbHelper.close();
        }
        return session;
    }


    /**
     * 获取当前see表中，不重复的session数量
     * @return 不重复的session数量
     */
    private int getSessionCountFromSee() {
        Cursor cursor = null;
        int count = 0;
        try {
            SQLiteDatabase db = mDbHelper.getReadableDatabase();
            cursor = db.rawQuery("SELECT DISTINCT  "+ KEY_SESSION_ID+" FROM "+TABLE_SEE,null);
            count = cursor.getCount();
        }catch (Exception e){
            ZGLogger.handleException(TAG,"get count from see error.",e);
        }finally {
            if (cursor!=null){
                cursor.close();
            }
            mDbHelper.close();
        }
        return count;
    }


    /**
     *
     * @param jsonObject 要添加的事件信息
     * @return 当前database中的记录数
     */
    /*package*/ int addEvent(JSONObject jsonObject){
        Cursor cursor = null;
        int count=-1;
        try {
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            contentValues.put(KEY_DATA, jsonObject.toString());
//            String dataString = new String(Base64.encode(jsonObject.toString().getBytes(),Base64.DEFAULT));
//            contentValues.put(KEY_DATA,dataString);

            contentValues.put(KEY_CREATED_AT, System.currentTimeMillis());

            db.insert(TABLE_NAME, null, contentValues);
            cursor = db.rawQuery("SELECT COUNT(*) FROM "+TABLE_NAME,null);
            cursor.moveToFirst();
            count = cursor.getInt(0);

        }catch (Exception  e){
            ZGLogger.handleException(TAG,"向zhuge数据库中写入数据时出错，重新初始化zhuge数据库。",e);
            mDbHelper.deleteDatabase();
        }finally {
            if (cursor!=null){
                cursor.close();
            }
            mDbHelper.close();
        }
        return count;
    }

    /**
     * 从数据库按照发生时间获取最近的50条事件。
     * @return String[3]的数组，第一个是这次取得事件的最后一个索引，第二个为事件数据，第三个为这次取得的事件数。
     * @param sessionID 当前会话ID
     * @param deepPram  UTM信息
     */
    /*package*/ String[] getDataAttachDeepShare(long sessionID, JSONObject deepPram){
        Cursor cursor = null;
        String data = null;
        String last_id = null; //当次获得事件的索引
        int size = 0;//这次上传的事件数
        try {
            SQLiteDatabase db = mDbHelper.getReadableDatabase();
            cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME+" ORDER BY "+KEY_CREATED_AT +
                    " ASC LIMIT 50",null);
            JSONArray array = new JSONArray();
            while (cursor.moveToNext()){
                if (cursor.isLast()){
                    last_id = cursor.getString(cursor.getColumnIndex("_id"));
                }

                String s = cursor.getString(cursor.getColumnIndex(KEY_DATA));
                JSONObject jsonObject = new JSONObject(s);
                JSONObject pr = jsonObject.optJSONObject("pr");
                long optLong = pr.optLong("$sid", -1);
                if (optLong == sessionID && deepPram != null){
                    Iterator<String> keys = deepPram.keys();
                    while (keys.hasNext()){
                        String key = keys.next();
                        pr.put(key,deepPram.opt(key));
                    }
                }
                array.put(jsonObject);
                size++;

//                String event = cursor.getString(cursor.getColumnIndex(KEY_DATA));
//                if (event != null) {
//                    String jsonString = new String(Base64.decode(event,Base64.DEFAULT));
//                    JSONObject jsonObject = new JSONObject(jsonString);
//                    JSONObject pr = jsonObject.optJSONObject("pr");
//                    long optLong = pr.optLong("$sid", -1);
//                    if (optLong == sessionID && deepPram != null){
//                        Iterator<String> keys = deepPram.keys();
//                        while (keys.hasNext()){
//                            String key = keys.next();
//                            pr.put(key,deepPram.opt(key));
//                        }
//                    }
//                    array.put(jsonObject);
//                    size++;
//                }

            }
            if (array.length()>0){
                data = array.toString();
            }
        }catch (Exception e){
            ZGLogger.handleException(TAG,"无法从zhuge数据库中读取数据--DeepShare。",e);
            //在没有对DB进行修改的情况下出现的错误，我们可以假定他可以自己恢复。在写操作时，出现错误，则删除文件。
            last_id = null;
            data = null;
        }finally {
            mDbHelper.close();
            if (cursor != null){
                cursor.close();
            }
        }
        if (data!=null && last_id!=null){
            return new String[]{last_id,data,Integer.toString(size)};
        }
        return null;
    }

    /**
     * 从数据库按照发生时间获取最近的50条事件。
     * @return Pair , first 是当次最后一条数据的索引，second 是当次的数据
     */
    /*package*/ Pair<String,JSONArray> getData(){
        Cursor cursor = null;
        String last_id = null; //当次获得事件的索引
        Pair<String,JSONArray> pair = null;
        JSONArray array = new JSONArray();
        try {
            SQLiteDatabase db = mDbHelper.getReadableDatabase();
            cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME+" ORDER BY "+KEY_CREATED_AT +
                    " ASC LIMIT 50",null);
            if (cursor.getCount() == 0){
                mDbHelper.close();
                return null;
            }
            while (cursor.moveToNext()){
                if (cursor.isLast()){
                    last_id = cursor.getString(cursor.getColumnIndex("_id"));
                }

                String s = cursor.getString(cursor.getColumnIndex(KEY_DATA));
                JSONObject jsonObject = new JSONObject(s);
                array.put(jsonObject);

//                String event = cursor.getString(cursor.getColumnIndex(KEY_DATA));
//                if (event != null) {
//                    String jsonString = new String(Base64.decode(event,Base64.DEFAULT));
//                    JSONObject jsonObject = new JSONObject(jsonString);
//                    array.put(jsonObject);
//                }

            }
            ZGLogger.logMessage(TAG,"get data from event , "+array.length()+" data and last id is "+last_id);
            pair = new Pair<>(last_id,array);
        }catch (Exception e){
            ZGLogger.handleException(TAG,"无法从zhuge数据库中读取数据--Default",e);

        }finally {
          mDbHelper.close();
            if (cursor != null){
                cursor.close();
            }
        }
        return pair;
    }

    /**
     * 从数据库中移除数据
     * @param lastID 移除lastID之前的数据
     */
    /*package*/ void removeEventFromSee(String lastID){
        try {
            final SQLiteDatabase db = mDbHelper.getWritableDatabase();
            int delete = db.delete(TABLE_SEE, "_id <= " + lastID, null);
            ZGLogger.logMessage(TAG,"delete from see with id "+lastID+" , "+delete+" has deleted.");
        } catch (final SQLiteException e) {
            ZGLogger.handleException(TAG,"无法从zhuge数据库中删除数据，重新初始化数据库。",e);
            // 修改SQL出错的情况下，删除DB。
            mDbHelper.deleteDatabase();
        } finally {
            mDbHelper.close();
        }
    }


    /**
     * 从数据库中移除数据
     * @param lastID 移除lastID之前的数据
     */
    /*package*/ void removeEvent(String lastID){
        try {
            final SQLiteDatabase db = mDbHelper.getWritableDatabase();
            int delete = db.delete(TABLE_NAME, "_id <= " + lastID, null);
            ZGLogger.logMessage(TAG,"delete event from event with id "+lastID+" , "+delete+" has delete");
        } catch (final SQLiteException e) {
            ZGLogger.handleException(TAG,"无法从zhuge数据库中删除数据，重新初始化数据库。",e);
            // 修改SQL出错的情况下，删除DB。
            mDbHelper.deleteDatabase();
        } finally {
            mDbHelper.close();
        }
    }

    /*package*/ long getEventCount() {
        long numberRows = 0;
        SQLiteStatement statement = null;
        try {
            SQLiteDatabase db = mDbHelper.getReadableDatabase();
            String query = "SELECT COUNT(*) FROM " + TABLE_NAME;
            statement = db.compileStatement(query);
            numberRows = statement.simpleQueryForLong();
        } catch (SQLiteException e) {
            ZGLogger.handleException(TAG,"查询事件数时出错。",e);
        } finally {
            if (statement != null) {
                statement.close();
            }
            mDbHelper.close();
        }
        return numberRows;
    }

    private static class ZhugeDbHelper extends SQLiteOpenHelper{
        private final String mFile;


        public ZhugeDbHelper(Context context, String name) {
            super(context, name, null, DATABASE_VERSION);
            mFile = context.getDatabasePath(DATABASE_NAME).getAbsolutePath();
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_EVENTS_TABLE);
            db.execSQL(CREATE_SEE_TABLE);
            db.execSQL(EVENTS_TIME_INDEX);
            db.execSQL(SEE_TIME_INDEX);
            ZGLogger.logMessage(TAG,"create zhuge database");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SEE);
            db.execSQL(CREATE_EVENTS_TABLE);
            db.execSQL(EVENTS_TIME_INDEX);
            db.execSQL(CREATE_SEE_TABLE);
            db.execSQL(SEE_TIME_INDEX);
            ZGLogger.logMessage(TAG,"upgrade zhuge database");

        }

        @Override
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SEE);
            db.execSQL(CREATE_EVENTS_TABLE);
            db.execSQL(EVENTS_TIME_INDEX);
            db.execSQL(CREATE_SEE_TABLE);
            db.execSQL(SEE_TIME_INDEX);
            ZGLogger.logMessage(TAG,"downgrade zhuge database");
        }

        /*package*/ void deleteDatabase() {
            close();
            File file = new File(mFile);
            file.delete();
        }
    }
}
