package lib.folder

import lib.Folder

data class Close(val folder: Folder)

fun close(folder: Folder) = Close(folder)
