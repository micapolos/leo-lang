package leo13.untyped.compiler

import leo.base.notNullIf
import leo13.ObjectScripting
import leo13.script
import leo13.script.emptyIfEmpty
import leo13.script.lineTo
import leo13.scripting
import leo13.untyped.expression.expression
import leo13.untyped.expression.op
import leo13.untyped.expression.plus
import leo13.untyped.pattern.contains
import leo13.untyped.value.item
import leo13.untyped.value.value
import leo9.Stack
import leo9.mapFirst
import leo9.push
import leo9.stack

data class Functions(val functionStack: Stack<FunctionCompiled>) : ObjectScripting() {
	override fun toString() = super.toString()
	override val scriptingLine get() = "functions" lineTo functionStack.scripting.script.emptyIfEmpty
}

fun functions() = Functions(stack())
fun Functions.plus(function: FunctionCompiled) = Functions(functionStack.push(function))

fun Functions.resolve(compiled: Compiled): Compiled =
	functionStack
		.mapFirst {
			notNullIf(arrow.lhs.contains(compiled.pattern)) {
				compiled(
					expression(value(item(function)).op).plus(leo13.untyped.expression.apply(compiled.expression).op),
					arrow.rhs)
			}
		}
		?: compiled