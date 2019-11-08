package bonch.dev.networkdz.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bonch.dev.networkdz.R
import bonch.dev.networkdz.adapters.Users_adapter
import bonch.dev.networkdz.models.User_post
import bonch.dev.networkdz.networking.RetrofitFactory
import kotlinx.android.synthetic.main.activity_users.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class UsersActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)


        val rv = findViewById<RecyclerView>(R.id.users_RecyclerView)

        rv.layoutManager = LinearLayoutManager(this)

        val service = RetrofitFactory.makeRetrofitService()

        CoroutineScope(Dispatchers.IO).launch {
            val response = service.getUserPosts()

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

    fun initRecyclerView(list: List<User_post>) {

        val ua = Users_adapter(list, this)
        users_RecyclerView.adapter = ua
    }
}
