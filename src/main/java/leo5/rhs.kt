package leo5

object Rhs

val rhs = Rhs
fun Rhs.invoke(script: Script) = (script as ApplicationScript).application.line.value.script