package leo14.typed

data class Function<T>(val param: Type, val body: Typed<T>)

infix fun <T> Type.ret(body: Typed<T>) = Function(this, body)
