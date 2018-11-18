package leo

sealed class Token
data class BeginToken(val word: Word) : Token()
object EndToken : Token()

val Token.reflect: Field<Nothing>
	get() =
		tokenWord fieldTo term(
			when (this) {
				is BeginToken -> beginWord fieldTo word.reflect.termOrNull
				is EndToken -> endWord.field
			})

val Field<Nothing>.parseToken: Token?
	get() =
		match(tokenWord) { tokenTerm ->
			when (tokenTerm) {
				endWord.term -> EndToken
				else -> tokenTerm?.match(beginWord) { beginTerm ->
					beginTerm?.onlyFieldOrNull?.let { field ->
						if (field.termOrNull != null) null
						else BeginToken(field.word)
					}
				}
			}
		}
