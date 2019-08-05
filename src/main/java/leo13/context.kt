package leo13

data class Context(
	val types: Types,
	val scope: Scope,
	val bindings: TypedExprBindings)

fun context() = Context(types(), scope(), typedExprBindings())

fun context(types: Types, scope: Scope, bindings: TypedExprBindings) =
	Context(types, scope, bindings)

fun Context.plus(function: Function) =
	context(types, scope.plus(function), bindings)

fun Context.plus(typeEntry: TypeEntry) =
	context(types.plus(typeEntry), scope, bindings)

fun Context.bind(typedExpr: TypedExpr) =
	context(types, scope, bindings.push(typedExpr))

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
