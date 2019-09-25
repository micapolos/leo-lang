package leo13.compiler

import leo13.*
import leo13.type.*
import leo13.script.*

data class NameTrace(val stack: Stack<String>) : ObjectScripting() {
	override fun toString() = super.toString()

	override val scriptingLine: ScriptLine
		get() = traceName lineTo stack.map { scriptLine }.script.emptyIfEmpty

	fun plus(name: String) = copy(stack = stack.push(name))

	fun recurseOrNull(name: String): Recurse? =
		when (stack) {
			is EmptyStack -> null
			is LinkStack ->
				if (stack.link.value == name) onceRecurse
				else NameTrace(stack.link.stack).recurseOrNull(name)?.increase
		}

	fun resolveItem(line: TypeLine): TypeItem =
		line
			.onlyNameOrNull
			?.let { name -> recurseOrNull(name) }
			?.let { item(it) }
			?: item(line)
}

fun nameTrace() = NameTrace(stack())
