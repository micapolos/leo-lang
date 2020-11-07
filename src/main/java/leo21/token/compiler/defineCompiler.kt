package leo21.token.compiler

import leo21.dictionary.Dictionary

data class DefineCompiler(
	val parentOrNull: DefineParent?,
	val dictionary: Dictionary
)