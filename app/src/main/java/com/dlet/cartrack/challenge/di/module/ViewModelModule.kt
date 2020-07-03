package com.dlet.cartrack.challenge.di.module

import androidx.lifecycle.ViewModel
import com.dlet.cartrack.challenge.di.mapkey.ViewModelKey
import com.dlet.cartrack.challenge.module.login.LogInViewModel
import com.dlet.cartrack.challenge.module.map.MapUsersViewModel
import com.dlet.cartrack.challenge.module.users.UserListViewModel
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.FragmentComponent
import dagger.multibindings.IntoMap


@Module
@InstallIn(ActivityComponent::class, FragmentComponent::class)
abstract class ViewModelModule {

  @Binds
  @IntoMap
  @ViewModelKey(LogInViewModel::class)
  abstract fun logInViewModel(logInViewModel: LogInViewModel): ViewModel

  @Binds
  @IntoMap
  @ViewModelKey(UserListViewModel::class)
  abstract fun userListViewModel(userListViewModel: UserListViewModel): ViewModel

  @Binds
  @IntoMap
  @ViewModelKey(MapUsersViewModel::class)
  abstract fun mapUsersViewModel(mapUsersViewModel: MapUsersViewModel): ViewModel

}