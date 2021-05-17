package com.ghodel.snapsaver.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ghodel.snapsaver.model.ContactModel;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "snapsaverdb";
    public static final int DATABASE_VERSION = 1;
    public static final String CONTACTS_TABLE_NAME = "contacts";
    public static final String CONTACTS_COLUMN_ID = "id";
    public static final String CONTACTS_COLUMN_MSG = "msg";
    public static final String CONTACTS_COLUMN_PHONE = "phone";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL("CREATE TABLE IF NOT EXISTS "+CONTACTS_TABLE_NAME+"(id INTEGER PRIMARY KEY AUTOINCREMENT,phone TEXT,msg TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }

    public boolean insertContact(String phone, String msg) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACTS_COLUMN_PHONE, phone);
        contentValues.put(CONTACTS_COLUMN_MSG, msg);
        db.insert(CONTACTS_TABLE_NAME, null, contentValues);
        db.close();
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from contacts where id="+id+"", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, CONTACTS_TABLE_NAME);
        return numRows;
    }

    public boolean updateContact(Integer id, String phone, String msg) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACTS_COLUMN_PHONE, phone);
        contentValues.put(CONTACTS_COLUMN_MSG, msg);
        db.update(CONTACTS_TABLE_NAME, contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        db.close();
        return true;
    }

    public void deleteItem(String name)throws SQLException {
        SQLiteDatabase db = getWritableDatabase();
        if (db == null) {
            return;
        }
        String[] whereArgs = new String[] { name };
        db.delete(CONTACTS_TABLE_NAME, CONTACTS_COLUMN_PHONE + "=?", whereArgs);
        db.close();
    }

    public boolean delete(String id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        //deleting row
        sqLiteDatabase.delete(CONTACTS_TABLE_NAME, "id=?", new String[]{id});
        sqLiteDatabase.close();
        return true;
    }

    public void deleteRecordID(int id){
        String query = "DELETE FROM "+ CONTACTS_TABLE_NAME +" WHERE " + CONTACTS_COLUMN_ID+" = "+id;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);
        db.close();
    }

    public boolean deleteContact(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CONTACTS_TABLE_NAME, CONTACTS_COLUMN_ID + " = ?",
                new String[]{id});
        db.close();
        return true;
    }

    //get the all notes
    public ArrayList<ContactModel> getContacts() {
        ArrayList<ContactModel> arrayList = new ArrayList<>();

        // select all query
        String select_query= "SELECT * FROM " + CONTACTS_TABLE_NAME;

        SQLiteDatabase db = this .getWritableDatabase();
        Cursor cursor = db.rawQuery(select_query, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ContactModel contactModel = new ContactModel();
                contactModel.setId(cursor.getString(0));
                contactModel.setPhone(cursor.getString(1));
                contactModel.setMessage(cursor.getString(2));

                arrayList.add(contactModel);
            }while (cursor.moveToNext());
        }
        return arrayList;
    }
}
