package leo13.script

import leo.base.ifOrNull
import leo.base.notNullIf
import leo13.*
import leo9.fold
import leo9.reverse

data class Compiler(
	val types: Types,
	val functions: Functions,
	val typeBindings: TypeBindings,
	val typedExpr: TypedExpr)

fun compiler() = Compiler(types(), functions(), typeBindings(), expr() of type())

fun Compiler.bind(type: Type) = copy(typeBindings = typeBindings.push(type))

fun Compiler.plus(type: Type) = copy(types = types.plus(type))

fun Compiler.plus(function: Function) = copy(functions = functions.plus(function))

fun Compiler.with(typedExpr: TypedExpr) = copy(typedExpr = typedExpr)

val Script.compile
	get() =
		compiler().push(this).typedExpr.expr.eval(leo13.script.evaluator.bindings())

fun Compiler.push(script: Script): Compiler =
	fold(script.lineStack.reverse, Compiler::push)

fun Compiler.push(line: ScriptLine): Compiler =
	null
		?: pushExistsOrNull(line)
		?: pushGivesOrNull(line)
		?: pushArgumentOrNull(line)
		?: pushAccessOrNull(line)
		?: pushResolution(line)

fun Compiler.pushArgumentOrNull(line: ScriptLine): Compiler? =
	ifOrNull(typedExpr.expr.isEmpty) {
		line.argumentOrNull?.let { argument ->
			copy(typedExpr = expr(op(argument)) of typeBindings.typeOrError(argument))
		}
	}

fun Compiler.pushExistsOrNull(line: ScriptLine): Compiler? =
	notNullIf(line == "exists" lineTo script()) {
		copy(
			types = types.plus(typedExpr.type.scriptOrError.type),
			typedExpr = expr() of type())
	}

fun Compiler.pushGivesOrNull(line: ScriptLine): Compiler? =
	notNullIf(line.name == "gives") {
		typedExpr.type.scriptOrError.type.let { parameterType ->
			copy(
				functions = functions.plus(
					function(
						typedExpr.type.scriptOrError.type,
						copy(
							typeBindings = typeBindings.push(parameterType),
							typedExpr = expr() of type()).push(line.rhs).typedExpr)),
				typedExpr = expr() of type())
		}
	}

fun Compiler.pushAccessOrNull(line: ScriptLine): Compiler? =
	ifOrNull(line.rhs.isEmpty) {
		typedExpr.accessOrNull(line.name)?.let { access ->
			copy(typedExpr = access)
		}
	}

fun Compiler.pushResolution(line: ScriptLine): Compiler =
	push(line.name lineTo copy(typedExpr = expr() of type()).push(line.rhs).typedExpr)

fun Compiler.push(line: TypedExprLine): Compiler =
	types
		.cast(typedExpr.plus(line))
		.let { castTypedExpr ->
			null
				?: pushFunctionCallOrNull(castTypedExpr)
				?: with(castTypedExpr)
		}

fun Compiler.pushFunctionCallOrNull(typedExpr: TypedExpr): Compiler? =
	functions.typedExprOrNull(typedExpr.type)?.let { functionTypedExpr ->
		copy(typedExpr = typedExpr.expr.plus(op(call(functionTypedExpr.expr))) of functionTypedExpr.type)
	}

