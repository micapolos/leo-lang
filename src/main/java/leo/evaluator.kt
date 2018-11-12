package leo

import leo.base.*

data class Evaluator(
	val scopeStack: Stack<Scope>,
	val wordOrNull: Word?,
	val readerScript: Script) {
	override fun toString() = reflect.string
}

val Stack<Scope>.evaluator
	get() =
		Evaluator(this, null, leoReaderScript)

val Word.evaluator
	get() =
		scope.stack.evaluator

val emptyEvaluator =
	evaluateWord.evaluator

val Evaluator.evaluatedScript: Script?
	get() =
		if (scopeStack.pop != null) null
		else if (readerScript != leoReaderScript) null
		else scopeStack.top.let { scope ->
			if (wordOrNull == null) scope.scriptOrNull
			else scope.scriptOrNull.push(wordOrNull)
		}

fun <R> R.foldBytes(evaluator: Evaluator, fn: R.(Byte) -> R): R =
	evaluator.scopeStack.reverse.foldTop { scope ->
		if (scope.scriptOrNull == null) this
		else foldBytes(scope.scriptOrNull, fn)
	}.andPop { folded, scope ->
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
						scope.push(updatedScopeStack.top.parentWord fieldTo updatedScopeStack.top.scriptOrNull?.term!!)
					}
				}
			}?.evaluator
		else scopeStack
			.pop
			?.let { poppedScopeStack ->
				if (scopeStack.top.scriptOrNull == null)
					poppedScopeStack.updateTopOrNull { scope ->
						scope.push(scopeStack.top.parentWord)
					}
				else
					poppedScopeStack.updateTopOrNull { scope ->
						scope.push(scopeStack.top.parentWord fieldTo scopeStack.top.scriptOrNull.term)
					}
			}
			?.evaluator

// === reflect ===

val Evaluator.reflect: Field<Nothing>
	get() =
		evaluatorWord fieldTo term(
			scopeStack.reflect(scopeWord, Scope::reflect),
			wordOrNull.orNullReflect(wordWord, Word::reflect),
			readerWord fieldTo readerScript.term)

