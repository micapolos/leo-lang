package leo21.token.repl

import leo14.Fragment
import leo14.Token
import leo14.begin
import leo14.error
import leo14.fragment
import leo14.parent
import leo14.script
import leo15.dsl.*
import leo21.token.strings.valueKeyword

data class DebugNode(val parentProcessorNode: ProcessorNode)

fun DebugNode.plus(token: Token): Repl = error { view { only } }

val DebugNode.printFragment: Fragment
	get() =
		parentProcessorNode
			.printFragment
			.parent(begin("debug".valueKeyword))
			.fragment(script(parentProcessorNode.processor.reflectScriptLine))