package androidapp.com.stalwartsecurity.Util;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidapp.com.stalwartsecurity.Pojo.OfflineVisitors;
import androidapp.com.stalwartsecurity.Pojo.Visitors;

/**
 * Created by User on 31-07-2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "stalwart.db";

    public static final String VISTORS_TABLE = "visitors";
    public static final String VISTORS_NAME = "visitors_name";
    public static final String VISTORS_ID = "visitors_id";
    public static final String VISITORS_PHONE = "visitors_phone";
    public static final String VISITORS_COMINGFROM = "visitors_coming";

    private static final int NEW_DATABASE_VERSION = 1;



    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, NEW_DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
         /*
        * Creation of Visitors table
        */
        db.execSQL(
                "create table " + VISTORS_TABLE +
                        "(" + VISTORS_NAME + " text, "
                        + VISITORS_PHONE + " text, "
                        + VISITORS_COMINGFROM + " text) "
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + VISTORS_TABLE);
        //Added in new version 2
        db.execSQL("DROP TABLE IF EXISTS " + VISTORS_TABLE);
        onCreate(db);
    }
    public void addVOffline(OfflineVisitors visitors) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(VISTORS_ID, visitors.getId());
        contentValues.put(VISTORS_NAME, visitors.getName());
        contentValues.put(VISITORS_PHONE, visitors.getPhone());
        contentValues.put(VISITORS_COMINGFROM, visitors.getComing_from());
        // Inserting Row
        db.insert(VISTORS_TABLE, null, contentValues);
        db.close(); // Closing database connection
    }

}
