package com.dtkachenko.sample.flickrsample.ui.base

import com.dtkachenko.sample.flickrsample.ui.BackMessage
import io.reactivex.functions.Consumer
import me.dmdev.rxpm.PresentationModel
import me.dmdev.rxpm.navigation.NavigationMessage

abstract class BaseScreenPm : PresentationModel() {

    open val backActionConsumer = Consumer<Unit> { sendMessage(BackMessage()) }

    protected fun sendMessage(message: NavigationMessage) {
        navigationMessages.consumer.accept(message)
    }
}