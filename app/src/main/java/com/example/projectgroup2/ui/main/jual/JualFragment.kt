package com.example.projectgroup2.ui.main.jual

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.example.projectgroup2.R
import com.example.projectgroup2.databinding.FragmentHomeBinding
import com.example.projectgroup2.databinding.FragmentJualBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JualFragment : Fragment() {
    private var _binding: FragmentJualBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentJualBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}