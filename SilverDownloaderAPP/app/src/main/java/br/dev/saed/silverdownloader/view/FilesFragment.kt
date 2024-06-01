package br.dev.saed.silverdownloader.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.dev.saed.silverdownloader.databinding.FragmentFilesBinding

class FilesFragment : Fragment() {
    private val binding by lazy {
        FragmentFilesBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }
}