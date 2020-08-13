package com.friendoye.recyclerxray.runner

import android.app.Application
import android.content.Context
import com.friendoye.recyclerxray.testing.ExceptionShooter
import com.karumi.shot.ShotTestRunner

class RecyclerXRayTestRunner : ShotTestRunner() {

    override fun newApplication(cl: ClassLoader?, className: String?, context: Context?): Application {
        val app = super.newApplication(cl, className, context)
        ExceptionShooter.current = ExceptionShooter.TestExceptionShooter()
        return app
    }

    override fun start() {
        super.start()
    }
}