package com.example.nguyenanhlinh_viewpager


import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.nguyenanhlinh_viewpager.model.Notes

class SQLite_Notes(context: Context?) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        const val DB_NAME = "Notes_List.db"
        const val DB_TABLE_NOTES = "notes"
        const val DB_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE notes(ID INTEGER PRIMARY KEY AUTOINCREMENT, note TEXT,localdate TEXT )")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion != newVersion) {
            db.execSQL(" DROP TABLE IF EXISTS $DB_TABLE_NOTES")
            onCreate(db)
        }
    }

    fun addNote(
        note: String,
        localdate: String,
    ): Long {
        val db = writableDatabase
        val contentValues = ContentValues()
        contentValues.put("note", note)
        contentValues.put("localdate", localdate)
        return db.insert("notes", null, contentValues)
    }
    fun deleteItemNotes(localdate: String): Int {
        val db = this.writableDatabase
        return db.delete(DB_TABLE_NOTES,
            "localdate=?",
            arrayOf(localdate))
    }
    fun deleteAllNotes(): Int {
        val db = this.writableDatabase
        return db.delete(DB_TABLE_NOTES,
            null,
           null)
    }
    fun updateData(note: String, localdate: String):
            Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("note", note)
        contentValues.put("localdate", localdate)
        db.update(DB_TABLE_NOTES, contentValues, "localdate = ?", arrayOf(localdate))
        return true
    }
    fun checkLocalDate(localDate: String): Boolean {
        var list = getAllListNotes()
        if (list != null) {
            for (item in list) {
                if (item.localdate == localDate)
                    return false
            }
        }
        return true
    }
    @SuppressLint("Range")
    fun getAllListNotes(): ArrayList<Notes>? {
        val notes: ArrayList<Notes> = ArrayList<Notes>()
        val db = this.writableDatabase
        val cursor: Cursor = db.query(
            false, DB_TABLE_NOTES,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        )
        while (cursor.moveToNext()) {
            val note = cursor.getString(cursor.getColumnIndex("note"))
            val localdate = cursor.getString(cursor.getColumnIndex("localdate"))
            notes.add(Notes(note, localdate,true))
        }
        return notes
    }
}