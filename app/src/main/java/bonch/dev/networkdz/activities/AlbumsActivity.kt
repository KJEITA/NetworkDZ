package bonch.dev.networkdz.activities

import android.content.Context
import android.net.ConnectivityManager
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
import bonch.dev.networkdz.models.User_post
import bonch.dev.networkdz.networking.RetrofitFactory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_albums.*
import kotlinx.android.synthetic.main.albums_item_post.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.lang.reflect.Type

class AlbumsActivity : AppCompatActivity() {

    lateinit var albumAdapter:Albums_adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_albums)

        val online = isOnline(applicationContext)
        Toast.makeText(
            applicationContext,
            "Интернет = $online",
            Toast.LENGTH_SHORT
        ).show()

        val rv = findViewById<RecyclerView>(R.id.albums_RecyclerView)
        rv.layoutManager = LinearLayoutManager(this@AlbumsActivity)

        if (online)
            loadDataOnline()
        else
            loadDataOffline()
    }

    fun initRecyclerView(list: List<Album_post>) {
        val albumAdapter = Albums_adapter(list, this)
        albums_RecyclerView.adapter = albumAdapter
    }

    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    fun saveData(list: List<Album_post>) {
        val sharedPreferences = getSharedPreferences("shared_prefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(list)
        editor.putString("Albumlist", json)
        editor.apply()
        Toast.makeText(applicationContext, "Сохранилось", Toast.LENGTH_SHORT).show()
    }

    fun loadDataOnline() {
        val service = RetrofitFactory.makeRetrofitService()

        CoroutineScope(Dispatchers.IO).launch {
            val response = service.getAlbumsPosts()

            try {
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        initRecyclerView(response.body()!!)
                        saveData(response.body()!!)
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

    fun loadDataOffline() {
        val sharedPreferences = getSharedPreferences("shared_prefs", MODE_PRIVATE)
        if (sharedPreferences.contains("Albumlist")) {

            val gson = Gson()
            val json = sharedPreferences.getString("Albumlist", null)
            val collectionType: Type = object : TypeToken<List<Album_post>>() {}.type

            var list: List<Album_post> = arrayListOf<Album_post>()
            list = gson.fromJson(json, collectionType)

            initRecyclerView(list)
        }else{
            Toast.makeText(
                applicationContext,
                "Очень нужен интернет",
                Toast.LENGTH_SHORT
            ).show()
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