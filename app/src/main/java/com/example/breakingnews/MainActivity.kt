package com.example.breakingnews

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.breakingnews.R
import com.example.breakingnews.database.ArticleDatabase
import com.example.breakingnews.databinding.ActivityMainBinding
import com.example.breakingnews.models.NewsViewModel
import com.example.breakingnews.models.NewsVmProvider
import com.example.breakingnews.repository.NewsRepository

class MainActivity : AppCompatActivity() {

    lateinit var newsViewModel: NewsViewModel
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val newsRepository=NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory=NewsVmProvider(application,newsRepository)
        newsViewModel=ViewModelProvider(this,viewModelProviderFactory).get(NewsViewModel::class.java)

        val navHost=supportFragmentManager.findFragmentById(R.id.main_container_view) as NavHostFragment
        val navController=navHost.navController
        binding.bottomNavigationView.setupWithNavController(navController)
    }
}