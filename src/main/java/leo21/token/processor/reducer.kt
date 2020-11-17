package leo21.token.processor

import leo.ansi
import leo.magenta
import leo.reset
import leo14.Reducer
import leo14.Token
import leo14.promptStringCharReducer
import leo14.reducer

val TokenProcessor.tokenReducer: Reducer<TokenProcessor, Token>
	get() =
		reducer { plus(it).tokenReducer }

val TokenProcessor.stringCharReducer: Reducer<String, Char>
	get() =
		tokenReducer
			.promptStringCharReducer { noPromptString to fragment }

val promptString = "${ansi.magenta}=> Leonardo v0.21${ansi.reset}\n"
val noPromptString = ""
