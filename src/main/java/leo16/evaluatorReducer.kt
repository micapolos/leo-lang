package leo16

import leo14.Reducer
import leo14.reducer
import leo14.stringCharReducer

val Evaluator.tokenReducer: Reducer<Evaluator, leo14.Token>
	get() =
		reducer { plus(it.tokenOrNull!!).tokenReducer }

val Evaluator.stringCharReducer: Reducer<String, Char>
	get() =
		tokenReducer.stringCharReducer { fragment }

