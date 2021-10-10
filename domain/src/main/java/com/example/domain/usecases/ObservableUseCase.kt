package com.example.domain.usecases

import io.reactivex.Observable

interface ObservableUseCase<R, T> {
    fun execute(input: T): Observable<R>
}