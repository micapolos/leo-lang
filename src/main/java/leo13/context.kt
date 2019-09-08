package leo13

import leo.base.Empty
import leo.base.empty

data class Context(val empty: Empty)

fun context() = Context(empty)
