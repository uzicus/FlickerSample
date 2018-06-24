package com.dtkachenko.sample.flickrsample.extension

import org.koin.KoinContext
import org.koin.core.parameter.Parameters
import org.koin.dsl.context.emptyParameters
import org.koin.standalone.StandAloneContext

inline fun <reified T> inject(
        name: String = "",
        noinline parameters: Parameters = emptyParameters()
): Lazy<T> = lazy {
    (StandAloneContext.koinContext as KoinContext).get<T>(name, parameters)
}