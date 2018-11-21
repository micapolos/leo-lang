package leo

import leo.base.*
import java.io.File
import java.io.IOException

data class Import(
	val file: File)

val File.import: Import
	get() =
		Import(this)

val Field<Nothing>.parseImport: Import?
	get() =
		match(importWord) { importTermOrNull ->
			(null as File?)
				.fold(importTermOrNull?.fieldStreamOrNull?.reverse) { field ->
					if (field.termOrNull != null) null
					else field.word.toString().let { component ->
						if (this == null) File(component)
						else File(this, component)
					}
				}
				?.run { File("$this.leo").import }
		}

val Import.theBitStreamOrNull: The<Stream<Bit>?>?
	get() =
		try {
			System.out.println("Importing: $file")
			file.readBytes().streamOrNull?.map(Byte::bitStream)?.join?.the
		} catch (e: IOException) {
			System.out.println("Error importing: $file")
			null
		}