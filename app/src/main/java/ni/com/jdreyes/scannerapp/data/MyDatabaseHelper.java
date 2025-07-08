package ni.com.jdreyes.scannerapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "scanner_app.db";
    private static final int DATABASE_VERSION = 1;

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE scanned_items (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "barcode TEXT NOT NULL, " +
                "timestamp TEXT NOT NULL" +
                ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop old table if exists and create again
        db.execSQL("DROP TABLE IF EXISTS scanned_items");
        onCreate(db);
    }
}

