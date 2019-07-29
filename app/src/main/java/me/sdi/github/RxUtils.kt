package me.sdi.github

import android.view.MenuItem
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

fun View.clicks(): Observable<Unit> = ViewClicksObserver(this)
fun BottomNavigationView.selectedMenuItem(): Observable<MenuItem> = NavigationItemSelectionObserver(this)

class NavigationItemSelectionObserver(private val navigationView: BottomNavigationView) : Observable<MenuItem>() {

    override fun subscribeActual(observer: Observer<in MenuItem>) {
        val navigationListener = Listener(navigationView, observer)
        navigationView.setOnNavigationItemSelectedListener(navigationListener)
        observer.onSubscribe(navigationListener)
    }

    private class Listener(
        private val navigationView: BottomNavigationView,
        private val downstream: Observer<in MenuItem>
    ) : BottomNavigationView.OnNavigationItemSelectedListener, MainThreadDisposable() {

        override fun onDispose() {
            navigationView.setOnNavigationItemSelectedListener(null)
        }

        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            if (!isDisposed) {
                downstream.onNext(item)
            }

            return true
        }
    }
}

class ViewClicksObserver(private val view: View) : Observable<Unit>() {

    override fun subscribeActual(observer: Observer<in Unit>) {
        val disposableListener = Listener(view, observer)
        view.setOnClickListener(disposableListener)
        observer.onSubscribe(disposableListener)
    }

    private class Listener(
        private val view: View,
        private val downstream: Observer<in Unit>
    ) : View.OnClickListener, MainThreadDisposable() {

        override fun onDispose() {
            view.setOnClickListener(null)
        }

        override fun onClick(v: View?) {
            if (!isDisposed) {
                downstream.onNext(Unit)
            }
        }
    }
}