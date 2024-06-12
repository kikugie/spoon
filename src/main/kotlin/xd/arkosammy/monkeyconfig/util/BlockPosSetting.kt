package xd.arkosammy.monkeyconfig.util

import com.mojang.brigadier.context.CommandContext
import net.minecraft.command.argument.BlockPosArgumentType
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.util.math.BlockPos
import xd.arkosammy.monkeyconfig.commands.CommandControllableSetting
import xd.arkosammy.monkeyconfig.settings.ConfigSetting
import xd.arkosammy.monkeyconfig.types.ListType
import xd.arkosammy.monkeyconfig.types.NumberType

open class BlockPosSetting @JvmOverloads constructor(
    settingIdentifier: SettingIdentifier,
    comment: String? = null,
    override val defaultValue: BlockPos,
    override var value: BlockPos = defaultValue) : ConfigSetting<BlockPos, ListType<NumberType<Int>>>(settingIdentifier, comment, defaultValue, value), CommandControllableSetting<BlockPos, BlockPosArgumentType> {

    override val serializedValue: ListType<NumberType<Int>>
        get() = ListType(listOf(NumberType(this.value.x), NumberType(this.value.y), NumberType(this.value.z)))

    override val serializedDefaultValue: ListType<NumberType<Int>>
        get() = ListType(listOf(NumberType(this.defaultValue.x), NumberType(this.defaultValue.y), NumberType(this.defaultValue.z)))

    override fun setValueFromSerialized(serializedValue: ListType<NumberType<Int>>) {
        this.value = BlockPos(serializedValue.value[0].value, serializedValue.value[1].value, serializedValue.value[2].value)
    }

    override val argumentType: BlockPosArgumentType
        get() = BlockPosArgumentType.blockPos()

    override val commandIdentifier: SettingIdentifier
        get() = this.settingIdentifier

    override fun getArgumentValue(ctx: CommandContext<ServerCommandSource>, argumentName: String): BlockPos {
        return BlockPosArgumentType.getBlockPos(ctx, argumentName)
    }

}