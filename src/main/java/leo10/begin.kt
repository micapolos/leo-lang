package leo10

import leo.base.failIfOr

data class StringBegin(val string: String)

fun begin(string: String) = failIfOr(string.isEmpty()) { StringBegin(string) }