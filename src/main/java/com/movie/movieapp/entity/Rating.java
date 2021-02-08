package com.movie.movieapp.entity;

public enum Rating {
    ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5);

    private int rating;

    Rating(int rating) {
        this.rating = rating;
    }

    public int getRating() {
        return rating;
    }
    public Rating valueOf(int rating){
        switch (rating){
            case 1:
                return ONE;
            case 2:
                return TWO;
            case 3:
                return THREE;
            case 4:
                return FOUR;
            case 5:
                return FIVE;
            default:
                throw new RuntimeException("Invalid Rating");
        }
    }
}
