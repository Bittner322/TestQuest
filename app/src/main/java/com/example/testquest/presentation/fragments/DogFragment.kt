package com.example.testquest.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import coil.load
import com.example.testquest.databinding.FragmentDogBinding

private const val DOG_URL = "dogUrl"

class DogFragment : Fragment() {

    private var _binding: FragmentDogBinding? = null
    private val binding: FragmentDogBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDogBinding.inflate(layoutInflater)
        val rootView = binding.root

        binding.fullscreenDogImageView.load(arguments?.getString(DOG_URL))

        binding.fullscreenDogImageView.setOnClickListener {
            parentFragmentManager
                .beginTransaction()
                .remove(this)
                .commit()
        }

        return rootView
    }

}