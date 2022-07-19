package id.co.binar.secondhand.ui.dashboard.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.ViewSizeResolver
import id.co.binar.secondhand.R
import id.co.binar.secondhand.databinding.ListItemProductHomeBinding
import id.co.binar.secondhand.model.buyer.product.GetProductResponse
import id.co.binar.secondhand.model.seller.category.GetCategoryResponse
import id.co.binar.secondhand.util.Constant.ARRAY_STATUS
import id.co.binar.secondhand.util.convertRupiah

class HomeDefaultAdapter : ListAdapter<GetProductResponse, RecyclerView.ViewHolder>(diffUtilCallback) {

    private var _onClickAdapter: ((Int, GetProductResponse) -> Unit)? = null

    fun onClickAdapter(listener: (Int, GetProductResponse) -> Unit) {
        _onClickAdapter = listener
    }

    private fun List<GetCategoryResponse>.toNameOnly(): String {
        val str = mutableListOf<String>()
        this.forEach {
            str.add(it.name.toString())
        }
        return str.joinToString()
    }

    inner class ViewHolder(private val binding: ListItemProductHomeBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                getItem(bindingAdapterPosition)?.let {
                    _onClickAdapter?.invoke(bindingAdapterPosition, it)
                }
            }
        }

        fun bind(item: GetProductResponse) {
            binding.ivImageProduct.load(item.imageUrl) {
                placeholder(R.color.purple_100)
                error(R.color.purple_100)
                size(ViewSizeResolver(binding.ivImageProduct))
            }
            if (item.status == ARRAY_STATUS[5]) {
                binding.imgSold.isVisible = true
                binding.root.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.neutral_2))
            } else {
                binding.imgSold.isVisible = false
                binding.root.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.white))
            }
            binding.tvNamaProduct.text = item.name
            binding.tvHargaProduct.text = item.basePrice?.convertRupiah()
            binding.tvJenisProduct.text = item.categories?.toNameOnly()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            ListItemProductHomeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as ViewHolder
        val item = getItem(position)
        item?.let {
            holder.bind(it)
        }
    }
}

private val diffUtilCallback = object : DiffUtil.ItemCallback<GetProductResponse>() {
    override fun areItemsTheSame(oldItem: GetProductResponse, newItem: GetProductResponse): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: GetProductResponse, newItem: GetProductResponse): Boolean {
        return oldItem == newItem
    }
}