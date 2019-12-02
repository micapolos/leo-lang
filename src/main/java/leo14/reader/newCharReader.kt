package leo14.reader

import leo.base.fold
import leo.base.notNullOrError
import leo.base.orNull
import leo.base.runIf
import leo14.*
import leo14.parser.*
import leo14.syntax.Syntax
import leo14.typed.compiler.process

data class NewCharReader(
	val tokenReader: TokenReader,
	val state: CharReaderState)

data class CharReaderParent(
	val simple: Boolean,
	val charReader: NewCharReader)

data class CharReaderState(
	val parent: CharReaderParent?,
	val beginReaderOrNull: BeginReader?)

sealed class BeginReader
data class LiteralBeginReader(val literalParser: LiteralParser) : BeginReader()
data class NameBeginReader(val nameParser: NameParser) : BeginReader()

val TokenReader.newCharReader
	get() =
		NewCharReader(this, CharReaderState(null, null))

fun NewCharReader.read(char: Char): NewCharReader? =
	readOrNull(char) ?: readFallback(char)

fun NewCharReader.readOrNull(char: Char): NewCharReader? =
	if (state.beginReaderOrNull == null)
		null
			?: newNameParser.parse(char)?.let {
				copy(state = state.copy(beginReaderOrNull = NameBeginReader(it)))
			}
			?: newLiteralParser.parse(char)?.let {
				copy(state = state.copy(beginReaderOrNull = LiteralBeginReader(it)))
			}
	else
		state.beginReaderOrNull.read(char)?.let {
			copy(state = state.copy(beginReaderOrNull = it))
		}

fun NewCharReader.readFallback(char: Char): NewCharReader? =
	when (char) {
		' ' -> readSpace
		':' -> readColon
		'\n' -> readNewline
		'\t' -> readTab
		else -> null
	}

fun BeginReader.read(char: Char): BeginReader? =
	when (this) {
		is LiteralBeginReader ->
			literalParser.parse(char)?.let {
				LiteralBeginReader(it)
			}
		is NameBeginReader ->
			nameParser.parse(char)?.let {
				NameBeginReader(it)
			}
	}

val NewCharReader.readSpace: NewCharReader?
	get() =
		if (state.beginReaderOrNull == null) null
		else when (state.beginReaderOrNull) {
			is LiteralBeginReader -> state.beginReaderOrNull.literalParser.literalOrNull?.let { read(it) }
			is NameBeginReader -> state.beginReaderOrNull.nameParser.nameOrNull?.let { readWord(it) }
		}

val NewCharReader.readColon: NewCharReader?
	get() =
		if (state.beginReaderOrNull == null) null
		else when (state.beginReaderOrNull) {
			is LiteralBeginReader -> null
			is NameBeginReader -> state.beginReaderOrNull.nameParser.nameOrNull?.let { readBegin(it, simple = true) }
		}

val NewCharReader.readTab: NewCharReader?
	get() =
		if (state.beginReaderOrNull == null) null
		else when (state.beginReaderOrNull) {
			is LiteralBeginReader -> null
			is NameBeginReader -> state.beginReaderOrNull.nameParser.nameOrNull?.let { readBegin(it, simple = false) }
		}

val NewCharReader.readNewline: NewCharReader?
	get() =
		if (state.parent == null) null
		else
			NewCharReader(
				tokenReader.read(token(end)),
				CharReaderState(state.parent.charReader.state.parent, null))
				.orNull
				.runIf(state.parent.simple) {
					this?.readNewline
				}

fun NewCharReader.read(literal: Literal): NewCharReader =
	copy(
		tokenReader = tokenReader.read(token(literal)), state = state.copy(beginReaderOrNull = null))

fun NewCharReader.readWord(name: String): NewCharReader =
	copy(
		tokenReader = tokenReader.read(token(begin(name))).read(token(end)),
		state = state.copy(beginReaderOrNull = null))

fun NewCharReader.readBegin(name: String, simple: Boolean): NewCharReader =
	copy(
		tokenReader = tokenReader.read(token(begin(name))),
		state = CharReaderState(
			parent = CharReaderParent(
				simple = simple,
				charReader = this),
			beginReaderOrNull = null))

fun NewCharReader.read(string: String): NewCharReader? =
	orNull.fold(string) { read(string) }

val NewCharReader.string: String
	get() =
		emptyFragment
			.foldProcessor<Fragment, Syntax>({ plus(it.token).notNullOrError("$this.write($it)") }) {
				process(tokenReader)
			}.indentString + state.string

val CharReaderState.string
	get() =
		beginReaderOrNull?.string ?: ""

val BeginReader.string
	get() =
		when (this) {
			is LiteralBeginReader -> literalParser.spacedString
			is NameBeginReader -> nameParser.spacedString
		}
