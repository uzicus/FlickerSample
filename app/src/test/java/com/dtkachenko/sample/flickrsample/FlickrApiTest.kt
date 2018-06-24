package com.dtkachenko.sample.flickrsample

import com.dtkachenko.sample.flickrsample.api.FlickrApi
import com.dtkachenko.sample.flickrsample.api.networkModule
import com.dtkachenko.sample.flickrsample.api.response.Status
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.standalone.StandAloneContext.closeKoin
import org.koin.standalone.StandAloneContext.startKoin
import org.koin.standalone.inject
import org.koin.test.KoinTest

class FlickrApiTest : KoinTest {

    private val api: FlickrApi by inject()

    @Before
    fun setUp() {
        startKoin(listOf(networkModule))
    }

    @Test
    fun shouldGetRecent() {
        api.getRecent(10, 1)
                .test()
                .apply {
                    awaitTerminalEvent()
                    assertNoErrors()
                    assertValue { response ->
                        response.status == Status.OK
                    }
                }
    }

    @Test
    fun shouldSearch() {
        api.search(10, 1, "house")
                .test()
                .apply {
                    awaitTerminalEvent()
                    assertNoErrors()
                    assertValue { response ->
                        response.status == Status.OK
                    }
                }
    }

    @After
    fun tearDown() {
        closeKoin()
    }
}