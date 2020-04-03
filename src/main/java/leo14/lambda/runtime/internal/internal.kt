package leo14.lambda.runtime.internal

typealias Fn = (Any?) -> Any?

fun Fn._invoke(any: Any?) = this(any)