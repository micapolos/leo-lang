package leo14.untyped.typed

data class Binding(val fromType: Type, val toCompiled: Compiled)

fun binding(fromType: Type, toTyped: Compiled) = Binding(fromType, toTyped)