package lib.folder

import lib.Folder

data class Path(val folder: Folder)

fun path(folder: Folder) = Path(folder)
