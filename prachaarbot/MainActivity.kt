package com.prachaarbot

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.ExperimentalMaterialApi
import com.prachaarbot.ui.screens.SplashScreenApp
import com.prachaarbot.ui.service.MyAppService
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SplashScreenApp()
        }
        Intent(this, MyAppService::class.java).also {
            startService(it)
        }
    }
}
