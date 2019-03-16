package com.journaler.database

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.journaler.Journaler

class DbHelper(val dbName: String, val version: Int):
    SQLiteOpenHelper(
        Journaler.ctx , dbName , null , version
    ) {
    companion object {
        val ID: String = "_id"
        val TABLE_TODOS = "todos"
        val TABLE_NOTES = "notes"
        val COLUMN_TITLE = "title"
        val COLUMN_SCHEDULED = "sheduled"
        val COLUMN_MESSAGE = "message"
        val COLUMN_LOCATION = "location"
    }

    private val tag = "Dbhelper"

    private val createTableNotes = """
        Create table if not exists $TABLE_TODOS(
            $ID integer primary key autoincrement,
            $COLUMN_TITLE text,
            $COLUMN_MESSAGE text,
            $COLUMN_LOCATION text
        )
    """.trimIndent()

    private val createTableTODOs = """
        Create table if not exists $TABLE_NOTES(
            $ID integer primary key autoincrement,
            $COLUMN_TITLE text,
            $COLUMN_MESSAGE text,
            $COLUMN_SCHEDULED integer,
            $COLUMN_LOCATION text
        )
    """.trimIndent()

    override fun onCreate(db: SQLiteDatabase?) {
        Log.d(tag , "Database [CREATING]")
        db!!.execSQL(createTableNotes)
        db.execSQL(createTableTODOs)
        Log.d(tag , "Database [CREATED]")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}