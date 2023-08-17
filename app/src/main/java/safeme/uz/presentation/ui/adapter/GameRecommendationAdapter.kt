package safeme.uz.presentation.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import safeme.uz.R
import safeme.uz.data.model.GameBookmarkState
import safeme.uz.data.remote.response.GameRecommendationResponse

class GameRecommendationAdapter(private var resultBookmarked:Boolean) : RecyclerView.Adapter<ViewHolder>() {

    var onItemClick: ((GameRecommendationResponse) -> Unit)? = null
    var onStarItemClick: ((GameBookmarkState) -> Unit)? = null

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

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val data = differ.currentList[position]
        holder.itemView.apply {
            val materialCardView: MaterialCardView = findViewById(R.id.game_rec_layout)
            val imageView: ImageView = findViewById(R.id.game_image)
            val title1: MaterialTextView = findViewById(R.id.game_title)
            val title2: MaterialTextView = findViewById(R.id.game_title2)
            val saveBtnLayout: LinearLayout = findViewById(R.id.star_btn_layout)
            val saveBtn: ImageView = findViewById(R.id.star_btn)
            materialCardView.tag = false
            data.image?.let {
                Glide.with(this).load(it).into(imageView)
            }
            title1.text = data.name ?: ""
            title2.text = data.description ?: ""
            materialCardView.setOnClickListener {
                onItemClick?.invoke(data)
            }

            if (resultBookmarked){
                saveBtn.setImageResource(R.drawable.star_yellow_btn)
                materialCardView.tag = true
            }else {
                saveBtn.setImageResource(R.drawable.star_btn)
                materialCardView.tag = false
            }


            saveBtnLayout.setOnClickListener {
                if (materialCardView.tag == true){
                    saveBtn.setImageResource(R.drawable.star_btn)
                    materialCardView.tag = false
                }else {
                    saveBtn.setImageResource(R.drawable.star_yellow_btn)
                    materialCardView.tag = true
                }
                val saveState = materialCardView.tag.toString().toBoolean()
                onStarItemClick?.invoke(GameBookmarkState(data.id,saveState))

            }


        }
    }

    fun setResultBookmark(result:Boolean){
        resultBookmarked = result
        notifyDataSetChanged()
    }

    override fun getItemCount() = differ.currentList.size


}