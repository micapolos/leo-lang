package leo13.pattern

import leo.base.fold
import leo13.*
import leo13.script.Script
import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.script

sealed class PatternNode : ObjectScripting() {
	override fun toString() = scriptingLine.toString()

	override val scriptingLine: ScriptLine
		get() = patternName lineTo
			when (this) {
				is EmptyPatternNode -> empty.scriptingLine.rhs
				is LinkPatternNode -> link.scriptingLine.rhs
				is OptionsPatternNode -> script(options.scriptingLine)
				is ArrowPatternNode -> arrow.scriptingLine.rhs
			}

	val isEmpty get() = this is EmptyPatternNode
	val linkOrNull get() = (this as? LinkPatternNode)?.link
	val optionsOrNull get() = (this as? OptionsPatternNode)?.options
	val arrowOrNull get() = (this as? ArrowPatternNode)?.arrow
	val lineOrNull get() = linkOrNull?.line

	fun plus(line: PatternLine) =
		node(pattern(this) linkTo line)

	fun contains(pattern: Pattern): Boolean =
		pattern is NodePattern && contains(pattern.node)

	fun contains(pattern: PatternNode): Boolean =
		when (this) {
			is EmptyPatternNode -> pattern.isEmpty
			is LinkPatternNode -> pattern is LinkPatternNode && link.contains(pattern.link)
			is OptionsPatternNode -> options.contains(pattern)
			is ArrowPatternNode -> pattern is ArrowPatternNode && arrow.contains(pattern.arrow)
		}

	fun lineRhsOrNull(name: String): Pattern? =
		linkOrNull?.lineRhsOrNull(name)

	fun setLineRhsOrNull(line: PatternLine): PatternNode? =
		linkOrNull?.setLineRhsOrNull(line)?.let { node(it) }

	fun getOrNull(name: String): Pattern? =
		linkOrNull?.getOrNull(name)

	fun setOrNull(line: PatternLine): PatternNode? =
		linkOrNull?.setOrNull(line)?.let { node(it) }

	val previousOrNull: Pattern?
		get() =
			linkOrNull?.lhs

	val contentOrNull: Pattern?
		get() =
			linkOrNull?.line?.rhs

	val onlyNameOrNull: String?
		get() =
			linkOrNull?.onlyLineOrNull?.onlyNameOrNull

	fun leafPlusOrNull(pattern: Pattern): Pattern? =
		when (this) {
			is EmptyPatternNode -> pattern
			is LinkPatternNode -> link.leafPlusOrNull(pattern)?.let { pattern(node(it)) }
			else -> null
		}

	val beginOptionsOrNull: Options?
		get() =
			when (this) {
				is EmptyPatternNode -> options()
				is OptionsPatternNode -> options
				else -> null
			}

	fun recurseExpand(rootRecurse: Recurse?, rootNode: PatternNode): PatternNode =
		when (this) {
			is EmptyPatternNode -> this
			is LinkPatternNode -> node(link.recurseExpand(rootRecurse, rootNode))
			is OptionsPatternNode -> node(options.recurseExpand(rootRecurse, rootNode))
			is ArrowPatternNode -> this
		}

	fun recurseContains(pattern: Pattern, trace: PatternTrace): Boolean =
		when (pattern) {
			is NodePattern -> recurseContains(pattern.node, trace)
			is RecursePattern -> false
		}

	fun recurseContains(node: PatternNode, trace: PatternTrace): Boolean =
		when (this) {
			is EmptyPatternNode -> node is EmptyPatternNode
			is LinkPatternNode -> node is LinkPatternNode && link.recurseContains(node.link, trace)
			is OptionsPatternNode -> options.recurseContains(node, trace)
			is ArrowPatternNode -> node is ArrowPatternNode && arrow.contains(node.arrow)
		}
}

data class EmptyPatternNode(val empty: Empty) : PatternNode()
data class LinkPatternNode(val link: PatternLink) : PatternNode()
data class OptionsPatternNode(val options: Options) : PatternNode()
data class ArrowPatternNode(val arrow: PatternArrow) : PatternNode()

fun patternNode() = node(empty)
fun node(empty: Empty): PatternNode = EmptyPatternNode(empty)
fun node(link: PatternLink): PatternNode = LinkPatternNode(link)
fun node(options: Options): PatternNode = OptionsPatternNode(options)
fun node(arrow: PatternArrow): PatternNode = ArrowPatternNode(arrow)

fun node(line: PatternLine, vararg lines: PatternLine) =
	node(pattern() linkTo line).fold(lines) { plus(it) }

val Script.pattern
	get() =
		pattern().fold(lineStack.reverse) { plus(it.patternLine) }
