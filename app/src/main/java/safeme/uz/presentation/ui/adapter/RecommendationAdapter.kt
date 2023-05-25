package safeme.uz.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.textview.MaterialTextView
import safeme.uz.R
import safeme.uz.data.model.CategoriesData
import safeme.uz.utils.dpToPx

class RecommendationAdapter : RecyclerView.Adapter<RecommendationAdapter.ViewHolder>() {

    var onItemClick: ((CategoriesData) -> Unit)? = null

    private val diffUtilItemCallBack = object : DiffUtil.ItemCallback<CategoriesData>() {
        override fun areItemsTheSame(oldItem: CategoriesData, newItem: CategoriesData) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: CategoriesData, newItem: CategoriesData) =
            oldItem == newItem
    }
    val differ = AsyncListDiffer(this, diffUtilItemCallBack)

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(position: Int) {
            val layoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.apply {
                when {

                    position % 2 == 0 -> {
                        marginStart = 16.dpToPx()
                        marginEnd = 8.dpToPx()
                    }

                    else -> {
                        marginStart = 8.dpToPx()
                        marginEnd = 16.dpToPx()

                    }
                }
            }
            val data = differ.currentList[position]
            val layout = itemView.findViewById<ConstraintLayout>(R.id.rec_layout)
            val recText = itemView.findViewById<MaterialTextView>(R.id.rec_text)
            val recIcon = itemView.findViewById<ImageView>(R.id.rec_icon)
            data.icon?.let { Glide.with(itemView).load(it).into(recIcon) }
            recText.text = data.title ?: ""
            layout.setOnClickListener {
                onItemClick?.invoke(data)
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recommendation_type_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)

    }
}