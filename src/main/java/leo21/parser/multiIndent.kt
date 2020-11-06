package leo21.parser

import leo13.StackLink
import leo13.push
import leo13.stackLink

inline class MultiIndent(val indentStackLink: StackLink<Indent>)

val Indent.multiIndent: MultiIndent get() = MultiIndent(stackLink(this))
fun MultiIndent.plus(indent: Indent) = MultiIndent(indentStackLink.push(indent))
