package com.ohyeah5566.fileexplore

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { FileExploreViewModel() }
}