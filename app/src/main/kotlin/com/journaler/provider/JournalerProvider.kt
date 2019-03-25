package com.journaler.provider

import android.content.*
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri
import android.text.TextUtils
import com.journaler.database.DbHelper
import java.lang.IllegalArgumentException
import java.sql.SQLException

class JournalerProvider: ContentProvider() {
    private val version = 1
    private val name = "journaler"
    private val db: SQLiteDatabase by lazy {
        DbHelper(name, version).writableDatabase
    }

    companion object {
        private val dataTypeNote = "note"
        private val dataTypeNotes = "notes"
        private val dataTypeTODO = "todo"
        private val dataTypeTODOs = "todos"
        private val NOTE_ALL = 1
        private val NOTE_ITEM = 2
        private val TODO_ALL = 3
        private val TODO_ITEM = 4
        private val matcher = UriMatcher(UriMatcher.NO_MATCH)
        val AUTHORITY = "com.journaler.provider"
        val URL_NOTE = "content://$AUTHORITY/$dataTypeNote"
        val URL_TODO = "content://$AUTHORITY/$dataTypeTODO"
        val URL_NOTES = "content://$AUTHORITY/$dataTypeNotes"
        val URL_TODOS = "content://$AUTHORITY/$dataTypeTODOs"
    }

    /**
     * We register uri paths in the following format:
     *
     * <prefix>://<authority>/<data_type>/<id>
     * <prefix> - This is always set to content://
     * <authority> - Name for the content provider
     * <data_type> - The type of data we provide in this Uri
     * <id> - Record ID.
     */
    init {
        /**
         * The calls to addURI() go here,
         * for all of the content URI patterns that the provider should
        recognize.
         *
         * First:
         *
         * Sets the integer value for multiple rows in notes (TODOs) to
        1.
         * Notice that no wildcard is used in the path.
         *
         * Second:
         *
         * Sets the code for a single row to 2. In this case, the "#"
        wildcard is
         * used. "content://com.journaler.provider/note/3" matches, but
         * "content://com.journaler.provider/note doesn't.
         *
         * The same applies for TODOs.
         *
         * addUri() params:
         *
         * authority - String: the authority to match
         *
         * path - String: the path to match.
         * * may be used as a wild card for any text,
         * and # may be used as a wild card for numbers.
         *
         * code - int: the code that is returned when a
        URI
         * is matched against the given components.
         */
        matcher.addURI(AUTHORITY, dataTypeNote, NOTE_ALL)
        matcher.addURI(AUTHORITY, "$dataTypeNotes/#", NOTE_ITEM)
        matcher.addURI(AUTHORITY, dataTypeTODO, TODO_ALL)
        matcher.addURI(AUTHORITY, "$dataTypeTODOs/#", TODO_ITEM)
    }

    override fun onCreate(): Boolean = true

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        values?.let {
            db.beginTransaction()
            val (url, table) = getParameters(uri)
            if (!TextUtils.isEmpty(table)) {
                val inserted = db.insert(table, null, values)
                val success = inserted > 0
                if (success)
                    db.setTransactionSuccessful()
                db.endTransaction()
                if (success) {
                    val resultUrl =
                        ContentUris.withAppendedId(Uri.parse(url), inserted)
                    context.contentResolver.notifyChange(resultUrl, null)
                    return resultUrl
                }
            }else
                throw SQLException("Insert failed, no table for uri: " + uri)
        }
        throw SQLException("Insert failed: " + uri)
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        values?.let {
            db.beginTransaction()
            val (_, table) = getParameters(uri)
            if (!TextUtils.isEmpty(table)){
                val updated = db.update(table, values, selection, selectionArgs)
                val success = updated > 0
                if (success)
                    db.setTransactionSuccessful()
                db.endTransaction()
                if (success){
                    context.contentResolver.notifyChange(uri, null)
                    return updated
                }
            }else
                throw SQLException("Update failed, no table for uri: " + uri)
        }
        throw SQLException("Update failed: " + uri)
    }

    override fun delete(
        uri: Uri,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        db.beginTransaction()
        val (_, table) = getParameters(uri)
        if (!TextUtils.isEmpty(table)){
            val count = db.delete(table, selection, selectionArgs)
            val success = count > 0
            if (success)
                db.setTransactionSuccessful()
            db.endTransaction()
            if (success){
                context.contentResolver.notifyChange(uri, null)
                return count
            }
        }else
            throw SQLException("Delete failed, no table for uri: " + uri)
        throw SQLException("Delete failed: " + uri)
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        val stb = SQLiteQueryBuilder()
        val (_, table) = getParameters(uri)
        stb.tables = table
        stb.setProjectionMap(mutableMapOf<String, String>())
        val cursor = stb.query(db, projection, selection,
            selectionArgs, null, null, null)
        // register to watch a content URI for changes
        cursor.setNotificationUri(context.contentResolver, uri)
        return cursor
    }

    override fun getType(uri: Uri): String?
            = when(matcher.match(uri)) {
        NOTE_ALL ->{
            "${ContentResolver.CURSOR_DIR_BASE_TYPE}/vnd.com.journaler.note.items"
        }
        NOTE_ITEM -> {
            "${ContentResolver.CURSOR_ITEM_BASE_TYPE}/vnd.com.journaler.note.items"
        }
        TODO_ALL -> {
            "${ContentResolver.CURSOR_DIR_BASE_TYPE}/vnd.com.journaler.todo.items"
        }
        TODO_ITEM -> {
            "${ContentResolver.CURSOR_ITEM_BASE_TYPE}/vnd.com.journaler.todo.item"
        }
        else -> throw IllegalArgumentException("Unsupported Uri [$uri]")
    }

    private fun getParameters(uri: Uri): Pair<String, String>{
        if (uri.toString().startsWith(URL_NOTE))
            return Pair(URL_NOTE, DbHelper.TABLE_NOTES)
        if (uri.toString().startsWith(URL_NOTES))
            return Pair(URL_NOTES, DbHelper.TABLE_NOTES)
        if (uri.toString().startsWith(URL_TODO))
            return Pair(URL_TODO, DbHelper.TABLE_TODOS)
        if (uri.toString().startsWith(URL_TODOS))
            return Pair(URL_TODOS, DbHelper.TABLE_TODOS)

        return Pair("", "")
    }
}
















































