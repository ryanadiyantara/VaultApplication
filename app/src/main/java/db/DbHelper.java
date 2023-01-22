package db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import model.Item;

public class DbHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "db_csgovault";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_ITEM = "item_table";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //--TABLE ITEM ------------------------------------------------------------------------------//
    private static final String ITEM_ID = "item_id";
    private static final String ITEM_NAME = "item_name";
    private static final String ITEM_PRICE = "item_price";
    private static final String ITEM_PICT = "item_pict";

    private static final String CREATE_TABLE_ITEM = "CREATE TABLE "
            +TABLE_ITEM+ "("+ITEM_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+ITEM_NAME+" TEXT, "+ITEM_PRICE+" TEXT, "+ITEM_PICT+" BLOB );";

    //--TABLE ORDER CRUD CODE---------------------------------------------------------------------//
    public long createItem(String item_name, String item_price, byte[] item_pict) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ITEM_NAME, item_name);
        values.put(ITEM_PRICE, item_price);
        values.put(ITEM_PICT, item_pict);

        long insert = db.insert(TABLE_ITEM, null, values);
        return insert;
    }

    @SuppressLint("Range")
    public ArrayList<Item> readItem() {
        ArrayList<Item> userModelArrayList = new ArrayList<Item>();

        String selectQuery = "SELECT * FROM " + TABLE_ITEM;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                Item order = new Item();
                order.setItem_id(c.getInt(c.getColumnIndex(ITEM_ID)));
                order.setItem_name(c.getString(c.getColumnIndex(ITEM_NAME)));
                order.setItem_price(c.getString(c.getColumnIndex(ITEM_PRICE)));
                order.setItem_pict(c.getBlob(c.getColumnIndex(ITEM_PICT)));
                userModelArrayList.add(order);
            } while (c.moveToNext());
        }
        return userModelArrayList;
    }

    public int updateItem(int item_id, String item_name, String item_price, byte[] item_pict) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ITEM_ID, item_id);
        values.put(ITEM_NAME, item_name);
        values.put(ITEM_PRICE, item_price);
        values.put(ITEM_PICT, item_pict);

        return db.update(TABLE_ITEM, values, ITEM_ID + " = ?", new String[]{String.valueOf(item_id)});
    }

    public void deleteItem(int item_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ITEM, ITEM_ID + " = ?", new String[]{String.valueOf(item_id)});
    }
    //--------------------------------------------------------------------------------------------//
    //--------------------------------------------------------------------------------------------//

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ITEM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_ITEM + "'");
        onCreate(db);
    }
}

