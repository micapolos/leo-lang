package leo13.js

data class Native(val string: String)

fun native(string: String) = Native(string)
val Native.code get() = string
