package leo13.pattern

import leo.base.fold
import leo.base.notNullOrError
import leo13.ObjectScripting
import leo13.empty
import leo13.patternName
import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.script

sealed class Pattern : ObjectScripting() {
	override fun toString() = scriptingLine.toString()

	override val scriptingLine: ScriptLine
		get() =
			patternName lineTo when (this) {
				is NodePattern -> node.scriptingLine.rhs
				is RecursePattern -> script(recurse.scriptingLine)
			}

	val isEmpty get() = nodeOrNull?.isEmpty ?: false
	val linkOrNull get() = nodeOrNull?.linkOrNull
	val arrowOrNull get() = nodeOrNull?.arrowOrNull
	val optionsOrNull get() = nodeOrNull?.optionsOrNull

	fun plus(line: PatternLine) = pattern(node(linkTo(line)))
	fun plus(name: String) = plus(name.patternLine)

	fun append(line: PatternLine) = recurseExpand().plus(line)
	fun append(name: String) = plus(name)

	fun lineRhsOrNull(name: String): Pattern? =
		recurseExpand().nodeOrNull?.lineRhsOrNull(name)

	fun setLineRhsOrNull(line: PatternLine): Pattern? =
		recurseExpand().nodeOrNull?.setLineRhsOrNull(line)?.let { pattern(it) }

	fun getOrNull(name: String): Pattern? =
		recurseExpand().nodeOrNull?.getOrNull(name)

	fun setOrNull(line: PatternLine): Pattern? =
		recurseExpand().nodeOrNull?.setOrNull(line)?.let { pattern(it) }

	val previousOrNull: Pattern?
		get() =
			recurseExpand().nodeOrNull?.previousOrNull

	val contentOrNull: Pattern?
		get() =
			recurseExpand().nodeOrNull?.contentOrNull

	val onlyNameOrNull: String?
		get() =
			nodeOrNull?.onlyNameOrNull

	fun leafPlusOrNull(pattern: Pattern): Pattern? =
		recurseExpand().nodeOrNull?.leafPlusOrNull(pattern)

	fun recurseExpand(rootOrNull: RecurseRoot? = null): Pattern =
		when (this) {
			is NodePattern ->
				pattern(node.recurseExpand(rootOrNull))
			is RecursePattern ->
				rootOrNull
					.notNullOrError("root")
					.let { root ->
						if (recurse == root.recurse) pattern(root.node)
						else this
					}
		}

	fun contains(pattern: Pattern, traceOrNull: PatternTrace? = null): Boolean =
		when (this) {
			is NodePattern ->
				when (pattern) {
					is NodePattern -> node.contains(pattern.node, traceOrNull)
					is RecursePattern -> false
				}
			is RecursePattern ->
				when (pattern) {
					is NodePattern -> traceOrNull.orNullPlus(recurse).let { it.node.contains(pattern.node, it) }
					is RecursePattern -> recurse == pattern.recurse
				}
		}
}

data class NodePattern(val node: PatternNode) : Pattern() {
	override fun toString() = super.toString()
}

data class RecursePattern(val recurse: Recurse) : Pattern() {
	override fun toString() = super.toString()
}

val Pattern.nodeOrNull get() = (this as? NodePattern)?.node
val Pattern.recurseOrNull get() = (this as? RecursePattern)?.recurse

fun pattern(node: PatternNode): Pattern = NodePattern(node)
fun pattern(recurse: Recurse): Pattern = RecursePattern(recurse)

fun pattern() = pattern(node(empty))
fun pattern(options: Options) = pattern(node(options))
fun pattern(arrow: PatternArrow) = pattern(node(arrow))
fun pattern(link: PatternLink) = pattern(node(link))

fun pattern(line: PatternLine, vararg lines: PatternLine) =
	pattern(node(pattern() linkTo line)).fold(lines) { plus(it) }

fun pattern(name: String, vararg names: String) =
	pattern(node(pattern() linkTo name.patternLine)).fold(names) { plus(it) }

