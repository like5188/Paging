package com.like.paging

/**
 * 请求类型
 */
sealed class RequestType {
    object Initial : RequestType() {
        override fun toString(): String {
            return "Initial"
        }
    }

    object Refresh : RequestType() {
        override fun toString(): String {
            return "Refresh"
        }
    }

    object After : RequestType() {
        override fun toString(): String {
            return "After"
        }
    }

    object Before : RequestType() {
        override fun toString(): String {
            return "Before"
        }
    }
}