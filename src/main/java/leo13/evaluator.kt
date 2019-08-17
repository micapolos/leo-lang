package leo13

import leo.base.ifOrNull
import leo.base.orNull
import leo9.fold
import leo9.reverse

data class Evaluator(
	val context: Context,
	val bindings: TypedValueBindings,
	val typedValue: TypedValue)

fun evaluator() = Evaluator(context(), typedValueBindings(), typedValue())

fun Evaluator.push(script: Script): Evaluator? =
	orNull.fold(script.lineStack.reverse) { this?.push(it) }

fun Evaluator.pushBinding(typedValue: TypedValue) =
	copy(bindings = bindings.plus(typedValue))

fun Evaluator.pushType(type: Type) =
	copy(context = context.plus(type))

fun Evaluator.push(line: ScriptLine): Evaluator? =
	if (line.rhs.type.isEmpty) begin.pushNormalized(line.name lineTo typedValue.script)
	else pushNormalized(line)

fun Evaluator.pushNormalized(line: ScriptLine): Evaluator? =
	when (line.name) {
		"given" -> pushArgument(line.rhs)
		"gives" -> pushGives(line.rhs)
		"switch" -> pushSwitch(line.rhs)
		else -> pushTyped(line)
	}

fun Evaluator.pushArgument(rhs: Script): Evaluator? =
	ifOrNull(typedValue.type.isEmpty) {
		rhs.rhsArgumentOrNull?.let { argument ->
			bindings.typedValueOrNull(argument)?.let {
				copy(typedValue = it)
			}
		}
	}

fun Evaluator.pushGives(rhs: Script): Evaluator? =
	context.functionOrNull(typedValue.script.arrowTo(rhs)).let { function ->
		copy(context = context.plus(function))
	}

fun Evaluator.pushSwitch(rhs: Script): Evaluator? =
	compiler(context, typedValue.typedExpr)
		.typedSwitchOrNull(rhs)?.let { typedSwitch ->
			typedValue.typedExpr.expr.typedExpr(typedSwitch).let { typedExpr ->
				typedExpr.expr.eval(bindings.valueBindings.push(typedValue.value)).let { value ->
					copy(typedValue = value of typedExpr.type)
				}
			}
		}

val Evaluator.begin
	get() =
		copy(typedValue = typedValue())

fun Evaluator.pushTyped(line: ScriptLine): Evaluator? =
	begin
		.push(line.rhs)
		?.let { rhsEvaluator ->
			push(line.name lineTo rhsEvaluator.typedValue)
		}

fun Evaluator.push(line: TypedValueLine): Evaluator? =
	typedValue.value.plus(line.valueLine).let { value ->
		typedValue.type.plus(line.typeLine).let { type ->
			context.types.containingType(type).let { containingType ->
				copy(typedValue = value of containingType)
			}
		}
	}

val Evaluator.typedScript get() = typedValue.typedScript