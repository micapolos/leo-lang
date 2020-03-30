package leo14.untyped

data class Break(val scope: Scope)

fun break_(scope: Scope) = Break(scope)