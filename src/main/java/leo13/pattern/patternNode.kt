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

	fun recurseExpand(rootOrNull: RecurseRoot?): PatternNode =
		when (this) {
			is EmptyPatternNode -> this
			is LinkPatternNode -> node(link.recurseExpand(rootOrNull))
			is OptionsPatternNode -> node(options.recurseExpand(rootOrNull))
			is ArrowPatternNode -> this
		}

	fun contains(pattern: Pattern, trace: PatternTrace?): Boolean =
		when (pattern) {
			is NodePattern -> contains(pattern.node, trace)
			is RecursePattern -> false
		}

	fun contains(node: PatternNode, trace: PatternTrace?): Boolean =
		when (this) {
			is EmptyPatternNode -> node is EmptyPatternNode
			is LinkPatternNode -> node is LinkPatternNode && link.contains(node.link, trace)
			is OptionsPatternNode -> options.contains(node, trace)
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
