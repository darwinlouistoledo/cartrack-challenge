package com.dlet.cartrack.challenge.module.adapter

import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.dlet.cartrack.challenge.R
import com.dlet.cartrack.challenge.common_android.base.SimpleListAdapter
import com.dlet.cartrack.challenge.databinding.ViewholderItemCountryBinding
import com.dlet.cartrack.challenge.domain.constants.AppConstant
import com.dlet.cartrack.challenge.domain.model.Country
import java.util.*

class CountryListItemAdapter(
  private val onItemSelected: (Country)->Unit
): SimpleListAdapter<ViewholderItemCountryBinding, Country>(
  R.layout.viewholder_item_country,
  CountryAdapterDiffCallback
) {

  override fun bind(
    holder: ViewHolder<ViewholderItemCountryBinding>,
    item: Country,
    position: Int
  ) {
    val context = holder.itemView.context

    holder.itemView.setOnClickListener {
      onItemSelected.invoke(item)
    }

    holder.binding.apply {
      tvName.text = item.name

      val icon = item.code.let {
        AppConstant.COUNTRY_ICON.replace(AppConstant.COUNTRY_CODE, it.toLowerCase(Locale.ENGLISH))
      }

      Glide.with(context)
        .asBitmap()
        .load(icon)
        .into(ivCountryIcon)
    }
  }
}

object CountryAdapterDiffCallback : DiffUtil.ItemCallback<Country>() {

  override fun areItemsTheSame(
    oldItem: Country,
    newItem: Country
  ): Boolean = oldItem.code == newItem.code

  override fun areContentsTheSame(
    oldItem: Country,
    newItem: Country
  ): Boolean = oldItem == newItem
}