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
    private ArrayList<ItemStack> customs;

    public NovsLoadout() {
        foodQuantity = 5;
        helmet = new ItemStack(Material.LEATHER_HELMET);
        chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        leggings = new ItemStack(Material.LEATHER_LEGGINGS);
        boots = new ItemStack(Material.LEATHER_BOOTS);
        weapon = new ItemStack(Material.WOOD_SWORD);
        food = new ItemStack(Material.COOKIE, foodQuantity);
        customs = new ArrayList<ItemStack>();
    }

    public NovsLoadout(ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots, ItemStack weapon, ItemStack food, int foodQuantity) {
        this.helmet = helmet;
        this.chestplate = chestplate;
        this.leggings = leggings;
        this.boots = boots;
        this.weapon = weapon;
        this.food = food;
        this.foodQuantity = foodQuantity;
        customs = new ArrayList<ItemStack>();
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

    public ArrayList<ItemStack> getCustoms() {
        return customs;
    }

}
