package com.ohyeah5566.fileexplore

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_DENIED
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.ohyeah5566.fileexplore.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var activityForResultLauncher: ActivityResultLauncher<Intent>

    companion object {
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        activityForResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    //TODO load file data
                } else {
                    finish()
                }
            }

       if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PERMISSION_DENIED) {
           registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
               if (!granted) {
                   launchPermissionRequestActivity()
               } else {
                   //TODO load file data
               }
           }.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
       }
    }

    private fun launchPermissionRequestActivity() {
        val intent = Intent(this, PermissionRequestActivity::class.java)
        intent.putExtra("permission", Manifest.permission.WRITE_EXTERNAL_STORAGE)
        activityForResultLauncher.launch(intent)
    }
}