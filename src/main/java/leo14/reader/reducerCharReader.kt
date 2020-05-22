package leo14.reader

import leo.base.orIfNull
import leo14.Reducer
import leo14.Token
import leo14.begin
import leo14.parser.EndStringParser
import leo14.parser.FullNumberParser
import leo14.parser.LiteralSpacedTokenParser
import leo14.parser.LiteralTokenParser
import leo14.parser.NameSpacedTokenParser
import leo14.parser.NewSpacedTokenParser
import leo14.parser.NumberLiteralParser
import leo14.parser.SpacedTokenParser
import leo14.parser.StringLiteralParser
import leo14.parser.canContinue
import leo14.parser.isNew
import leo14.parser.newSpacedTokenParser
import leo14.parser.parse
import leo14.parser.tokenOrNull
import leo14.reduce
import leo14.reducer
import leo14.token
import leo16.names.*

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
	else if (char == '.')
		if (tokenParser is NameSpacedTokenParser) putRaw(' ').putRaw(' ')
		else if ((tokenParser is LiteralSpacedTokenParser
				&& tokenParser.literalParser is StringLiteralParser
				&& tokenParser.literalParser.stringParser is EndStringParser)
			|| (tokenParser is LiteralSpacedTokenParser
				&& tokenParser.literalParser is NumberLiteralParser
				&& tokenParser.literalParser.numberParser is FullNumberParser)) putRaw(' ')
		else putRaw(char)
	else putRaw(char)

fun <S> ReducerCharReader<S>.putOperatorOrNull(char: Char): ReducerCharReader<S>? =
	if (tokenParser.isNew)
		when (char) {
			'+' ->
				ReducerCharReader(
					tokenReducer.reduce(token(begin(_plus))),
					NewSpacedTokenParser)
			'-' ->
				ReducerCharReader(
					tokenReducer.reduce(token(begin(_minus))),
					NewSpacedTokenParser)
			'*' ->
				ReducerCharReader(
					tokenReducer.reduce(token(begin(_times))),
					NewSpacedTokenParser)
			'=' ->
				ReducerCharReader(
					tokenReducer.reduce(token(begin(_equals))),
					NewSpacedTokenParser)
			'&' ->
				ReducerCharReader(
					tokenReducer.reduce(token(begin(_and))),
					NewSpacedTokenParser)
			'|' ->
				ReducerCharReader(
					tokenReducer.reduce(token(begin(_or))),
					NewSpacedTokenParser)
			'@' ->
				ReducerCharReader(
					tokenReducer.reduce(token(begin(_at))),
					NewSpacedTokenParser)
			'%' ->
				ReducerCharReader(
					tokenReducer.reduce(token(begin(_percent))),
					NewSpacedTokenParser)
			',' ->
				ReducerCharReader(
					tokenReducer.reduce(token(begin(_comma))),
					NewSpacedTokenParser)
			'#' ->
				ReducerCharReader(
					tokenReducer.reduce(token(begin(_hash))),
					NewSpacedTokenParser)
			'/' ->
				ReducerCharReader(
					tokenReducer.reduce(token(begin(_by))),
					NewSpacedTokenParser)
			'\'' ->
				ReducerCharReader(
					tokenReducer.reduce(token(begin(_quote))),
					NewSpacedTokenParser)
			'`' ->
				ReducerCharReader(
					tokenReducer.reduce(token(begin(_comment))),
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
