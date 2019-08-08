package leo13

import leo9.*

data class TypedValueBindings(val typedValueStack: Stack<TypedValue>)

val Stack<TypedValue>.typedValueBindings get() = TypedValueBindings(this)
fun TypedValueBindings.plus(typedValue: TypedValue) = typedValueStack.push(typedValue).typedValueBindings
fun typedValueBindings(vararg typedValues: TypedValue) = stack(*typedValues).typedValueBindings
fun TypedValueBindings.typedValueOrNull(argument: Argument) = typedValueStack.drop(argument.previousStack)?.linkOrNull?.value
val TypedValueBindings.valueBindings get() = typedValueStack.map { value }.valueBindings