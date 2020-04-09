package leo14.untyped.typed

import leo14.untyped.*

val minusNumberType = type(minusName(numberType))
val textNumberType = type(textName(numberType))
val textPlusTextType = type(textTypeLine, plusName(textType))
val numberPlusNumberType = type(numberTypeLine, plusName(numberType))
val numberMinusNumberType = type(numberTypeLine, minusName(numberType))
val numberTimesNumberType = type(numberTypeLine, timesName(numberType))

val Type.listType: Type get() = type(listName lineTo repeating.toType)