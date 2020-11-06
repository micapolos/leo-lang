package leo21.parser

data class Lead(
	val leadingLevel: Level,
	val trailingLevel: Level
)

fun Lead.plus(indent: Indent): Lead? =
	trailingLevel.linkOrNull?.let { link ->
		Lead(leadingLevel.plus(link.head), link.tail)
	}