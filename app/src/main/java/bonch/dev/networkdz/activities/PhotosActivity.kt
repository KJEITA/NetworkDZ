package bonch.dev.networkdz.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bonch.dev.networkdz.R
import bonch.dev.networkdz.adapters.Photo_adapter
import bonch.dev.networkdz.models.Unit_photo
import bonch.dev.networkdz.networking.RetrofitFactory
import com.bumptech.glide.load.HttpException
import kotlinx.android.synthetic.main.activity_photos.*
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PhotosActivity : AppCompatActivity() {

    val array = arrayListOf<Unit_photo>()
    lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photos)

        val rv = findViewById<RecyclerView>(R.id.photo_RecyclerView)
        rv.layoutManager = LinearLayoutManager(this)


        Realm.init(this)

        val config = RealmConfiguration.Builder().name("person realm").build()

        realm = Realm.getInstance(config)

        val allPerson = realm.where(Unit_photo::class.java).findAll()

        /*if (allPerson != null) {
            allPerson.forEach { array.add(it) }
            initRecyclerView(array)
        }else{
            loadDataOnline()
        }*/
        loadDataOnline()
    }

    fun loadDataOnline(){
        val service = RetrofitFactory.makeRetrofitService()

        CoroutineScope(Dispatchers.IO).launch {
            val response = service.getPhotoPosts()

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

    fun initRecyclerView(list: List<Unit_photo>) {
        val ua = Photo_adapter(list, this)
        photo_RecyclerView.adapter = ua
    }

    fun saveData() {
        realm.executeTransaction({ bgRealm ->
            bgRealm.beginTransaction()
            val user = bgRealm.createObject(Unit_photo::class.java)
            //user.name = editText.text.toString()
            bgRealm.commitTransaction()
        })
    }
}
