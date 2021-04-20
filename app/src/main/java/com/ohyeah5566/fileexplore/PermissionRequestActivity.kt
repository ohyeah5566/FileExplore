package com.ohyeah5566.fileexplore

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.ohyeah5566.fileexplore.databinding.ActivityFilePermissionRequestBinding

class PermissionRequestActivity : AppCompatActivity() {
    lateinit var binding: ActivityFilePermissionRequestBinding
    lateinit var filePermissionLauncher: ActivityResultLauncher<String>
    lateinit var appInfoLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityFilePermissionRequestBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        checkPermissionAndSetTextAndAction()

        filePermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                if (granted) {
                    permissionGranted()
                } else {
                    checkPermissionAndSetTextAndAction();
                }
            }

        appInfoLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PERMISSION_GRANTED) {
                    permissionGranted()
                } else {
                    checkPermissionAndSetTextAndAction()
                }
            }
    }

    private fun checkPermissionAndSetTextAndAction() {
        intent.getStringExtra(INTENT_KEY_PERMISSION)?.let { permission ->
            if (shouldShowRequestPermissionRationale(permission)) {
                binding.requestPermission.setText(R.string.request_file_permission_button)
                binding.requestPermission.setOnClickListener {
                    filePermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            } else {
                binding.requestPermission.setText(R.string.goto_file_permission_setting)
                binding.requestPermission.setOnClickListener {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    intent.setData(Uri.parse("package:$packageName"))
                    appInfoLauncher.launch(intent)
                }
            }
        }
    }

    private fun permissionGranted(){
        setResult(Activity.RESULT_OK)
        finish()
    }

    companion object {
        const val INTENT_KEY_PERMISSION = "permission"
    }
}