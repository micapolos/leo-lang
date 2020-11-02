package leo21.term

import leo14.lambda.Term
import leo14.lambda.id
import leo14.lambda.pair
import leo21.value.Value
import leo21.value.value

fun <T> Term<T>.plus(term: Term<T>): Term<T> = pair(this, term)

val nilTerm = id<Value>()
fun term(string: String) = leo14.lambda.term(value(string))
fun term(double: Double) = leo14.lambda.term(value(double))
fun term(int: Int) = term(int.toDouble())
