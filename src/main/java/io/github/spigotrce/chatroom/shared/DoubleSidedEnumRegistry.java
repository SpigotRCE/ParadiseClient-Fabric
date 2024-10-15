package io.github.spigotrce.chatroom.shared;

/**
 * <p>A registry that is double sided.</p>
 * <p><b>WARN:</b> In order to maximize optimization this registry is barely length checking.</p>
 * @param <L> the left side.
 * @param <R> the right side.
 * @since 1.0.0
 * @author Zffu
 */
public class DoubleSidedEnumRegistry<L extends Enum<?>, R> {

    /**
     * <p>Caches the enum's valueOf() output as it generates a new array everytime.</p>
     */
    private L[] left;
    private R[] right;

    public DoubleSidedEnumRegistry(L[] left, R[] right) {
        if(left.length != right.length) throw new IllegalArgumentException("Double sided registry isn't balanced!");
        this.left = left;
        this.right = right;
    }

    /**
     * <p>Gets the right registry value from the provided left.</p>
     * @param left the left value.
     * @return the right as R
     */
    public R getRight(L left) {
        return this.right[left.ordinal()];
    }

    public L getLeft(int index) {
        return this.left[index];
    }

}
