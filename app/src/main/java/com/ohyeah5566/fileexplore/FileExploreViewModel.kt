package com.ohyeah5566.fileexplore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.File

class FileExploreViewModel : ViewModel() {
    private val _fileList = MutableLiveData<Array<File>>()
    val fileList: LiveData<Array<File>>
        get() = _fileList

    fun loadFile(rootFile: File?) {
        val files = rootFile?.listFiles()
        files?.sortByDescending { it.isDirectory }
        _fileList.value = files!!
    }

    companion object {
        const val TAG = "FileExploreVM"
    }
}