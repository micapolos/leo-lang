package leo14.lambda

data class Value<T>(val scope: Scope<T>, val term: Term<T>)

fun <T> value(scope: Scope<T>, body: Term<T>) = Value(scope, body)
