package leo20

import leo.base.notNullIf
import leo.base.runIf
import leo13.Stack
import leo13.fold
import leo13.mapFirst
import leo13.push
import leo13.reverse
import leo13.stack
import leo14.Script
import leo14.fieldOrNull
import leo14.linkOrNull
import leo14.onlyLineOrNull

data class Scope(val bindingStack: Stack<Binding>)

data class Binding(val pattern: Pattern, val value: Value, val isFunction: Boolean)

val Line.binding get() = Binding(pattern(selectName lineTo pattern()), value(this), false)

val emptyScope = Scope(stack())
fun Scope.push(binding: Binding): Scope = Scope(bindingStack.push(binding))
fun Scope.push(param: Value): Scope = fold(param.lineStack.reverse) { push(it.binding) }

fun Scope.resolveOrNull(param: Value): Value? =
	bindingStack.mapFirst { resolveOrNull(param) }

fun Scope.resolve(param: Value): Value =
	resolveOrNull(param) ?: param

fun Binding.resolveOrNull(param: Value): Value? =
	notNullIf(param.matches(pattern)) {
		value.runIf(isFunction) { apply(param) }
	}

fun Scope.defineOrNull(script: Script): Scope? =
	script.linkOrNull?.let { link ->
		link.lhs.patternOrNull?.let { pattern ->
			link.line.fieldOrNull?.let { field ->
				when (field.string) {
					"gives" -> push(Binding(pattern, value(field.rhs), false))
					"does" -> push(Binding(pattern, functionValue(field.rhs), true))
					else -> null
				}
			}
		}
	}

fun Scope.functionValue(script: Script): Value =
	value(line(parseFunction(script)))

fun Scope.parseFunction(script: Script): Function =
	recursiveFunctionOrNull(script) ?: function(script)

fun Scope.recursiveFunctionOrNull(script: Script): Function? =
	script.onlyLineOrNull?.fieldOrNull?.let { field ->
		notNullIf(field.string == "recursive") {
			function(field.rhs)
		}
	}