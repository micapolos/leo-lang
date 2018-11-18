package leo

import leo.base.clampedByte
import leo.base.fail
import leo.base.stack

class ByteReaderTest {
	var noSpacesFunction =
		Function(
			stack(
				rule(
					term(
						leoWord fieldTo term(
							readWord fieldTo term(
								32.clampedByte.reflect.map<Nothing, Choice> { fail }))),
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