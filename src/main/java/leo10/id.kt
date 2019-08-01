package leo10

import leo.base.Empty
import leo.base.empty
import leo.base.fold
import leo.binary.Bit
import leo.binary.utf8BitSeq

sealed class Id

data class EmptyId(val empty: Empty) : Id()
data class LinkId(val link: IdLink) : Id()
data class IdLink(val id: Id, val bit: Bit)

fun id(empty: Empty): Id = EmptyId(empty)
fun id(link: IdLink): Id = LinkId(link)
fun link(id: Id, bit: Bit) = IdLink(id, bit)
operator fun Id.plus(bit: Bit) = id(link(this, bit))

val String.id get() = id(empty).fold(utf8BitSeq, Id::plus)
