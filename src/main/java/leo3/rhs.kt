package leo3

object Rhs

val rhs = Rhs

fun Rhs.apply(script: Script): Script =
	script.nodeOrNull!!.run { script(rhs) }
