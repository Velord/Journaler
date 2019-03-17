package com.journaler.database

import android.content.ContentValues
import android.location.Location
import android.os.DropBoxManager
import android.util.Log
import com.google.gson.Gson
import com.journaler.model.Note
import com.journaler.model.TODO
import com.github.salomonbrys.kotson.*
import java.lang.IllegalArgumentException
import kotlin.reflect.KClass

object DbOld: CRUDOld<DbEntry> {
    private val tag = "DbOld"
    private val version = 1
    private val name = "students"
    private val gson = Gson()

    override fun insert(what: DbEntry): Boolean {
        val inserted = insert(listOf(what))
        if (inserted)
            return true
        return false
    }

    override fun insert(what: Collection<DbEntry>): Boolean {
        val db = DbHelper(name, version).writableDatabase
        db.beginTransaction()
        var inserted = 0
        val items = mutableListOf<Long>()
        what.forEach { item ->
            when (item) {
                is DropBoxManager.Entry -> {
                    val values = ContentValues()
                    val table = DbHelper.TABLE_NOTES
                    values.put(DbHelper.COLUMN_LOCATION, gson.toJson(item.location))
                    values.put(DbHelper.COLUMN_TITLE, item.title)
                    values.put(DbHelper.COLUMN_MESSAGE, item.message)

                    val id = db.insert(table, null, values)
                    if (id > 0) {
                        items.add(id)
                        Log.v(tag, "Entry ID assigned [$id]")
                        ++inserted
                    }
                }
            }
        }
        val success = inserted == what.size
        if (success)
            db.setTransactionSuccessful()
        else
            items.clear()

        db.endTransaction()
        db.close()
        return success
    }

    override fun update(what: DbEntry): Boolean{
        val update =  update(listOf(what))
        if (update)
            return true
        else
            return false
    }

    override fun update(what: Collection<DbEntry>): Boolean {
        val db = DbHelper(name, version).writableDatabase
        db.beginTransaction()
        var updated = 0
        what.forEach {
            when (it) {
                is DropBoxManager.Entry -> {
                    val values = ContentValues()
                    val table = DbHelper.TABLE_NOTES
                    values.put(DbHelper.COLUMN_TITLE, it.title)
                    values.put(DbHelper.COLUMN_MESSAGE, it.message)
                    values.put(
                        DbHelper.COLUMN_LOCATION,
                        gson.toJson(it.location)
                    )
                    db.update(
                        table, values, "_id = ?",
                        arrayOf(it.id.toString())
                    )
                    ++updated
                }
            }
        }
        val success = updated == what.size
        if (success)
            db.setTransactionSuccessful()
        else
            updated = 0

        db.endTransaction()
        db.close()
        return success
    }

    override fun select(
        args: Pair<String, String>,
        clazz: KClass<DbEntry>
    ): List<DbEntry> = select(listOf(args), clazz)

