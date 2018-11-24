package leo

val Term<Nothing>.invokeExtern: Term<Nothing>?
	get() = null
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
