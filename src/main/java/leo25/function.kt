package leo25

data class Function(val context: Context, val body: Body)

fun Context.function(body: Body): Function = Function(this, body)

fun Function.apply(value: Value): Value = body.apply(context, value)