package leo14.dispatching

import leo14.untyped.Thunk

data class Compiled(val type: Thunk, val erase: () -> Any?)