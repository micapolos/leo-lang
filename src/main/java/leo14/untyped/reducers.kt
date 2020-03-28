package leo14.untyped

import leo14.Reducer
import leo14.Token
import leo14.mapState
import leo14.parser.coreString
import leo14.reader.charReader
import leo14.reader.reducer
import leo14.reducer

val Reader.reducer: Reducer<Reader, Token>
	get() =
		reducer { token ->
			write(token)!!.reducer
		}

val Reader.stringCharReducer: Reducer<String, Char>
	get() =
		reducer.charReader().reducer.mapState {
			tokenReducer.state.fragment.leoStringNonTail + tokenParser.coreString
		}

