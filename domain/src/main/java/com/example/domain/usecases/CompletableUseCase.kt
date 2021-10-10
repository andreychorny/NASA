package com.example.domain.usecases

import io.reactivex.Completable

interface CompletableUseCase<T> {
    fun execute(input: T): Completable
}