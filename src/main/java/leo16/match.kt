package leo16

import leo.base.notNullIf
import leo.base.orIfNull
import leo13.EmptyStack
import leo13.LinkStack
import leo13.Stack
import leo13.StackLink
import leo13.asStack
import leo13.fold
import leo13.linkTo
import leo13.map
import leo13.onlyStack
import leo13.push
import leo13.stack
import leo16.names.*

data class Selector(val wordStackLink: StackLink<String>) {
	override fun toString() = asField.toString()
	val asValue get() = wordStackLink.map { this() }.value
	val asField get() = _selector(asValue)
}

data class Case(val selector: Selector, val compiled: Compiled) {
	override fun toString() = asField.toString()
	val asValue: Value
		get() =
			selector.wordStackLink.stack.map { this() }
				.push(selector.wordStackLink.value(compiled.bodyValue))
				.value
	val asField: Field get() = _case(asValue)
}

data class Match(val caseStack: Stack<Case>) {
	override fun toString() = asField.toString()
	val asField: Field get() = _match(asValue)
	val asValue: Value get() = value().fold(caseStack.map { asValue }) { plus(it) }
}

val StackLink<String>.selector get() = Selector(this)
val String.selector get() = stack<String>().linkTo(this).selector
infix fun Selector.caseTo(compiled: Compiled) = Case(this, compiled)
val CompiledSentence.case: Case get() = word.selector.caseTo(compiled)
val Stack<Case>.match get() = Match(this)

fun Selector?.plus(word: String): Selector =
	(if (this == null) stack() else wordStackLink.asStack).linkTo(word).selector

fun Case.plusOrNull(sentence: CompiledSentence): Case? =
	notNullIf(this.compiled.isEmpty) {
		selector.plus(sentence.word).caseTo(sentence.compiled)
	}

fun Match.plus(sentence: CompiledSentence): Match =
	when (caseStack) {
		is EmptyStack -> sentence.case.onlyStack.match
		is LinkStack ->
			caseStack.link.value.plusOrNull(sentence)
				?.let { caseStack.link.stack.push(it) }
				.orIfNull { caseStack.push(sentence.case) }
				.match
	}
