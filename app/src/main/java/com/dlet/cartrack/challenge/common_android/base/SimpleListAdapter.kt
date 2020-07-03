package com.dlet.cartrack.challenge.common_android.base

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dlet.cartrack.challenge.common_android.ext.component.withBinding

abstract class SimpleListAdapter<B : ViewDataBinding, M> constructor(
  @LayoutRes private val layoutResId: Int,
  diffCallback: DiffUtil.ItemCallback<M>
) : ListAdapter<M, SimpleListAdapter.ViewHolder<B>>(diffCallback) {

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): ViewHolder<B> {
    val binding: B = parent.withBinding(layoutResId)
    return ViewHolder(binding)
  }

  override fun onBindViewHolder(
    holder: ViewHolder<B>,
    position: Int
  ) {
    bind(holder, getItem(holder.adapterPosition), holder.adapterPosition)
    holder.binding.executePendingBindings()
  }

  abstract fun bind(
    holder: ViewHolder<B>,
    item: M,
    position: Int
  )

  class ViewHolder<B : ViewDataBinding>(val binding: B) : RecyclerView.ViewHolder(binding.root)

}