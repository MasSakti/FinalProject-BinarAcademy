package id.co.binar.secondhand.ui.dashboard.list_sell

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import id.co.binar.secondhand.R
import id.co.binar.secondhand.databinding.ListItemCategoryHomeBinding
import id.co.binar.secondhand.model.seller.category.GetCategoryResponse

class ListSellCategoryAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val asyncDiffer = AsyncListDiffer(this, diffUtilCallback)
    private var setPosition: Int? = 0
    private var _onClickAdapter: ((Int, GetCategoryResponse) -> Unit)? = null

    inner class ViewHolder(val binding: ListItemCategoryHomeBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                setPosition = bindingAdapterPosition
                _onClickAdapter?.invoke(bindingAdapterPosition, asyncDiffer.currentList[bindingAdapterPosition])
                notifyDataSetChanged()
            }
        }

        fun bind(item: GetCategoryResponse) {
            binding.txtCategory.text = item.name
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        asyncDiffer.submitList(listOf(
            GetCategoryResponse(
                name = "Produk", id = null, check = true
            ),
            GetCategoryResponse(
                name = "Diminati", id = 1
            ),
            GetCategoryResponse(
                name = "Terjual", id = 2
            )
        ))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            ListItemCategoryHomeBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as ViewHolder
        holder.bind(asyncDiffer.currentList[position])
        if (setPosition == position) {
            asyncDiffer.currentList[position].check = true
            holder.binding.root.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.purple_500))
            holder.binding.imgView.setImageResource(R.drawable.ic_round_search_white)
            holder.binding.txtCategory.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.white))
        } else {
            asyncDiffer.currentList[position].check = false
            holder.binding.root.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.purple_100))
            holder.binding.imgView.setImageResource(R.drawable.ic_round_search_24)
            holder.binding.txtCategory.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.black))
        }
    }

    override fun getItemCount(): Int {
        return asyncDiffer.currentList.size
    }

    fun onClickAdapter(listener: (Int, GetCategoryResponse) -> Unit) {
        _onClickAdapter = listener
    }
}

private val diffUtilCallback = object : DiffUtil.ItemCallback<GetCategoryResponse>() {
    override fun areItemsTheSame(oldItem: GetCategoryResponse, newItem: GetCategoryResponse): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: GetCategoryResponse, newItem: GetCategoryResponse): Boolean {
        return oldItem == newItem
    }
}