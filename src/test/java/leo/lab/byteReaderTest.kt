package leo.lab

import leo.base.byte
import leo.base.fail
import leo.base.stack
import leo.continueWord
import leo.leoWord
import leo.readWord

class ByteReaderTest {
	var noSpacesFunction =
		Function(
			stack(
				rule(
					term(
						leoWord fieldTo term(
							readWord fieldTo term(
								32.byte.reflect.map<Nothing, Choice> { fail }))),
					body(
						term(
							leoWord fieldTo continueWord.term()),
						identityFunction))))

//	@Test
//	fun noSpaces() {
//		ByteReader(
//			ByteEvaluator(
//				TokenEvaluator(
//					null,
//					Scope(
//						noSpacesFunction,
//						null)),
//				null),
//			null)
//			.plus('f'.byte)!!
//			.plus('o'.byte)!!
//			.plus(' '.byte)!!
//			.plus(' '.byte)!!
//			.plus('o'.byte)!!
//			.plus('('.byte)!!
//			.plus(' '.byte)!!
//			.plus(')'.byte)!!
//			.assertEqualTo(null)
//	}
}