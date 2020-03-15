package leo14.reader

import leo.base.orIfNull
import leo14.Reducer
import leo14.Token
import leo14.parser.*
import leo14.reduce
import leo14.reducer

data class ReducerCharReader<S>(
	val tokenReducer: Reducer<S, Token>,
	val tokenParser: SpacedTokenParser)

fun <S> Reducer<S, Token>.charReader() =
	ReducerCharReader(this, newSpacedTokenParser)

fun <S> ReducerCharReader<S>.put(char: Char): ReducerCharReader<S> =
	if (char == '\n')
		if (tokenParser is NewSpacedTokenParser || tokenParser.parse(' ')?.tokenOrNull != null) putRaw(' ')
		else putRaw(' ').putRaw(' ')
	else putRaw(char)

fun <S> ReducerCharReader<S>.putRaw(char: Char): ReducerCharReader<S> =
	tokenParser
		.parse(char)
		?.let { parsedTokenParser ->
			parsedTokenParser
				.tokenOrNull
				?.let { token ->
					if (parsedTokenParser.canContinue) ReducerCharReader(tokenReducer, parsedTokenParser)
					else ReducerCharReader(tokenReducer.reduce(token), newSpacedTokenParser)
				}
				?: ReducerCharReader(tokenReducer, parsedTokenParser)
		}
		.orIfNull {
			tokenParser
				.tokenOrNull
				?.let { token -> ReducerCharReader(tokenReducer.reduce(token), newSpacedTokenParser).put(char) }
				?: error("$this.put($char)")
		}

val <S> ReducerCharReader<S>.reducer: Reducer<ReducerCharReader<S>, Char>
	get() =
		reducer { char ->
			put(char).reducer
		}
