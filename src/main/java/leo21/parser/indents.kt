package leo21.parser

import leo.base.nullOf
import leo13.Link
import leo13.linkTo

inline class Indents(val link: Link<Indents?, Indent>)

val Indent.indents: Indents get() = Indents(nullOf<Indents>() linkTo this)
fun Indents.plus(indent: Indent) = Indents(this linkTo indent)
