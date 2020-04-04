package leo14.untyped.typed

import leo14.untyped.Thunk

data class Compiled(val type: Thunk, val erase: () -> Any?)