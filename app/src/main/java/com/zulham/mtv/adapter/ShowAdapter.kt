package com.zulham.mtv.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.zulham.mtv.R
import com.zulham.mtv.data.local.room.entity.DataEntity
import kotlinx.android.synthetic.main.list_item.view.*

class ShowAdapter : PagedListAdapter<DataEntity, ShowAdapter.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataEntity>(){
            override fun areItemsTheSame(oldItem: DataEntity, newItem: DataEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: DataEntity, newItem: DataEntity): Boolean {
                return oldItem == newItem
            }

        }
    }

    fun getSwipeData(swipePosition: Int): DataEntity? = getItem(swipePosition)

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: DataEntity)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val w = 1000
        private val h = 1000
        private val imgUrl = "https://image.tmdb.org/t/p/w300/"

        fun bind(showTime: DataEntity){
            with(itemView){
                Glide.with(context)
                        .load( imgUrl + showTime.posterPath)
                        .error(R.drawable.ic_launcher_foreground)
                        .apply(RequestOptions().override(w, h))
                        .into(img_poster)
                
                tv_item_date.text = showTime.releaseDate
                tv_item_title.text = showTime.title
                tv_item_overview.text = showTime.overview

                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(showTime) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }
}