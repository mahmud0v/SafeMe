package safeme.uz.presentation.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import safeme.uz.R
import safeme.uz.data.model.NewsData
import safeme.uz.utils.Colors

class NewsRecyclerAdapter : RecyclerView.Adapter<NewsRecyclerAdapter.ViewHolder>() {

    var onItemClick: ((NewsData) -> Unit)? = null

    private val diffUtilItemCallBack = object : DiffUtil.ItemCallback<NewsData>() {
        override fun areItemsTheSame(oldItem: NewsData, newItem: NewsData) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: NewsData, newItem: NewsData) =
          true


    }
    val differ = AsyncListDiffer(this, diffUtilItemCallBack)

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(position: Int) {
            val data = differ.currentList[position]
            val color = Colors.getCategoryColors()
            val materialCardView = itemView.findViewById<MaterialCardView>(R.id.announcements_item)
            materialCardView.strokeColor = Color.parseColor(color[position%color.size])
            val image = itemView.findViewById<ImageView>(R.id.announcements_img)
            val videoPodkat = itemView.findViewById<TextView>(R.id.videopodkat_text)
            val videoPodDesc = itemView.findViewById<TextView>(R.id.videopd_desc)
            val dateText = itemView.findViewById<TextView>(R.id.date_text)
            val viewedText = itemView.findViewById<TextView>(R.id.viewed_text)
            data.image?.let { Glide.with(itemView).load(it).into(image) }
            videoPodkat.text = data.title ?: ""
            videoPodDesc.text = data.shortText ?: ""
            dateText.text = trimDate(data.created_date!!) ?: ""
            viewedText.text = data.views.toString()
            materialCardView.setOnClickListener {
                onItemClick?.invoke(data)
            }
        }

    }

    private fun trimDate(date: String?): String? {
        return date?.substring(0, 10)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_announcement, parent, false)
        return ViewHolder(view)


    }

    override fun getItemCount() = differ.currentList.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)

    }


}