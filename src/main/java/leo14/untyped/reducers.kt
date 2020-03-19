package leo14.untyped

import leo14.*
import leo14.parser.coreString
import leo14.reader.charReader
import leo14.reader.reducer

val Tokenizer.reducer: Reducer<Tokenizer, Token>
	get() =
		reducer { token ->
			write(token)!!.reducer
		}

val Tokenizer.stringCharReducer: Reducer<String, Char>
	get() =
		reducer.charReader().reducer.mapState {
			tokenReducer.state.fragment.indentString + tokenParser.coreString
		}
