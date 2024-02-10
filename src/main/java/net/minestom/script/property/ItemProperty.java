package net.minestom.script.property;

import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ItemProperty extends Properties {

    private final ItemStack itemStack;

    public ItemProperty(@NotNull ItemStack itemStack) {
        this.itemStack = itemStack;
        Properties.applyExtensions(ItemProperty.class, itemStack, this);
        putMember("material", itemStack.material().toString());
        putMember("amount", itemStack.amount());
    }

    @Override
    public String toString() {
        final String namespace = itemStack.material().name();
        final String nbt = itemStack.meta().toSNBT();
        return namespace + nbt;
    }
}
