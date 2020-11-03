package leo21.term

import leo14.lambda.Term
import leo14.lambda.nativeTerm
import leo14.lambda.pair
import leo21.prim.nilPrim
import leo21.prim.prim

fun <T> Term<T>.plus(term: Term<T>): Term<T> = pair(this, term)

val nilTerm = nativeTerm(nilPrim)
fun term(string: String) = leo14.lambda.term(prim(string))
fun term(double: Double) = leo14.lambda.term(prim(double))
fun term(int: Int) = term(int.toDouble())
