package leo14.untyped

data class Moduled(val module: Module, val thunk: Thunk)

fun Module.with(thunk: Thunk) = Moduled(this, thunk)
