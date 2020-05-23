package leo16

import leo.ansi
import leo.blue
import leo.magenta
import leo.reset
import leo13.size
import leo14.Reducer
import leo14.promptStringCharReducer
import leo14.reducer

val Evaluator.tokenReducer: Reducer<Evaluator, leo14.Token>
	get() =
		reducer { plus(it)!!.tokenReducer }

val Evaluator.stringCharReducer: Reducer<String, Char>
	get() =
		tokenReducer
			.promptStringCharReducer { promptString to fragment }

val Evaluator.promptString: String
	get() =
		""
//"${ansi.magenta}=> Leonardo v0.1 ${ansi.blue}(${evaluated.scope.dictionary.definitionStack.size} definitions)${ansi.reset}\n"