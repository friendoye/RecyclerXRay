package com.friendoye.recyclerxray.sample.watcher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.friendoye.recyclerxray.sample.databinding.FragmentSamplesBinding
import kotlin.properties.Delegates

class SamplesFragment : Fragment() {

    companion object {
        const val ARGS_SAMPLES_TAG = "SAMPLES_TAG"
        fun newInstance(samplesTag: String) = SamplesFragment().apply {
            arguments = Bundle().apply {
                putString(ARGS_SAMPLES_TAG, samplesTag)
            }
        }
    }

    private var bindings by Delegates.notNull<FragmentSamplesBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindings = FragmentSamplesBinding.inflate(inflater, container, false)
        return bindings.root
    }

}