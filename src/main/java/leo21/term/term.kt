package leo21.term

import leo14.lambda.Term
import leo14.lambda.invoke
import leo14.lambda.nativeTerm
import leo14.lambda.pair
import leo21.prim.NumberEqualsNumberPrim
import leo21.prim.NumberMinusNumberPrim
import leo21.prim.NumberPlusNumberPrim
import leo21.prim.NumberStringPrim
import leo21.prim.NumberTimesNumberPrim
import leo21.prim.Prim
import leo21.prim.StringPlusStringPrim
import leo21.prim.nilPrim
import leo21.prim.prim

fun <T> Term<T>.plus(term: Term<T>): Term<T> = pair(this, term)

val nilTerm = nativeTerm(nilPrim)
fun term(string: String) = nativeTerm(prim(string))
fun term(double: Double) = nativeTerm(prim(double))
fun term(int: Int) = term(int.toDouble())
fun arg(index: Int): Term<Prim> = leo14.lambda.arg(index)

fun Term<Prim>.numberPlusNumber(rhs: Term<Prim>) = op(NumberPlusNumberPrim, rhs)
fun Term<Prim>.numberMinusNumber(rhs: Term<Prim>) = op(NumberMinusNumberPrim, rhs)
fun Term<Prim>.numberTimesNumber(rhs: Term<Prim>) = op(NumberTimesNumberPrim, rhs)
fun Term<Prim>.numberEqualsNumber(rhs: Term<Prim>) = op(NumberEqualsNumberPrim, rhs)
val Term<Prim>.numberString get() = op(NumberStringPrim)

fun Term<Prim>.stringPlusString(rhs: Term<Prim>) = op(StringPlusStringPrim, rhs)

fun Term<Prim>.op(prim: Prim) = nativeTerm(prim).invoke(this)
fun Term<Prim>.op(prim: Prim, rhs: Term<Prim>) = nativeTerm(prim).invoke(this.plus(rhs))
