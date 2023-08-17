package safeme.uz.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import safeme.uz.data.remote.response.Address
import safeme.uz.data.remote.response.AddressResponse
import safeme.uz.data.remote.response.RegionInfo
import safeme.uz.databinding.ItemDialogAddressBinding

class AddressAdapter : ListAdapter<RegionInfo, AddressAdapter.AddressViewHolder>(ItemDiffUtil) {

    private var onItemClickListener: ((RegionInfo) -> Unit)? = null

    fun setOnItemClickListener(block: ((RegionInfo) -> Unit)) {
        onItemClickListener = block
    }

    inner class AddressViewHolder(private val binding: ItemDialogAddressBinding) :
        ViewHolder(binding.root) {

        init {
            binding.tvTitle.setOnClickListener {
//                onItemClickListener?.invoke(getItem(absoluteAdapterPosition))
            }
        }

        fun bind(item: RegionInfo) {
            item.name?.let {
                binding.tvTitle.text = it
            }
        }
    }

    private object ItemDiffUtil : DiffUtil.ItemCallback<RegionInfo>() {
        override fun areItemsTheSame(oldItem: RegionInfo, newItem: RegionInfo) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: RegionInfo,
            newItem: RegionInfo
        ) = oldItem == newItem


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        return AddressViewHolder(
            ItemDialogAddressBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}