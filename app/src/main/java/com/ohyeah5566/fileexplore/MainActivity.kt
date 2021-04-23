package com.ohyeah5566.fileexplore

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_DENIED
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable
import androidx.recyclerview.selection.*
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.ohyeah5566.fileexplore.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File


class MainActivity : AppCompatActivity() {
    private val viewModel by viewModel<FileExploreViewModel>()
    lateinit var binding: ActivityMainBinding

    //goto activity for request permission
    private val activityForResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                viewModel.nextFile(PathUtil.getRootPath())
            } else {
                finish()
            }
        }

    private val adapter = FileAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PERMISSION_DENIED) {
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                if (!granted) {
                    launchPermissionRequestActivity()
                } else {
                    viewModel.nextFile(PathUtil.getRootPath())
                }
            }.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        } else {
            viewModel.nextFile(PathUtil.getRootPath())
        }

        binding.recyclerView.adapter = adapter
        binding.recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayout.VERTICAL))

        val tracker = SelectionTracker.Builder(
            "fileSelection",
            binding.recyclerView,
            StableIdKeyProvider(binding.recyclerView),
            MyItemDetailsLookup(binding.recyclerView),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(
            SelectionPredicates.createSelectAnything()
        ).build()
        adapter.selectionTracker = tracker

        adapter.onDirClick = { file ->
            viewModel.nextFile(file)
        }

        viewModel.fileList.observe(this) {
            adapter.updateFiles(it)
        }
        viewModel.actionBarTitle.observe(this) { title ->
            supportActionBar?.title = title
        }
        viewModel.finishActivityEvent.observe(this) {
            it.getContentIfNotHandled()?.let {
                finish()
            }
        }
    }

    class MyItemDetailsLookup(private val recyclerView: RecyclerView) :
        ItemDetailsLookup<Long>() {
        override fun getItemDetails(event: MotionEvent): ItemDetails<Long>? {
            val view = recyclerView.findChildViewUnder(event.x, event.y)
            if (view != null) {
                return (recyclerView.getChildViewHolder(view) as FileAdapter.FileViewHolder)
                    .getItemDetails()
            }
            return null
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            viewModel.previousFile()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        viewModel.previousFile()
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