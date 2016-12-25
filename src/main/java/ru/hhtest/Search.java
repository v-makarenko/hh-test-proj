package ru.hhtest;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by VMakarenko on 21.12.2016.
 */
public class Search {

    public Set<Article> find(String word) {
        Set<Place> set = Storage.INSTANCE.getIndex().get(word.toLowerCase());
        return set != null ? set.stream().map(Place::getArticle).collect(Collectors.toSet()) : new HashSet<>();
    }
}
