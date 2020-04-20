package com.torresj.springbootadmin.exceptions

import java.lang.Exception

class StatusException(val unknownStatus: String) : Exception() {
    fun getMessageError(): String = "Status ${unknownStatus} is unknown"
}