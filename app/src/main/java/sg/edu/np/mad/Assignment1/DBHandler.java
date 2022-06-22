package sg.edu.np.mad.Assignment1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {

    public DBHandler(Context c) {super(c, "Listings.db", null, 1);}

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE ListingDetails (ID Text, Title TEXT, Description TEXT, Image TEXT, ID INTEGER PRIMARY KEY, Follow INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS ListingDetails");
        onCreate(db);
    }

    public void insertListings(Listings listings) {
        //int f;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", listings.id);
        contentValues.put("Title", listings.title);
        contentValues.put("Description", listings.desc);
        contentValues.put("Image", listings.image);

        db.insert("ListingDetails", null, contentValues);
        db.close();
    }

    public ArrayList<Listings> getListings(){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Listings> imageList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT*FROM ListingDetails", null);
        while(cursor.moveToNext()){
            Listings listings = new Listings();
            listings.id = cursor.getString(0);
            listings.title = cursor.getString(1);
            listings.desc = cursor.getString(2);
            listings.image = cursor.getString(3);

            imageList.add(listings);
        }
        db.close();
        return imageList;
    }
}
