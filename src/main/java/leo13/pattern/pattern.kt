package leo13.pattern

import leo.base.fold
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

	fun lineRhsOrNull(name: String): Pattern? =
		nodeOrNull?.lineRhsOrNull(name)

	fun setLineRhsOrNull(line: PatternLine): Pattern? =
		nodeOrNull?.setLineRhsOrNull(line)?.let { pattern(it) }

	fun getOrNull(name: String): Pattern? =
		nodeOrNull?.getOrNull(name)

	fun setOrNull(line: PatternLine): Pattern? =
		nodeOrNull?.setOrNull(line)?.let { pattern(it) }

	val previousOrNull: Pattern?
		get() =
			nodeOrNull?.previousOrNull

	val contentOrNull: Pattern?
		get() =
			nodeOrNull?.contentOrNull

	val onlyNameOrNull: String?
		get() =
			nodeOrNull?.onlyNameOrNull

	fun contains(pattern: Pattern): Boolean =
		when (this) {
			is NodePattern -> pattern is NodePattern && node.contains(pattern.node)
			is RecursePattern -> pattern is RecursePattern && recurse == pattern.recurse
		}

	fun leafPlusOrNull(pattern: Pattern): Pattern? =
		nodeOrNull?.leafPlusOrNull(pattern)

	fun recurseExpand(rootRecurse: Recurse?, rootNode: PatternNode): Pattern =
		when (this) {
			is RecursePattern ->
				if (recurse == rootRecurse) pattern(rootNode)
				else this
			is NodePattern ->
				pattern(node.recurseExpand(rootRecurse, rootNode))
		}

	val recurseExpand: Pattern
		get() =
			when (this) {
				is NodePattern -> pattern(node.recurseExpand(null, node))
				is RecursePattern -> error("recurse")
			}

	fun recurseContains(pattern: Pattern, trace: PatternTrace? = null): Boolean =
		when (this) {
			is NodePattern ->
				when (pattern) {
					is NodePattern -> node.recurseContains(pattern.node, trace.plus(node))
					is RecursePattern -> false
				}
			is RecursePattern ->
				when (pattern) {
					is NodePattern -> trace.plus(recurse).let { it.node.recurseContains(pattern.node, it) }
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

