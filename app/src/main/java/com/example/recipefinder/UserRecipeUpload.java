package com.example.recipefinder;

import java.util.ArrayList;

public class UserRecipeUpload {
    String uploadBy,recipeName,recipeTime,recipeServing;
    ArrayList<UploadRecipe.ItemModel> recipeEquipment;
    ArrayList<UploadRecipe.ItemModel> recipeInstruction;
    ArrayList<UploadRecipe.ItemModel> recipeIngredient;
    public UserRecipeUpload(String uploadBy,String recipeName,String recipeTime,String recipeServing, ArrayList<UploadRecipe.ItemModel> recipeEquipment, ArrayList<UploadRecipe.ItemModel> recipeIngredient,ArrayList<UploadRecipe.ItemModel> recipeInstruction){
        this.uploadBy=uploadBy;
        this.recipeEquipment=recipeEquipment;
        this.recipeIngredient=recipeIngredient;
        this.recipeInstruction=recipeInstruction;
        this.recipeName=recipeName;
        this.recipeTime=recipeTime;
        this.recipeServing=recipeServing;
    }
    public void setUploadBy(String uploadBy){
        this.uploadBy=uploadBy;
    }
    public String getUploadBy(){
        return uploadBy;
    }

    public void setRecipeName(String recipeName){
        this.recipeName=recipeName;
    }
    public String getRecipeName(){
        return recipeName;
    }

    public void setRecipeTime(String recipeTime){
        this.recipeTime=recipeTime;
    }
    public String getRecipeTime(){
        return recipeTime;
    }

    public void setRecipeServing(String recipeServing){
        this.recipeServing=recipeServing;
    }
    public String getRecipeServing(){
        return recipeServing;
    }

    public void setRecipeEquip(ArrayList<UploadRecipe.ItemModel> recipeEquipment){
        this.recipeEquipment=recipeEquipment;
    }
    public ArrayList<UploadRecipe.ItemModel> getRecipeEquip(){
        return recipeEquipment;
    }

    public void setRecipeInst(ArrayList<UploadRecipe.ItemModel> recipeInstruction){
        this.recipeInstruction=recipeInstruction;
    }
    public ArrayList<UploadRecipe.ItemModel> getRecipeInstruction(){
        return recipeInstruction;
    }

    public void setRecipeIngre(ArrayList<UploadRecipe.ItemModel> recipeIngredient){
        this.recipeIngredient=recipeIngredient;
    }
    public ArrayList<UploadRecipe.ItemModel> getRecipeIngredient(){
        return recipeIngredient;
    }
}
