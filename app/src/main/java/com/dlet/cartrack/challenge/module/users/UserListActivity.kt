package com.dlet.cartrack.challenge.module.users

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dlet.cartrack.challenge.R
import com.dlet.cartrack.challenge.common_android.ext.aac.observe
import com.dlet.cartrack.challenge.common_android.ext.aac.withViewModel
import com.dlet.cartrack.challenge.common_android.ext.component.showToast
import com.dlet.cartrack.challenge.common_android.ext.component.withBinding
import com.dlet.cartrack.challenge.common_android.ext.view.makeVisibleOrGone
import com.dlet.cartrack.challenge.databinding.ActivityUserListBinding
import com.dlet.cartrack.challenge.di.factory.ViewModelFactory
import com.dlet.cartrack.challenge.domain.manager.ErrorHandler
import com.dlet.cartrack.challenge.module.adapter.UserListItemAdapter
import com.dlet.cartrack.challenge.module.map.MapUsersActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class UserListActivity : AppCompatActivity(){

  @Inject
  lateinit var viewModelFactory: ViewModelFactory

  @Inject
  lateinit var errorHandler: ErrorHandler<AppCompatActivity>

  private lateinit var viewModel: UserListViewModel

  private lateinit var binding: ActivityUserListBinding

  private val adapterItems = UserListItemAdapter()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = withBinding(R.layout.activity_user_list)

    viewModel = withViewModel(this, viewModelFactory){
      observe(observableState, ::render)
    }

    binding.apply {
      fabMap.setOnClickListener {
        startActivity(Intent(applicationContext, MapUsersActivity::class.java))
      }

      recyclerView.apply {
        adapter = adapterItems
        layoutManager = LinearLayoutManager(
          context, LinearLayoutManager.VERTICAL, false
        )
      }
    }
  }

  private fun render(state: UserListViewModel.State){
    adapterItems.submitList(state.userList)

    state.isLoading?.getContentIfNotHandled()?.let {
      binding.spinkitView.makeVisibleOrGone(it)
    }

    state.error?.getContentIfNotHandled()?.let {
      it.printStackTrace()
      errorHandler.handle(this, it)
    }
  }

}