package leo16

import leo.base.fold
import leo14.Reducer
import leo14.reducer
import leo14.stringCharReducer

val Compiler.tokenReducer: Reducer<Compiler, leo14.Token>
	get() =
		reducer { plus(it)!!.tokenReducer }

val Compiler.stringCharReducer: Reducer<String, Char>
	get() =
		tokenReducer.stringCharReducer { fragment }

