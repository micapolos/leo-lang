package leo

import leo.base.The
import leo.base.the

val Term<Nothing>.invokeExtern: The<Term<Nothing>?>?
	get() = the
//		onlyFieldOrNull?.parseImport?.let { import ->
//			emptyBitReader
//				.orNull
//				.fold(import.theBitStreamOrNull?.value) { bit ->
//					this?.read(bit)
//				}
//				?.bitEvaluator
//				?.byteReader
//				?.byteEvaluator
//				?.characterReader
//				?.characterEvaluator
//				?.tokenReader
//				?.tokenEvaluator
//				?.theEvaluatedTermOrNull
//		}
