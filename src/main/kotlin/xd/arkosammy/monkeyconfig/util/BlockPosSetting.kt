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
    settingLocation: SettingLocation,
    comment: String? = null,
    override val defaultValue: BlockPos,
    override var value: BlockPos = defaultValue) : ConfigSetting<BlockPos, ListType<NumberType<Int>>>(settingLocation, comment, defaultValue, value), CommandControllableSetting<BlockPos, BlockPosArgumentType> {

    override val valueToSerializedConverter: (BlockPos) -> ListType<NumberType<Int>>
        get() = { blockPos -> ListType(listOf(NumberType(blockPos.x), NumberType(blockPos.y), NumberType(blockPos.z))) }

    override val serializedToValueConverter: (ListType<NumberType<Int>>) -> BlockPos
        get() = { serializedBlockPos -> BlockPos(serializedBlockPos.rawValue[0].rawValue, serializedBlockPos.rawValue[1].rawValue, serializedBlockPos.rawValue[2].rawValue) }

    override val argumentType: BlockPosArgumentType
        get() = BlockPosArgumentType.blockPos()

    override val commandIdentifier: SettingLocation
        get() = this.settingLocation

    override fun getArgumentValue(ctx: CommandContext<ServerCommandSource>, argumentName: String): BlockPos {
        return BlockPosArgumentType.getBlockPos(ctx, argumentName)
    }

    class  Builder @JvmOverloads constructor(settingLocation: SettingLocation, comment: String? = null, defaultValue: BlockPos) : ConfigSetting.Builder<BlockPosSetting, BlockPos, ListType<NumberType<Int>>>(settingLocation, comment, defaultValue) {
        override fun build(): BlockPosSetting {
            return BlockPosSetting(settingLocation, comment, defaultValue)
        }

    }

}