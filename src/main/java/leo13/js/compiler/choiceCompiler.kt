package leo13.js.compiler

import leo13.Stack
import leo13.mapFirst
import leo13.stack

data class ChoiceCompiler(
	val choices: Stack<CompilerChoice>) : Compiler {
	override fun write(token: Token): Compiler =
		(token as BeginToken).let { token ->
			choices.mapFirst {
				compile(token.begin.string)
			}!!
		}
}

fun compiler(choice: CompilerChoice, vararg choices: CompilerChoice) =
	ChoiceCompiler(stack(choice, *choices))
