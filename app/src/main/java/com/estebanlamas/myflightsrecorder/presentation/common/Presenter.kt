package com.estebanlamas.myflightsrecorder.presentation.common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

open class Presenter<T>: CoroutineScope {
    var view: T? = null
    lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    fun attacheView(view: T) {
        this.view = view
        job = Job()
    }
}