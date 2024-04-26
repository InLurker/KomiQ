package com.inlurker.komiq.model.translation.helper

import okhttp3.Request
import okhttp3.Response

class QueuedRequest(val request: Request, val callback: (Response?) -> Unit) {
    var isRunning: Boolean = false
}