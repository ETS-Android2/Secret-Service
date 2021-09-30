package org.vandy.secretservice.datacontentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import java.util.Objects;

public class DataContentProvider extends ContentProvider {
    public DataContentProvider() {
    }

    // defining authority so that other application can access it
    static final String PROVIDER_NAME = "org.vandy.secretservice.datacontentprovider";

    // defining content URI
//    static final String URL = "content://" + PROVIDER_NAME + "/users";
    public final String URL = "content://" + PROVIDER_NAME + "/data";
    // parsing the content URI
    public final Uri CONTENT_URI = Uri.parse(URL);

//    static final String id = "id";
//    static final String name = "name";

    public final String _ID = "id";
    public final String NAME = "name";
    public final String LAT = "lat";
    public final String LNG = "lng";
    public final String ADR = "address";
    public final String URLaddress = "url";

    static final int uriCode = 1;
    static final UriMatcher uriMatcher;
//    private static HashMap<String, String> values;

    static {

        // to match the content URI
        // every time user access table under content provider
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        // to access whole table
        uriMatcher.addURI(PROVIDER_NAME, "data", uriCode);

        // to access a particular row of the table
        uriMatcher.addURI(PROVIDER_NAME, "data/*", uriCode);
    }

    @Override
    public String getType(Uri uri) {
        if (uriMatcher.match(uri) == uriCode) {
            return "vnd.android.cursor.dir/data";
        }
        throw new IllegalArgumentException("Unsupported URI: " + uri);
    }

    // creating the database
    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
        if (db != null) {
            return true;
        }
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_NAME);
        switch (uriMatcher.match(uri)) {
            case uriCode:
//                qb.setProjectionMap(values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        if (sortOrder == null || sortOrder == "") {
            sortOrder = _ID;
        }
        Cursor c = qb.query(db, projection, selection, selectionArgs, null,
                null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    // adding data to the database
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowID = db.insert(TABLE_NAME, "", values);
        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            Objects.requireNonNull(getContext()).getContentResolver()
                    .notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLiteException("Failed to add a record into " + uri);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        return 0;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        return 0;
    }

    // creating object of database
    // to perform query
    private SQLiteDatabase db;

    // declaring name of the database
    static final String DATABASE_NAME = "UserDB";

    // declaring table name of the database
//    static final String TABLE_NAME = "Users";
    static final String TABLE_NAME = "Data";
    // declaring version of the database
    static final int DATABASE_VERSION = 1;

    // sql query to create the table

    public final String CREATE_DB_TABLE = "create table " + TABLE_NAME + "("
            + _ID + " integer primary key autoincrement, "
            + NAME + " text, "
            + LAT + " real, "
            + LNG + " real, "
            + ADR + " text, "
            + URLaddress + " text"
            + ");";




    // creating a database
    public static class DatabaseHelper extends SQLiteOpenHelper {

        // defining a constructor
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME,
                    null, DATABASE_VERSION);
        }

        // creating a table in the database
        @Override
        public void onCreate(SQLiteDatabase db) {
            DataContentProvider dcp = new DataContentProvider();

            db.execSQL(dcp.CREATE_DB_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            // sql query to drop a table
            // having similar name
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }
}




