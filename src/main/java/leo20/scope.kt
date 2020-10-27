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
					"does" -> defineDoes(pattern, field.rhs)
					else -> null
				}
			}
		}
	}

fun Scope.defineDoes(pattern: Pattern, script: Script): Scope =
	script.recursivelyBodyOrNull
		?.let { recursivelyBody ->
			push(FunctionBinding(pattern, function(body(recursivelyBody)), isRecursive = true))
		}
		?: push(FunctionBinding(pattern, function(body(script)), isRecursive = false))

val Scope.pushPrelude
	get() = this
		.push(numberPlusBinding)
		.push(numberMinusBinding)
		.push(numberEqualsBinding)

val Script.recursivelyBodyOrNull: Script?
	get() =
		onlyLineOrNull?.fieldOrNull?.let { field ->
			notNullIf(field.string == "recursively") {
				field.rhs
			}
		}