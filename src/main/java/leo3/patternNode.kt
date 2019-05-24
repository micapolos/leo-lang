package leo3

data class PatternNode(
	val dict: Dict<Pattern>,
	val endNodeOrNull: PatternNode?)