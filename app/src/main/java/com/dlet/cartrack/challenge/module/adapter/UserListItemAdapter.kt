package com.dlet.cartrack.challenge.module.adapter

import androidx.recyclerview.widget.DiffUtil
import com.dlet.cartrack.challenge.R
import com.dlet.cartrack.challenge.common_android.base.SimpleListAdapter
import com.dlet.cartrack.challenge.databinding.ViewholderItemUserBinding
import com.dlet.cartrack.challenge.domain.model.User

class UserListItemAdapter : SimpleListAdapter<ViewholderItemUserBinding, User>(
  R.layout.viewholder_item_user,
  UserAdapterDiffCallback
) {

  override fun bind(
    holder: ViewHolder<ViewholderItemUserBinding>,
    item: User,
    position: Int
  ) {
    val context = holder.itemView.context

    holder.binding.apply {
      tvName.text = item.name

      tvAddress.text = context.getString(R.string.lbl_value_address, item.address.displayAddress)
      tvEmail.text = context.getString(R.string.lbl_value_email, item.email)
      tvPhone.text = context.getString(R.string.lbl_value_phone, item.phone)
      tvWebsite.text = context.getString(R.string.lbl_value_website, item.website)
    }
  }
}

object UserAdapterDiffCallback : DiffUtil.ItemCallback<User>() {

  override fun areItemsTheSame(
    oldItem: User,
    newItem: User
  ): Boolean = oldItem.id == newItem.id

  override fun areContentsTheSame(
    oldItem: User,
    newItem: User
  ): Boolean = oldItem == newItem
}