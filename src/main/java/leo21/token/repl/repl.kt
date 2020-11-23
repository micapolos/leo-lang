package leo21.token.repl

import leo14.Fragment
import leo14.Reducer
import leo14.Token
import leo14.reducer
import leo14.stringCharReducer
import leo21.token.processor.emptyEvaluatorProcessor

sealed class Repl
data class ProcessorRepl(val processorNode: ProcessorNode) : Repl()
data class DebugRepl(val debugNode: DebugNode) : Repl()

val emptyRepl: Repl get() = ProcessorRepl(ProcessorNode(null, emptyEvaluatorProcessor))

fun Repl.plus(token: Token): Repl =
	when (this) {
		is ProcessorRepl -> processorNode.plus(token)
		is DebugRepl -> debugNode.plus(token)
	}

val Repl.printFragment: Fragment
	get() =
		when (this) {
			is ProcessorRepl -> processorNode.printFragment
			is DebugRepl -> debugNode.printFragment
		}

val Repl.reducer: Reducer<Repl, Token>
	get() =
		reducer { plus(it).reducer }

val Repl.stringCharReducer: Reducer<String, Char>
	get() =
		reducer.stringCharReducer { printFragment }
