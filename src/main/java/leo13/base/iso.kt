package leo13.base

data class Iso<A, B>(val ab: A.() -> B, val ba: B.() -> A)

fun <A, B> iso(ab: A.() -> B, ba: B.() -> A) = Iso(ab, ba)
