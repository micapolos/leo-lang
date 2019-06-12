package leo

import leo.base.*

data class Word(
	val letterStack: Stack<Letter>) {
	override fun toString() = appendableString { it.append(this) }
}

val Stack<Letter>.word
	get() =
		Word(this)

val Letter.onlyWord
	get() =
		onlyStack.word

fun Word.plus(letter: Letter) =
	letterStack.push(letter).word

fun Word?.orNullPlus(letter: Letter) =
	this?.plus(letter) ?: letter.onlyWord

val String.wordOrNull: Word?
	get() =
		if (isEmpty()) null
		else get(0).letterOrNull?.onlyWord?.orNull.fold(substring(1)) { char ->
			char.letterOrNull?.let { letter ->
				this?.plus(letter)
			}
		}

fun <R> R.foldLetters(word: Word, fn: R.(Letter) -> R) =
	fold(word.letterStack.reverse.stream, fn)

// === Appendable

fun Appendable.append(word: Word): Appendable =
	foldLetters(word, Appendable::append)

fun Appendable.scriptAppend(word: Word) =
	append(word).append(' ')

// === folding bytes

val Word.letterStream: Stream<Letter>
	get() =
		letterStack.reverse.stream

val Word.byteStream: Stream<Byte>
	get() =
		letterStream.map(Letter::byte)

val Word.bitStream: Stream<EnumBit>
	get() =
		letterStream.mapJoin(Letter::bitStream)

val Word.binaryKey: BinaryKey
	get() =
		bitStream.then { EnumBit.ONE.onlyStream }.binaryKey

val Word.reflect: Field<Nothing>
	get() =
		wordWord fieldTo term

val Field<Nothing>.parseWord: Word?
	get() =
		matchKey(wordWord) {
			matchWord {
				this
			}
		}

val Stream<EnumBit>.bitParseWord: Parse<EnumBit, Word>?
	get() =
		bitParseLetter?.map { letter -> letter.onlyWord }?.bitParseWord

val Parse<EnumBit, Word>.bitParseWord: Parse<EnumBit, Word>
	get() =
		streamOrNull?.bitParseLetter?.map { letter -> parsed.plus(letter) }?.bitParseWord ?: this

// === words ===

