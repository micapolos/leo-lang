package leo13

data class Context(
	val scope: Scope,
	val bindings: TypedExprBindings)

fun context() = Context(scope(), typedExprBindings())

fun context(scope: Scope, bindings: TypedExprBindings) =
	Context(scope, bindings)
