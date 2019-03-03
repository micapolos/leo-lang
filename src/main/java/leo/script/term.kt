package leo.script

import leo.Word
import leo.append
import leo.base.ifNotNull
import leo.beginChar
import leo.binary.Int2
import leo.binary.wrappingInt2
import leo.endChar

sealed class Term

data class IntTerm(val int: Int) : Term()
data class FloatTerm(val float: Float) : Term()
data class StringTerm(val string: String) : Term()
data class ScriptTerm(val word: Word, val rhs: Script? = null) : Term()

// === append

fun Appendable.append(intTerm: IntTerm): Appendable =
	append(intTerm.int.toString())

fun Appendable.append(floatTerm: FloatTerm): Appendable =
	append(floatTerm.float.toString())

fun Appendable.append(stringTerm: StringTerm): Appendable =
	this
		.append('"')
		.append(stringTerm.string) // escape!!!
		.append('"')

fun Appendable.append(scriptTerm: ScriptTerm): Appendable =
	this
		.append(scriptTerm.word)
		.append(beginChar)
		.ifNotNull(scriptTerm.rhs, Appendable::append)
		.append(endChar)

fun Appendable.append(term: Term): Appendable =
	when (term) {
		is IntTerm -> append(term)
		is FloatTerm -> append(term)
		is StringTerm -> append(term)
		is ScriptTerm -> append(term)
	}

// === bitStream

val termClassList =
	listOf(IntTerm::class, FloatTerm::class, StringTerm::class, ScriptTerm::class)

val Term.classInt2: Int2
	get() =
		when (this) {
			is IntTerm -> 0.wrappingInt2
			is FloatTerm -> 1.wrappingInt2
			is StringTerm -> 2.wrappingInt2
			is ScriptTerm -> 3.wrappingInt2
		}
