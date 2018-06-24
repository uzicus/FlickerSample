package com.dtkachenko.sample.flickrsample.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.dtkachenko.sample.flickrsample.R
import com.dtkachenko.sample.flickrsample.extension.back
import com.dtkachenko.sample.flickrsample.extension.goTo
import com.dtkachenko.sample.flickrsample.extension.setRoot
import com.dtkachenko.sample.flickrsample.ui.photos.PhotosScreen
import com.dtkachenko.sample.flickrsample.ui.photoview.PhotoViewScreen
import kotlinx.android.synthetic.main.container.*
import me.dmdev.rxpm.navigation.NavigationMessage
import me.dmdev.rxpm.navigation.NavigationMessageHandler

class MainActivity : AppCompatActivity(), NavigationMessageHandler {

    private lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.container)

        router = Conductor.attachRouter(this, container, savedInstanceState)

        if (router.hasRootController().not()) {
            handleNavigationMessage(StartUpMessage())
        }
    }

    override fun onBackPressed() {
        if (!router.handleBack()) {
            super.onBackPressed()
        }
    }

    override fun handleNavigationMessage(message: NavigationMessage): Boolean {
        when (message) {
            is BackMessage -> {
                if (router.back())
                else super.onBackPressed()
            }

            is StartUpMessage -> {
                router.setRoot(PhotosScreen())
            }

            is OpenPhotoViewMessage -> {
                router.goTo(PhotoViewScreen.newInstance(message.photoUrl))
            }
        }

        return true
    }
}
