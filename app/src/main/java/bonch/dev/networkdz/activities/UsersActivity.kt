package bonch.dev.networkdz.activities

import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bonch.dev.networkdz.adapters.Users_adapter
import bonch.dev.networkdz.models.User_post
import bonch.dev.networkdz.networking.RetrofitFactory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_users.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.lang.reflect.Type



class UsersActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bonch.dev.networkdz.R.layout.activity_users)

        val online = isOnline(applicationContext)
        Toast.makeText(
            applicationContext,
            "Интернет = $online",
            Toast.LENGTH_SHORT
        ).show()

        val rv = findViewById<RecyclerView>(bonch.dev.networkdz.R.id.users_RecyclerView)
        rv.layoutManager = LinearLayoutManager(this)

        if (online)
            loadDataOnline()
        else
            loadDataOffline()
    }

    fun initRecyclerView(list: List<User_post>) {

        val ua = Users_adapter(list, this)
        users_RecyclerView.adapter = ua
    }

    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    fun saveData(list: List<User_post>) {
        val sharedPreferences = getSharedPreferences("shared_prefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(list)
        editor.putString("Userlist", json)
        editor.apply()
        Toast.makeText(applicationContext, "Сохранилось", Toast.LENGTH_SHORT).show()
    }

    fun loadDataOnline() {
        val service = RetrofitFactory.makeRetrofitService()

        CoroutineScope(Dispatchers.IO).launch {
            val response = service.getUserPosts()

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
        if (sharedPreferences.contains("Userlist")) {

            val gson = Gson()
            val json = sharedPreferences.getString("Userlist", null)
            val collectionType: Type = object : TypeToken<List<User_post>>() {}.type

            var list: List<User_post> = arrayListOf<User_post>()
            list = gson.fromJson(json, collectionType)

            initRecyclerView(list)

            removePref()
        }else{
            Toast.makeText(
                applicationContext,
                "Очень нужен интернет",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun removePref(){
        val preferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.clear()
        editor.commit()
    }
}
