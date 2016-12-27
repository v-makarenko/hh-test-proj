package ru.hhtest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.stream.Collectors;

/**
 * Created by VMakarenko on 21.12.2016.
 */
public class ArticleTask implements Runnable {
    private static Logger LOG = LoggerFactory.getLogger(ArticleTask.class);

    public String getUrl() {
        return url;
    }

    private final String url;
    private final Loader loader;
    private final int depth;

    public ArticleTask(String url, int depth, Loader loader) {
        this.url = url;
        this.depth = depth;
        this.loader = loader;
    }

    @Override
    public void run() {
        final String baseUrl = url.split("/wiki")[0];
        Document root = null;
        Article article = null;
        try {
            if (!Storage.INSTANCE.getStorage().containsKey(url)) {
//                synchronized (Storage.INSTANCE.getStorage()) {
//                    if (!Storage.INSTANCE.getStorage().containsKey(url)) {
                // we still can havesome duplicates but we are not reducing performance too hard
                root = Jsoup.connect(url).get();
                String text = root
                        .select("#mw-content-text p")
                        .stream()
                        .map(Element::text)
                        .collect(Collectors.joining("\n")); // load all the text
                String name = root.getElementById("firstHeading").text();
                article = new Article(name, text, root.outerHtml());
                Storage.INSTANCE.getStorage().put(url, article);
                LOG.debug("Loaded {} article", article.getName());
//                    }
//                }
                if (article != null) {
                    Storage.INSTANCE.addIndex(article);
                    LOG.debug("Indexed {} article", article.getName());
                }
            }
        } catch (IOException e) {
            LOG.error("Problem loading {}", url);
            loader.getTasksLeft().decrementAndGet();
            return;
        }
        if (article == null) {
            article = Storage.INSTANCE.getStorage().get(url);
            root = Jsoup.parse(article.getHtml());
        }


        if (depth != 0) {
            root.select("#mw-content-text p > a").forEach(link ->
            {
                String newLink = link.attr("href");
                if (!newLink.matches("http[s]?://\\S+")) {  // check if it is relative link or absolute
                    newLink = baseUrl + newLink;
                }
                if (newLink.matches("http[s]?://\\S+wikipedia\\S+")) { // check only wiki links
                    ArticleTask task = new ArticleTask(newLink, depth - 1, loader);
                    loader.getToDoQueue().add(task);
                    loader.getTasksLeft().incrementAndGet();
                }
            });
        }


        loader.getTasksLeft().decrementAndGet();
    }
}
