package com.like.paging

/**
 * 请求类型
 * [Initial]和[Refresh]的区别就是：[Initial]失败后，需要显示错误视图；而[Refresh]失败后则不需要显示错误视图，需要保留原来的数据不变。
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