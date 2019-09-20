package leo13.value

import leo.base.ifOrNull
import leo.base.notNullIf
import leo13.LeoObject
import leo13.Stack
import leo13.script.*
import leo13.stack

object Outside : LeoObject() {
	override val scriptableName get() = "outside"
	override val scriptableBody get() = script()
}

val outside = Outside

data class Given(val previousStack: Stack<Outside>) : LeoObject() {
	override fun toString() = super.toString()
	override val scriptableName get() = "given"
	override val scriptableBody get() = previousStack.asScript { scriptableLine }
}

val Stack<Outside>.given get() = Given(this)
fun given(vararg outsides: Outside) = stack(*outsides).given

val Script.givenOrNull
	get() =
		onlyLineOrNull?.givenOrNull

val ScriptLine.givenOrNull: Given?
	get() =
		ifOrNull(name == "given") {
			rhs.rhsGivenOrNull
		}

// TODO: Support "outside"
val Script.rhsGivenOrNull: Given?
	get() =
		notNullIf(isEmpty) { given() }
