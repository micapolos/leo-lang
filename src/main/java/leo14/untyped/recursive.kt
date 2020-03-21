package leo14.untyped

data class Recursive(val context: Context)

fun recursive(context: Context) = Recursive(context)