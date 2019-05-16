package leo3

object Lhs

val lhs = Lhs

fun Lhs.apply(script: Script): Script =
	script.termOrNull!!.run { script(lhs) }
