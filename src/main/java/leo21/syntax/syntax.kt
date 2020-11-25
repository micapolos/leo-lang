package leo21.syntax

import leo14.lambda.syntax.Syntax
import leo14.lambda.syntax.invoke
import leo14.lambda.syntax.native
import leo14.lambda.syntax.term
import leo21.prim.NumberEqualsNumberPrim
import leo21.prim.NumberMinusNumberPrim
import leo21.prim.NumberPlusNumberPrim
import leo21.prim.NumberStringPrim
import leo21.prim.NumberTimesNumberPrim
import leo21.prim.Prim
import leo21.prim.StringPlusStringPrim
import leo21.prim.prim
import leo21.prim.runtime.value

typealias X = Syntax<Prim>

fun number(int: Int): X = native(prim(int))
fun number(double: Double): X = native(prim(double))
fun text(string: String): X = native(prim(string))

fun X.numberPlusNumber(rhs: X): X = op2(NumberPlusNumberPrim, rhs)
fun X.numberMinusNumber(rhs: X): X = op2(NumberMinusNumberPrim, rhs)
fun X.numberTimesNumber(rhs: X): X = op2(NumberTimesNumberPrim, rhs)
fun X.numberEqualsNumber(rhs: X): X = op2(NumberEqualsNumberPrim, rhs)
val X.numberText: X get() = op(NumberStringPrim)

fun X.textPlusText(rhs: X): X = op2(StringPlusStringPrim, rhs)

fun X.switch(vararg cases: (X) -> X): X = TODO()

fun fn(f: (X) -> X): X = leo14.lambda.syntax.fn(f)
fun recFn(f: (X, X) -> X): X = leo14.lambda.syntax.recFn(f)
infix fun X.pairTo(rhs: X) = fn { lhs -> fn { rhs -> fn { f -> f.invoke(lhs).invoke(rhs) } } }.invoke(this).invoke(rhs)

fun X.op(prim: Prim): X = native(prim).invoke(this)
fun X.op2(prim: Prim, rhs: X): X = native(prim).invoke(this pairTo rhs)

val X.value get() = term.value