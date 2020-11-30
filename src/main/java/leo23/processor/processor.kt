package leo23.processor

import leo14.Fragment
import leo14.Reducer
import leo14.Token
import leo14.reducer
import leo14.stringCharReducer

sealed class Processor
data class CompilerProcessor(val node: CompilerNode) : Processor()
data class EvaluatorProcessor(val node: EvaluatorNode) : Processor()

val emptyProcessor: Processor get() = CompilerProcessor(emptyCompilerNode)

fun Processor.plus(token: Token): Processor =
	when (this) {
		is CompilerProcessor -> node.plus(token)
		is EvaluatorProcessor -> node.plus(token)
	}

val Processor.printFragment: Fragment
	get() =
		when (this) {
			is CompilerProcessor -> node.printFragment
			is EvaluatorProcessor -> node.printFragment
		}

val Processor.reducer: Reducer<Processor, Token>
	get() =
		reducer { plus(it).reducer }

val Processor.stringCharReducer: Reducer<String, Char>
	get() =
		reducer.stringCharReducer { printFragment }
