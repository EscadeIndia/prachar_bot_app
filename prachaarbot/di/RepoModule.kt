package com.prachaarbot.di

import com.prachaarbot.data.impl.LoginRepoImpl
import com.prachaarbot.data.impl.SentMessageRepoImpl
import com.prachaarbot.domain.repo.LoginRepository
import com.prachaarbot.domain.repo.SentMessageRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface RepoModule {
    @Binds
    fun bindsLoginRepo(loginRepoImpl: LoginRepoImpl): LoginRepository

    @Binds
    fun bindsSentMessageRepo(sentMessageRepoImpl: SentMessageRepoImpl): SentMessageRepository
}