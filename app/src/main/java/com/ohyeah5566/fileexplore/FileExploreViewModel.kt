package com.ohyeah5566.fileexplore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.File
import java.util.*

class FileExploreViewModel : ViewModel() {
    private val _fileList = MutableLiveData<Array<File>>()
    val fileList: LiveData<Array<File>>
        get() = _fileList

    private val _finishActivityEvent = MutableLiveData<Event<Boolean>>()
    val finishActivityEvent: LiveData<Event<Boolean>>
        get() = _finishActivityEvent

    private val _actionBarTitle = MutableLiveData<String>()
    val actionBarTitle: LiveData<String>
        get() = _actionBarTitle

    private val fileStack = Stack<File>()

    fun loadFile(rootFile: File?) {
        val files = rootFile?.listFiles()
        files?.sortByDescending { it.isDirectory }
        _fileList.value = files!!
        if (fileStack.size > 1)
            _actionBarTitle.value = rootFile.name
        else
            _actionBarTitle.value = "內部儲存空間"
    }

    fun nextFile(file: File) {
        fileStack.push(file)
        loadFile(file)
    }

    fun previousFile() {
        fileStack.pop()
        if (fileStack.isNotEmpty()) {
            loadFile(fileStack.peek())
        } else {
            _finishActivityEvent.value = Event(true)
        }
    }

    companion object {
        const val TAG = "FileExploreVM"
    }
}