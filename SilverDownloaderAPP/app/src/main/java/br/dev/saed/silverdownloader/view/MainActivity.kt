package br.dev.saed.silverdownloader.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import br.dev.saed.silverdownloader.R
import br.dev.saed.silverdownloader.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding) {
            buttonDownload.setOnClickListener {
                supportFragmentManager.commit {
                    replace<DownloadFragment>(R.id.fragmentContainerView)
                }
            }
            buttonFiles.setOnClickListener {
                supportFragmentManager.commit {
                    replace<FilesFragment>(R.id.fragmentContainerView)
                }
            }
        }
    }


}