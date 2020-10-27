package leo20

import leo.base.notNullIf
import leo13.Stack
import leo13.fold
import leo13.get
import leo13.mapFirst
import leo13.push
import leo13.reverse
import leo13.stack
import leo14.Script
import leo14.fieldOrNull
import leo14.linkOrNull
import leo14.onlyLineOrNull

data class Scope(val bindingStack: Stack<Binding>)

val emptyScope = Scope(stack())
fun Scope.push(binding: Binding): Scope = Scope(bindingStack.push(binding))
fun Scope.push(param: Value): Scope = fold(param.lineStack.reverse) { push(it.binding) }
fun Scope.unsafeValueAt(index: Int): Value = (bindingStack.get(index)!! as ValueBinding).value

fun Scope.resolveOrNull(param: Value): Value? =
	bindingStack.mapFirst { resolveOrNull(param) }

fun Scope.resolve(param: Value): Value =
	resolveOrNull(param) ?: param

fun Scope.defineOrNull(script: Script): Scope? =
	script.linkOrNull?.let { link ->
		link.lhs.patternOrNull?.let { pattern ->
			link.line.fieldOrNull?.let { field ->
				when (field.string) {
					"gives" -> push(ValueBinding(pattern, value(field.rhs)))
					"does" -> push(FunctionBinding(pattern, parseFunction(field.rhs)))
					else -> null
				}
			}
		}
	}

fun Scope.functionValue(script: Script): Value =
	value(line(parseFunction(script)))

fun Scope.parseFunction(script: Script): Function =
	recursivelyFunctionOrNull(script) ?: function(body(script))

fun Scope.recursivelyFunctionOrNull(script: Script): Function? =
	script.onlyLineOrNull?.fieldOrNull?.let { field ->
		notNullIf(field.string == "recursively") {
			Function(this, body(field.rhs), isRecursive = true)
		}
	}

val Scope.pushPrelude
	get() = this
		.push(numberPlusBinding)
		.push(numberMinusBinding)
		.push(numberEqualsBinding)
