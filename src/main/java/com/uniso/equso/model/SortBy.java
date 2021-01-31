package com.uniso.equso.model;

public enum SortBy {
    DATE("p.created_at"),POPULARITY("p.last_updated_at");

    String value;

    SortBy(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
