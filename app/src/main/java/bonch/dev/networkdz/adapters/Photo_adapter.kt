package bonch.dev.networkdz.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import bonch.dev.networkdz.R
import bonch.dev.networkdz.models.Unit_photo
import com.bumptech.glide.Glide

class Photo_adapter(listik: List<Unit_photo>, val context: Context) :
    RecyclerView.Adapter<Photo_adapter.ItemPostHolder>() {
    var list = listik
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemPostHolder {
        return ItemPostHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.photo_item_post, parent, false)
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ItemPostHolder, position: Int) {
        val post = list[position]
        holder.bind(post)
    }

    class ItemPostHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        private val idPostTextView = itemview.findViewById<TextView>(R.id.photo_id)
        private val titlePostTextView = itemview.findViewById<TextView>(R.id.photo_title)
        private val urlPostTextView = itemview.findViewById<ImageView>(R.id.glide_image_view)

        fun bind(albumpost: Unit_photo) {
            //Glide.with(urlPostTextView).load(albumpost.url).into(urlPostTextView)
            //urlPostTextView.setImageDrawable(albumpost.bitMap)
            idPostTextView.text = albumpost.id.toString()
            titlePostTextView.text = albumpost.title
        }
    }
}