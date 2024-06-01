package br.dev.saed.silverdownloader.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.dev.saed.silverdownloader.api.LinkAPI
import br.dev.saed.silverdownloader.api.RetrofitHelper
import br.dev.saed.silverdownloader.databinding.FragmentDownloadBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class DownloadFragment : Fragment() {
    private val binding by lazy {
        FragmentDownloadBinding.inflate(layoutInflater)
    }
    private val retrofit by lazy {
        RetrofitHelper.retrofit
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding.buttonDownloadFrag.setOnClickListener {
            if (binding.editCodigo.text.toString().isEmpty()) {
                binding.editCodigo.error = "Digite o código"
            } else if (binding.editCodigo.text.toString().toInt() in 10000..99999) {
                CoroutineScope(Dispatchers.IO).launch {
                    getDownloadLink(binding.editCodigo.text.toString().toInt())
                }
            } else {
                binding.editCodigo.error = "Código inválido"
            }
        }

        return binding.root
    }

    private suspend fun getDownloadLink(code: Int) {
        var result = ""
        var retorno: Response<br.dev.saed.silverdownloader.model.Url>? = null
        try {
            val linkAPI = retrofit.create(LinkAPI::class.java)
            retorno = linkAPI.getUrl(code)
            if (retorno != null) {
                if (retorno.isSuccessful) {
                    result = retorno.body()?.link.toString()
                    withContext(Dispatchers.Main) {
                        binding.editCodigo.setText("$result")
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}
