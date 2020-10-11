package leo19.compiler

import leo13.Stack
import leo13.push
import leo13.stack
import leo14.Script
import leo19.type.Arrow
import leo19.typed.Typed
import leo19.typed.nullTyped

data class Scope(val arrowStack: Stack<Arrow>)

val emptyScope = Scope(stack())
fun scope(vararg arrows: Arrow) = Scope(stack(*arrows))
fun Scope.plus(arrow: Arrow) = Scope(arrowStack.push(arrow))

fun Scope.compile(script: Script): Typed =
	Compiler(this, nullTyped).plus(script).typed