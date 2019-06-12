package leo5.script

data class Extension(val script: Script, val line: Line)

fun extension(script: Script, line: Line) = Extension(script, line)
fun Appendable.append(extension: Extension) = append(extension.script).append(extension.line)
