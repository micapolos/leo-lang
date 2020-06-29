package leo16.base

data class Link<Tail, Head>(val tail: Tail, val head: Head)

infix fun <Tail, Head> Tail.linkTo(head: Head) = Link(this, head)
