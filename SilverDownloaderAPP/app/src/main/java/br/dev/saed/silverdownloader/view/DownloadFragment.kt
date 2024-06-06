package br.dev.saed.silverdownloader.view

import android.Manifest
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.URLUtil
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import br.dev.saed.silverdownloader.api.LinkAPI
import br.dev.saed.silverdownloader.api.RetrofitHelper
import br.dev.saed.silverdownloader.databinding.FragmentDownloadBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
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

    private var downloadId: Long? = null

    private val onDownloadComplete: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (downloadId != null) {
                if (downloadId == id) {
                    Toast.makeText(requireContext(), "Download Finalizado", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private val requestPermissionLauncher by lazy {
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Log.i("Permission", "Granted")
            } else {
                Log.i("Permission", "Denied")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().registerReceiver(onDownloadComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        requestManagePermission()
        binding.buttonDownloadFrag.setOnClickListener {
            requestManagePermission()
            if (binding.editCodigo.text.toString().isEmpty()) {
                binding.editCodigo.error = "Digite o código"
            } else if (binding.editCodigo.text.toString().toInt() in 10000..99999) {
                CoroutineScope(Dispatchers.IO).launch {
                    val url = async {
                        getDownloadLink(binding.editCodigo.text.toString().toInt())
                    }
                    downloadFile(url.await().toString())
                }
            } else {
                binding.editCodigo.error = "Código inválido"
            }
        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().unregisterReceiver(onDownloadComplete)
    }

    private fun download(url: String) {

    }

    private suspend fun downloadFile(url: String) {
        Log.i("result", url)
        val title = URLUtil.guessFileName(url, null, null)
        Log.i("result", title)
        val request = DownloadManager
            .Request(Uri.parse(url))
            .setTitle(title)
            .setDescription("Baixando aplicativo...")
            .addRequestHeader("cookie", CookieManager.getInstance().getCookie(url))
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title)

        Log.i("result", request.toString())
        val downloadManager = context?.getSystemService(DownloadManager::class.java)
        downloadId = downloadManager?.enqueue(request)
        withContext(Dispatchers.Main) {
            Toast.makeText(requireContext(), "Iniciando Download", Toast.LENGTH_SHORT).show()
        }
    }


    private fun requestManagePermission() {
        if (isDirectToTV() && Build.VERSION.SDK_INT <= 32) {
            requestRuntimePermissions()
        } else if (isDirectToTV() && Build.VERSION.SDK_INT >= 33) {
            if (!Environment.isExternalStorageManager()) {
                AlertDialog.Builder(requireContext()).setTitle("Permissão necessária")
                    .setMessage("Precisa de acesso ao armazenamento interno").setPositiveButton(
                        "OK!"
                    ) { _, _ ->
                        try {
                            val uri = Uri.parse("package:${requireContext().packageName}")
                            val intent =
                                Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri)
                            startActivity(intent)
                        } catch (e: Exception) {
                            val intent = Intent()
                            intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                            startActivity(intent)
                        }
                    }.create().show()
            }
        } else if (Build.VERSION.SDK_INT >= 30) {
            if (!Environment.isExternalStorageManager()) {
                AlertDialog.Builder(requireContext()).setTitle("Permissão necessária")
                    .setMessage("Precisa de acesso ao armazenamento interno").setPositiveButton(
                        "OK!"
                    ) { _, _ ->
                        try {
                            val uri = Uri.parse("package:${requireContext().packageName}")
                            val intent =
                                Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION, uri)
                            startActivity(intent)
                        } catch (e: Exception) {
                            val intent = Intent()
                            intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                            startActivity(intent)
                        }
                    }.create().show()
            }
        } else {
            requestRuntimePermissions()
        }
    }

    private fun requestRuntimePermissions() {
        Log.i("Permission", "Cheguei aqui")
        when {
            ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                Log.i("Permission", "op1")
                // Permissão concedida
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) -> {
                AlertDialog.Builder(requireContext()).setTitle("Permissão necessária")
                    .setMessage("Precisa de acesso ao armazenamento interno").setPositiveButton(
                        "OK!"
                    ) { _, _ -> requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE) }
                    .create().show()
                //Solicitar Permissão de novo
                Log.i("Permission", "op2")
            }

            else -> {
                AlertDialog.Builder(requireContext()).setTitle("Permissão necessária")
                    .setMessage("Precisa de acesso ao armazenamento interno").setPositiveButton(
                        "OK!"
                    ) { _, _ -> requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE) }
                    .create().show()
                Log.i("Permission", "op3")
                //Solicitar Permissão
            }
        }
    }

    private fun isDirectToTV(): Boolean {
        return (requireContext().packageManager.hasSystemFeature(PackageManager.FEATURE_LEANBACK)
                || (requireContext().packageManager.hasSystemFeature(PackageManager.FEATURE_LEANBACK)))
    }

    private suspend fun getDownloadLink(code: Int): String? {
        val retorno: Response<br.dev.saed.silverdownloader.model.Url>?
        try {
            val linkAPI = retrofit.create(LinkAPI::class.java)
            retorno = linkAPI.getUrl(code)
            if (retorno.isSuccessful) {
                return retorno.body()?.link.toString()

                /*withContext(Dispatchers.Main) {
                    binding.editCodigo.setText(result)
                }*/
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }


}
