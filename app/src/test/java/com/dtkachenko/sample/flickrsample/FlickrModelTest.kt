package com.dtkachenko.sample.flickrsample

import com.dtkachenko.sample.flickrsample.api.networkModule
import com.dtkachenko.sample.flickrsample.model.FlickrModel
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.standalone.StandAloneContext.closeKoin
import org.koin.standalone.StandAloneContext.startKoin
import org.koin.standalone.inject
import org.koin.test.KoinTest

class FlickrModelTest : KoinTest {

    private val model: FlickrModel by inject()

    @Before
    fun setUp() {
        startKoin(listOf(appModule, networkModule))
    }

    @Test
    fun shouldGetRecentPhotos() {
        model.getRecentPhotos(1, 20)
                .test()
                .apply {
                    awaitTerminalEvent()
                    assertNoErrors()
                    assertValue {
                        it.size == 20
                    }
                }
    }

    @Test
    fun shouldSearchPhotos() {
        model.searchPhotos("house", 1, 20)
                .test()
                .apply {
                    awaitTerminalEvent()
                    assertNoErrors()
                    assertValue {
                        it.size == 20
                    }
                }
    }

    @After
    fun tearDown() {
        closeKoin()
    }
}