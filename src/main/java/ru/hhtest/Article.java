package ru.hhtest;

/**
 * Created by VMakarenko on 21.12.2016.
 */
public class Article {
    final private String name;
    final private String text;
    final private String html;

    public Article(String name, String text,String html) {
        this.name = name;
        this.text = text;
        this.html = html;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public String getHtml() {
        return html;
    }
}
