/**
 * Course: MAD302-01 Android Development
 * Assignment: Assignment 3
 * Student Name: Khushi Patel A00198843
 * Date: 04/24/2026
 * Description: Smart Utility App. MainActivity loads HomeFragment directly.
 */

package com.example.mad302_assignment03_khushipatel

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * Entry point. Loads HomeFragment into the fragment container.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment())
                .commit()
        }
    }
}