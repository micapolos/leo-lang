package leo21.native

import leo14.number

val nil: Native = NilNative
fun boolean(boolean: Boolean): Native = BooleanNative(boolean)
fun text(string: String): Native = StringNative(string)
fun number(int: Int): Native = NumberNative(int.number)
fun number(double: Double): Native = NumberNative(double.number)
val Native.isNil: Native get() = IsNilNative(this)
operator fun Native.plus(rhs: Native): Native = PlusNative(this, rhs)
operator fun Native.minus(rhs: Native): Native = MinusNative(this, rhs)
operator fun Native.times(rhs: Native): Native = TimesNative(this, rhs)
fun Native.numberEquals(rhs: Native): Native = EqualsNative(this, rhs)
val Native.numberText: Native get() = NumberStringNative(this)
fun Native.textAppend(rhs: Native): Native = StringAppendNative(this, rhs)
fun Native.textEquals(rhs: Native): Native = StringEqualsNative(this, rhs)
val Native.textNumberOrNil: Native get() = StringNumberOrNilNative(this)
infix fun Native.pairTo(rhs: Native): Native = PairNative(this, rhs)
val Native.lhs: Native get() = LhsNative(this)
val Native.rhs: Native get() = RhsNative(this)
fun vector(vararg natives: Native): Native = VectorNative(listOf(*natives))
operator fun Native.get(index: Native): Native = VectorAtNative(this, index)
fun Native.ifThenElse(trueNative: Native, falseNative: Native): Native = ConditionalNative(this, trueNative, falseNative)
fun fn(arity: Int, body: Native): Native = FunctionNative(arity, body)
fun Native.apply(vararg params: Native): Native = ApplyNative(this, listOf(*params))
fun v(index: Int): Native = VariableNative(index)

fun fn0(body: Native) = fn(0, body)
fun fn1(body: Native) = fn(1, body)
fun fn2(body: Native) = fn(2, body)

val v0 = v(0)
val v1 = v(1)
val v2 = v(2)
