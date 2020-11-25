package leo23.term

import leo14.number

val nil: Term get() = NilTerm
fun boolean(boolean: Boolean): Term = BooleanTerm(boolean)
fun text(string: String): Term = StringTerm(string)
fun number(int: Int): Term = NumberTerm(int.number)
fun number(double: Double): Term = NumberTerm(double.number)
val Term.isNil: Term get() = IsNilTerm(this)
operator fun Term.plus(rhs: Term): Term = PlusTerm(this, rhs)
operator fun Term.minus(rhs: Term): Term = MinusTerm(this, rhs)
operator fun Term.times(rhs: Term): Term = TimesTerm(this, rhs)
fun Term.numberEquals(rhs: Term): Term = EqualsTerm(this, rhs)
val Term.numberText: Term get() = NumberStringTerm(this)
fun Term.textAppend(rhs: Term): Term = StringAppendTerm(this, rhs)
fun Term.textEquals(rhs: Term): Term = StringEqualsTerm(this, rhs)
val Term.textNumberOrNil: Term get() = StringNumberOrNilTerm(this)
infix fun Term.pairTo(rhs: Term): Term = PairTerm(this, rhs)
val Term.lhs: Term get() = LhsTerm(this)
val Term.rhs: Term get() = RhsTerm(this)
fun vector(vararg terms: Term): Term = VectorTerm(listOf(*terms))
operator fun Term.get(index: Term): Term = VectorAtTerm(this, index)
fun Term.ifThenElse(trueTerm: Term, falseTerm: Term): Term = ConditionalTerm(this, trueTerm, falseTerm)
fun fn(arity: Int, body: Term): Term = FunctionTerm(arity, body)
fun Term.apply(vararg params: Term): Term = ApplyTerm(this, listOf(*params))
fun v(index: Int): Term = VariableTerm(index)

fun fn0(body: Term) = fn(0, body)
fun fn1(body: Term) = fn(1, body)
fun fn2(body: Term) = fn(2, body)

val v0 get() = v(0)
val v1 get() = v(1)
val v2 get() = v(2)
