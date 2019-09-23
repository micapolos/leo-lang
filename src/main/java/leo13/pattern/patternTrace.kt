package leo13.pattern

import leo.base.notNullOrError
import leo.base.updateIfNotNull
import leo13.Empty
import leo13.ObjectScripting
import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.plus
import leo13.script.script
import leo13.traceName

data class PatternTrace(val lhsOrNull: PatternTrace?, val node: PatternNode) : ObjectScripting() {
	override fun toString() = super.toString()

	override val scriptingLine: ScriptLine
		get() = traceName lineTo (lhsOrNull?.scriptingLine?.rhs ?: script()).plus(node.scriptingLine)

	fun set(node: PatternNode) = PatternTrace(lhsOrNull, node)

	fun set(recurse: Recurse) = lhsOrNull!!.plus(recurse)

	fun set(pattern: Pattern) =
		when (pattern) {
			is NodePattern -> set(pattern.node)
			is RecursePattern -> set(pattern.recurse)
		}

	fun plus(line: PatternLine) =
		set(node.plus(line))

	fun plus(node: PatternNode) = PatternTrace(this, node)

	fun plus(pattern: Pattern): PatternTrace =
		when (pattern) {
			is RecursePattern -> plus(pattern.recurse)
			is NodePattern -> plus(pattern.node)
		}

	fun plus(recurse: Recurse): PatternTrace =
		if (recurse.lhsOrNull == null) this
		else lhsOrNull!!.run { plus(recurse.lhsOrNull) }

	val previousOrNull: PatternTrace?
		get() =
			node.previousOrNull?.let { set(it) }

	val contentOrNull: PatternTrace?
		get() =
			node.linkOrNull?.run { plus(line.rhs) }

	fun getOrNull(name: String): PatternTrace? =
		node.getOrNull(name)?.let { plus(it) }

	fun contains(trace: PatternTrace): Boolean =
		if (trace.lhsOrNull == null) contains(trace.node)
		else TODO()

	fun contains(pattern: Pattern): Boolean =
		when (pattern) {
			is NodePattern -> contains(pattern.node)
			is RecursePattern -> false
		}

	fun contains(otherNode: PatternNode): Boolean =
		when (otherNode) {
			is EmptyPatternNode -> contains(otherNode.empty)
			is LinkPatternNode -> contains(otherNode.link)
			is OptionsPatternNode -> contains(otherNode.options)
			is ArrowPatternNode -> contains(otherNode.arrow)
		}

	fun contains(empty: Empty): Boolean =
		node.isEmpty

	fun contains(link: PatternLink): Boolean =
		if (link.lhs.isEmpty) contains(link.line)
		else node
			.linkOrNull
			?.let { nodeLink ->
				set(nodeLink.lhs).contains(link.lhs)
					&& nodeLink.line.name == link.line.name
					&& plus(nodeLink.line.rhs).contains(link.line.rhs)
			}
			?: false

	fun contains(line: PatternLine): Boolean =
		when (node) {
			is EmptyPatternNode -> false
			is LinkPatternNode -> node.link.lhs.isEmpty
				&& node.link.line.name == line.name
				&& plus(node.link.line.rhs).contains(line.rhs)
			is OptionsPatternNode ->
				when (node.options) {
					is EmptyOptions -> false
					is LinkOptions ->
						(node.options.link.line.name == line.name
							&& plus(node.options.link.line.rhs).contains(line.rhs))
							|| set(node(node.options.link.lhs)).contains(line)
				}
			is ArrowPatternNode -> false
		}

	fun contains(options: Options): Boolean =
		node
			.optionsOrNull
			?.let { nodeOptions ->
				when (nodeOptions) {
					is EmptyOptions -> options is EmptyOptions
					is LinkOptions -> options is LinkOptions
						&& set(node(nodeOptions.link.lhs)).contains(options.link.lhs)
						&& nodeOptions.link.line.name == options.link.line.name
						&& plus(nodeOptions.link.line.rhs).contains(options.link.line.rhs)
				}
			}
			?: false

	fun contains(arrow: PatternArrow): Boolean =
		// TODO: Could it be less restrictive?
		node.arrowOrNull?.let { it == arrow } ?: false

	fun expand(pattern: Pattern): PatternTrace =
		when (pattern) {
			is NodePattern -> plus(pattern.node)
			is RecursePattern -> plus(onceRecurse).let { trace ->
				if (trace.lhsOrNull == null) duplicate
				else trace
			}
		}

	val duplicate: PatternTrace
		get() =
			plus(this)

	fun plus(trace: PatternTrace): PatternTrace =
		updateIfNotNull(trace.lhsOrNull) { plus(it) }.plus(trace.node)
}

fun trace(node: PatternNode) = PatternTrace(null, node)

fun PatternTrace?.plus(node: PatternNode) =
	this?.plus(node) ?: trace(node)

fun PatternTrace?.plus(recurse: Recurse): PatternTrace =
	notNullOrError("recurse").plus(recurse)
