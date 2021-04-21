package com.ohyeah5566.fileexplore

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_DENIED
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.DividerItemDecoration
import com.ohyeah5566.fileexplore.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {
    private val viewModel by viewModel<FileExploreViewModel>()
    lateinit var binding: ActivityMainBinding

    //goto activity for request permission
    private val activityForResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                viewModel.loadFile(PathUtil.getRootPath())
            } else {
                finish()
            }
        }

    private val adapter = FileAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PERMISSION_DENIED) {
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                if (!granted) {
                    launchPermissionRequestActivity()
                } else {
                    viewModel.loadFile(PathUtil.getRootPath())
                }
            }.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        } else {
            viewModel.loadFile(PathUtil.getRootPath())
        }

        binding.recyclerView.adapter = adapter
        binding.recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayout.VERTICAL))

        adapter.onDirClick = { file ->
        }

        viewModel.fileList.observe(this) {
            adapter.updateFiles(it)
        }
    }

    private fun launchPermissionRequestActivity() {
        val intent = Intent(this, PermissionRequestActivity::class.java)
        intent.putExtra(
            PermissionRequestActivity.INTENT_KEY_PERMISSION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        activityForResultLauncher.launch(intent)
    }

    companion object {
        const val TAG = "MainActivity"
    }

}