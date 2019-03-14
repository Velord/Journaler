package com.journaler.database

interface CRUD<T> where T : DbModel {
    companion object {
        val BROADCAST_ACTION = "com.journaler.broadcast.crud"
        val BROADCAST_EXTRAS_KEY_OPERATION_RESULT = "crud_result"
    }

    fun insert(what: T): Boolean

    fun insert(what: Collection<T>): List<Long>

    fun delete(what: T):Int

    fun delete(what: List<T>): Int

    fun update(what: T): Boolean

    fun update(what: Collection<T> ): Int

    fun select(args: Pair<String ,String>): List<T>

    fun select(args: Collection<Pair<String , String>>): List<T>

    fun selectAll(): List<T>
}