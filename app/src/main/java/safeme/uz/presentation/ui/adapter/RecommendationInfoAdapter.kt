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
import safeme.uz.data.remote.response.GameRecommendationResponse
import safeme.uz.data.remote.response.RecommendationInfo

class RecommendationInfoAdapter :
    RecyclerView.Adapter<RecommendationInfoAdapter.ViewHolder>() {

    var onItemClick: ((RecommendationInfo) -> Unit)? = null
    private val diffUtilItemCallback = object : DiffUtil.ItemCallback<RecommendationInfo>() {
        override fun areItemsTheSame(
            oldItem: RecommendationInfo,
            newItem: RecommendationInfo
        ) = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: RecommendationInfo,
            newItem: RecommendationInfo
        ) = oldItem == newItem

    }



    val differ = AsyncListDiffer(this, diffUtilItemCallback)

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(position: Int) {
            val data = differ.currentList[position]
            val rulesLayout = itemView.findViewById<LinearLayout>(R.id.rules_layout)
            val image = itemView.findViewById<ImageView>(R.id.rule_img)
            val title = itemView.findViewById<MaterialTextView>(R.id.rules_text)
            title.text = data.title
            rulesLayout.setOnClickListener {
                onItemClick?.invoke(data)
            }
            data.image?.let { Glide.with(itemView).load(it).into(image) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rules_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }
}