    override fun select(
        args: Collection<Pair<String, String>>,
        clazz: KClass<DbEntry>
    ): List<DbEntry> {
        val db = DbHelper(name, version).writableDatabase
        val selection = StringBuilder()
        val selectionArgs = mutableListOf<String>()
        args.forEach { arg ->
            selection.append("${arg.first} == ?")
            selectionArgs.add(arg.second)
        }

        if (clazz.simpleName == Note::class.simpleName) {
            val result = mutableListOf<DbEntry>()
            val cursor = db.query(
                true,
                DbHelper.TABLE_TODOS,
                null,
                selection.toString(),
                selectionArgs.toTypedArray(),
                null, null,
                null, null
            )
            while (cursor.moveToNext()) {
                val id = cursor.getLong(
                    cursor.getColumnIndexOrThrow(DbHelper.ID)
                )
                val titleIdx =
                    cursor.getColumnIndexOrThrow(DbHelper.COLUMN_TITLE)
                val title = cursor.getString(titleIdx)

                val messageIdx =
                    cursor.getColumnIndexOrThrow(DbHelper.COLUMN_MESSAGE)
                val message = cursor.getString(messageIdx)

                val locationIdx =
                    cursor.getColumnIndexOrThrow(DbHelper.COLUMN_LOCATION)
                val locationJson = cursor.getString(locationIdx)
                val location = gson.fromJson<Location>(locationJson)

                val note = Note(title, message, location)
                note.id = id
                result.add(note)
            }

            cursor.close()
            db.endTransaction()
            db.close()
            return result
        }

        if (clazz.simpleName == TODO::class.simpleName) {
            val result = mutableListOf<DbEntry>()
            val cursor = db.query(
                true,
                DbHelper.TABLE_TODOS,
                null,
                selection.toString(),
                selectionArgs.toTypedArray(),
                null, null,
                null, null
            )
            while (cursor.moveToNext()) {
                val id = cursor.getLong(
                    cursor.getColumnIndexOrThrow
                        (DbHelper.ID)
                )
                val titleIdx =
                    cursor.getColumnIndexOrThrow(DbHelper.COLUMN_TITLE)
                val title = cursor.getString(titleIdx)

                val messageIdx =
                    cursor.getColumnIndexOrThrow(DbHelper.COLUMN_MESSAGE)
                val message = cursor.getString(messageIdx)

                val locationIdx =
                    cursor.getColumnIndexOrThrow(DbHelper.COLUMN_LOCATION)
                val locationJson = cursor.getString(locationIdx)
                val location = gson.fromJson<Location>(locationJson)

                val scheduledForIdx = cursor.getColumnIndexOrThrow(
                    DbHelper.COLUMN_SCHEDULED
                )
                val scheduledFor = cursor.getLong(scheduledForIdx)

                val todo = TODO(title, message, location, scheduledFor)
                todo.id = id
                result.add(todo)
            }

            cursor.close()
            db.endTransaction()
            db.close()
            return result
        }
        db.endTransaction()
        db.close()
        throw IllegalArgumentException("Unsupported entry type: $clazz")
    }

    override fun delete(
        what: DbEntry,
        clazz: KClass<DbEntry>
    ): Int = delete(listOf(what), clazz)

    override fun delete(what: List<DbEntry>, clazz: KClass<DbEntry>): Int {
        val db = DbHelper(name, version).writableDatabase
        db.beginTransaction()
        val ids = StringBuilder()
        what.forEachIndexed { index, it ->
            ids.append(it.id.toString())
            if (index < (what.lastIndex))
                ids.append(", ")
        }

        if (clazz.simpleName == Note::class.simpleName){
            val table = DbHelper.TABLE_NOTES
            val statement = db.compileStatement(
                "DELETE FROM $table WHERE ${DbHelper.ID} IN ($ids);"
            )
            val count = statement.executeUpdateDelete()
            val success = count > 0
            if (success) {
                db.setTransactionSuccessful()
                Log.i(tag, "Delete [$table] [SUCCESS] [$count] [$statement]")
            }
            else
                Log.w(tag, "Delete [$table] [ FAILED] [$statement]")

            db.endTransaction()
            db.close()
            return count
        }

        if (clazz.simpleName == TODO::class.simpleName){
            val table = DbHelper.TABLE_TODOS
            val statement = db.compileStatement(
                "DELETE FROM $table WHERE ${DbHelper.ID} IN ($ids);"
            )
            val count = statement.executeUpdateDelete()

            val success = count > 0
            if (success) {
                db.setTransactionSuccessful()
                Log.i(tag, "Delete [$table] [SUCCESS] [$count] [$statement]")
            } else
                Log.w(tag, "Delete [$table] [FAILED] [$statement]")

            db.endTransaction()
            db.close()
            return count
        }
        db.endTransaction()
        db.close()
        throw IllegalArgumentException("Unsupported entry type: $clazz")
    }
}
