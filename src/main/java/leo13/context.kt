package leo13

data class Context(
	val typeFunctions: TypeFunctions,
	val scope: Scope,
	val bindings: TypedExprBindings)

fun context() = Context(types(), scope(), typedExprBindings())

fun context(typeFunctions: TypeFunctions, scope: Scope, bindings: TypedExprBindings) =
	Context(typeFunctions, scope, bindings)

fun Context.plus(function: Function) =
	context(typeFunctions, scope.plus(function), bindings)

fun Context.plus(typeFunction: TypeFunction) =
	context(typeFunctions.plus(typeFunction), scope, bindings)

fun Context.bind(typedExpr: TypedExpr) =
	context(typeFunctions, scope, bindings.push(typedExpr))

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
