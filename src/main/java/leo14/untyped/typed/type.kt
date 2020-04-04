package leo14.untyped.typed

import leo14.Begin
import leo14.untyped.*

data class Type(val thunk: Thunk)

fun type(thunk: Thunk) = Type(thunk)

val emptyType = type(emptyThunk)
val numberType = type(thunk(value(numberName)))
val textType = type(thunk(value(textName)))
val nativeType = type(thunk(value(nativeName)))
val compiledType = type(thunk(value(compiledName)))

fun Type.append(begin: Begin, type: Type) =
	type(thunk.plus(begin.string lineTo type.thunk))

val Type.isText get() = this == textType