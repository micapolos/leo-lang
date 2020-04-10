package leo14.untyped.typed

import leo14.Reducer
import leo14.Token
import leo14.reducer
import leo14.stringCharReducer

val Reader.tokenReducer: Reducer<Reader, Token>
	get() =
		reducer { plus(it).tokenReducer }

val Reader.stringCharReducer: Reducer<String, Char>
	get() =
		tokenReducer.stringCharReducer { fragment }
