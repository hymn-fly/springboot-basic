package org.prgms.utils;

import org.springframework.util.Assert;

import java.nio.ByteBuffer;
import java.util.UUID;

public final class UuidUtils {

    // 정적 메서드 클래스로 활용할 것임. 생성자 이용 금지
    private UuidUtils() {
    }

    public static byte[] uuidToBytes(UUID uuid) {
        Assert.notNull(uuid, "UUID 필수");

        var byteBuffer = ByteBuffer.wrap(new byte[16]);

        byteBuffer.putLong(uuid.getMostSignificantBits());
        byteBuffer.putLong(uuid.getLeastSignificantBits());

        return byteBuffer.array();
    }

    public static UUID bytesToUUID(byte[] bytes) {
        Assert.notNull(bytes, "bytes 필수");

        var byteBuffer = ByteBuffer.wrap(bytes);

        return new UUID(byteBuffer.getLong(), byteBuffer.getLong());
    }
}
