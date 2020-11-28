package leo23.type

import leo13.Stack
import leo13.stack

val numberPlusTypeStack: Stack<Type> get() = stack(numberType, "plus" struct fields(numberType))
val numberMinusTypeStack: Stack<Type> get() = stack(numberType, "minus" struct fields(numberType))
val numberTimesTypeStack: Stack<Type> get() = stack(numberType, "times" struct fields(numberType))
val numberEqualsTypeStack: Stack<Type> get() = stack(numberType, "equals" struct fields(numberType))
val numberTextTypeStack: Stack<Type> get() = stack(numberType, "text".struct)

val textPlusTypeStack: Stack<Type> get() = stack(textType, "plus" struct fields(textType))
val textEqualsTypeStack: Stack<Type> get() = stack(textType, "equals" struct fields(textType))
