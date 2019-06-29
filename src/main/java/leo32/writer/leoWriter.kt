package leo32.writer

import leo.base.*
import leo.binary.utf8ByteSeq
import leo32.LeoReader
import leo32.byteSeq
import leo32.plus

fun Writer<Seq<Byte>>.leoWriter(leoReader: LeoReader): Writer<Byte> =
	Writer {
		leoReader
			.plus(this)
			?.let { newLeoReader ->
				this@leoWriter.write("Leonardo (v0.1) char: ${this}\n".utf8ByteSeq.thenFn { newLeoReader.byteSeq })
				leoWriter(newLeoReader)
			}
			.orIfNull {
				this@leoWriter.write(leoReader.byteSeq.thenFn { 7.clampedByte.onlySeq })
				leoWriter(leoReader)
			}
	}
