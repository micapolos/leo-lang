package leo5.script

data class Extension(val script: Script, val line: Line)

fun Appendable.append(extension: Extension) = append(extension.script).append(extension.line)
