package bonch.dev.networkdz.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bonch.dev.networkdz.R
import bonch.dev.networkdz.adapters.Albums_adapter
import bonch.dev.networkdz.models.Album_post
import bonch.dev.networkdz.networking.RetrofitFactory
import kotlinx.android.synthetic.main.activity_albums.*
import kotlinx.android.synthetic.main.albums_item_post.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class AlbumsActivity : AppCompatActivity() {

    lateinit var albumAdapter:Albums_adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_albums)

        val rv = findViewById<RecyclerView>(R.id.albums_RecyclerView)
        rv.layoutManager = LinearLayoutManager(this)

        fill()
    }

    fun initRecyclerView(list: List<Album_post>) {

        albumAdapter= Albums_adapter(list, this)
        albums_RecyclerView.adapter = albumAdapter
    }

    fun fill() {
        val service = RetrofitFactory.makeRetrofitService()

        CoroutineScope(Dispatchers.IO).launch {
            val response = service.getAlbumsPosts()

            try {
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {

                        initRecyclerView(response.body()!!)
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "${response.errorBody()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (err: HttpException) {
                Log.e("Retrofit", "${err.printStackTrace()}")
            }
        }
    }

    fun deletePost(view: View) {
        val service = RetrofitFactory.makeRetrofitService()
        CoroutineScope(Dispatchers.IO).launch {
            val response = service.deleteAlbumsPosts(view.id)

            try {
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        var list = albumAdapter.list.toMutableList()

                        list.removeAt(view.album_id.text.toString().toInt())
                        albumAdapter.list = list
                        albumAdapter.notifyDataSetChanged()
                    }
                }
            } catch (err: HttpException) {
                Log.e("Retrofit", "${err.printStackTrace()}")
            }
        }

    }
}