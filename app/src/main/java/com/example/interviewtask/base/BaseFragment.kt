package com.example.interviewtask.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.interviewtask.utils.UiActivity
import timber.log.Timber


abstract class BaseFragment<T>(layoutRes: Int) : Fragment(layoutRes) {

    protected var _binding: T? = null
    protected val binding: T get() = _binding!!
    protected lateinit var ui: UiActivity

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindView(view)
    }

    abstract fun bindView(view: View)

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            ui = context as UiActivity
        } catch (e: ClassCastException) {
            Timber.i("$context must implement UiActivity")
        }
    }

    protected fun navigate(id: Int, bundle: Bundle? = null) {
        findNavController().navigate(id, bundle)
    }
}