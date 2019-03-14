package com.journaler.database

import android.content.ContentValues
import android.location.Location
import android.util.Log
import com.journaler.model.Note
import com.journaler.model.TODO

object Db {
    private val tag = "Db"
    private val version = 1
    private val name = "students"

    val note = object: CRUD<Note>{
        override fun insert(what: Note): Boolean {
            val inserted = insert(listOf(what))
            if (!inserted.isEmpty())
                return true
            return false
        }

        override fun insert(what: Collection<Note>): List<Long> {
            val db = DbHelper(name , version).writableDatabase
            db.beginTransaction()
            var inserted = 0
            val items = mutableListOf<Long>()
            what.forEach{it ->
                val values = ContentValues()
                val table = DbHelper.TABLE_NOTES
                values.put(DbHelper.COLUMN_TITLE , it.title)
                values.put(DbHelper.COLUMN_MESSAGE , it.message)
                values.put(DbHelper.COLUMN_LOCATION_LATITUDE ,
                    it.location.altitude)
                values.put(DbHelper.COLUMN_LOCATION_LONGITUDE ,
                    it.location.longitude)

                val id = db.insert(table , null , values)
                if (id > 0){
                    items.add(id)
                    Log.v(tag , "Entry ID assigned [$id]")
                    ++inserted
                }
            }
            val success = inserted == what.size
            if (success)
                db.setTransactionSuccessful()
            else
                items.clear()

            db.endTransaction()
            db.close()
            return items
        }

        override fun update(what: Note): Boolean{
            val update =  update(listOf(what))
            if (update == 0)
                return false
            else return true
        }

        override fun update(what: Collection<Note>): Int {
            val db  = DbHelper(name , version).writableDatabase
            db.beginTransaction()
            var updated = 0
            what.forEach { it ->
                val values = ContentValues()
                val table = DbHelper.TABLE_NOTES
                values.put(DbHelper.COLUMN_TITLE, it.title)
                values.put(DbHelper.COLUMN_MESSAGE, it.message)
                values.put(DbHelper.COLUMN_LOCATION_LATITUDE,
                    it.location.latitude)
                values.put(DbHelper.COLUMN_LOCATION_LONGITUDE,
                    it.location.longitude)
                db.update(table, values, "_id = ?",
                    arrayOf(it.id.toString()))
                ++updated
            }
            val success = updated == what.size
            if (success)
                db.setTransactionSuccessful()
            else
                updated = 0

            db.endTransaction()
            db.close()
            return updated
        }

        override fun delete(what: Note): Int = delete(listOf(what))

        override fun delete(what: List<Note>): Int {
            val db  = DbHelper(name , version).writableDatabase
            db.beginTransaction()
            val ids = StringBuilder()
            what.forEachIndexed { index, it ->
                ids.append(it.id.toString())
                if (index < (what.size - 1))
                    ids.append(", ")
            }
            val table = DbHelper.TABLE_NOTES
            val statement = db.compileStatement(
                "DELETE FROM $table WHERE ${DbHelper.ID} IN ($ids);")
            val count = statement.executeUpdateDelete()
            val success = count > 0
            if (success){
                db.setTransactionSuccessful()
                Log.i(tag , "Delete [$table] [SUCCESS] [$count] [$statement]")
            }
            else
                Log.w(tag , "Delete [$table] [ FAILED] [$statement]")

            db.endTransaction()
            db.close()
            return count
        }

        override fun select(args: Pair<String, String>): List<Note>
                = select(listOf(args))

        override fun select(args: Collection<Pair<String, String>>): List<Note> {
            val db = DbHelper(name , version).writableDatabase
            val selection = StringBuilder()
            val selectionArgs = mutableListOf<String>()
            args.forEach { arg ->
                selection.append("${arg.first} == ?")
                selectionArgs.add(arg.second)
            }

            val result = mutableListOf<Note>()
            val cursor = db.query(
                true,
                DbHelper.TABLE_TODOS,
                null,
                selection.toString(),
                selectionArgs.toTypedArray(),
                null, null,
                null , null
            )

            while (cursor.moveToNext()){
                val id = cursor.getLong(
                    cursor.getColumnIndexOrThrow(DbHelper.ID))
                val titleIdx =
                    cursor.getColumnIndexOrThrow(DbHelper.COLUMN_TITLE)
                val title = cursor.getString(titleIdx)
                val messageIdx =
                    cursor.getColumnIndexOrThrow(DbHelper.COLUMN_MESSAGE)
                val message = cursor.getString(messageIdx)
                val latitudeIdx =
                    cursor.getColumnIndexOrThrow(DbHelper.COLUMN_LOCATION_LATITUDE)
                val latitude = cursor.getDouble(latitudeIdx)
                val longitudeIdx =
                    cursor.getColumnIndexOrThrow(DbHelper.COLUMN_LOCATION_LONGITUDE)
                val longitude = cursor.getDouble(longitudeIdx)
                val location = Location("")
                location.latitude = latitude
                location.longitude = longitude
                val note = Note(title , message, location)
                note.id = id
                result.add(note)
            }

            cursor.close()
            return result
        }

        override fun selectAll(): List<Note> {
            val db  = DbHelper(name , version).writableDatabase
            val result = mutableListOf<Note>()
            val cursor = db.query(
                true,
                DbHelper.TABLE_NOTES,
                null,null,null,
                null,null,null,null
            )

            while (cursor.moveToNext()){
                val id = cursor.getLong(
                    cursor.getColumnIndexOrThrow(DbHelper.ID))
                val titleIdx =
                    cursor.getColumnIndexOrThrow(DbHelper.COLUMN_TITLE)
                val title = cursor.getString(titleIdx)
                val messageIdx =
                    cursor.getColumnIndexOrThrow(DbHelper.COLUMN_MESSAGE)
                val message = cursor.getString(messageIdx)
                val latitudeIdx =
                    cursor.getColumnIndexOrThrow(DbHelper.COLUMN_LOCATION_LATITUDE)
                val latitude = cursor.getDouble(latitudeIdx)
                val longitudeIdx =
                    cursor.getColumnIndexOrThrow(DbHelper.COLUMN_LOCATION_LONGITUDE)
                val longitude = cursor.getDouble(longitudeIdx)
                val location = Location("")
                location.latitude = latitude
                location.longitude = longitude
                val note = Note(title , message, location)
                note.id = id
                result.add(note)
            }

            cursor.close()
            return  result
        }
    }

