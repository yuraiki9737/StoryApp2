package com.navigation.latihan.storyappsubmission1intermediateandroid.ui.home


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.navigation.latihan.storyappsubmission1intermediateandroid.data.model.LoginUser
import com.navigation.latihan.storyappsubmission1intermediateandroid.ui.addstory.AddStory
import com.navigation.latihan.storyappsubmission1intermediateandroid.ui.setting.Setting
import com.navigation.latihan.storyappsubmission1intermediateandroid.databinding.ActivityHomeBinding
import com.navigation.latihan.storyappsubmission1intermediateandroid.ui.maps.StoryMaps
import com.navigation.latihan.storyappsubmission1intermediateandroid.ui.home.adapter.AdapterHome
import com.navigation.latihan.storyappsubmission1intermediateandroid.ui.home.adapter.LoadingAdapter
import com.navigation.latihan.storyappsubmission1intermediateandroid.ui.viewmodel.HomeListModel
import com.navigation.latihan.storyappsubmission1intermediateandroid.ui.viewmodel.ViewModelFactory

class Home : AppCompatActivity() {

    private var binding_ : ActivityHomeBinding? = null
    private val binding get() = binding_

    private lateinit var loginUser: LoginUser
     private lateinit var adapterHome: AdapterHome
    private val homeListModel: HomeListModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding_ = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        loginUser = intent.getParcelableExtra(EXTRA_USER)!!


        recyclerSwipeRefresh()
        bindingButton()

    }

    override fun onResume() {
        super.onResume()
        recyclerViewInitAdapter()
    }

    private fun bindingButton(){
        binding?.btnCreate?.setOnClickListener{
            val view = Intent(this, AddStory::class.java)
            view.putExtra(AddStory.EXTRA_USER, loginUser)
            startActivity(view)
        }

        binding?.btnSetting?.setOnClickListener {
            val view = Intent(this@Home, Setting::class.java)
            startActivity(view)

        }
        binding?.storyLocation?.setOnClickListener {
            val viewLocation = Intent(this, StoryMaps::class.java)
            viewLocation.putExtra(AddStory.EXTRA_USER, loginUser)
            startActivity(viewLocation)
        }
    }

    private fun recyclerViewInitAdapter() {
        adapterHome = AdapterHome()
        binding?.storyAppRv?.adapter = adapterHome.withLoadStateHeaderAndFooter(
            footer = LoadingAdapter { adapterHome.retry() },
            header = LoadingAdapter { adapterHome.retry() }
        )

        binding?.storyAppRv?.layoutManager = LinearLayoutManager(this)
        binding?.storyAppRv?.setHasFixedSize(true)

        lifecycleScope.launchWhenCreated {
            adapterHome.loadStateFlow.collect {
                binding?.swipe?.isRefreshing = it.mediator?.refresh is LoadState.Loading
            }
        }


        homeListModel.getStory(loginUser.token).observe(this) {
            adapterHome.submitData(lifecycle, it)

        }
    }

    private fun recyclerSwipeRefresh() {
        binding?.swipe?.setOnRefreshListener {
            adapterHome.refresh()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding_ = null
    }

    companion object {
        const val EXTRA_USER = "user"
    }
}
