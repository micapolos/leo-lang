package leo13

data class Context(
	val typeFunctions: TypeFunctions,
	val functions: Functions,
	val bindings: TypedExprBindings)

fun context() = Context(types(), functions(), typedExprBindings())

fun context(typeFunctions: TypeFunctions, functions: Functions, bindings: TypedExprBindings) =
	Context(typeFunctions, functions, bindings)

fun Context.plus(function: Function) =
	context(typeFunctions, functions.plus(function), bindings)

fun Context.plus(typeFunction: TypeFunction) =
	context(typeFunctions.plus(typeFunction), functions, bindings)

fun Context.bind(typedExpr: TypedExpr) =
	context(typeFunctions, functions, bindings.push(typedExpr))

fun Context.typedExpr(script: Script): TypedExpr =
	compiler(this, typedExpr()).push(script).typedExpr

fun Context.typedExpr(link: TypedExprLink): TypedExpr =
	null
		?: argumentTypedExprOrNull(link)
		?: link.accessTypedExprOrNull
		?: link.typedExpr

fun Context.argumentTypedExprOrNull(link: TypedExprLink): TypedExpr? =
	link.argumentOrNull?.let { argument ->
		bindings.typedExprOrNull(argument)
	}

fun Context.functionOrNull(arrow: ScriptArrow) =
	function(
		parameter(arrow.lhs.type),
		arrow.rhs.typedExpr(this))
