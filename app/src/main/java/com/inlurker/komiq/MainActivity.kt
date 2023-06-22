package com.inlurker.komiq

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.room.Room
import com.inlurker.komiq.model.data.repository.ComicRepository
import com.inlurker.komiq.model.data.room.ComicDatabase
import com.inlurker.komiq.ui.screens.MainScreen
import com.inlurker.komiq.ui.theme.KomiQTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize Comic Repository's Room Database
        Room.databaseBuilder(
            applicationContext,
            ComicDatabase::class.java,
            "comic_database"
        ).build()
        setContent {
            ComicRepository.initialize(applicationContext)
            KomiQTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}
