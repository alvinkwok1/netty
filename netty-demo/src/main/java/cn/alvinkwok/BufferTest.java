package cn.alvinkwok;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;

/**
 * @author alvinkwok
 * @since 2023/7/18
 */
public class BufferTest {
    public static void main(String[] args) {
        ByteBufAllocator allocator = new PooledByteBufAllocator();
        ByteBuf buffer = allocator.buffer(3*1024*1024, 7*1024*1024);
    }
}
