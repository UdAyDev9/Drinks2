package com.uday.drinks.Model;

public class Banner {
    private String id,Name,Link;

    public Banner() {
    }

    public Banner(String id, String name, String link) {
        this.id = id;
        Name = name;
        Link = link;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }
}
