package leo13.type

import leo13.script.Script

data class EitherMatch(val either: Either, val script: Script)

fun match(either: Either, script: Script) = EitherMatch(either, script)
