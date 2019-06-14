package lib.file.path

import lib.file.Path

data class Name(val path: Path)

fun name(path: Path) = Name(path)
