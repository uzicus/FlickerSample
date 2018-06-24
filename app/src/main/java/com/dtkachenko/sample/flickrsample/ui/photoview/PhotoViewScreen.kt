package com.dtkachenko.sample.flickrsample.ui.photoview

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dtkachenko.sample.flickrsample.R
import com.dtkachenko.sample.flickrsample.extension.inject
import com.dtkachenko.sample.flickrsample.ui.base.BaseScreen
import kotlinx.android.synthetic.main.screen_photo_view.*

class PhotoViewScreen private constructor(bundle: Bundle?) :
        BaseScreen<PhotoViewScreenPm>(bundle) {

    companion object {
        private const val ARG_PHOTO_URL = "photo_url"

        fun newInstance(photoUrl: String): PhotoViewScreen {
            return PhotoViewScreen(Bundle().apply {
                putString(ARG_PHOTO_URL, photoUrl)
            })
        }
    }

    private val photoUrl get() = args.getString(ARG_PHOTO_URL)

    override val screenLayout = R.layout.screen_photo_view
    override val pm: PhotoViewScreenPm by inject()

    override fun onInitView(view: View, savedViewState: Bundle?) {
        Glide.with(view)
                .asBitmap()
                .load(photoUrl)
                .apply(RequestOptions.placeholderOf(R.drawable.bg_photo_placeholder))
                .into(photoImage)
    }
}