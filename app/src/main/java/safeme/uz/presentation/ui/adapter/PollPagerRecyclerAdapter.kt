package safeme.uz.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.textview.MaterialTextView
import safeme.uz.R
import safeme.uz.data.remote.response.PollResponseInfo

class PollPagerRecyclerAdapter : RecyclerView.Adapter<PollPagerRecyclerAdapter.ViewHolder>() {

    var onItemClick: ((PollResponseInfo) -> Unit)? = null

    private val diffUtilItemCallback = object : DiffUtil.ItemCallback<PollResponseInfo>() {
        override fun areItemsTheSame(
            oldItem: PollResponseInfo,
            newItem: PollResponseInfo
        ) = oldItem.id == newItem.id


        override fun areContentsTheSame(
            oldItem: PollResponseInfo,
            newItem: PollResponseInfo
        ) = oldItem == newItem

    }

    val differ = AsyncListDiffer(this, diffUtilItemCallback)

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(position: Int) {
            val data = differ.currentList[position]
            val imageView: ImageView = itemView.findViewById(R.id.poll_image)
            val title: MaterialTextView = itemView.findViewById(R.id.poll_title)
            val btn: LinearLayout = itemView.findViewById(R.id.start_btn)
            btn.setOnClickListener {
                onItemClick?.invoke(differ.currentList[position])
            }
            data.media?.let {
                Glide.with(imageView).load(it).into(imageView)
            }
            title.text = data.text ?: ""
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.poll_rec_layout, parent, false)
        return ViewHolder((view))
    }

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)


    }
}