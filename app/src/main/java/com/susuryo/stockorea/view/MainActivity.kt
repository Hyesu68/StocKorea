package com.susuryo.stockorea.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.susuryo.stockorea.R
import com.susuryo.stockorea.databinding.ActivityMainBinding
class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.menu_main -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment, MainFragment()).commit()
                    true
                }
                R.id.menu_star -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment, StarFragment()).commit()
                    true
                }
                else -> false
            }
        }
        supportFragmentManager.beginTransaction().replace(R.id.fragment, MainFragment()).commit()

    }

}