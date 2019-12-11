package leo14.lib

import leo14.lambda.invoke
import leo14.lambda.native
import leo14.lambda.nativeEval
import leo14.lambda.term
import leo14.native.native
import leo14.native.string
import leo14.native.stringPlusStringNative
import leo14.typed.textLine

data class Text(override val term: Term) : Obj() {
	override val typeLine get() = textLine
	override fun toString() = super.toString()

	constructor(string: String) : this(term(native(string)))

	val string get() = term.native.string
	operator fun plus(text: Text) =
		term(stringPlusStringNative).invoke(term).invoke(text.term).nativeEval.text
}

val String.text get() = Text(this)
val Term.text get() = Text(this)
