package com.ohyeah5566.fileexplore

import android.os.Environment
import java.io.File

object PathUtil {
    fun getRootPath(): File = Environment.getExternalStorageDirectory()
}