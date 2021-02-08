package com.movie.movieapp;

public enum Roles {
    ADMIN(1,"ADMIN"),USER(2,"USER");
    private final int id;
    private final String name;

    Roles(int id, String name){
        this.id = id;
        this.name = name;
    }
    public int getId(){
        return id;
    }

    public String getName() {
        return name;
    }

    public static Roles valueOf(int i){
        switch (i){
            case 1:
                return ADMIN;
            case 2:
                return USER;
            default:
                throw new RuntimeException("Role not found");
        }
    }
}
