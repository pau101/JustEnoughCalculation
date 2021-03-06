package pers.towdium.just_enough_calculation.plugin;

import com.google.common.collect.ImmutableList;
import mezz.jei.api.*;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.IRecipeCategory;
import pers.towdium.just_enough_calculation.JECConfig;
import pers.towdium.just_enough_calculation.gui.guis.GuiCalculator;
import pers.towdium.just_enough_calculation.gui.guis.GuiEditor;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Towdium
 */
@mezz.jei.api.JEIPlugin
public class JEIPlugin implements IModPlugin {
    public static IJeiRuntime runtime;
    public static IRecipeRegistry recipeRegistry;
    public static IModRegistry registry;

    @Override
    public void registerItemSubtypes(@Nonnull ISubtypeRegistry iSubtypeRegistry) {
    }

    @Override
    public void registerIngredients(@Nonnull IModIngredientRegistration iModIngredientRegistration) {
    }

    @Override
    public void register(@Nonnull IModRegistry registry) {
        JEIPlugin.registry = registry;
        String[] blackList = JECConfig.EnumItems.ListRecipeBlackList.getProperty().getStringList();
        LOOP:
        for (String s : JECConfig.EnumItems.ListRecipeCategory.getProperty().getStringList()) {
            for (String black : blackList) {
                if (black.equals(s)) {
                    continue LOOP;
                }
            }
            registry.getRecipeTransferRegistry().addRecipeTransferHandler(new JECRecipeTransferHandler(s, GuiEditor.ContainerEditor.class), s);
            registry.getRecipeTransferRegistry().addRecipeTransferHandler(new JECRecipeTransferHandler(s, GuiCalculator.ContainerCalculator.class), s);
        }
    }

    @Override
    public void onRuntimeAvailable(@Nonnull IJeiRuntime jeiRuntime) {
        Set<String> identifiers = new HashSet<>();
        runtime = jeiRuntime;
        recipeRegistry = jeiRuntime.getRecipeRegistry();
        identifiers.addAll(ImmutableList.copyOf(JECConfig.EnumItems.ListRecipeCategory.getProperty().getStringList()));
        identifiers.addAll(recipeRegistry.getRecipeCategories().stream().map(IRecipeCategory::getUid).collect(Collectors.toList()));
        int size = identifiers.size();
        String[] buffer = new String[size];
        for (String s : identifiers) {
            buffer[--size] = s;
        }
        JECConfig.EnumItems.ListRecipeCategory.getProperty().set(buffer);
        JECConfig.save();
    }
}
