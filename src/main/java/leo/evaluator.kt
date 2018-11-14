package leo

import leo.base.*

data class Evaluator(
	val scopeStack: Stack<Scope>,
	val wordOrNull: Word?,
	val readerValueTerm: Term<Value>) {
	override fun toString() = reflect.string
}

val Stack<Scope>.evaluator
	get() =
		Evaluator(this, null, leoReaderTerm())

val Word.evaluator
	get() =
		scope.stack.evaluator

val emptyEvaluator =
	evaluateWord.evaluator

val Evaluator.evaluatedValueTerm: Term<Value>?
	get() =
		if (scopeStack.pop != null) null
		else if (readerValueTerm != leoReaderTerm<Value>()) null
		else scopeStack.top.let { scope ->
			if (wordOrNull == null) scope.valueTermOrNull
			else scope.valueTermOrNull.push(wordOrNull)
		}

fun <R> R.foldBytes(evaluator: Evaluator, fn: R.(Byte) -> R): R =
	evaluator.scopeStack.reverse.foldTop { scope ->
		if (scope.valueTermOrNull == null) this
		else foldBytes(scope.valueTermOrNull, fn)
	}.foldPop { folded, scope ->
		folded
			.fn('('.toByte())
			.foldBytes(scope, fn)
			.let { folded2 ->
				if (evaluator.wordOrNull == null) folded2
				else folded2.fn('('.toByte()).foldBytes(evaluator.wordOrNull, fn)
			}
	}

fun Evaluator.push(byte: Byte): Evaluator? =
	when (byte) {
		'('.toByte() -> begin
		')'.toByte() -> end
		else -> byte.letterOrNull?.let(this::push)
	}

fun Evaluator.push(letter: Letter): Evaluator =
	copy(wordOrNull = wordOrNull.plus(letter))

fun Evaluator.push(word: Word): Evaluator? =
	foldLetters(word, Evaluator::push)

val Evaluator.begin: Evaluator?
	get() =
		if (wordOrNull == null) null
		else copy(wordOrNull = null)
			.scopeStack
			.push(scopeStack.top.beginChild(wordOrNull))
			.evaluator

val Evaluator.end: Evaluator?
	get() =
		if (wordOrNull != null)
			scopeStack.updateTopOrNull { topScope ->
				topScope.push(wordOrNull)
			}?.let { updatedScopeStack ->
				updatedScopeStack.pop?.let { poppedScopeStack ->
					poppedScopeStack.updateTopOrNull { scope ->
						scope.push(updatedScopeStack.top.parentWord fieldTo updatedScopeStack.top.valueTermOrNull!!)
					}
				}
			}?.evaluator
		else scopeStack
			.pop
			?.let { poppedScopeStack ->
				if (scopeStack.top.valueTermOrNull == null)
					poppedScopeStack.updateTopOrNull { scope ->
						scope.push(scopeStack.top.parentWord)
					}
				else
					poppedScopeStack.updateTopOrNull { scope ->
						scope.push(scopeStack.top.parentWord fieldTo scopeStack.top.valueTermOrNull)
					}
			}
			?.evaluator

// === reflect ===

val Evaluator.reflect: Field<Value>
	get() =
		evaluatorWord fieldTo term(
			scopeStack.reflect(scopeWord, Scope::reflect),
			wordOrNull.orNullReflect(wordWord, Word::reflect),
			readerWord fieldTo readerValueTerm)

