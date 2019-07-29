package me.sdi.github

import android.view.ViewGroup
import com.uber.rib.core.RibActivity
import com.uber.rib.core.ViewRouter
import me.sdi.github.ribs.RootBuilder

class MainActivity : RibActivity() {

    override fun createRouter(parentViewGroup: ViewGroup?): ViewRouter<*, *, *> {
        val rootBuilder = RootBuilder(object : RootBuilder.ParentComponent {})
        return rootBuilder.build(parentViewGroup!!)
    }
}
