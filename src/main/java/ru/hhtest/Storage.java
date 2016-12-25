package ru.hhtest;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by VMakarenko on 21.12.2016.
 */
public enum Storage {
    INSTANCE;

    private final Map<String, Article> storage = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Set<Place>> index = new ConcurrentHashMap<>();

    public Map<String, Article> getStorage() {
        return storage;
    }



    public String getArticleNamesJoined() {
        return storage.values().stream().map(Article::getName).collect(Collectors.joining(", "));
    }

    public void addIndex(Article article) {
        String[] words = Pattern.compile("\\W+", Pattern.UNICODE_CHARACTER_CLASS).split(article.getText());
        for (String word : words) {
            word = word.toLowerCase();
            index.putIfAbsent(word, new HashSet<>());
            index.get(word).add(new Place(article));
        }
    }

    public ConcurrentHashMap<String, Set<Place>> getIndex() {
        return index;
    }
}
