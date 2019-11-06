package leo13

import leo.base.notNullIf

data class Link<T, H>(val tail: T, val head: H)

infix fun <T, H> T.linkTo(head: H) = Link(this, head)
val <T, H : Any> Link<T, H>.onlyHeadOrNull get() = notNullIf(tail == null) { head }