package safeme.uz.presentation.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import safeme.uz.R
import safeme.uz.data.remote.response.InspectorInfo

class InspectorRecyclerAdapter : RecyclerView.Adapter<InspectorRecyclerAdapter.ViewHolder>() {

    var onItemClick: ((InspectorInfo) -> Unit)? = null
    private val diffUtilItemCallback = object : DiffUtil.ItemCallback<InspectorInfo>() {
        override fun areItemsTheSame(oldItem: InspectorInfo, newItem: InspectorInfo) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: InspectorInfo, newItem: InspectorInfo) =
            oldItem == newItem
    }

    val differ = AsyncListDiffer(this, diffUtilItemCallback)

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        @SuppressLint("SetTextI18n")
        fun bind(position: Int) {
            val data = differ.currentList[position]
            val inspectorFullName: TextView = itemView.findViewById(R.id.tvInsName)
            val inspectorPosition: TextView = itemView.findViewById(R.id.tvInsDetail)
            val inspectorPhoneNumber: TextView = itemView.findViewById(R.id.tvInsPhoneNumber)
            inspectorFullName.text = "${data.lastName} ${data.firstName} ${data.patranomic}"
            inspectorPosition.text = (data.mahalla ?: "") + (" "+data.lavozimi ?: "")
            inspectorPhoneNumber.text = data.phone ?: ""
            val btnCall: MaterialButton = itemView.findViewById(R.id.btnCall)
            btnCall.setOnClickListener {
                onItemClick?.invoke(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_inspector, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }
}