package com.inlurker.komiq.viewmodel.paging

enum class ListState {
    IDLE,
    LOADING,
    PAGINATION_EXHAUST,
    ERROR
}