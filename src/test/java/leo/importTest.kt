package leo

import leo.base.assertEqualTo
import java.io.File
import kotlin.test.Test

class ImportTest {
	@Test
	fun parseEmpty() {
		importWord
			.field
			.parseImport
			.assertEqualTo(null)
	}

	@Test
	fun parseNonEmpty() {
		importWord
			.fieldTo(
				term(
					oneWord.field,
					twoWord.field))
			.parseImport
			.assertEqualTo(File("one/two.leo").import)
	}

	@Test
	fun bitStream() {
		File("import-test.leo")
			.import
			.theBitStreamOrNull
			.assertEqualTo(null)
	}
}