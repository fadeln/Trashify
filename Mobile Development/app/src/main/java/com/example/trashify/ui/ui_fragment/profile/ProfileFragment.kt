package com.example.trashify.ui.ui_fragment.profile

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.trashify.R
import com.example.trashify.databinding.FragmentProfileBinding
import com.example.trashify.model.AuthViewModel
import com.example.trashify.ui.WelcomeActivity

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding


    private lateinit var tokenViewModel: AuthViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(LayoutInflater.from(requireActivity()))
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Change Langguage
        val language = binding?.root?.findViewById<Button>(R.id.botton_language)

        language?.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }

        // logout
        binding?.tvLogout?.setOnClickListener {
            startActivity(Intent(requireContext(), WelcomeActivity::class.java))
        }


    }

}