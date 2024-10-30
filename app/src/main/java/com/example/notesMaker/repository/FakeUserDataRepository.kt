package com.example.notesMaker.repository


import com.example.notesMaker.network.CreateNoteResponse
import com.example.notesMaker.network.DetailedNote
import com.example.notesMaker.network.LoginResponse
import com.example.notesMaker.network.ShortNote
import com.example.notesMaker.network.UpdatedShortNote
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class FakeUserDataRepository @Inject constructor () : UserDataRepository {
    override suspend fun registerUser(username: String, password: String): Response<ResponseBody> {
        TODO("Not yet implemented")
    }
    override suspend fun loginUser(username: String, password: String): Response<LoginResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllUserNotes(): Response<List<UpdatedShortNote>> {
      return  Response.success(
            listOfNotes
        )
    }

    override suspend fun createNewNote(shortNote: ShortNote): Response<CreateNoteResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun isTokenValid(): Response<ResponseBody> {
        return Response.success(null)
    }

    override suspend fun updateNote(updatedShortNote: UpdatedShortNote): Response<ResponseBody> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteSelectedNote(id: Int): Response<ResponseBody> {
        TODO("Not yet implemented")
    }

    override suspend fun getSelectedNote(id: Int): Response<DetailedNote> {
        TODO("Not yet implemented")
    }


}

val listOfNotes = listOf(
    UpdatedShortNote("hero", "rajeshwar", 1, 0) ,
    UpdatedShortNote("rahjua", "rajeshwar", 1, 0) ,
    UpdatedShortNote("dasf", "rajeshwar", 1, 0) ,

)