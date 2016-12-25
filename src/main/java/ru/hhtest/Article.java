package ru.hhtest;

/**
 * Created by VMakarenko on 21.12.2016.
 */
public class Article {
    final private String name;
    final private String text;

    public Article(String name, String text) {
        this.name = name;
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }
}
