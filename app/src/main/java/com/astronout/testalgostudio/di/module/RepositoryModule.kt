package com.astronout.testalgostudio.di.module

import com.astronout.testalgostudio.data.remote.repository.RemoteRepository
import org.koin.dsl.module

val repoModule = module {
    single {
        RemoteRepository(get())
    }
}