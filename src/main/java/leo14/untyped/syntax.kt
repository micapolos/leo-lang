package leo14.untyped

import leo13.EmptyStack
import leo13.LinkStack
import leo13.Stack
import leo13.push

sealed class BeginSyntax
object SpaceBeginSyntax : BeginSyntax()
object IndentBeginSyntax : BeginSyntax()

sealed class LineSyntax
object DotLineSyntax : LineSyntax()
object NewLineSyntax : LineSyntax()

data class IndentSyntax(
	val beginStack: Stack<BeginSyntax>,
	val isEmpty: Boolean)

val IndentSyntax.plusSpace: IndentSyntax?
	get() =
		when (beginStack) {
			is EmptyStack -> null
			is LinkStack ->
				when (beginStack.link.value) {
					SpaceBeginSyntax ->
						if (isEmpty) IndentSyntax(beginStack.link.stack.push(IndentBeginSyntax), true)
						else null
					IndentBeginSyntax -> null
				}
		}

val Pair<Int, IndentSyntax>.plusNewline: Pair<Int, IndentSyntax>?
	get() =
		second.beginStack.let { beginStack ->
			when (beginStack) {
				is EmptyStack -> this
				is LinkStack ->
					when (beginStack.link.value) {
						SpaceBeginSyntax ->
							if (second.isEmpty) (first.inc() to IndentSyntax(beginStack.link.stack, false)).plusNewline
							else null
						IndentBeginSyntax -> first.inc() to IndentSyntax(beginStack.link.stack, false)
					}
			}
		}
