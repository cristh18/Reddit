package com.tolodev.reddit.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.tolodev.reddit.databinding.FragmentSignInBinding
import com.tolodev.reddit.extensions.safeNavigate

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SignInFragment : Fragment(), View.OnClickListener {

    private var param1: String? = null
    private var param2: String? = null

    private var binding: FragmentSignInBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        initListeners()
        subscribe()
        return binding?.root
    }

    private fun initListeners() {
        binding?.buttonSignIn?.setOnClickListener(this)
    }

    private fun subscribe() {
        // with(loginViewModel) {
        //     hasSessionObserver().observe(this@LoginActivity, {})
        // }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding?.buttonSignIn?.id -> {
                // Toast.makeText(this@SignInFragment, "Click", Toast.LENGTH_LONG).show()
                // AuthorizationDialog(this@SignInFragment, this).show()

                NavHostFragment.findNavController(this)
                    .safeNavigate(
                        SignInFragmentDirections.actionSignInFragmentToRedditAuthenticationFragment()
                    )
            }
        }
    }

    // private fun redirectView() {
    //     startActivity(Intent(this, RedditActivity::class.java))
    // }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SignInFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}