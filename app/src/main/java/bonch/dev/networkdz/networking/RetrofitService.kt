package bonch.dev.networkdz.networking

import bonch.dev.networkdz.models.Album_post
import bonch.dev.networkdz.models.Photo_post
import bonch.dev.networkdz.models.User_post
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface RetrofitService {
    @GET("/users")
    suspend fun getUserPosts() : Response<List<User_post>>

    @GET("/albums?userId=1")
    suspend fun getAlbumsPosts() : Response<List<Album_post>>

    @DELETE("/albums/{id}")
    suspend fun deleteAlbumsPosts(@Path("id") id:Int) : Response<*>

    @GET("/photos?albumId=1")
    suspend fun getPhotoPosts() : Response<List<Photo_post>>

    @POST("/posts")
    suspend fun postPosts(@Field("title") title:String, @Field("body") body:String) : Response<*>
}