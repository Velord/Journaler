package com.journaler.database

import kotlin.reflect.KClass

interface CRUD<T> where T : DbModel {
    companion object {
        val BROADCAST_ACTION = "com.journaler.broadcast.crud"
        val BROADCAST_EXTRAS_KEY_OPERATION_RESULT = "crud_result"
    }

    fun insert(what: T): Boolean

    fun insert(what: Collection<T>): Boolean

    fun update(what: T): Boolean

    fun update(what: Collection<T> ): Boolean

    fun select(args: Pair<String ,String>, clazz: KClass<T>): List<T>

    fun select(args: Collection<Pair<String , String>>, clazz: KClass<T>): List<T>

    fun delete(what: T, clazz: KClass<T>):Int

    fun delete(what: List<T>, clazz: KClass<T>): Int
}