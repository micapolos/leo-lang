package leo5.script

data class PathNonEmpty(val string: String, val path: Path)

fun nonEmpty(string: String, path: Path) = PathNonEmpty(string, path)