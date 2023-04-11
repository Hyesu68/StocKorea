package com.susuryo.stockorea.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.susuryo.stockorea.databinding.ActivityDetailBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailBinding
    lateinit var id : String
    private lateinit var name : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        id = intent.getStringExtra("isinCd").toString()
        name = intent.getStringExtra("name").toString()
        binding.name.text = name

        val adapter = MyPagerAdapter(supportFragmentManager, lifecycle)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tablayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Graph"
                1 -> tab.text = "Stock price calculator"
            }
        }.attach()

    }

    class MyPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
        FragmentStateAdapter(fragmentManager, lifecycle) {

        private val tabFragmentsCreators: Map<Int, () -> Fragment> = mapOf(
            0 to { GraphFragment() },
            1 to { InformationFragment() }
        )

        override fun getItemCount(): Int = tabFragmentsCreators.size

        override fun createFragment(position: Int): Fragment {
            return tabFragmentsCreators[position]?.invoke() ?: throw IndexOutOfBoundsException()
        }
    }
}