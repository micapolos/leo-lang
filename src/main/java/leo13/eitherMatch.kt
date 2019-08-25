package leo13

data class EitherMatch(val either: Either, val script: Script)

fun match(either: Either, script: Script) = EitherMatch(either, script)
