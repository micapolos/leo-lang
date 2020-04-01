package leo14.untyped

data class Local(val scope: Scope)
data class Public(val scope: Scope)
data class Module(val local: Local, val public: Public)

fun local(scope: Scope) = Local(scope)
fun public(scope: Scope) = Public(scope)
fun module(local: Local, public: Public) = Module(local, public)
val Scope.emptyModule get() = module(local(this), public(scope()))
