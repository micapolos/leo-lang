package leo32.writer

import leo.base.*
import leo.binary.utf8ByteSeq
import leo32.runtime.LeoReader
import leo32.runtime.byteSeq
import leo32.runtime.plus

fun Writer<Seq<Byte>>.leoWriter(leoReader: LeoReader): Writer<Byte> =
	Writer {
		leoReader
			.plus(this)
			?.let { newLeoReader ->
				this@leoWriter.write("Leonardo (v0.1) char: ${this}\n".utf8ByteSeq.then { newLeoReader.byteSeq })
				leoWriter(newLeoReader)
			}
			.orIfNull {
				this@leoWriter.write(leoReader.byteSeq.then { 7.clampedByte.onlySeq })
				leoWriter(leoReader)
			}
	}
