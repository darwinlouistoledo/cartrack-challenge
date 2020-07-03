package com.dlet.cartrack.challenge.module.countries

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dlet.cartrack.challenge.R
import com.dlet.cartrack.challenge.common_android.ext.aac.observe
import com.dlet.cartrack.challenge.common_android.ext.aac.withViewModel
import com.dlet.cartrack.challenge.common_android.ext.component.withBinding
import com.dlet.cartrack.challenge.databinding.ActivityCountryListBinding
import com.dlet.cartrack.challenge.di.factory.ViewModelFactory
import com.dlet.cartrack.challenge.domain.manager.ErrorHandler
import com.dlet.cartrack.challenge.module.adapter.CountryListItemAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_users_map.*
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject

@AndroidEntryPoint
class CountryListActivity : AppCompatActivity(){

  @Inject
  lateinit var viewModelFactory: ViewModelFactory

  @Inject
  lateinit var errorHandler: ErrorHandler<AppCompatActivity>

  private lateinit var viewModel: CountryListViewModel

  private lateinit var binding: ActivityCountryListBinding

  private lateinit var br: BufferedReader

  private val adapterItems = CountryListItemAdapter()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = withBinding(R.layout.activity_country_list)

    viewModel = withViewModel(this, viewModelFactory){
      observe(observableState, ::render)
    }

    binding.apply {
      toolbarBack.setOnClickListener {
        onBackPressed()
      }

      recyclerView.apply {
        adapter = adapterItems
        layoutManager = LinearLayoutManager(
          context, LinearLayoutManager.VERTICAL, false
        )
      }
    }

    br = BufferedReader( InputStreamReader(resources.openRawResource(R.raw.countries)))

    viewModel.dispatch(CountryListViewModel.Action.LoadCountries(br.readLine()))
  }

  private fun render(state: CountryListViewModel.State){
    adapterItems.submitList(state.countryList)

    if (state.countryList.isNotEmpty())
      br.close()

    state.error?.getContentIfNotHandled()?.let {
      br.close()
      it.printStackTrace()
      errorHandler.handle(this, it)
    }
  }

}