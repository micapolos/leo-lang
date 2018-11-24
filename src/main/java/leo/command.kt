package leo

import leo.base.*

// TODO: Find better name
sealed class Command<out V>

data class BeginCommand<out V>(
	val begin: Begin) : Command<V>()

data class EndCommand<out V>(
	val end: End) : Command<V>()

data class FieldCommand<out V>(
	val field: Field<V>) : Command<V>()

data class ScalarCommand<out V>(
	val scalar: Scalar<V>) : Command<V>()

fun <V> Begin.command(): Command<V> =
	BeginCommand(this)

val Begin.command: Command<Nothing>
	get() =
		command()

fun <V> End.command(): Command<V> =
	EndCommand(this)

val End.command: Command<Nothing>
	get() =
		command()

val <V> Scalar<V>.command: Command<V>
	get() =
		ScalarCommand(this)

val <V> Field<V>.command: Command<V>
	get() =
		FieldCommand(this)

// === command to token

val <V> Command<V>.token: Stream<Token<V>>
	get() =
		when (this) {
			is BeginCommand -> begin.token<V>().onlyStream
			is EndCommand -> end.token<V>().onlyStream
			is ScalarCommand -> scalar.token.onlyStream
			is FieldCommand -> field.tokenStream
		}

val <V> Stream<Command<V>>.commandTokenStream: Stream<Token<V>>
	get() =
		first.token.then { nextOrNull?.commandTokenStream }

// === reader

data class CommandReader<V>(
	val wordStackOrNull: Stack<Word>?,
	val scalarOrNull: Scalar<V>?,
	val commandWriter: Writer<Command<V>>)


fun <V> CommandReader<V>.read(token: Token<V>): CommandReader<V>? =
	when (token) {
		is MetaToken -> read(token)
		is WordToken -> read(token)
		is BeginToken -> readBeginToken
		is EndToken -> readEndToken
	}

fun <V> CommandReader<V>.read(metaToken: MetaToken<V>): CommandReader<V>? =
	scalarOrNull.ifNull {
		copy(scalarOrNull = metaToken.value.meta.scalar)
	}

fun <V> CommandReader<V>.read(wordToken: WordToken<V>): CommandReader<V>? =
	scalarOrNull.ifNull {
		copy(scalarOrNull = wordToken.word.scalar)
	}

val <V> CommandReader<V>.readBeginToken: CommandReader<V>?
	get() =
		scalarOrNull?.let { scalar ->
			when (scalar) {
				is MetaScalar -> null
				is WordScalar ->
					copy(
						wordStackOrNull = wordStackOrNull.push(scalar.word),
						scalarOrNull = null)
			}
		}

val <V> CommandReader<V>.readEndToken: CommandReader<V>?
	get() =
		scalarOrNull?.let { scalar ->
			wordStackOrNull?.let { wordStack ->
				wordStack.top.let { topWord ->
					copy(
						wordStackOrNull = wordStack.pop,
						commandWriter = commandWriter
							.write(scalar.command)
							.write(topWord.fieldTo(scalar.term).command),
						scalarOrNull = null)
				}
			}
		}

