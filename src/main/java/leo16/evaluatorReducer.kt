package leo16

import leo.base.fold
import leo14.Reducer
import leo14.reducer
import leo14.stringCharReducer

val Evaluator.tokenReducer: Reducer<Evaluator, leo14.Token>
	get() =
		reducer { fold(it.tokenSeq) { plus(it)!! }.tokenReducer }

val Evaluator.stringCharReducer: Reducer<String, Char>
	get() =
		tokenReducer.stringCharReducer { fragment }

