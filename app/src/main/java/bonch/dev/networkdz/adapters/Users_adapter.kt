package bonch.dev.networkdz.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import bonch.dev.networkdz.R
import bonch.dev.networkdz.models.User_post

class Users_adapter(val list: List<User_post>, val context: Context) :
    RecyclerView.Adapter<Users_adapter.ItemPostHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemPostHolder {
        return ItemPostHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.users_item_post, parent, false)
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ItemPostHolder, position: Int) {
        val post = list[position]
        holder.bind(post)
    }

    class ItemPostHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        private val namePostTextView = itemview.findViewById<TextView>(R.id.user_name)
        private val nickNamePostTextView = itemview.findViewById<TextView>(R.id.user_nick_name)
        private val emailPostTextView = itemview.findViewById<TextView>(R.id.user_email)
        //private val addressPostTextView = itemview.findViewById<TextView>(R.id.user_address)
        private val phonePostTextView = itemview.findViewById<TextView>(R.id.user_phone)

        fun bind(userpost: User_post) {
            namePostTextView.text = userpost.name
            nickNamePostTextView.text = userpost.username
            emailPostTextView.text = userpost.email
            //nickNamePostTextView.text = userpost.username
            phonePostTextView.text = userpost.phone
        }
    }
}