package leo14.untyped.typed

import leo14.Begin
import leo14.untyped.*

data class Type(val thunk: Thunk)

fun type(thunk: Thunk) = Type(thunk)

val emptyType = type(emptyThunk)
val numberType = type(thunk(value(numberName)))
val stringType = type(thunk(value(stringName)))
val intType = type(thunk(value(intName)))
val booleanType = type(thunk(value(booleanName)))
val textType = type(thunk(value(textName)))
val nativeType = type(thunk(value(nativeName)))
val compiledType = type(thunk(value(compiledName)))
val classType = type(thunk(value(className)))
val constructorType = type(thunk(value(constructorName)))
val methodType = type(thunk(value(methodName)))
val fieldType = type(thunk(value(fieldName)))

fun Type.append(begin: Begin, type: Type) =
	type(thunk.plus(begin.string lineTo type.thunk))

val Type.isText get() = this == textType

val Type.split: Pair<Type, Type>?
	get() =
		thunk.value.let { value ->
			when (value) {
				is EmptyValue -> null
				is SequenceValue -> type(value.sequence.previousThunk) to type(thunk(value(value.sequence.lastLine)))
			}
		}
