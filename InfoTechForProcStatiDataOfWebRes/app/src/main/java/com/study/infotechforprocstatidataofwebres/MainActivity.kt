package com.study.infotechforprocstatidataofwebres

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.FirebaseApp
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.study.infotechforprocstatidataofwebres.models.SlideInMenuController
import com.study.infotechforprocstatidataofwebres.network.SaveReceivingClient
import com.study.infotechforprocstatidataofwebres.ui.general.SlideInMenu
import com.study.infotechforprocstatidataofwebres.ui.pages.AllImages
import com.study.infotechforprocstatidataofwebres.ui.pages.GeneralInfo
import com.study.infotechforprocstatidataofwebres.ui.pages.MetaInfo
import com.study.infotechforprocstatidataofwebres.ui.pages.QuantityTags
import com.study.infotechforprocstatidataofwebres.ui.pages.ScriptsInfo
import com.study.infotechforprocstatidataofwebres.ui.pages.StylesInfo
import com.study.infotechforprocstatidataofwebres.ui.pages.WelcomePage
import com.study.infotechforprocstatidataofwebres.ui.theme.InfoTechForProcStatiDataOfWebResTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MainActivity : ComponentActivity() {
    fun convertToRetrofitBaseUrl(url: String) = if (url.endsWith("/")) url else "$url/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var serverBaseUrl = ""
        val slideInMenuController = GlobalContext.getOrNull()?.get<SlideInMenuController>()
            ?: SlideInMenuController.DEFAULT
        val saveReceivingClient: SaveReceivingClient = GlobalContext.getOrNull()?.get<SaveReceivingClient>()
            ?: SaveReceivingClient()
        FirebaseApp.initializeApp(baseContext)
        val remoteConfig = Firebase.remoteConfig
        lifecycleScope.launch(Dispatchers.IO) {
            remoteConfig.run {
                reset().await()
                fetchAndActivate().await()
                serverBaseUrl = convertToRetrofitBaseUrl(getString("server_base_url"))
            }
        }
        remoteConfig.addOnConfigUpdateListener(object : ConfigUpdateListener {
            override fun onUpdate(configUpdate: ConfigUpdate) {
                Log.i("Updated keys", configUpdate.updatedKeys.joinToString())

                if (configUpdate.updatedKeys.contains("server_base_url")) {
                    Log.i("Update value", "Value before: ${remoteConfig.getString("server_base_url")}")
                    remoteConfig.reset().addOnCanceledListener {
                        remoteConfig.fetchAndActivate().addOnCanceledListener {
                            saveReceivingClient.setOrChangeClient(
                                convertToRetrofitBaseUrl(
                                    remoteConfig.getString("server_base_url")
                                )
                            )
                            Log.i("Update value", "Value after: " +
                                    remoteConfig.getString("server_base_url")
                            )
                        }
                    }
                }
            }

            override fun onError(error: FirebaseRemoteConfigException) {
                Log.e("FirebaseRemoteConfigException", null, error)
            }
        })
        if (GlobalContext.getKoinApplicationOrNull() == null) {
            startKoin {
                modules(
                    module {
                        single {
                            Log.i("Koin [TEST]", "Client has created. Url: $serverBaseUrl")
                            return@single saveReceivingClient.apply {
                                if (!wasInstalled()) {
                                    setOrChangeClient(serverBaseUrl)
                                }
                            }
                        }
                        single {
                            slideInMenuController
                        }
                    }
                )
            }
        }
        else {
            Log.i("Global context", "We have global context.")
            remoteConfig.reset().addOnCanceledListener {
                remoteConfig.fetchAndActivate().addOnCanceledListener {
                    saveReceivingClient.setOrChangeClient(
                        remoteConfig.getString("server_base_url")
                    )
                }
            }
        }
        setContent {
            var backPressedBefore by remember {
                mutableStateOf(false)
            }
            var slideInMenuIsOpen by remember {
                mutableStateOf(false)
            }

            slideInMenuController.openCallback = { slideInMenuIsOpen = true }
            slideInMenuController.closeCallback = { slideInMenuIsOpen = false }

            onBackPressedDispatcher.addCallback {
                if (!backPressedBefore) {
                    backPressedBefore = true
                    Toast.makeText(
                        this@MainActivity,
                        getString(R.string.press_again_to_exit),
                        Toast.LENGTH_SHORT
                    ).show()
                    lifecycleScope.launch {
                        delay(2500)
                        if (backPressedBefore) {
                            backPressedBefore = false
                        }
                    }
                }
                else {
                    finish()
                }
            }
            InfoTechForProcStatiDataOfWebResTheme {
                val navigationController = rememberNavController()
                var enteredUrl by remember {
                    mutableStateOf("")
                }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(
                        navController = navigationController,
                        startDestination = Destinations.WELCOME
                    ) {
                        composable(Destinations.WELCOME) {
                            WelcomePage(navigationController = navigationController)
                        }
                        composable(Destinations.GENERAL_INFO) {
                            GeneralInfo(navigationController, enteredUrl) {
                                enteredUrl = it
                            }
                        }
                        composable(Destinations.META_INFO) {
                            MetaInfo(navigationController, enteredUrl) {
                                enteredUrl = it
                            }
                        }
                        composable(Destinations.ALL_IMAGES) {
                            AllImages(navigationController, enteredUrl) {
                                enteredUrl = it
                            }
                        }
                        composable(Destinations.STYLES_INFO) {
                            StylesInfo(navigationController, enteredUrl) {
                                enteredUrl = it
                            }
                        }
                        composable(Destinations.SCRIPTS_INFO) {
                            ScriptsInfo(navigationController, enteredUrl) {
                                enteredUrl = it
                            }
                        }
                        composable(Destinations.QUANTITY_TAGS_INFO) {
                            QuantityTags(navigationController, enteredUrl) {
                                enteredUrl = it
                            }
                        }
                    }
                    SlideInMenu(
                        slideInMenuIsOpen = slideInMenuIsOpen,
                        navigationController = navigationController
                    )
                }
            }
        }
    }

    object Destinations {
        const val WELCOME = "welcome"
        const val GENERAL_INFO = "general_info"
        const val META_INFO = "meta_info"
        const val ALL_IMAGES = "all_images"
        const val STYLES_INFO = "styles_info"
        const val SCRIPTS_INFO = "scripts_info"
        const val QUANTITY_TAGS_INFO = "quantity_tags"
    }
}