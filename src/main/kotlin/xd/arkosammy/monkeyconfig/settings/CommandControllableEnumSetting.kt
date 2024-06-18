package xd.arkosammy.monkeyconfig.settings

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import net.minecraft.command.CommandSource
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.util.StringIdentifiable
import xd.arkosammy.monkeyconfig.commands.CommandControllableSetting
import xd.arkosammy.monkeyconfig.util.SettingLocation
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.stream.Collectors

/**
 * Represents an [EnumSetting] whose value can be modified via auto generated commands
 *
 * @param [E] The type of the [Enum] value to store in this [CommandControllableSetting].
 * Note that, unlike in [EnumSetting], [E] has the added restriction of having to implement [StringIdentifiable].
 */
class CommandControllableEnumSetting<E> @JvmOverloads constructor(
    settingLocation: SettingLocation,
    comment: String? = null,
    defaultValue: E,
    value: E = defaultValue) : EnumSetting<E>(settingLocation, comment, value), CommandControllableSetting<Enum<E>, StringArgumentType> where E : Enum<E>, E : StringIdentifiable {

    override val argumentType: StringArgumentType
        get() = StringArgumentType.word()

    override val commandIdentifier: SettingLocation
        get() = this.settingLocation

    @Throws(IllegalArgumentException::class)
    override fun getArgumentValue(ctx: CommandContext<ServerCommandSource>, argumentName: String): E {
        val string: String = StringArgumentType.getString(ctx, argumentName)
        for(enumValue: E in this.defaultValue::class.java.enumConstants) {
            if (enumValue.asString() == string) {
                return enumValue
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

    open class Builder<E> @JvmOverloads constructor(settingLocation: SettingLocation, comment: String? = null, defaultValue: E) : EnumSetting.Builder<E>(settingLocation, comment, defaultValue) where E : Enum<E>, E : StringIdentifiable {

        override fun build(): EnumSetting<E> = CommandControllableEnumSetting(this.settingLocation, this.comment, this.defaultValue)

    }

}