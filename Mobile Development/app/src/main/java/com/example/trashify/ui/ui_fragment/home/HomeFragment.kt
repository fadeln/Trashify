package com.example.trashify.ui.ui_fragment.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.trashify.databinding.FragmentHomeBinding
import com.example.trashify.ui.ClasificationActivity
import com.example.trashify.ui.ui_post.PostActivity
import com.example.trashify.utils.PrefsManager

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding

    private lateinit var prefsManager: PrefsManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(LayoutInflater.from(requireActivity()))
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefsManager = PrefsManager(requireContext())

        // Intent to Scan
        binding?.imageButton?.setOnClickListener {
            startActivity(Intent(requireContext(), ClasificationActivity::class.java))
        }

        // Intent to More Post
        binding?.tvMorePost?.setOnClickListener {
            startActivity(Intent(requireContext(), PostActivity::class.java))
        }
    }

}