    val todo = object : CRUD<TODO>{
        override fun insert(what: TODO): Boolean {
            val inserted = insert(listOf(what))
            if (!inserted.isEmpty())
                return true
            return false
        }

        override fun insert(what: Collection<TODO>): List<Long> {
            val db = DbHelper(name, version).writableDatabase
            db.beginTransaction()
            var inserted = 0
            val items = mutableListOf<Long>()
            what.forEach { it ->
                val table = DbHelper.TABLE_TODOS
                val values = ContentValues()
                values.put(DbHelper.COLUMN_TITLE, it.title)
                values.put(DbHelper.COLUMN_MESSAGE, it.message)
                values.put(DbHelper.COLUMN_LOCATION_LATITUDE,
                    it.location.latitude)
                values.put(DbHelper.COLUMN_LOCATION_LONGITUDE,
                    it.location.longitude)
                values.put(DbHelper.COLUMN_SCHEDULED, it.scheduledFor)
                val id = db.insert(table, null, values)
                if (id > 0) {
                    // in book was this line:  it.id = id
                    items.add(id)
                    Log.v(tag, "Entry ID assigned [ $id ]")
                    inserted++
                }
            }
            val success = inserted == what.size
            if (success)
                db.setTransactionSuccessful()
            else
                items.clear()

            db.endTransaction()
            db.close()
            return items
        }

        override fun update(what: TODO): Boolean{
            val update = update(listOf(what))
            if (update == 0)
                return false
            else
                return true
        }

        override fun update(what: Collection<TODO>): Int {
            val db = DbHelper(name , version).writableDatabase
            db.beginTransaction()
            var updated = 0
            what.forEach { it ->
                val values = ContentValues()
                val table = DbHelper.TABLE_TODOS
                values.put(DbHelper.COLUMN_TITLE , it.title)
                values.put(DbHelper.COLUMN_MESSAGE, it.message)
                values.put(DbHelper.COLUMN_LOCATION_LATITUDE, it.location.latitude)
                values.put(DbHelper.COLUMN_LOCATION_LONGITUDE , it.location.longitude)
                values.put(DbHelper.COLUMN_SCHEDULED , it.scheduledFor)
                db.update(table , values , "_id = ?", arrayOf(it.id.toString()))
                ++updated
            }
            val success = updated == what.size
            if (success)
                db.setTransactionSuccessful()
            else
                updated = 0

            db.endTransaction()
            db.close()
            return updated
        }

        override fun delete(what: TODO): Int = delete(listOf(what))

        override fun delete(what: List<TODO>): Int {
            val db = DbHelper(name , version).writableDatabase
            db.beginTransaction()
            val ids = StringBuilder()
            what.forEachIndexed { index, todo ->
                ids.append(todo.id.toString())
                if (index < what.lastIndex)
                    ids.append(", ")
            }

            val table  = DbHelper.TABLE_TODOS
            val statement = db.compileStatement(
                "DELETE FROM $table WHERE ${DbHelper.ID} IN ($ids);")
            val count = statement.executeUpdateDelete()

            val success = count == what.size
            if (success){
                db.setTransactionSuccessful()
                Log.i(tag , "Delete [$table] [SUCCESS] [$count] [$statement]")
            }
            else
                Log.w(tag , "Delete [$table] [FAILED] [$statement]")

            db.endTransaction()
            db.close()
            return count
        }

        override fun select(args: Pair<String, String>): List<TODO>
                = select(listOf(args))

        override fun select(args: Collection<Pair<String, String>>): List<TODO> {
            val db = DbHelper(name, version).writableDatabase
            val selection = StringBuilder()
            val selectionArgs = mutableListOf<String>()
            args.forEach { arg ->
                selection.append("${arg.first} == ?")
                selectionArgs.add(arg.second)
            }

            val result = mutableListOf<TODO>()
            val cursor = db.query(
                true,
                DbHelper.TABLE_NOTES,
                null,
                selection.toString(),
                selectionArgs.toTypedArray(),
                null, null,
                null, null
            )

            while (cursor.moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow
                    (DbHelper.ID))
                val titleIdx =
                    cursor.getColumnIndexOrThrow(DbHelper.COLUMN_TITLE)
                val title = cursor.getString(titleIdx)
                val messageIdx =
                    cursor.getColumnIndexOrThrow(DbHelper.COLUMN_MESSAGE)
                val message = cursor.getString(messageIdx)
                val latitudeIdx =
                    cursor.getColumnIndexOrThrow(DbHelper.COLUMN_LOCATION_LATITUDE)
                val latitude = cursor.getDouble(latitudeIdx)
                val longitudeIdx =
                    cursor.getColumnIndexOrThrow(DbHelper.COLUMN_LOCATION_LONGITUDE)
                val longitude = cursor.getDouble(longitudeIdx)
                val location = Location("")
                val scheduledForIdx = cursor.getColumnIndexOrThrow(
                    DbHelper.COLUMN_SCHEDULED
                )
                val scheduledFor = cursor.getLong(scheduledForIdx)
                location.latitude = latitude
                location.longitude = longitude
                val todo = TODO(title, message, location, scheduledFor)
                todo.id = id
                result.add(todo)
            }

            cursor.close()
            return result
        }

