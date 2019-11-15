package bonch.dev.networkdz.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import bonch.dev.networkdz.R
import bonch.dev.networkdz.models.Album_post

class Albums_adapter(listik: List<Album_post>, val context: Context) :
    RecyclerView.Adapter<Albums_adapter.ItemPostHolder>() {
    var list = listik
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemPostHolder {
        return ItemPostHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.albums_item_post, parent, false)
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ItemPostHolder, position: Int) {
        val post = list[position]
        holder.bind(post)
    }

    class ItemPostHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        private val userIdPostTextView = itemview.findViewById<TextView>(R.id.album_userId)
        private val idPostTextView = itemview.findViewById<TextView>(R.id.album_id)
        private val titlePostTextView = itemview.findViewById<TextView>(R.id.album_title)

        fun bind(albumpost: Album_post) {
            userIdPostTextView.text = albumpost.userId.toString()
            idPostTextView.text = albumpost.id.toString()
            titlePostTextView.text = albumpost.title
        }
    }
}