//package leo.lab
//
//import leo.base.Bit
//import leo.leoWord
//import leo.readWord
//
//data class Preprocessor(
//	val bitReader: BitReader,
//	val termOrNull: Term<Nothing>?)
//
//val emptyPreprocessor =
//	Preprocessor(emptyBitReader, null)
//
//fun Preprocessor.plus(bit: Bit): Preprocessor? =
//	this
//		.push(leoWord fieldTo term(readWord fieldTo bit.reflect.term))
//		.evaluate
//		.
//
//
//	termOrNull
//		.push(leoWord fieldTo term(readWord fieldTo bit.reflect.term))
//		.let { nextTerm ->
//			readerFn(nextTerm)
//				.let { resultValueTerm ->
//					resultValueTerm.structureTermOrNull?.let { resultTerm ->
//						resultTerm.fieldStack.reverse.stream
//							.foldFirst { topField ->
//								if (topField != leoReaderField<Value>()) null
//								else to(reader.copy(valueTerm = leoReaderTerm()))
//							}
//							.foldNext { followingField ->
//								this?.let { (folded, reader) ->
//									when {
//										followingField.key != readWord -> null
//										else -> followingField.value.match(byteWord) { byteValue ->
//											byteWord.fieldTo(byteValue).parseByte?.let { byte ->
//												readFn(folded, byte) to reader.copy(valueTerm = leoReaderTerm())
//											}
//										}
//									}
//								}
//							}
//					} ?: to(reader.copy(valueTerm = resultValueTerm))
//				}
//		}
//
//
//fun Preprocessor.push(field: Field<Nothing>): Preprocessor =
//	copy(termOrNull = termOrNull.push(field))
//
//val Preprocessor.evaluate: Preprocessor
//  get() =
//	  if (termOrNull == null) this
//		else copy(termOrNull = bitReader.byteReader.tokenReader.scope.function.invoke(termOrNull))
//
//val Preprocessor.parse: Preprocessor? =
//	if (termO)
//	resultValueTerm.structureTermOrNull?.let { resultTerm ->
//		resultTerm.fieldStack.reverse.stream
//			.foldFirst { topField ->
//				if (topField != leoReaderField<Value>()) null
//				else to(reader.copy(valueTerm = leoReaderTerm()))
//			}
//			.foldNext { followingField ->
//				this?.let { (folded, reader) ->
//					when {
//						followingField.key != readWord -> null
//						else -> followingField.value.match(byteWord) { byteValue ->
//							byteWord.fieldTo(byteValue).parseByte?.let { byte ->
//								readFn(folded, byte) to reader.copy(valueTerm = leoReaderTerm())
//							}
//						}
//					}
//				}
//			}
//	} ?: to(reader.copy(valueTerm = resultValueTerm))
