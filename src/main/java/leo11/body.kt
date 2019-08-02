package leo11

data class Body(val function: Function, val script: Script)

fun body(function: Function, script: Script) = Body(function, script)

// TODO: Implement
fun Body.apply(script: Script) = this.script