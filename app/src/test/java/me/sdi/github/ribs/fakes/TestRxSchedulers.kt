package me.sdi.github.ribs.fakes

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import me.sdi.github.RxSchedulers

class TestRxSchedulers: RxSchedulers {
    override fun main(): Scheduler = Schedulers.trampoline()

    override fun io(): Scheduler = Schedulers.trampoline()
}