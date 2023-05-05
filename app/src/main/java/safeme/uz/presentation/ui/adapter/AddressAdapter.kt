package safeme.uz.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import safeme.uz.data.remote.response.Address
import safeme.uz.databinding.ItemDialogAddressBinding

class AddressAdapter : ListAdapter<Address, AddressAdapter.AddressViewHolder>(ItemDiffUtil) {

    private var onItemClickListener: ((Address) -> Unit)? = null

    fun setOnItemClickListener(block: ((Address) -> Unit)) {
        onItemClickListener = block
    }

    inner class AddressViewHolder(private val binding: ItemDialogAddressBinding) :
        ViewHolder(binding.root) {

        init {
            binding.tvTitle.setOnClickListener {
                onItemClickListener?.invoke(getItem(absoluteAdapterPosition))
            }
        }

        fun bind(item: Address) {
            item.name?.let {
                binding.tvTitle.text = item.name
            }
        }
    }

    private object ItemDiffUtil : DiffUtil.ItemCallback<Address>() {
        override fun areItemsTheSame(
            oldItem: Address, newItem: Address
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Address, newItem: Address
        ): Boolean {
            return oldItem == newItem
        }

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