package lib.folder.path

import lib.folder.Path

data class Name(val path: Path)

fun name(path: Path) = Name(path)
