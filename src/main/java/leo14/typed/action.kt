package leo14.typed

data class Action<T>(val param: Type, val body: Typed<T>)

infix fun <T> Type.ret(body: Typed<T>) = Action(this, body)
