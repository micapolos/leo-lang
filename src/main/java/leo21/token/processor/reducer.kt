package leo21.token.processor

import leo.ansi
import leo.magenta
import leo.reset
import leo14.Reducer
import leo14.Token
import leo14.promptStringCharReducer
import leo14.reducer

val Processor.reducer: Reducer<Processor, Token>
	get() =
		reducer { plus(it).reducer }

val Processor.stringCharReducer: Reducer<String, Char>
	get() =
		reducer
			.promptStringCharReducer { noPromptString to fragment }

val promptString = "${ansi.magenta}=> Leonardo v0.21${ansi.reset}\n"
val noPromptString = ""
