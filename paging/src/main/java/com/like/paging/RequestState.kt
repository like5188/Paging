package com.like.paging

/**
 * 请求状态
 */
sealed class RequestState<out ResultType> {

    object Running : RequestState<Nothing>() {
        override fun toString(): String {
            return "Running"
        }
    }

    data class Success<T>(val data: T) : RequestState<T>() {
        override fun toString(): String {
            return "Success[$data]"
        }
    }

    data class Failed(val throwable: Throwable) : RequestState<Nothing>() {
        override fun toString(): String {
            return "Failed[$throwable]"
        }
    }
}