package net.koteslaw.commands

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import net.koteslaw.SaveData
import net.minecraft.command.CommandSource
import net.minecraft.server.command.ServerCommandSource
import java.util.concurrent.CompletableFuture

class KitArgumentType private constructor() : ArgumentType<String> {
    override fun parse(reader: StringReader?): String {
        return reader!!.readUnquotedString()
    }
    override fun <S> listSuggestions(
        context: CommandContext<S>,
        builder: SuggestionsBuilder?
    ): CompletableFuture<Suggestions> {
        return if (context.source is CommandSource)
            CommandSource.suggestMatching(SaveData.kits.keys, builder)
        else
            Suggestions.empty()
    }

    companion object {
        @JvmStatic
        fun create(): KitArgumentType {
            return KitArgumentType()
        }
        fun getString(context: CommandContext<*>, name: String): String {
            return context.getArgument(name, String::class.java)
        }
    }
}