package ru.netology.nmedia.application

import android.app.Application
import android.icu.util.TimeUnit
import androidx.work.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.work.RefreshPostsWorker

//class NMediaApplication : Application() {
//    override fun onCreate() {
//        super.onCreate()
//        AppAuth.initApp(this)
//    }
//}

class NMediaApplication : Application() {
    private val appScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        setupAuth()
        setupWork()
    }

    private fun setupAuth() {
        appScope.launch {
            AppAuth.initApp(this@NMediaApplication)
        }
    }

    private fun setupWork() {

        //Здесь мы иницаилизируем класс воркера, заставляя его запускаться каждую минуту
        appScope.launch {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()                            //тут класс его указываем
            val request = PeriodicWorkRequestBuilder<RefreshPostsWorker>(1,
                java.util.concurrent.TimeUnit.MINUTES
            )
                .setConstraints(constraints)
                .build()

            //запускаем!
            WorkManager.getInstance(this@NMediaApplication).enqueueUniquePeriodicWork(
                RefreshPostsWorker.name,
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )
        }
    }
}