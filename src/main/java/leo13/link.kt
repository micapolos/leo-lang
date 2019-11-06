package leo13

import leo.base.notNullIf

data class Link<T: Any, H>(val tail: T?, val head: H)
infix fun <T: Any, H> T?.linkTo(head: H) = Link(this, head)
val <T: Any, H: Any> Link<T, H>.onlyHeadOrNull get() = notNullIf(tail == null) { head }