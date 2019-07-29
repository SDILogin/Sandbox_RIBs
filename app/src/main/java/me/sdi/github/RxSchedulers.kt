package me.sdi.github

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

interface RxSchedulers {
    fun io(): Scheduler
    fun main(): Scheduler
}

class RuntimeRxSchedulers: RxSchedulers {
    override fun io() = Schedulers.io()

    override fun main() = AndroidSchedulers.mainThread()
}