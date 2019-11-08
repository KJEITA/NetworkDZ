package bonch.dev.networkdz.activities

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import bonch.dev.networkdz.R
import bonch.dev.networkdz.networking.RetrofitFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class PostCreateDialogFragment : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v = inflater.inflate(R.layout.fragment_dialog, container, false)

        val sendButton = v.findViewById<Button>(R.id.post_sendButton)

        sendButton.setOnClickListener {
            Send()
        }

        return v
    }
    fun Send(){
        val service = RetrofitFactory.makeRetrofitService()

        CoroutineScope(Dispatchers.IO).launch {
            val response = service.getPhotoPosts()

            try {
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            context,
                            "${response.code()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            context,
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
}

