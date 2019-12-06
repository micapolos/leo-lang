package leo14.lambda

data class Value<T>(val scope: Scope<T>, val term: Term<T>)

fun <T> Scope<T>.value(term: Term<T>) = Value(this, term)
val <T> Term<T>.value get() = scope<T>().value(this)
