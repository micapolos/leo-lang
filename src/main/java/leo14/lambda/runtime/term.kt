@file:Suppress("UNCHECKED_CAST")

package leo14.lambda.runtime

typealias X = Any?
typealias F = (Any?) -> Any?

fun fn(f: F): X = f
operator fun X.invoke(x: X): X = (this as F)(x)
