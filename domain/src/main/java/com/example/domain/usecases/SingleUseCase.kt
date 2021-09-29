package com.example.domain.usecases

import io.reactivex.Single

interface SingleUseCase<R, T> {
    fun execute(input: T): Single<R>
}