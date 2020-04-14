package leo15

import leo14.untyped.*
import leo14.untyped.typed.javaType
import leo14.untyped.typed.lineTo
import leo14.untyped.typed.type

val javaClassTypeLine = className lineTo javaType
val javaClassType = type(javaClassTypeLine)

val javaFieldTypeLine = fieldName lineTo javaType
val javaFieldType = type(javaFieldTypeLine)

val javaConstructorTypeLine = constructorName lineTo javaType
val javaConstructorType = type(javaConstructorTypeLine)

val javaMethodTypeLine = methodName lineTo javaType
val javaMethodType = type(javaMethodTypeLine)

val javaParameterTypeLine = parameterName lineTo javaType
val javaParameterType = type(javaParameterTypeLine)

val javaClassParameterTypeLine = parameterName lineTo javaClassType
val javaClassParameterType = type(javaClassParameterTypeLine)

