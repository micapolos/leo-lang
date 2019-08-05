package leo13

data class Context(
	val scope: Scope,
	val bindings: TypedExprBindings)

fun context() = Context(scope(), typedExprBindings())

fun context(scope: Scope, bindings: TypedExprBindings) =
	Context(scope, bindings)

fun Context.bind(typedExpr: TypedExpr) =
	context(scope, bindings.push(typedExpr))

fun Context.plus(function: Function) =
	context(scope.plus(function), bindings)

fun Context.typedExpr(script: Script): TypedExpr =
	interpreter(this, typedExpr()).push(script).typedExpr

fun Context.typedExpr(link: TypedExprLink): TypedExpr =
	null
		?: argumentTypedExprOrNull(link)
		?: link.accessTypedExprOrNull
		?: link.typedExpr

fun Context.argumentTypedExprOrNull(link: TypedExprLink): TypedExpr? =
	link.argumentOrNull?.let { argument ->
		bindings.typedExprOrNull(argument)
	}
