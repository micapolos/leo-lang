package leo

import leo.base.*

data class Word(
	val letterStack: Stack<Letter>) {
	override fun toString() = appendableString { it.append(this) }

	sealed class Reader {
		object Empty : Reader()
		data class Full(
			val word: Word
		) : Reader()
	}
}

val Stack<Letter>.word
	get() =
		Word(this)

operator fun Word?.plus(letter: Letter) =
	this?.letterStack.push(letter).word

val String.wordOrNull: Word?
	get() =
		fold(emptyWordReader.orNull) { readerOrNull, char ->
			readerOrNull?.plus(char)
		}?.fullOrNull?.word

fun <R> R.foldLetters(word: Word, fn: R.(Letter) -> R) =
	fold(word.letterStack.reverse.stream, fn)

// === Appendable

fun Appendable.append(word: Word): Appendable =
	foldLetters(word, Appendable::append)

// === reader

val emptyWordReader: Word.Reader =
	Word.Reader.Empty

fun Word.Reader.plus(char: Char) =
	when (this) {
		is Word.Reader.Empty -> wordReader(char)
		is Word.Reader.Full -> plus(char)
	}

fun wordReader(char: Char) =
	char.letterOrNull?.let { letter ->
		Word.Reader.Full(letter.onlyStack.word)
	}

fun Word.Reader.Full.plus(char: Char) =
	char.letterOrNull?.let { letter ->
		Word.Reader.Full(word + letter)
	}

val Word.Reader.fullOrNull: Word.Reader.Full?
	get() =
		this as? Word.Reader.Full

// === folding bytes

val Word.letterStream: Stream<Letter>
	get() =
		letterStack.reverse.stream

val Word.byteStream: Stream<Byte>
	get() =
		letterStream.map(Letter::byte)

val Word.bitStream: Stream<Bit>
	get() =
		letterStream.map(Letter::bitStream).join

val Word.reflect: Field<Nothing>
	get() =
		wordWord fieldTo term

// === words ===

val aWord = "a".wordOrNull!!
val ageWord = "age".wordOrNull!!
val anyWord = "any".wordOrNull!!
val anythingWord = "anything".wordOrNull!!
val arrayWord = "array".wordOrNull!!
val bWord = "b".wordOrNull!!
val beginWord = "begin".wordOrNull!!
val bitWord = "bit".wordOrNull!!
val bodyWord = "body".wordOrNull!!
val booleanWord = "boolean".wordOrNull!!
var byteWord = "byte".wordOrNull!!
val cWord = "c".wordOrNull!!
val charWord = "char".wordOrNull!!
val characterWord = "character".wordOrNull!!
val choiceWord = "choice".wordOrNull!!
val classWord = "class".wordOrNull!!
val continueWord = "continue".wordOrNull!!
val coreWord = "core".wordOrNull!!
val defineWord = "define".wordOrNull!!
val doneWord = "done".wordOrNull!!
val eightWord = "eight".wordOrNull!!
val eitherWord = "either".wordOrNull!!
val emptyWord = "empty".wordOrNull!!
val endWord = "end".wordOrNull!!
val entryWord = "entry".wordOrNull!!
val errorWord = "error".wordOrNull!!
val evaluateWord = "evaluate".wordOrNull!!
val evaluatorWord = "evaluator".wordOrNull!!
val falseWord = "false".wordOrNull!!
val fieldWord = "field".wordOrNull!!
val firstWord = "first".wordOrNull!!
val functionWord = "function".wordOrNull!!
val identifierWord = "identifier".wordOrNull!!
val identityWord = "identity".wordOrNull!!
val isWord = "is".wordOrNull!!
val itWord = "it".wordOrNull!!
val itemWord = "item".wordOrNull!!
val keyWord = "key".wordOrNull!!
val lastWord = "last".wordOrNull!!
val leoWord = "leo".wordOrNull!!
val letterWord = "letter".wordOrNull!!
val literalWord = "literal".wordOrNull!!
val metaWord = "meta".wordOrNull!!
val nameWord = "name".wordOrNull!!
val nativeWord = "native".wordOrNull!!
val negateWord = "negate".wordOrNull!!
val noneWord = "none".wordOrNull!!
val notWord = "not".wordOrNull!!
val nothingWord = "nothing".wordOrNull!!
val nullWord = "null".wordOrNull!!
val nullabilityWord = "nullability".wordOrNull!!
val nullableWord = "nullable".wordOrNull!!
val numberWord = "number".wordOrNull!!
val oneWord = "one".wordOrNull!!
val ofWord = "of".wordOrNull!!
val optionalWord = "optional".wordOrNull!!
val parentWord = "parent".wordOrNull!!
val patternWord = "pattern".wordOrNull!!
val personWord = "person".wordOrNull!!
val plusWord = "plus".wordOrNull!!
val popWord = "pop".wordOrNull!!
val previousWord = "previous".wordOrNull!!
val quoteWord = "quote".wordOrNull!!
val readWord = "read".wordOrNull!!
val readerWord = "reader".wordOrNull!!
val replWord = "repl".wordOrNull!!
val ruleWord = "rule".wordOrNull!!
val scopeWord = "scope".wordOrNull!!
val selectorWord = "selector".wordOrNull!!
val stackWord = "stack".wordOrNull!!
val stringWord = "string".wordOrNull!!
val structureWord = "structure".wordOrNull!!
val termWord = "term".wordOrNull!!
val textWord = "text".wordOrNull!!
val theWord = "the".wordOrNull!!
val thisWord = "this".wordOrNull!!
val timesWord = "times".wordOrNull!!
val todoWord = "todo".wordOrNull!!
val tokenWord = "token".wordOrNull!!
val topWord = "top".wordOrNull!!
val trueWord = "true".wordOrNull!!
val twoWord = "two".wordOrNull!!
val unquoteWord = "unquote".wordOrNull!!
val utfWord = "utf".wordOrNull!!
val valueWord = "value".wordOrNull!!
val versionWord = "version".wordOrNull!!
var wordWord = "word".wordOrNull!!
val zeroWord = "zero".wordOrNull!!