package leo13.untyped

import leo13.script.*

object ObjectScript

val script = ObjectScript

val objectScriptName = "name"
val objectScriptReader: Reader<ObjectScript> = reader("script", script)
val objectScriptWriter: Writer<ObjectScript> = writer("script", script)

fun ObjectScript.matches(script: Script): Boolean = true
