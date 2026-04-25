/**
 * Course: MAD302-01 Android Development
 * Assignment: Assignment 3
 * Student Name: Khushi Patel A00198843
 * Date: 04/24/2026
 * Description: Home screen. Fetches simulated data asynchronously using
 *              coroutines and displays a loading indicator while waiting.
 */

package com.example.mad302_assignment03_khushipatel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.mad302_assignment03_khushipatel.databinding.FragmentHomeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Displays simulated API data fetched with coroutines.
 * Navigates to LocationFragment when the user taps the location button.
 */
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnFetchData.setOnClickListener {
            fetchData()
        }

        binding.btnGoToLocation.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, LocationFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    /**
     * Launches a coroutine that simulates a network request.
     * Shows a loading spinner during the operation and handles errors gracefully.
     */
    private fun fetchData() {
        val input = binding.etUserInput.text.toString().trim()

        if (input.isEmpty()) {
            binding.tvResult.text = getString(R.string.error_empty_input)
            return
        }

        if (!isInputSafe(input)) {
            binding.tvResult.text = getString(R.string.error_invalid_input)
            return
        }

        binding.progressBar.visibility = View.VISIBLE
        binding.tvResult.text = ""
        binding.btnFetchData.isEnabled = false

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    simulateApiCall(input)
                }
                binding.tvResult.text = result
            } catch (e: Exception) {
                binding.tvResult.text = getString(R.string.error_network)
            } finally {
                binding.progressBar.visibility = View.GONE
                binding.btnFetchData.isEnabled = true
            }
        }
    }

    /**
     * Simulates an API call with a 2-second delay.
     *
     * @param query The sanitized user query.
     * @return A result string.
     */
    private suspend fun simulateApiCall(query: String): String {
        delay(2000)
        return "Result for: $query\nStatus: OK\nTimestamp: ${System.currentTimeMillis()}"
    }

    /**
     * Validates that the input does not contain special characters.
     *
     * @param input Raw user input string.
     * @return True if input is safe, false otherwise.
     */
    private fun isInputSafe(input: String): Boolean {
        val pattern = Regex("^[a-zA-Z0-9 .,!?'-]+$")
        return pattern.matches(input)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}