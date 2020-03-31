package leo14.untyped

import leo.base.notNullIf

data class Binding(val key: Thunk, val value: Thunk)

fun binding(key: Thunk, value: Thunk) = Binding(key, value)
fun Thunk.bindingTo(value: Thunk) = Binding(this, value)
fun Binding.apply(thunk: Thunk): Thunk? = notNullIf(key == thunk) { value }