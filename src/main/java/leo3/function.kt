package leo3

import leo32.Dict

data class Function(
	val tokenToMatchDict: Dict<Token, Match>,
	val defaultMatchOrNull: Match?)

