package leo21.quotable

import leo14.ScriptLine
import leo14.Scriptable
import leo14.anyReflectScriptLine
import leo14.lineTo
import leo14.script

sealed class Quotable<out T> : Scriptable() {
	override val reflectScriptLine: ScriptLine
		get() = "quotable" lineTo script(
			when (this) {
				is UnquotedQuotable -> "unquoted" lineTo script(value.anyReflectScriptLine)
				is QuotedQuotable -> "quoted" lineTo script(quotable.reflectScriptLine)
			})
}

data class UnquotedQuotable<T>(val value: T) : Quotable<T>() {
	override fun toString() = super.toString()
}

data class QuotedQuotable<T>(val quotable: Quotable<T>) : Quotable<T>() {
	override fun toString() = super.toString()
}

val <T> T.quotable: Quotable<T> get() = UnquotedQuotable(this)
val <T> Quotable<T>.quote: Quotable<T> get() = QuotedQuotable(this)
val <T> Quotable<T>.unquote: Quotable<T> get() = unquoteOrNull!!
val <T> Quotable<T>.unquoteOrNull: Quotable<T>? get() = (this as? QuotedQuotable)?.quotable
val <T> Quotable<T>.value: T get() = valueOrNull!!
val <T> Quotable<T>.valueOrNull: T? get() = (this as? UnquotedQuotable)?.value
