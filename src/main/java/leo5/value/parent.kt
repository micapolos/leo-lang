package leo5.value

import leo.base.Empty
import leo.base.empty

sealed class Parent

data class EmptyParent(val empty: Empty): Parent()
data class LinkParent(val link: ParentLink): Parent()

fun parent(): Parent = EmptyParent(empty)
fun parent(link: ParentLink): Parent = LinkParent(link)

val Parent.linkOrNull get() = (this as? LinkParent)?.link
