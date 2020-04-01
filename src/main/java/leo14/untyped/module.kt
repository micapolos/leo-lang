package leo14.untyped

data class Module(val localScope: Scope, val publicScope: Scope)

val Scope.emptyModule get() = Module(this, scope())

fun Module.push(definition: Definition): Module =
	Module(localScope.push(definition), publicScope.push(definition))

fun Module.import(definition: Definition): Module =
	Module(localScope.push(definition), publicScope)

fun Module.resolve(thunk: Thunk): Module? =
	thunk.parseDefinition?.let(::push)
