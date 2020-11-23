package leo21.token.processor

import leo14.Reducer
import leo14.Token
import leo14.reducer
import leo14.stringCharReducer

val Processor.reducer: Reducer<Processor, Token>
	get() =
		reducer { plus(it).reducer }

val Processor.stringCharReducer: Reducer<String, Char>
	get() =
		reducer.stringCharReducer { printFragment }
