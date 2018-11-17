package leo.base

import java.io.OutputStream

val OutputStream.byteWriter: Writer<Byte>
	get() =
		Writer {
			this@byteWriter.write(this.toInt())
			this@byteWriter.byteWriter
		}

val OutputStream.bitWriter: Writer<Bit>
	get() =
		Writer {
			Writer {
				Writer {
					Writer {
						Writer {
							Writer {
								Writer {
									Writer {
										Writer {
											int.or(int.or(int.or(int.or(int.or(int.or(int.or(int.or(int.shl(1)).shl(1)).shl(1)).shl(1)).shl(1)).shl(1)).shl(1)).shl(1)).let { int ->
												this@bitWriter.write(int)
												this@bitWriter.bitWriter
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}