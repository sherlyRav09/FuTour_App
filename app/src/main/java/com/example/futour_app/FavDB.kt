package com.example.futour_app

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class FavDB(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DB_VERSION) {

    companion object {
        private const val DB_VERSION = 1
        private const val DATABASE_NAME = "WisataDB"
        private const val TABLE_NAME = "favoriteTabel"
        private const val KEY_ID = "id"
        private const val ITEM_TITLE = "itemTitle"
        private const val ITEM_IMAGE = "itemImage"
        const val FAVORITE_STATUS = "fstatus"

        private const val CREATE_TABLE = "CREATE TABLE $TABLE_NAME (" +
                "$KEY_ID TEXT," +
                "$ITEM_TITLE TEXT," +
                "$ITEM_IMAGE TEXT," +
                "$FAVORITE_STATUS TEXT)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Implement upgrade logic if needed
    }

    fun insertEmpty() {
        val db = writableDatabase
        val cv = ContentValues()
        for (x in 1..10) {
            cv.put(KEY_ID, x.toString())
            cv.put(FAVORITE_STATUS, "0")
            db.insert(TABLE_NAME, null, cv)
        }
    }

    fun insertIntoTheDatabase(itemTitle: String, itemImage: Int, id: String, favStatus: String) {
        val db = writableDatabase
        val cv = ContentValues()
        cv.put(ITEM_TITLE, itemTitle)
        cv.put(ITEM_IMAGE, itemImage)
        cv.put(KEY_ID, id)
        cv.put(FAVORITE_STATUS, favStatus)
        db.insert(TABLE_NAME, null, cv)
        Log.d("FavoDB Status", "$itemTitle, favstatus - $favStatus - $cv")
    }

    fun readAllData(id: String): Cursor {
        val db = readableDatabase
        val sql = "SELECT * FROM $TABLE_NAME WHERE $KEY_ID=$id"
        return db.rawQuery(sql, null)
    }

    fun removeFav(id: String) {
        val db = writableDatabase
        val sql = "UPDATE $TABLE_NAME SET $FAVORITE_STATUS='0' WHERE $KEY_ID=$id"
        db.execSQL(sql)
        Log.d("remove", id)
    }

    fun selectAllFavoriteList(): Cursor {
        val db = readableDatabase
        val sql = "SELECT * FROM $TABLE_NAME WHERE $FAVORITE_STATUS='1'"
        return db.rawQuery(sql, null)
    }
}
