package leo4

import leo.base.Empty

sealed class Script
data class EmptyScript(val empty: Empty) : Script()
data class TermScript(val term: Term) : Script()

fun script(empty: Empty): Script = EmptyScript(empty)
fun script(term: Term): Script = TermScript(term)