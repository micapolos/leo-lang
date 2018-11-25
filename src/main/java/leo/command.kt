package leo

import leo.base.*

// TODO: Find better name
sealed class Command<out V>

data class ControlCommand<out V>(
	val control: Control) : Command<V>()

data class FieldCommand<out V>(
	val field: Field<V>) : Command<V>()

data class ScalarCommand<out V>(
	val scalar: Scalar<V>) : Command<V>()

fun Control.command(): Command<Nothing> =
	ControlCommand(this)

val Control.command: Command<Nothing>
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
			is ControlCommand -> control.token.onlyStream
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
		is ControlToken -> readControlToken(token)
	}

fun <V> CommandReader<V>.read(metaToken: MetaToken<V>): CommandReader<V>? =
	scalarOrNull.ifNull {
		copy(scalarOrNull = metaToken.value.meta.scalar)
	}

fun <V> CommandReader<V>.read(wordToken: WordToken<V>): CommandReader<V>? =
	scalarOrNull.ifNull {
		copy(scalarOrNull = wordToken.word.scalar)
	}

fun <V> CommandReader<V>.readControlToken(controlToken: ControlToken<V>): CommandReader<V>? =
	when (controlToken.control) {
		is BeginControl -> readBeginControlToken
		is EndControl -> readEndControlToken
	}

val <V> CommandReader<V>.readBeginControlToken: CommandReader<V>?
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

val <V> CommandReader<V>.readEndControlToken: CommandReader<V>?
	get() =
		scalarOrNull?.let { scalar ->
			wordStackOrNull?.let { wordStack ->
				copy(
					wordStackOrNull = wordStack.pop,
					commandWriter = commandWriter
						.write(scalar.command)
						.write(wordStack.top.fieldTo(scalar.term).command),
					scalarOrNull = null)
			}
		}