        override fun selectAll(): List<TODO> {
            val db = DbHelper(name, version).writableDatabase
            val result = mutableListOf<TODO>()
            val cursor = db.query(
                true,
                DbHelper.TABLE_NOTES,
                null, null, null, null, null, null, null
            )
            while (cursor.moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow
                    (DbHelper.ID))
                val titleIdx =
                    cursor.getColumnIndexOrThrow(DbHelper.COLUMN_TITLE)
                val title = cursor.getString(titleIdx)
                val messageIdx =
                    cursor.getColumnIndexOrThrow(DbHelper.COLUMN_MESSAGE)
                val message = cursor.getString(messageIdx)
                val latitudeIdx =
                    cursor.getColumnIndexOrThrow(DbHelper.COLUMN_LOCATION_LATITUDE)
                val latitude = cursor.getDouble(latitudeIdx)
                val longitudeIdx =
                    cursor.getColumnIndexOrThrow(DbHelper.COLUMN_LOCATION_LONGITUDE)
                val longitude = cursor.getDouble(longitudeIdx)
                val location = Location("")
                val scheduledForIdx =
                    cursor.getColumnIndexOrThrow(DbHelper.COLUMN_SCHEDULED)
                val scheduledFor = cursor.getLong(scheduledForIdx)
                location.latitude = latitude
                location.longitude = longitude
                val todo = TODO(title, message, location, scheduledFor)
                todo.id = id
                result.add(todo)
            }

            cursor.close()
            return result
        }
    }
}