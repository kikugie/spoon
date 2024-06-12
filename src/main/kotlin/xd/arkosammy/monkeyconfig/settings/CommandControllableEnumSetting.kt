package xd.arkosammy.monkeyconfig.settings

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import net.minecraft.command.CommandSource
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.util.StringIdentifiable
import xd.arkosammy.monkeyconfig.commands.CommandControllableSetting
import xd.arkosammy.monkeyconfig.types.EnumType
import xd.arkosammy.monkeyconfig.util.SettingIdentifier
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.stream.Collectors

class CommandControllableEnumSetting<E> @JvmOverloads constructor(
    settingIdentifier: SettingIdentifier,
    comment: String? = null,
    defaultValue: E,
    value: E = defaultValue) : EnumSetting<E>(settingIdentifier, comment, value), CommandControllableSetting<Enum<E>, StringArgumentType> where E : Enum<E>, E : StringIdentifiable {

    override val argumentType: StringArgumentType
        get() = StringArgumentType.word()

    override val commandIdentifier: SettingIdentifier
        get() = this.settingIdentifier

    @Throws(IllegalArgumentException::class)
    override fun getArgumentValue(ctx: CommandContext<ServerCommandSource>, argumentName: String): E {
        val string: String = StringArgumentType.getString(ctx, argumentName)
        this.defaultValue::class.java.enumConstants.forEach { enumInstance ->
            if(enumInstance.asString() == string) {
                return enumInstance
            }
        }
        throw IllegalArgumentException("Enum constant of type \"${this.defaultValue::class.java.simpleName}\" not found for name \"$string\"")
    }

    override fun getSuggestions(ctx: CommandContext<ServerCommandSource>, suggestionsBuilder: SuggestionsBuilder): CompletableFuture<Suggestions> {
        return CommandSource.suggestMatching(
            Arrays.stream(this.defaultValue::class.java.enumConstants)
                .map { value -> value.asString() }
                .collect(Collectors.toList()),
                suggestionsBuilder
        )
    }

    open class Builder<E> @JvmOverloads constructor(id: SettingIdentifier, comment: String? = null, defaultValue: E) : ConfigSetting.Builder<E, EnumType<E>, EnumSetting<E>>(id, comment, defaultValue) where E : Enum<E>, E : StringIdentifiable {

        override fun build(): EnumSetting<E> {
            return CommandControllableEnumSetting(this.id, this.comment, this.defaultValue)
        }

    }

}