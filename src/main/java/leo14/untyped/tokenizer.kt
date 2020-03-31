package leo14.untyped

import leo14.Script

data class Tokenizer(
	val parentOrNull: TokenizerParent?,
	val script: Script,
	val dotted: Boolean)

data class TokenizerParent(
	val tokenizer: Tokenizer,
	val newline: Boolean,
	val name: String)
