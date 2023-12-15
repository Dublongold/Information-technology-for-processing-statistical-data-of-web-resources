package com.study.infotechforprocstatidataofwebres.models

interface DataReceivingState<T> {
    class None<T>: DataReceivingState<T>
    class Changed<T>(val previousContent: T?): DataReceivingState<T>
    class Loading<T> : DataReceivingState<T>

    class Empty<T>: DataReceivingState<T>

    class Finish<T>(val content: T) : DataReceivingState<T>
    class Error<T>(val errorMessage: String) : DataReceivingState<T>
}