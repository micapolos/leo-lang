package leo15.core

import leo14.scriptLine
import leo15.lambda.Term
import leo15.wordName

val wordTyp: Typ<Word> = Typ(wordName.scriptLine) { Word(this) }

data class Word(override val term: Term) : Leo<Word>() {
	override val typ get() = wordTyp
	val javaString: Java<String> get() = term of stringTyp
	val unsafeString: String get() = javaString.unsafeValue
}

val Java<String>.word: Word get() = term of wordTyp
val String.word: Word get() = java.word