val aWord = "a".wordOrNull!!
val ageWord = "age".wordOrNull!!
val andWord = "and".wordOrNull!!
val anyWord = "any".wordOrNull!!
val anythingWord = "anything".wordOrNull!!
val argumentWord = "argument".wordOrNull!!
val arrayWord = "array".wordOrNull!!
val atomWord = "atom".wordOrNull!!
val bWord = "b".wordOrNull!!
val backWord = "back".wordOrNull!!
val beginWord = "begin".wordOrNull!!
val bitWord = "bit".wordOrNull!!
val bodyWord = "body".wordOrNull!!
val booleanWord = "boolean".wordOrNull!!
var byteWord = "byte".wordOrNull!!
val cWord = "c".wordOrNull!!
val charWord = "char".wordOrNull!!
val characterWord = "character".wordOrNull!!
val childWord = "child".wordOrNull!!
val choiceWord = "choice".wordOrNull!!
val classWord = "class".wordOrNull!!
val continueWord = "continue".wordOrNull!!
val controlWord = "control".wordOrNull!!
val coreWord = "core".wordOrNull!!
val dWord = "d".wordOrNull!!
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
val fileWord = "file".wordOrNull!!
val firstWord = "first".wordOrNull!!
val fiveWord = "five".wordOrNull!!
val fourWord = "four".wordOrNull!!
val functionWord = "function".wordOrNull!!
val getWord = "get".wordOrNull!!
val givesWord = "gives".wordOrNull!!
val goWord = "go".wordOrNull!!
val identifierWord = "identifier".wordOrNull!!
val identityWord = "identity".wordOrNull!!
val importWord = "import".wordOrNull!!
val incrementWord = "increment".wordOrNull!!
val invokeWord = "invoke".wordOrNull!!
val invokedWord = "invoked".wordOrNull!!
val ioWord = "io".wordOrNull!!
val isWord = "is".wordOrNull!!
val itWord = "it".wordOrNull!!
val javaWord = "java".wordOrNull!!
val itemWord = "item".wordOrNull!!
val keyWord = "key".wordOrNull!!
val lastWord = "last".wordOrNull!!
val leafWord = "leaf".wordOrNull!!
val leftWord = "left".wordOrNull!!
val leoWord = "leo".wordOrNull!!
val letterWord = "letter".wordOrNull!!
val literalWord = "literal".wordOrNull!!
val maybeWord = "maybe".wordOrNull!!
val metaWord = "meta".wordOrNull!!
val nameWord = "name".wordOrNull!!
val nativeWord = "native".wordOrNull!!
val naturalWord = "natural".wordOrNull!!
val negateWord = "negate".wordOrNull!!
val nineWord = "nine".wordOrNull!!
val noneWord = "none".wordOrNull!!
val notWord = "not".wordOrNull!!
val nothingWord = "nothing".wordOrNull!!
val nullWord = "null".wordOrNull!!
val nullabilityWord = "nullability".wordOrNull!!
val nullableWord = "nullable".wordOrNull!!
val numberWord = "number".wordOrNull!!
val oneWord = "one".wordOrNull!!
val ofWord = "of".wordOrNull!!
val okWord = "ok".wordOrNull!!
val optionalWord = "optional".wordOrNull!!
val orWord = "or".wordOrNull!!
val parentWord = "parent".wordOrNull!!
val patternWord = "pattern".wordOrNull!!
val personWord = "person".wordOrNull!!
val plusWord = "plus".wordOrNull!!
val popWord = "pop".wordOrNull!!
val previousWord = "previous".wordOrNull!!
val quoteWord = "quote".wordOrNull!!
val readWord = "run".wordOrNull!!
val readerWord = "reader".wordOrNull!!
val recurseWord = "recurse".wordOrNull!!
val recursionWord = "recursion".wordOrNull!!
val replWord = "repl".wordOrNull!!
val rightWord = "right".wordOrNull!!
val rootWord = "root".wordOrNull!!
val ruleWord = "rule".wordOrNull!!
val scalarWord = "scalar".wordOrNull!!
val scopeWord = "scope".wordOrNull!!
val scriptWord = "script".wordOrNull!!
val selectorWord = "selector".wordOrNull!!
val sixWord = "six".wordOrNull!!
val secondWord = "second".wordOrNull!!
val sevenWord = "seven".wordOrNull!!
val srcWord = "src".wordOrNull!!
val stackWord = "stack".wordOrNull!!
val stringWord = "string".wordOrNull!!
val structureWord = "structure".wordOrNull!!
val termWord = "term".wordOrNull!!
val testWord = "test".wordOrNull!!
val textWord = "text".wordOrNull!!
val theWord = "the".wordOrNull!!
val thisWord = "this".wordOrNull!!
val threeWord = "three".wordOrNull!!
val timesWord = "times".wordOrNull!!
val todoWord = "todo".wordOrNull!!
val tokenWord = "token".wordOrNull!!
val topWord = "top".wordOrNull!!
val traceWord = "trace".wordOrNull!!
val treeWord = "tree".wordOrNull!!
val trueWord = "true".wordOrNull!!
val twoWord = "two".wordOrNull!!
val uniqueWord = "unique".wordOrNull!!
val unitWord = "unit".wordOrNull!!
val unquoteWord = "unquote".wordOrNull!!
val utfWord = "utf".wordOrNull!!
val valueWord = "value".wordOrNull!!
val versionWord = "version".wordOrNull!!
var wordWord = "word".wordOrNull!!
val zeroWord = "zero".wordOrNull!!