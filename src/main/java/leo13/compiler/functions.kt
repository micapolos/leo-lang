package leo13.compiler

import leo.base.notNullIf
import leo13.*
import leo13.expression.expression
import leo13.expression.op
import leo13.expression.plus
import leo13.pattern.contains
import leo13.script.emptyIfEmpty
import leo13.script.lineTo
import leo13.value.item
import leo13.value.value

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
					expression(value(item(function)).op).plus(leo13.expression.apply(compiled.expression).op),
					arrow.rhs)
			}
		}
		?: compiled