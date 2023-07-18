package safeme.uz.presentation.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import safeme.uz.R
import safeme.uz.data.model.DrawerItemInfo
import safeme.uz.utils.Keys

class DrawerItemRecyclerAdapter : RecyclerView.Adapter<DrawerItemRecyclerAdapter.ViewHolder>() {
    private var selectedItem = -1

    var onItemClick: ((DrawerItemInfo) -> Unit)? = null

    private val diffItemCallback = object : DiffUtil.ItemCallback<DrawerItemInfo>() {
        override fun areItemsTheSame(oldItem: DrawerItemInfo, newItem: DrawerItemInfo) =
            oldItem.title == newItem.title


        override fun areContentsTheSame(oldItem: DrawerItemInfo, newItem: DrawerItemInfo) =
            oldItem == newItem

    }

    val differ = AsyncListDiffer(this, diffItemCallback)


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(position: Int) {
            val data = differ.currentList[position]
            val linearLayout: LinearLayout = itemView.findViewById(R.id.linear_layout)
            val icon: ImageView = itemView.findViewById(R.id.option_icon)
            val text: MaterialTextView = itemView.findViewById(R.id.option_text)
            icon.setImageResource(data.icon)
            text.text = itemView.resources.getString(data.title)
            linearLayout.setOnClickListener {
                onItemClick?.invoke(data)
                selectedItem = position
                notifyDataSetChanged()
            }
            if (selectedItem == position){
                linearLayout.setBackgroundColor(Color.parseColor(Keys.BLUE))
                text.setTextColor(Color.parseColor(Keys.WHITE))
                icon.setImageResource(data.whiteIcon)
            }else {
                linearLayout.setBackgroundResource(R.color.white)
                text.setTextColor(Color.parseColor(Keys.BLACK))
                icon.setImageResource(data.icon)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.drawer_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)

    }
}