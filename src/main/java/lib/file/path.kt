package lib.file

import lib.File

data class Path(val file: File)

fun path(file: File) = Path(file)