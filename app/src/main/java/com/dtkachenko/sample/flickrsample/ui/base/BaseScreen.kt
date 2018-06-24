package com.dtkachenko.sample.flickrsample.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.dmdev.rxpm.base.PmController
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.*

abstract class BaseScreen<PM : BaseScreenPm>(bundle: Bundle? = null) :
        PmController<PM>(bundle),
        LayoutContainer {

    // Holds the view to allow the usage of android extensions right after the view is inflated.
    private var internalContainerView: View? = null

    abstract val screenLayout: Int

    abstract val pm: PM

    open fun onInitView(view: View, savedViewState: Bundle?) {}

    open fun onBindPresentationModel(view: View, pm: PM) {}

    override val containerView: View?
        get() = internalContainerView

    override fun providePresentationModel() = pm

    override fun createView(inflater: LayoutInflater, container: ViewGroup, savedViewState: Bundle?): View {
        return inflater.inflate(screenLayout, container, false).also {
            internalContainerView = it
            onInitView(it, savedViewState)
        }
    }

    override fun onBindPresentationModel(pm: PM) {
        onBindPresentationModel(view!!, pm)
    }

    override fun handleBack(): Boolean {
        passTo(presentationModel.backActionConsumer)
        return true
    }

    override fun onDestroyView(view: View) {
        super.onDestroyView(view)

        internalContainerView = null
        clearFindViewByIdCache()
    }
}