package leo32.writer

import leo.base.Seq
import leo.base.fold
import leo.base.uint

val systemByteSeqWriter: Writer<Seq<Byte>>
	get() =
		Writer {
			System.out
				.apply { print("\u001Bc") }
				.fold(this) { byte -> apply { write(byte.uint) } }
				.apply { flush() }
			systemByteSeqWriter
		}
