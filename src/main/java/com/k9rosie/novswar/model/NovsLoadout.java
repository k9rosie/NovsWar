package com.k9rosie.novswar.model;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class NovsLoadout {

    private ItemStack helmet;
    private ItemStack chestplate;
    private ItemStack leggings;
    private ItemStack boots;
    private ItemStack weapon;
    private ItemStack food;
    private int foodQuantity;
    private ItemStack custom;
    private int customQuantity;
    private ItemStack arrows;
    private int arrowQuantity;
    private ArrayList<ItemStack> potions;

    public NovsLoadout(Material helmet, Material chestplate, Material leggings,
                       Material boots, Material weapon, Material food,
                       int foodQuantity, Material custom, int customQuantity,
                       Material arrows, int arrowQuantity, ArrayList<ItemStack> potions) {
        this.helmet = new ItemStack(helmet);
        this.chestplate = new ItemStack(chestplate);
        this.leggings = new ItemStack(leggings);
        this.boots = new ItemStack(boots);
        this.weapon = new ItemStack(weapon);
        this.foodQuantity = foodQuantity;
        this.food = new ItemStack(food, this.foodQuantity);
        this.customQuantity = customQuantity;
        this.custom = new ItemStack(custom, this.customQuantity);
        this.arrowQuantity = arrowQuantity;
        this.arrows = new ItemStack(arrows, arrowQuantity);
        this.potions = potions;
    }

    public ItemStack getHelmet() {
        return helmet;
    }

    public void setHelmet(ItemStack helmet) {
        this.helmet = helmet;
    }

    public ItemStack getChestplate() {
        return chestplate;
    }

    public void setChestplate(ItemStack chestplate) {
        this.chestplate = chestplate;
    }

    public ItemStack getLeggings() {
        return leggings;
    }

    public void setLeggings(ItemStack leggings) {
        this.leggings = leggings;
    }

    public ItemStack getBoots() {
        return boots;
    }

    public void setBoots(ItemStack boots) {
        this.boots = boots;
    }

    public ItemStack getWeapon() {
        return weapon;
    }

    public void setWeapon(ItemStack weapon) {
        this.weapon = weapon;
    }

    public ItemStack getFood() {
        return food;
    }

    public void setFood(ItemStack food) {
        this.food = food;
    }

    public int getFoodQuantity() {
        return foodQuantity;
    }

    public void setFoodQuantity(int foodQuantity) {
        this.foodQuantity = foodQuantity;
    }

    public ItemStack getCustom() {
        return custom;
    }

    public void setCustom(ItemStack custom) {
        this.custom = custom;
    }

    public int getCustomQuantity() {
        return customQuantity;
    }

    public ItemStack getArrows() {
        return arrows;
    }

    public void setArrows(ItemStack arrows) {
        this.arrows = arrows;
    }

    public int getArrowQuantity() {
        return arrowQuantity;
    }

    public void setArrowQuantity(int arrowQuantity) {
        this.arrowQuantity = arrowQuantity;
    }

    public ArrayList<ItemStack> getPotions() {
        return potions;
    }

    public void setPotions(ArrayList<ItemStack> potions) {
        this.potions = potions;
    }

}
