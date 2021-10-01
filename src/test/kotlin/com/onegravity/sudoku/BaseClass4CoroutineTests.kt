package com.onegravity.sudoku

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
open class BaseClass4CoroutineTests {

    @ObsoleteCoroutinesApi
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @BeforeAll
    @ExperimentalCoroutinesApi
    @ObsoleteCoroutinesApi
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @AfterAll
    @ExperimentalCoroutinesApi
    @ObsoleteCoroutinesApi
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

}
