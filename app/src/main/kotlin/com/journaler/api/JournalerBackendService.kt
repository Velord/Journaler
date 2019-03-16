package com.journaler.api

import android.service.voice.AlwaysOnHotwordDetector
import com.journaler.model.Note
import com.journaler.model.TODO
import retrofit2.Call
import retrofit2.http.*

interface JournalerBackendService {
    companion object {
        fun obtain(): JournalerBackendService{
            return BackendServiceRetrofit
                .obtain()
                .create(JournalerBackendService::class.java)
        }
    }

    @POST("authenticate")
    fun login(
        @HeaderMap headers: Map<String , String>,
        @Body payload: UserLoginRequest
    ): Call<JournalerApiToken>

    @GET("notes")
    fun getNotes(
        @HeaderMap headers: Map<String, String>
    ): Call<List<Note>>

    @GET("todos")
    fun getTODOs(
        @HeaderMap headers: Map<String, String>
    ): Call<List<TODO>>

    @PUT("notes")
    fun publishNotes(
        @HeaderMap headers: Map<String, String>,
        @Body payload: List<Note>
    ): Call<Unit>

    @PUT("todos")
    fun publishTODOs(
        @HeaderMap headers: Map<String, String>,
        @Body payload: List<TODO>
    ): Call<Unit>

    @DELETE("notes")
    fun removeNotes(
        @HeaderMap headers: Map<String, String>,
        @Body payload: List<Note>
    ): Call<Unit>

    @DELETE("todos")
    fun removeTODOs(
        @HeaderMap headers: Map<String, String>,
        @Body payload: List<TODO>
    ): Call<Unit>



}