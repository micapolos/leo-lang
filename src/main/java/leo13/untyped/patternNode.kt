package leo13.untyped

import leo13.script.*

sealed class PatternNode

data class ChoicePatternNode(val choice: Choice) : PatternNode()
data class LinkPatternNode(val link: PatternLink) : PatternNode()

fun node(choice: Choice): PatternNode = ChoicePatternNode(choice)
fun node(link: PatternLink): PatternNode = LinkPatternNode(link)

fun node(line: PatternLine): PatternNode =
	node(link(pattern(null), line))

fun PatternNode.plus(line: PatternLine): PatternNode =
	node(link(pattern(this), line))

fun PatternNode.matches(script: Script): Boolean =
	when (this) {
		is ChoicePatternNode -> choice.matches(script)
		is LinkPatternNode -> link.matches(script)
	}

val patternNodeName = "node"

val patternNodeReader: Reader<PatternNode> =
	reader(patternNodeName) {
		unsafeLink.run {
			when {
				lhs.isEmpty && line.name == "choice" -> node(choiceReader.unsafeBodyValue(line.rhs))
				else -> node(patternLinkReader.unsafeBodyValue(script))
			}
		}
	}

val patternNodeWriter: Writer<PatternNode> =
	writer(patternNodeName) {
		when (this) {
			is ChoicePatternNode -> choiceWriter.script(choice)
			is LinkPatternNode -> patternLinkWriter.bodyScript(link)
		}
	}
