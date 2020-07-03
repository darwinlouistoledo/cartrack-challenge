package com.dlet.cartrack.challenge.module.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dlet.cartrack.challenge.R
import com.dlet.cartrack.challenge.common_android.ext.aac.observe
import com.dlet.cartrack.challenge.common_android.ext.aac.withViewModel
import com.dlet.cartrack.challenge.common_android.ext.component.showToast
import com.dlet.cartrack.challenge.common_android.ext.component.withBinding
import com.dlet.cartrack.challenge.common_android.ext.view.makeGone
import com.dlet.cartrack.challenge.common_android.ext.view.makeVisible
import com.dlet.cartrack.challenge.common_android.ext.view.makeVisibleOrGone
import com.dlet.cartrack.challenge.databinding.ActivityLoginBinding
import com.dlet.cartrack.challenge.di.factory.ViewModelFactory
import com.dlet.cartrack.challenge.domain.exceptions.InvalidUsernameAndPasswordException
import com.dlet.cartrack.challenge.domain.exceptions.RequiredFieldException
import com.dlet.cartrack.challenge.domain.manager.ErrorHandler
import com.dlet.cartrack.challenge.module.users.UserListActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LogInActivity : AppCompatActivity(){

  @Inject
  lateinit var viewModelFactory: ViewModelFactory

  @Inject
  lateinit var errorHandler: ErrorHandler<AppCompatActivity>

  private lateinit var binding: ActivityLoginBinding

  private lateinit var viewModel: LogInViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = withBinding(R.layout.activity_login)

    viewModel = withViewModel(this, viewModelFactory){
      observe(observableState, ::render)
    }

    binding.apply {
      btnLogin.setOnClickListener {
        tvInvalidMessage.makeGone()
        viewModel.dispatch(
          LogInViewModel.Action.Login(
            username = tietUsername.text.toString(),
            password = tietPassword.text.toString()
          )
        )
      }
    }
  }

  private fun render(state: LogInViewModel.State){

    state.successLogin?.getContentIfNotHandled()?.let {
      showToast(R.string.login_success)
      startActivity(Intent(applicationContext, UserListActivity::class.java))
      finish()
    }

    state.showLoading?.getContentIfNotHandled()?.let {isLoading->
      binding.apply {
        btnLogin.makeVisibleOrGone(!isLoading)
        spinkitView.makeVisibleOrGone(isLoading)
        tietUsername.isEnabled = !isLoading
        tietPassword.isEnabled = !isLoading
      }
    }

    state.invalidLogin?.getContentIfNotHandled()?.let { e ->
      binding.apply {
        btnLogin.makeVisible()
        spinkitView.makeVisible()
        tietUsername.isEnabled = true
        tietPassword.isEnabled = true

        tvInvalidMessage.makeVisible()
        when(e){
          is InvalidUsernameAndPasswordException -> tvInvalidMessage.text = getString(R.string.login_error)
          is RequiredFieldException -> tvInvalidMessage.text = e.message
        }
      }
    }

    state.error?.getContentIfNotHandled()?.let {
      errorHandler.handle(this, it)
    }
  }
}