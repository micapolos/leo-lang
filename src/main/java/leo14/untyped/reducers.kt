package leo14.untyped

import leo14.Reducer
import leo14.Token
import leo14.reducer
import leo14.stringCharReducer

val Reader.reducer: Reducer<Reader, Token>
	get() =
		reducer { token ->
			write(token)!!.reducer
		}

val Reader.stringCharReducer: Reducer<String, Char>
	get() =
		reducer.stringCharReducer { fragment }

