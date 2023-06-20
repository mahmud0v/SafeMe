package safeme.uz.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import safeme.uz.R
import safeme.uz.data.remote.response.GameRecommendationResponse

class GameRecommendationAdapter : RecyclerView.Adapter<ViewHolder>() {

    var onItemClick: ((GameRecommendationResponse) -> Unit)? = null
    var shareBtnClick: (() -> Unit)? = null
    var saveBtnClick: (() -> Unit)? = null

    private val diffUtiItemCallback = object : DiffUtil.ItemCallback<GameRecommendationResponse>() {
        override fun areItemsTheSame(
            oldItem: GameRecommendationResponse,
            newItem: GameRecommendationResponse
        ) = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: GameRecommendationResponse,
            newItem: GameRecommendationResponse
        ) = oldItem == newItem

    }

    val differ = AsyncListDiffer(this, diffUtiItemCallback)

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.game_rec_layout, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = differ.currentList[position]
        holder.itemView.apply {
            val materialCardView: MaterialCardView = findViewById(R.id.game_rec_layout)
            val imageView: ImageView = findViewById(R.id.game_image)
            val title1: MaterialTextView = findViewById(R.id.game_title)
            val title2: MaterialTextView = findViewById(R.id.game_title2)
            val shareBtn: MaterialButton = findViewById(R.id.share_btn)
            val saveBtn: MaterialButton = findViewById(R.id.star_btn)
            data.image?.let {
                Glide.with(this).load(it).into(imageView)
            }
            title1.text = data.categoryName ?: ""
            title2.text = data.name ?: ""
            materialCardView.setOnClickListener {
                onItemClick?.invoke(data)
            }
            shareBtn.setOnClickListener {
                shareBtnClick?.invoke()
            }

            saveBtn.setOnClickListener {
                saveBtnClick?.invoke()
            }

        }
    }

    override fun getItemCount() = differ.currentList.size


}