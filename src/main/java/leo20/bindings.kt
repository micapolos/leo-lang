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
import leo14.lineTo
import leo14.linkOrNull
import leo14.onlyLineOrNull
import leo14.plus

data class Bindings(val bindingStack: Stack<Binding>)

val emptyScope = Bindings(stack())
fun Bindings.push(binding: Binding): Bindings = Bindings(bindingStack.push(binding))
fun Bindings.push(param: Value): Bindings = fold(param.lineStack.reverse) { push(it.binding) }
fun Bindings.unsafeValueAt(index: Int): Value = (bindingStack.get(index)!! as ValueBinding).value

fun Bindings.resolveOrNull(param: Value): Value? =
	bindingStack.mapFirst { resolveOrNull(param) }

fun Bindings.resolve(param: Value): Value =
	resolveOrNull(param) ?: param.resolve

fun Bindings.defineOrNull(script: Script): Bindings? =
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

fun Bindings.defineDoes(pattern: Pattern, script: Script): Bindings =
	script.recursivelyBodyOrNull
		?.let { recursivelyBody ->
			push(FunctionBinding(pattern, function(body(recursivelyBody)), isRecursive = true))
		}
		?: push(FunctionBinding(pattern, function(body(script)), isRecursive = false))

val Bindings.pushPrelude
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

fun Bindings.test(script: Script) {
	val link = script.linkOrNull ?: error("syntax" lineTo script)
	val field = link.line.fieldOrNull ?: error("syntax" lineTo script)
	if (field.string != "equals") error("syntax" lineTo script)
	val lhsValue = value(link.lhs)
	val rhsValue = value(field.rhs)
	if (lhsValue != rhsValue)
		error(
			"test" lineTo script,
			"result" lineTo lhsValue.script.plus("equals" lineTo rhsValue.script))
}