package leo14.untyped.typed

import leo13.*
import leo13.base.negate
import leo14.lambda.runtime.*
import java.lang.reflect.Constructor
import java.lang.reflect.Field
import java.lang.reflect.Method

typealias ValueFn = () -> Value

data class Expression(val valueFn: ValueFn)

fun expression(valueFn: ValueFn) = Expression(valueFn)

val Expression.value get() = valueFn()
val Expression.boolean get() = value.asBoolean
val Expression.string get() = value.asString
val Expression.int get() = value.asInt
val Expression.number get() = value.asNumber
val Expression.array get() = value as Array<*>
val Expression.function get() = value.asF
val Expression.class_ get() = value as Class<*>
val Expression.method get() = value as Method
val Expression.constructor get() = value as Constructor<*>
val Expression.field get() = value as Field
val Expression.stack get() = value as Stack<Expression>

fun Expression.eq(rhs: Expression) = expression { value == rhs.value }

val Expression.booleanNegate get() = expression { boolean.negate }
fun Expression.booleanAndBoolean(rhs: Expression) = expression { boolean and rhs.boolean }
fun Expression.booleanOrBoolean(rhs: Expression) = expression { boolean or rhs.boolean }
fun Expression.booleanXorBoolean(rhs: Expression) = expression { boolean xor rhs.boolean }

val Expression.intUnaryMinus get() = expression { int.unaryMinus() }
fun Expression.intPlusInt(rhs: Expression) = expression { int + rhs.int }
fun Expression.intMinusInt(rhs: Expression) = expression { int - rhs.int }
fun Expression.intTimesInt(rhs: Expression) = expression { int * rhs.int }
val Expression.intString get() = expression { int.toString() }

fun Expression.stringPlusString(rhs: Expression) = expression { string + rhs.string }
val Expression.stringLength get() = expression { string.length }

fun Expression.functionInvokeValue(rhs: Expression) = expression { function(rhs.value) }

// === Reflection ===

val Expression.stringClass
	get() =
		expression { Expression::class.java.classLoader.loadClass(string) }

fun Expression.classConstructor(rhs: Expression) =
	expression { class_.getConstructor(*rhs.stack.map { class_ }.array) }

fun Expression.classMethod(rhs: Expression) =
	expression { class_.getMethod(rhs.stack.link.stack.top.string, *rhs.stack.top.stack.map { class_ }.array) }

fun Expression.classField(rhs: Expression) =
	expression { class_.getField(rhs.string) }

fun Expression.constructorInvoke(rhs: Expression) =
	expression { constructor.newInstance(*rhs.stack.map { value }.array) }

fun Expression.methodInvoke(rhs: Expression) =
	expression { method.invoke(rhs.stack.link.stack.top.value, *rhs.stack.top.stack.map { value }.array) }

fun Expression.fieldGet(rhs: Expression) =
	expression { field.get(rhs.value) }
