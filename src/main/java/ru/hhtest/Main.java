package ru.hhtest;

import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    Loader loader = new Loader();
    Search search = new Search();

    public static void main(String[] args) {
        new Main().runLoop();
    }


    private void runLoop() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            printOptions();
            String line = sc.nextLine();
            if (line.matches("^load .+ \\d+$")) {
                new Thread(() -> loader.load(line.split(" ")[1], Integer.parseInt(line.split(" ")[2]))).start();
            } else if (line.matches("^search .+$")) {
                System.out.println(search.find(line.split(" ")[1]).stream().map(Article::getName).collect(Collectors.joining(", ")));
            } else if (line.matches("stats")) {
                System.out.println(loader.getStats());
            } else if (line.matches("list")) {
                System.out.println(Storage.INSTANCE.getArticleNamesJoined());
            }else if (line.matches("exit")) {
                loader.stop();
                return;
            } else {
                System.out.println("Sorry, illegal command.");
            }
        }

    }


    private void printOptions() {
        System.out.println("========================== WIKI DOWNLOADER ===================================");
        System.out.println("load %url% %depth% - load %url% page content recursively for %depth% times");
        System.out.println("search %query% - list page names and lines that contain %query%");
        System.out.println("stats - show statistics");
        System.out.println("list - list loaded articles");
        System.out.println("exit - leave app");
    }
}
