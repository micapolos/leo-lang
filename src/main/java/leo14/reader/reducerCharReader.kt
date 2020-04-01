package leo14.reader

import leo.base.orIfNull
import leo14.*
import leo14.parser.*
import leo14.untyped.itName
import leo14.untyped.minusName
import leo14.untyped.plusName
import leo14.untyped.timesName

data class ReducerCharReader<S>(
	val tokenReducer: Reducer<S, Token>,
	val tokenParser: SpacedTokenParser)

fun <S> Reducer<S, Token>.charReader() =
	ReducerCharReader(this, newSpacedTokenParser)

fun <S> ReducerCharReader<S>.put(char: Char): ReducerCharReader<S> =
	null
		?: putOperatorOrNull(char)
		?: putNonOperator(char)

fun <S> ReducerCharReader<S>.putNonOperator(char: Char): ReducerCharReader<S> =
	if (char == '\n')
		if (tokenParser is NameSpacedTokenParser) putRaw(' ').putRaw(' ')
		else if (tokenParser is NewSpacedTokenParser || tokenParser.parse(' ')?.tokenOrNull != null) putRaw(' ')
		else putRaw(' ').putRaw(' ')
	else putRaw(char)

fun <S> ReducerCharReader<S>.putOperatorOrNull(char: Char): ReducerCharReader<S>? =
	if (tokenParser.isNew)
		when (char) {
			'+' ->
				ReducerCharReader(
					tokenReducer.reduce(token(begin(plusName))),
					NewSpacedTokenParser)
			'-' ->
				ReducerCharReader(
					tokenReducer.reduce(token(begin(minusName))),
					NewSpacedTokenParser)
			'*' ->
				ReducerCharReader(
					tokenReducer.reduce(token(begin(timesName))),
					NewSpacedTokenParser)
			'>' ->
				ReducerCharReader(
					tokenReducer.reduce(token(begin(itName))),
					NewSpacedTokenParser)
			else -> null
		}
	else null

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
