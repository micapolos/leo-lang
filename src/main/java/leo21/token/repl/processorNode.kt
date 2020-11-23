package leo21.token.repl

import leo14.BeginToken
import leo14.EndToken
import leo14.Fragment
import leo14.LiteralToken
import leo14.Token
import leo21.token.processor.Processor
import leo21.token.processor.plus
import leo21.token.processor.printFragment

data class ProcessorNode(
	val parentOrNull: ProcessorNode?,
	val processor: Processor
)

fun ProcessorNode.plus(token: Token): Repl =
	when (token) {
		is LiteralToken ->
			ProcessorRepl(ProcessorNode(parentOrNull, processor.plus(token)))
		is BeginToken ->
			when (token.begin.string) {
				"debug" -> DebugRepl(DebugNode(this))
				else -> ProcessorRepl(ProcessorNode(this, processor.plus(token)))
			}
		is EndToken ->
			ProcessorRepl(ProcessorNode(parentOrNull!!.parentOrNull, processor.plus(token)))
	}

val ProcessorNode.printFragment: Fragment get() = processor.printFragment