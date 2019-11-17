package bonch.dev.networkdz.activities

import android.content.Context
import android.media.Image
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bonch.dev.networkdz.adapters.Photo_adapter
import bonch.dev.networkdz.models.Photo_post
import bonch.dev.networkdz.models.Unit_photo
import bonch.dev.networkdz.networking.RetrofitFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.load.HttpException
import kotlinx.android.synthetic.main.activity_photos.*
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.android.synthetic.main.photo_item_post.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class PhotosActivity : AppCompatActivity() {

    val array = arrayListOf<Unit_photo>()
    lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bonch.dev.networkdz.R.layout.activity_photos)

        val rv = findViewById<RecyclerView>(bonch.dev.networkdz.R.id.photo_RecyclerView)
        rv.layoutManager = LinearLayoutManager(this)


        Realm.init(this)
        val config = RealmConfiguration.Builder().name("Unit.realm").build()
        realm = Realm.getInstance(config)

        val online = isOnline(applicationContext)

        if (online)
            loadDataOnline()
        else {
            val allPerson = realm.where(Unit_photo::class.java).findAll()
            if (allPerson != null) {
                allPerson.forEach { array.add(it) }
                initRecyclerView(array)
            } else
                Toast.makeText(
                    applicationContext,
                    "Очень нужен интернет",
                    Toast.LENGTH_SHORT
                ).show()
        }
    }

    private fun loadDataOnline() {
        val service = RetrofitFactory.makeRetrofitService()

        CoroutineScope(Dispatchers.IO).launch {
            val response = service.getPhotoPosts()

            try {
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        saveData(response.body()!!)

                        val allPerson = realm.where(Unit_photo::class.java).findAll()
                        if (allPerson != null) {
                            allPerson.forEach { array.add(it) }
                            initRecyclerView(array)
                        }
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

    private fun initRecyclerView(list: List<Unit_photo>) {
        val ua = Photo_adapter(list, this)
        photo_RecyclerView.adapter = ua
    }

    private fun saveData(list: List<Photo_post>) {
        val all = realm.where(Unit_photo::class.java).findAll()

        realm.beginTransaction()
        all.forEach {
            it.deleteFromRealm()
        }
        realm.commitTransaction()

        list.forEach {
            realm.executeTransaction { bgRealm ->
                val user = bgRealm.createObject(Unit_photo::class.java, it.id)
                user.title = it.title
                //user.bitMap = Glide.with(glide_image_view).load(it.url).fallbackDrawable
            }
        }
    }

    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}
