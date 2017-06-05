package com.badlogic.gdx.ingenuity.test.protocol;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.alibaba.fastjson.JSONObject;

/**
 * @作者 mitkey
 * @时间 2017年6月3日 下午12:24:20
 * @类说明 PacketBuffer.java <br/>
 * @版本 0.0.1
 */
public class PacketBuffer {

	/**
	 * <pre>
	 * 	信息的长度 4 个字节 ---- 协议的版本 1 个字节 ---- 状态码 1 个字节 ---- 消息的 id 标识 4 个字节 --- 接口名字的长度 2 个字节 ---- 接口名字 bytes ... ---- 消息体 bytes ...
	 * 	int					byte					byte			int 					short					byte[]					byte[]
	 * </pre>
	 **/

	// 标识消息长度占用的字节数
	private static final byte FIXED_MSG_LEN = 4;
	// 标识协议版本占用的字节数
	private static final byte FIXED_PROTOCOL_VERSION_LEN = 1;
	// 标识异常状态占用的字节数
	private static final byte FIXED_EX_LEN = 1;
	// 标识消息序列占用的字节数
	private static final byte FIXED_RI_LEN = 4;
	// 标识接口名字占用的字节数
	private static final byte FIXED_SI_NAME_LEN = 2;

	// 已明确数量的字节数量，不包含 msgLen 占用的字节数
	private static final byte FIXED_FULL_BODY_LEN_FILLER_MSG_LEN = FIXED_PROTOCOL_VERSION_LEN + FIXED_EX_LEN + FIXED_RI_LEN + FIXED_SI_NAME_LEN;
	// 已明确数量的字节数量
	private static final byte FIXED_FULL_BODY_LEN = FIXED_MSG_LEN + FIXED_FULL_BODY_LEN_FILLER_MSG_LEN;

	private static final AtomicInteger RI = new AtomicInteger(0);
	private static ByteBuffer DECODE_BYTE_BUFFER;

	/** 编码 */
	public static byte[] encode(String si, JSONObject body) {
		byte[] bytesSi = si.getBytes();
		byte[] bytesBody = body.toJSONString().getBytes();

		int siLen = bytesSi.length;
		int bodyLen = bytesBody.length;
		// 消息的总长度
		int msgLen = FIXED_FULL_BODY_LEN_FILLER_MSG_LEN + siLen + bodyLen;

		ByteBuffer byteBuffer = ByteBuffer.allocate(msgLen + FIXED_MSG_LEN);
		// 消息的长度 4 个字节，int
		byteBuffer.putInt(msgLen);
		// 协议的版本 1 个字节，byte
		byteBuffer.put((byte) 1);
		// 状态码 1 个字节，byte
		byteBuffer.put((byte) 0);
		// 消息 id 标识 4 个字节，int
		byteBuffer.putInt(RI.incrementAndGet());
		// 接口名字占用的字节数 2 个字节，short
		byteBuffer.putShort((short) siLen);
		// 接口名字 byte[]
		byteBuffer.put(bytesSi);
		// 消息体 byte[]
		byteBuffer.put(bytesBody);

		byteBuffer.flip();
		byte[] data = new byte[byteBuffer.remaining()];
		byteBuffer.get(data);
		return data;
	}

	/** 解码 */
	public static List<JeffData> decode(byte[] bs) {
		List<JeffData> result = new ArrayList<>();

		if (DECODE_BYTE_BUFFER == null) {
			// 第一帧
			DECODE_BYTE_BUFFER = ByteBuffer.wrap(bs);
		} else {
			// 不是第一帧
			ByteBuffer temp = ByteBuffer.allocate(DECODE_BYTE_BUFFER.capacity() + bs.length);
			DECODE_BYTE_BUFFER.clear();
			temp.put(DECODE_BYTE_BUFFER);
			temp.put(bs);
			DECODE_BYTE_BUFFER = temp;
			DECODE_BYTE_BUFFER.flip();
		}

		// 剩余可读有效字节数
		while (DECODE_BYTE_BUFFER.remaining() >= FIXED_FULL_BODY_LEN) {
			// 该消息的长度
			int msgLen = DECODE_BYTE_BUFFER.getInt();
			// 协议版本
			byte protocolVersion = DECODE_BYTE_BUFFER.get();
			// 状态码
			byte ex = DECODE_BYTE_BUFFER.get();
			// 消息 id
			int ri = DECODE_BYTE_BUFFER.getInt();
			// 接口名字占用字节数
			short siLen = DECODE_BYTE_BUFFER.getShort();
			if (DECODE_BYTE_BUFFER.remaining() < siLen) {
				break;
			}

			// 接口的名字
			byte[] siNameBytes = new byte[siLen];
			DECODE_BYTE_BUFFER.get(siNameBytes);

			// 消息体占用字节数。剩下的就是 body 的消息长度
			int bodyLen = msgLen - FIXED_FULL_BODY_LEN_FILLER_MSG_LEN - siLen;
			if (DECODE_BYTE_BUFFER.remaining() < bodyLen) {
				break;
			}

			// 消息体
			byte[] bodyBytes = new byte[bodyLen];
			DECODE_BYTE_BUFFER.get(bodyBytes);

			result.add(new JeffData(protocolVersion, ri, ex, new String(siNameBytes), new String(bodyBytes)));
		}

		// 该次数据是完整的已读完
		if (!DECODE_BYTE_BUFFER.hasRemaining()) {
			DECODE_BYTE_BUFFER = null;
		}
		return result;
	}

	public static final class JeffData {
		private byte protocolVersion;
		private int ri;
		private byte ex;
		private String si;
		private String body;

		public JeffData(byte protocolVersion, int ri, byte ex, String si, String body) {
			super();
			this.protocolVersion = protocolVersion;
			this.ri = ri;
			this.ex = ex;
			this.si = si;
			this.body = body;
		}

		public byte getProtocolVersion() {
			return protocolVersion;
		}

		public int getRi() {
			return ri;
		}

		public byte getEx() {
			return ex;
		}

		public String getSi() {
			return si;
		}

		public String getBody() {
			return body;
		}
	}

}
