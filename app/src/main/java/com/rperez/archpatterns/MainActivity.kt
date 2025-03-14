package com.rperez.archpatterns

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.rperez.archpatterns.repository.presentation.ui.StockScreen
import com.rperez.archpatterns.ui.theme.ArchPatternsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArchPatternsTheme {
                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    StockScreen()
                }
            }
        }
    }
}

/**
 * Project for Architecture Pattern Samples
 *
 * 1. Repository Pattern
 *      a. abstracts data layer
 *      b. provide clean access for rest of app, mediator between data sources
 *      c. ssot - single source of truth
 *          1. manage data from 3x sources
 *          2. ensure app is always up to date
 *      d. abstraction - hide details of how fetching is done
 *          1. rest of app doesn't need to know where data comes from
 *      e. separation of concerns - repo separates data layer from rest of app
 *      f. caching - implement caching strategies to improve performance
 *          1. reduce redundancy in network calls
 */