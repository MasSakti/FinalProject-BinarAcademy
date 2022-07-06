package com.example.projectgroup2.ui.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.projectgroup2.R
import com.example.projectgroup2.databinding.ActivityMainBinding
import com.example.projectgroup2.ui.main.akun.AkunFragment
import com.example.projectgroup2.ui.main.daftarjual.DaftarJualFragment
import com.example.projectgroup2.ui.main.home.HomeFragment
import com.example.projectgroup2.ui.main.jual.JualFragment
import com.example.projectgroup2.ui.main.notif.NotifFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val homeFragment = HomeFragment()
        val notifikasiFragment = NotifFragment()
        val jualFragment = JualFragment()
        val daftarjualFragment = DaftarJualFragment()
        val akunFragment = AkunFragment()

        setCurrentFragment(homeFragment)

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.navbar_Home -> setCurrentFragment(homeFragment)
                R.id.navbar_Notifikasi -> setCurrentFragment(notifikasiFragment)
                R.id.navbar_Jual -> {
                    setCurrentFragment(jualFragment)
                    binding.bottomNavigationView.visibility = View.GONE
                }
                R.id.navbar_DaftarJual -> setCurrentFragment(daftarjualFragment)
                R.id.navbar_Akun -> setCurrentFragment(akunFragment)
            }
            true
        }
    }

//        val navController = findNavController(R.id.flFragment)
//        binding.bottomNavigationView.setupWithNavController(navController)

//        navController.addOnDestinationChangedListener { _, destination, _ ->
//            when (destination.id) {
//                R.id.jualFragment -> {
//                    binding.bottomNavigationView.visibility = View.GONE
//                }
//                R.id.loginActivity -> {
//                    binding.bottomNavigationView.visibility = View.GONE
//                }
//                R.id.registerActivity -> {
//                    binding.bottomNavigationView.visibility = View.GONE
//                }
//                else -> {
//                    binding.bottomNavigationView.visibility = View.VISIBLE
//                }
//            }
//        }

    private fun setCurrentFragment(fragment: Fragment) = supportFragmentManager.beginTransaction().apply {
        replace(R.id.flFragment,fragment)
        commit()
    }
}