package com.raywenderlich.spacedaily

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.raywenderlich.spacedaily.network.PhotoResponse
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.core.KoinComponent
import org.koin.core.inject

class DailyPhotoView(private val mainView: ViewGroup): MainView, KoinComponent, LayoutContainer {
    private val activityRetriever: ActivityRetriever by inject()
    override val containerView: View?
        get() = mainView
    init {
        val viewModel = ViewModelProvider(activityRetriever.getActivity() as FragmentActivity)
            .get(MainViewModel::class.java)
        viewModel.view = this
        viewModel.getDailyPhoto()
    }

    override fun setDailyPhoto(dailyPhoto: PhotoResponse) {
        GlideApp.with(activityRetriever.context)
            .load(dailyPhoto.url)
            .into(nasaImage)
        date.text = dailyPhoto.date
        explanation.text = dailyPhoto.explanation
    }
}