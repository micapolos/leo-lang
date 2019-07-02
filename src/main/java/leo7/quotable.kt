package leo7

sealed class Quotable<out T>

data class RawQuotable<T>(val raw: Raw<T>) : Quotable<T>()
data class QuotedQuotable<T>(val quoted: Quoted<Quotable<T>>) : Quotable<T>()

val <T> Raw<T>.quotable: Quotable<T> get() = RawQuotable(this)
val <T> Quoted<Quotable<T>>.quotable: Quotable<T> get() = QuotedQuotable(this)

val <T> Quotable<T>.quote: Quotable<T>
	get() =
		quoted.quotable

val <T> Quotable<T>.unquote
	get() = when (this) {
		is RawQuotable -> null
		is QuotedQuotable -> quoted.unquoted
	}
