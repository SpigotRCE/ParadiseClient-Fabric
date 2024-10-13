package io.github.spigotrce.chatroom.shared;

/**
 * <p>Stores bits that are received. Only really there to make code readable.</p>
 */
public class BitBuffer {

    public static final int SECTION = 32;

    public int[] buffers;

    public int bitIndex; // The index of the bit we are walking on.

    public BitBuffer(int bitCapacity) {
        this.buffers = new int[bitCapacity / SECTION + (bitCapacity % SECTION != 0 ? 1 : 0)]; // Puts an extra buffer to not lose bits.
    }

    /**
     * <p>Reads the bits from the buffers.</p>
     * <p>Currently only supports bitSize < 32</p>
     * @param bitSize the bits you want to get must be < 32
     * @return the bits as int.
     */
    public int readBits(int bitSize) {
        int leftover = (this.bitIndex / 32 != (this.bitIndex + bitSize) / 32 ? (this.bitIndex + bitSize) % 32 : 0);
        int newIndex = this.bitIndex + bitSize;

        // Grabs the bits from the current buffer.
        int bits = (this.buffers[this.bitIndex / 32] & (((1 << ((this.bitIndex % 32 + bitSize - leftover) - this.bitIndex % 32)) - 1) << this.bitIndex % 32)) >>> this.bitIndex % 32 + 1;

        // Some bits are leftover, take them
        if(leftover > 0) {
            int leftIndex = newIndex % 32 - leftover;
            bits += this.buffers[newIndex / 32] & (((1 << ((newIndex % 32) - leftIndex)) - 1) << leftIndex); // If bits are missing we take them from the next buffer.
        }

        this.bitIndex = newIndex;
        return bits;
    }

    public void addBits(int bits, int amount) {
        int leftover = (amount > 32 ? amount % 32 : 0);
        int newIndex = this.bitIndex + amount;

        // Grabs the bits from the given int and masks them into the buffer.
        this.buffers[this.bitIndex / 32] = (bits & (((1 << ((this.bitIndex % 32 + amount - leftover) - this.bitIndex % 32)) - 1) << this.bitIndex % 32)) >>> this.bitIndex % 32 + 1;

        // Some bits are left over, copy them.
        if(leftover > 0) {
            int leftIndex = newIndex % 32 - leftover;
            this.buffers[this.bitIndex / 32] += (bits & (((1 << (newIndex % 32) - leftIndex)) - 1) << leftIndex);
        }
    }

}
