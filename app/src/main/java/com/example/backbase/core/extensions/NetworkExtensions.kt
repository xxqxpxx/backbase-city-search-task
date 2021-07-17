package com.example.backbase.core.extensions

import com.example.backbase.data.remote.exception.AppException
import com.example.backbase.data.remote.model.Resource

suspend fun <T> handleException(apiCall: suspend () -> T): Resource<T> {
    return try {
        Resource.success(apiCall.invoke())
    } catch (throwable: Throwable) {
        Resource.error(AppException(throwable))
    }
}