package com.tolodev.reddit.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.tolodev.reddit.databinding.FragmentSignInBinding
import com.tolodev.reddit.extensions.safeNavigate

class SignInFragment : Fragment(), View.OnClickListener {

    private var binding: FragmentSignInBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        initListeners()
        return binding?.root
    }

    private fun initListeners() {
        binding?.buttonSignIn?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding?.buttonSignIn?.id -> {
                NavHostFragment.findNavController(this)
                    .safeNavigate(
                        SignInFragmentDirections.actionSignInFragmentToRedditAuthenticationFragment()
                    )
            }
        }
    }
}
