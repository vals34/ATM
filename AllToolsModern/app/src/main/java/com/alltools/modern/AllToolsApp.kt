package com.alltools.modern

import android.app.Application
import com.alltools.modern.data.db.AppDatabase

class AllToolsApp : Application() {
    val database by lazy { AppDatabase.getInstance(this) }

    override fun onCreate() {
        super.onCreate()
    }
}
