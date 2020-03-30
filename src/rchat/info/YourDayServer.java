package rchat.info;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import rchat.info.containters.*;
import rchat.info.containters.day.Day;
import rchat.info.containters.day.Description;
import rchat.info.containters.day.Present;
import rchat.info.containters.day.Type;
import rchat.info.containters.moon_calend.MainDescription;
import rchat.info.containters.moon_calend.MoonDay;
import rchat.info.containters.moon_calend.Tip;
import rchat.info.containters.moon_calend.TipTypes;
import rchat.info.containters.recipe.NewRecipeType;
import rchat.info.containters.recipe.NewRecipes;
import rchat.info.containters.recipe.Recipe;
import rchat.info.containters.recipe.Step;

import java.io.*;
import java.net.*;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.*;

import static sun.util.logging.LoggingSupport.log;

public class YourDayServer extends WebSocketServer {
    static List<Present> presents = new ArrayList<>();
    static List<String> churchPresents = new ArrayList<>();
    static Map<String, String> gorosope = new HashMap<>();
    static List<String> weekendCombinations = new ArrayList<>();
    static List<String> names = new ArrayList<>();
    static List<Human> humans = new ArrayList<>();
    static Omen omen;
    static MoonDay moonDay;
    static Recipe recipe = new Recipe();
    static String version = "1.3";
    static String update = "• Мы сменили источнк праздников: теперь Вы можете более конкретно узнать о новом празднике!" + "\n" + "• Добавлен пункт \"Лунный календарь\"." + "\n";


    public static class JSoupPropsLoader {
        private static void loadMoonDay() {
            try {
                Document doc = Jsoup.connect("https://my-calend.ru/moon/today").get(); //today
                MainDescription mainDescription;
                Element desc = doc.selectFirst("[class=moon-day-info-1]");
                String date = desc.select("tr td").get(0).text();
                int imgType = Integer.parseInt(desc.selectFirst("[class=icon-moon-v2]").attr("data-type"));
                String moonName = desc.select("td b").get(0).text();
                String phaseName = desc.select("td div").get(0).text();
                String dawn = desc.select("tr td").get(3).text();
                String sunset = desc.select("tr td").get(4).text();
                if (!sunset.contains("Закат") && !sunset.contains("Восход")) {
                    sunset = "";
                }
                String visibility = desc.select("td div").get(1).text();
                String fromAndTo = desc.select("td small").get(0).text();
                mainDescription = new MainDescription(date, imgType, moonName, phaseName, dawn, sunset, visibility, fromAndTo);
                Elements desriptions = doc.select("[class=moon-effect negative], [class=moon-effect positive], [class=moon-effect neutral]");
                List<Tip> tips = new ArrayList<>();
                for (Element t : desriptions) {
                    if (t.toString().contains("moon-effect negative")) {
                        String name = t.select("h2").text();
                        String descc = "";
                        for (Element a : t.select("div p")) {
                            descc += a.text();
                        }
                        String link = t.select("div a").attr("href");
                        Document aDoc = Jsoup.connect(link).get();
                        List<Description> descriptions = new ArrayList<>();
                        if (link.contains("-lunnyy-den")) {
                            Elements els = aDoc.select("div p");
                            for (Element el : els) {
                                String k = el.toString();
                                if (el.toString().contains("<p><a href=")) {
                                    break;
                                } else {
                                    descriptions.add(new Description(Type.USUAL_TEXT, el.text()));
                                }
                            }
                        } else if (link.contains("ubyvayushchaya-luna") || link.contains("novolunie") || link.contains("rastushchaya-luna") || link.contains("polnolunie")) {
                            Elements elements = aDoc.select("[class=moon-info-item]").select("div p, h2");
                            for (Element h : elements) {
                                String k = h.toString();
                                if (h.toString().contains("<p><a href=") || (h.toString().contains("Календарь"))) {
                                    break;
                                } else {
                                    if (h.toString().endsWith("</p>")) {
                                        descriptions.add(new Description(Type.USUAL_TEXT, h.text()));
                                    } else if (h.toString().endsWith("</h2>")) {
                                        descriptions.add(new Description(Type.BOLD_HEADING, h.text()));
                                    }
                                }
                            }
                        } else if (link.contains("luna-v-znake-")) {
                            Elements els = aDoc.select("div p");
                            for (Element el : els) {
                                String k = el.toString();
                                if (el.toString().contains("<p><a href=")) {
                                    break;
                                } else {
                                    descriptions.add(new Description(Type.USUAL_TEXT, el.text()));
                                }
                            }
                        } else {
                            Elements elements = aDoc.select("[class=moon-info-item]").select("div p, h2");
                            for (Element h : elements) {
                                String k = h.toString();
                                if (h.toString().contains("<p><a href=") || (h.toString().contains("Календарь"))) {
                                    break;
                                } else {
                                    if (h.toString().endsWith("</p>")) {
                                        descriptions.add(new Description(Type.USUAL_TEXT, h.text()));
                                    } else if (h.toString().endsWith("</h2>")) {
                                        descriptions.add(new Description(Type.BOLD_HEADING, h.text()));
                                    }
                                }
                            }
                        }
                        descriptions = descriptions;
                        Tip tip = new Tip(name, descc, descriptions, TipTypes.BAD);
                        tips.add(tip);
                    } else if (t.toString().contains("moon-effect positive")) {
                        String name = t.select("h2").text();
                        String descc = "";
                        for (Element a : t.select("div p")) {
                            descc += a.text();
                        }
                        String link = t.select("div a").attr("href");
                        Document aDoc = Jsoup.connect(link).get();
                        List<Description> descriptions = new ArrayList<>();
                        if (link.contains("-lunnyy-den")) {
                            Elements els = aDoc.select("div p");
                            for (Element el : els) {
                                String k = el.toString();
                                if (el.toString().contains("<p><a href=")) {
                                    break;
                                } else {
                                    descriptions.add(new Description(Type.USUAL_TEXT, el.text()));
                                }
                            }
                        } else if (link.contains("ubyvayushchaya-luna") || link.contains("novolunie") || link.contains("rastushchaya-luna") || link.contains("polnolunie")) {
                            Elements elements = aDoc.select("[class=moon-info-item]").select("div p, h2");
                            for (Element h : elements) {
                                String k = h.toString();
                                if (h.toString().contains("<p><a href=") || (h.toString().contains("Календарь"))) {
                                    break;
                                } else {
                                    if (h.toString().endsWith("</p>")) {
                                        descriptions.add(new Description(Type.USUAL_TEXT, h.text()));
                                    } else if (h.toString().endsWith("</h2>")) {
                                        descriptions.add(new Description(Type.BOLD_HEADING, h.text()));
                                    }
                                }
                            }
                        } else if (link.contains("luna-v-znake-")) {
                            Elements els = aDoc.select("div p");
                            for (Element el : els) {
                                String k = el.toString();
                                if (el.toString().contains("<p><a href=")) {
                                    break;
                                } else {
                                    descriptions.add(new Description(Type.USUAL_TEXT, el.text()));
                                }
                            }
                        } else {
                            Elements elements = aDoc.select("[class=moon-info-item]").select("div p, h2");
                            for (Element h : elements) {
                                String k = h.toString();
                                if (h.toString().contains("<p><a href=") || (h.toString().contains("Календарь"))) {
                                    break;
                                } else {
                                    if (h.toString().endsWith("</p>")) {
                                        descriptions.add(new Description(Type.USUAL_TEXT, h.text()));
                                    } else if (h.toString().endsWith("</h2>")) {
                                        descriptions.add(new Description(Type.BOLD_HEADING, h.text()));
                                    }
                                }
                            }
                        }
                        descriptions = descriptions;
                        Tip tip = new Tip(name, descc, descriptions, TipTypes.GOOD);
                        tips.add(tip);
                    } else if (t.toString().contains("moon-effect neutral")) {
                        String name = t.select("h2").text();
                        String descc = "";
                        for (Element a : t.select("div p")) {
                            descc += a.text();
                        }
                        String link = t.select("div a").attr("href");
                        Document aDoc = Jsoup.connect(link).get();
                        List<Description> descriptions = new ArrayList<>();
                        if (link.contains("-lunnyy-den")) {
                            Elements els = aDoc.select("div p");
                            for (Element el : els) {
                                String k = el.toString();
                                if (el.toString().contains("<p><a href=")) {
                                    break;
                                } else {
                                    descriptions.add(new Description(Type.USUAL_TEXT, el.text()));
                                }
                            }
                        } else if (link.contains("ubyvayushchaya-luna") || link.contains("novolunie") || link.contains("rastushchaya-luna") || link.contains("polnolunie")) {
                            Elements elements = aDoc.select("[class=moon-info-item]").select("div p, h2");
                            for (Element h : elements) {
                                String k = h.toString();
                                if (h.toString().contains("<p><a href=") || (h.toString().contains("Календарь"))) {
                                    break;
                                } else {
                                    if (h.toString().endsWith("</p>")) {
                                        descriptions.add(new Description(Type.USUAL_TEXT, h.text()));
                                    } else if (h.toString().endsWith("</h2>")) {
                                        descriptions.add(new Description(Type.BOLD_HEADING, h.text()));
                                    }
                                }
                            }
                        } else if (link.contains("luna-v-znake-")) {
                            Elements els = aDoc.select("div p");
                            for (Element el : els) {
                                String k = el.toString();
                                if (el.toString().contains("<p><a href=")) {
                                    break;
                                } else {
                                    descriptions.add(new Description(Type.USUAL_TEXT, el.text()));
                                }
                            }
                        } else {
                            Elements elements = aDoc.select("[class=moon-info-item]").select("div p, h2");
                            for (Element h : elements) {
                                String k = h.toString();
                                if (h.toString().contains("<p><a href=") || (h.toString().contains("Календарь"))) {
                                    break;
                                } else {
                                    if (h.toString().endsWith("</p>")) {
                                        descriptions.add(new Description(Type.USUAL_TEXT, h.text()));
                                    } else if (h.toString().endsWith("</h2>")) {
                                        descriptions.add(new Description(Type.BOLD_HEADING, h.text()));
                                    }
                                }
                            }
                        }
                        descriptions = descriptions;
                        Tip tip = new Tip(name, descc, descriptions, TipTypes.NEUTRAL);
                        tips.add(tip);
                    }
                }
                tips = tips;
                YourDayServer.moonDay = new MoonDay(mainDescription, tips);
                ConsoleHelper.writeString("Лунный день загружен!");
            } catch (IOException e) {
                ConsoleHelper.writeException(e);
                loadMoonDay();
            }
        }

        //TODO:This is JSoupPropsLoader
        private static Joke getJoke() {
            try {
                Document doc = Jsoup.connect("https://randstuff.ru/joke/").userAgent("Chrome/4.0.249.0 Safari/532.5")
                        .referrer("http://www.google.com")
                        .get();
                Elements elements = doc.select("tr td");
                elements = elements;
                Joke joke = new Joke(elements.get(0).text());
                return joke;
            } catch (IOException e) {
                ConsoleHelper.writeException(e);
            }
            return null;
        }

        private static void loadOmens() {
            try {
                String mesyac;
                mesyac = new SimpleDateFormat("MMMM", new DateFormatSymbols() {
                    @Override
                    public String[] getMonths() {
                        return new String[]{"january", "february", "march", "april", "may", "june",
                                "july", "august", "september", "october", "november", "december"};
                    }
                }).format(updateTime);
                Document doc = Jsoup.connect("https://my-calend.ru/day-omens/" + new SimpleDateFormat("d").format(updateTime) + "-" + mesyac).get();
                Elements omens = doc.select("[class=day-omens-item]").select("ul li");
                List<String> oms = new ArrayList<>();
                for (Element t : omens) {
                    oms.add(t.text());
                }
                Omen omen = new Omen(oms);
                YourDayServer.omen = omen;
                ConsoleHelper.writeString("Предсказания загружены!");
            } catch (IOException e) {
                ConsoleHelper.writeException(e);
            }
        }

        private static void loadRecipe() {
            try {
                String mesyac;
                mesyac = new SimpleDateFormat("MMMM", new DateFormatSymbols() {
                    @Override
                    public String[] getMonths() {
                        return new String[]{"january", "february", "march", "april", "may", "june",
                                "jule", "august", "september", "october", "november", "december"};
                    }
                }).format(updateTime);
                String request = "https://canadacook.ru/recipes_month/" + mesyac + "/";
                Document doc = Jsoup.connect(request).get();
                Elements pages = doc.select("[class=page-numbers]");
                if (pages.size() != 0) {
                    int page = 1;
                    for (int i = 0; i < 10; i++) {
                        page = (int) (Math.random() * (Integer.parseInt(pages.get(pages.size() - 1).text()))) + 1;
                    }
                    if (page != 1) {
                        doc = Jsoup.connect(request + "page/" + page + "/").get();
                    }
                    pages = doc.select("[class=article__title entry-title]");
                    int recipeNumber = (int) (pages.size() * Math.random());
                    Element p = pages.get(recipeNumber);
                    String link = p.select("a[href]").attr("abs:href");
                    doc = Jsoup.connect(link).get();
                    String name = doc.select("[class=article__title entry-title fn]").get(0).text();
                    String imgURL = doc.select("[class=article__featured-image reciept_img]").select("[src]").attr("abs:src");
                    String description = doc.select("section p").get(0).text();
                    Elements ingredients = doc.select("[itemprop=ingredients]");
                    List<String> ings = new ArrayList<>();
                    for (Element g : ingredients) {
                        ings.add(g.text());
                    }
                    Elements steps = doc.select("[class=instruction]");
                    List<Step> listOfSteps = new ArrayList<>();
                    for (Element g : steps) {
                        String step = g.text();
                        List<String> imgs = new ArrayList<>();
                        for (Element h : g.select("[class=step_photo]")) {
                            imgs.add(h.select("[src]").attr("abs:src"));
                        }
                        Step stepp = new Step(step, imgs);
                        listOfSteps.add(stepp);
                    }
                    recipe = new Recipe(imgURL, name, description, ings, listOfSteps);
                } else {
                    pages = doc.select("[class=article__title entry-title]");
                    int recipeNumber = (int) (pages.size() * Math.random());
                    Element p = pages.get(recipeNumber);
                    String link = p.select("a[href]").attr("abs:href");
                    doc = Jsoup.connect(link).get();
                    String name = doc.select("[class=article__title entry-title fn]").get(0).text();
                    String imgURL = doc.select("[class=article__featured-image reciept_img]").select("[src]").attr("abs:src");
                    String description = doc.select("section p").get(0).text();
                    Elements ingredients = doc.select("[itemprop=ingredients]");
                    List<String> ings = new ArrayList<>();
                    for (Element g : ingredients) {
                        ings.add(g.text());
                    }
                    Elements steps = doc.select("[class=instruction]");
                    List<Step> listOfSteps = new ArrayList<>();
                    for (Element g : steps) {
                        String step = g.text();
                        List<String> imgs = new ArrayList<>();
                        for (Element h : g.select("[class=step_photo]")) {
                            imgs.add(h.select("[src]").attr("abs:src"));
                        }
                        Step stepp = new Step(step, imgs);
                        listOfSteps.add(stepp);
                    }
                    recipe = new Recipe(imgURL, name, description, ings, listOfSteps);
                }
                ConsoleHelper.writeString("Рецепты добавлены!");

            } catch (IOException e) {
                ConsoleHelper.writeException(e);
                loadRecipe();
            }
        }

        private static void loadGoroscope() {
            try {
                Document doc = Jsoup.connect("https://1001goroskop.ru/?znak=aries").get();
                Elements list = doc.select("[itemprop=description]");
                String res = list.get(0).text();
                gorosope.put("Aries", res);
                doc = Jsoup.connect("https://1001goroskop.ru/?znak=taurus").get();
                list = doc.select("[itemprop=description]");
                res = list.get(0).text();
                gorosope.put("Taurus", res);
                doc = Jsoup.connect("https://1001goroskop.ru/?znak=gemini").get();
                list = doc.select("[itemprop=description]");
                res = list.get(0).text();
                gorosope.put("Gemini", res);
                doc = Jsoup.connect("https://1001goroskop.ru/?znak=cancer").get();
                list = doc.select("[itemprop=description]");
                res = list.get(0).text();
                gorosope.put("Cancer", res);
                doc = Jsoup.connect("https://1001goroskop.ru/?znak=leo").get();
                list = doc.select("[itemprop=description]");
                res = list.get(0).text();
                gorosope.put("Leo", res);
                doc = Jsoup.connect("https://1001goroskop.ru/?znak=virgo").get();
                list = doc.select("[itemprop=description]");
                res = list.get(0).text();
                gorosope.put("Virgo", res);
                doc = Jsoup.connect("https://1001goroskop.ru/?znak=libra").get();
                list = doc.select("[itemprop=description]");
                res = list.get(0).text();
                gorosope.put("Libra", res);
                doc = Jsoup.connect("https://1001goroskop.ru/?znak=scorpio").get();
                list = doc.select("[itemprop=description]");
                res = list.get(0).text();
                gorosope.put("Scorpio", res);
                doc = Jsoup.connect("https://1001goroskop.ru/?znak=sagittarius").get();
                list = doc.select("[itemprop=description]");
                res = list.get(0).text();
                gorosope.put("Sagittarius", res);
                doc = Jsoup.connect("https://1001goroskop.ru/?znak=capricorn").get();
                list = doc.select("[itemprop=description]");
                res = list.get(0).text();
                gorosope.put("Сapricorn", res);
                doc = Jsoup.connect("https://1001goroskop.ru/?znak=aquarius").get();
                list = doc.select("[itemprop=description]");
                res = list.get(0).text();
                gorosope.put("Aquarius", res);
                doc = Jsoup.connect("https://1001goroskop.ru/?znak=pisces").get();
                list = doc.select("[itemprop=description]");
                res = list.get(0).text();
                gorosope.put("Pisces", res);
                ConsoleHelper.writeString("Гороскопы добавлены!");
            } catch (IOException e) {
                ConsoleHelper.writeException(e);
                loadGoroscope();
            }
        }

        private static void loadPresent() {
            try {
                presents = new ArrayList<>();
                Document doc = Jsoup.connect("https://my-calend.ru/holidays/russia").get();
                Elements list = doc.select("[class='holidays-items']").select("li");
                for (Element t : list) {
                    try {
                        String presentName = t.select("a").text();
                        String link = t.select("a").attr("href");
                        doc = Jsoup.connect(link).get();
                        Element heading = doc.selectFirst("[class=holidays-item-info]");
                        List<String> headingStrings = new ArrayList<>();
                        Elements aa = heading.select("tr td");
                        for (int i = 0; i < aa.size(); i++) {
                            if (!aa.get(i).text().equals("")) {
                                headingStrings.add(aa.get(i).text());
                            }
                        }
                        List<String> shortDesc = new ArrayList<>();
                        aa = doc.select("[class=holidays-text]").select("p, [class=names-item-sub]");
                        for (Element i : aa) {
                            if (!i.toString().contains("names-item-sub")) {
                                shortDesc.add(i.text());
                            } else {
                                break;
                            }
                        }
                        Elements soderzhanie = doc.select("ol li");
                        List<Description> descs = new ArrayList<>();
                        Elements els = doc.select("h2, p, li");
                        List<Description> descriptions = new ArrayList<>();
                        for (Element stat : soderzhanie) {
                            String id = stat.select("a").attr("href").split("#")[1];
                            boolean recording = false;
                            for (int i = 0; i < els.size(); i++) {
                                String idd = els.get(i).id();
                                if (id.equalsIgnoreCase(els.get(i).id())) {
                                    recording = true;
                                    descriptions.add(new Description(Type.BOLD_HEADING, els.get(i).text()));
                                } else if (idd.equalsIgnoreCase("") && recording) {
                                    if (els.get(i).toString().contains("li")) {
                                        descriptions.add(new Description(Type.LIST_ITEM, els.get(i).text()));
                                    } else {
                                        descriptions.add(new Description(Type.USUAL_TEXT, els.get(i).text()));
                                    }
                                } else if (recording) {
                                    recording = false;
                                    break;
                                }
                            }
                        }
                        List<Description> finalDescription = new ArrayList<>();
                        for (int i = 0; i < headingStrings.size(); i++) {
                            if (i % 2 == 0) {
                                finalDescription.add(new Description(Type.BOLD_HEADING, headingStrings.get(i)));
                            } else {
                                finalDescription.add(new Description(Type.USUAL_TEXT, headingStrings.get(i)));
                            }
                        }
                        for (int i = 0; i < shortDesc.size(); i++) {
                            finalDescription.add(new Description(Type.USUAL_TEXT, shortDesc.get(i)));
                        }
                        finalDescription.addAll(descriptions);
                        Present present = new Present(presentName, finalDescription);
                        presents.add(present);
                    } catch (IllegalArgumentException e) {
                        if (t.toString().contains("holidays-like")) {
                            String res = "";
                            for (int i = 0; i < t.text().split(" ").length - 1; i++) {
                                res += t.text().split(" ")[i] + " ";
                            }
                            res = res.substring(0, res.length() - 1);
                            presents.add(new Present(res));
                        } else {
                            presents.add(new Present(t.text()));
                        }
                    }
                }
                ConsoleHelper.writeString("Праздники добавлены!");
            } catch (IOException e) {
                ConsoleHelper.writeException(e);
                loadPresent();
            }
        }

        private static Quote getQuote() {
            try {
                Document doc = Jsoup.connect("https://quote-citation.com/random").get();
                Elements list = doc.select("div p");
                String quote = list.get(0).text();
                list = doc.select("[class=source]").select("[target=_blank]");
                String author = "";
                for (int i = 0; i < list.size(); i++) {
                    if (i != list.size() - 1) {
                        author += list.get(i).text() + ", ";
                    } else {
                        author += list.get(i).text();
                    }
                }
                return new Quote(author, quote);
            } catch (IOException e) {
                ConsoleHelper.writeException(e);
                return null;
            }
        }

        private static Fact getFact() {
            try {
                Document doc = Jsoup.connect("https://randstuff.ru/fact/").userAgent("Chrome/4.0.249.0 Safari/532.5")
                        .referrer("http://www.google.com")
                        .get();
                Elements list = doc.select("tr td");
                return new Fact(list.get(0).text());
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        private static void loadChurchPresent() {
            try {
                churchPresents = new ArrayList<>();
                Document doc = Jsoup.connect("https://my-calend.ru/orthodox-calendar").get();
                Elements list = doc.select("tbody tr td");
                int i = 0;
                for (Element t : list) {
                    if (t.text().equalsIgnoreCase("7")) {
                        break;
                    }
                    if (i % 2 == 0) {
                        churchPresents.add(t.text());
                    } else {
                    }
                    i++;
                }
                ConsoleHelper.writeString("Церковные праздники добавлены!");
            } catch (IOException e) {
                ConsoleHelper.writeException(e);
                loadChurchPresent();
            }
        }

        private static void loadNames() {
            try {
                names = new ArrayList<>();
                Document doc = Jsoup.connect("https://my-calend.ru/name-days").get();
                Elements list = doc.select("p span");
                for (int i = 0; i < list.size() - 1; i++) {
                    String a = list.get(i).text().substring(0, list.get(i).text().length() - 1);
                    names.add(a);
                }
                ConsoleHelper.writeString("Именины добавлены!");
            } catch (IOException e) {
                ConsoleHelper.writeException(e);
                loadNames();
            }
        }

        private static void loadHumans() {
            try {
                humans = new ArrayList<>();
                String mesyac;
                String day;
                day = new SimpleDateFormat("d").format(updateTime);
                mesyac = new SimpleDateFormat("MMMM", new DateFormatSymbols() {
                    @Override
                    public String[] getMonths() {
                        return new String[]{"yanvarya", "fevralya", "marta", "aprelya", "maya", "iyunya",
                                "iyulya", "avgusta", "sentyabrya", "oktyabrya", "noyabrya", "dekabrya"};
                    }
                }).format(updateTime);
                Document doc = Jsoup.connect("https://stuki-druki.com/DenRozhdenia/Kto-rodilsya-" + day + "-" + mesyac + ".php").get();
                Elements images = doc.select("[class=leftimg auto_img roundfoto]");
                Elements names = doc.select("[class=hdr4_dr]");
                Elements descs = doc.select("div span");
                for (int i = 0; i < images.size(); i++) {
                    String h = images.get(i).toString();
                    Element imageElement = images.get(i).select("img").first();
                    String srcValue = imageElement.attr("src");
                    if (!srcValue.startsWith("https")) {
                        srcValue = "https:" + srcValue;
                    }
                    humans.add(new Human(names.get(i).text(), descs.get(i).text(), srcValue));
                }
                ConsoleHelper.writeString("Дни рождения знаменитостей добавлены!");
            } catch (Exception e) {
                ConsoleHelper.writeException(e);
                loadHumans();
            }
        }

        public static NewRecipes getRecipes() {
            try {
                Document doc = Jsoup.connect("https://aboutfood.club/random-dish/").get();
                Elements el = doc.select("[class=entry-title acme_grid_title]");
                String url1 = el.get(0).select("a[href]").attr("abs:href");
                String url2 = el.get(1).select("a[href]").attr("abs:href");
                String url3 = el.get(2).select("a[href]").attr("abs:href");
                Document first = Jsoup.connect(url1).get();
                Document second = Jsoup.connect(url2).get();
                Document third = Jsoup.connect(url3).get();
                NewRecipes recipes = new NewRecipes();
                {
                    String name = first.select("[class=entry-title]").text();
                    String url = first.select("[class=featured-image]").select("[src]").attr("abs:src");
                    String everything = "";
                    Elements w = first.select("div p");
                    for (int i = 1; i < w.size(); i++) {
                        if (!w.get(i).text().equalsIgnoreCase("Оцените рецепт")) {
                            everything += w.get(i).text() + "\n";
                        } else {
                            break;
                        }
                    }
                    everything = everything.substring(0, everything.length() - 1);
                    recipes.addRecipe(name, url, everything, NewRecipeType.FIRST);
                }
                {
                    String name = second.select("[class=entry-title]").text();
                    String url = second.select("[class=featured-image]").select("[src]").attr("abs:src");
                    String everything = "";
                    Elements w = second.select("div p");
                    for (int i = 1; i < w.size(); i++) {
                        if (!w.get(i).text().equalsIgnoreCase("Оцените рецепт")) {
                            everything += w.get(i).text() + "\n";
                        } else {
                            break;
                        }
                    }
                    everything = everything.substring(0, everything.length() - 1);
                    recipes.addRecipe(name, url, everything, NewRecipeType.SECOND);
                }
                {
                    String name = third.select("[class=entry-title]").text();
                    String url = third.select("[class=featured-image]").select("[src]").attr("abs:src");
                    String everything = "";
                    Elements w = third.select("div p");
                    for (int i = 1; i < w.size(); i++) {
                        if (!w.get(i).text().equalsIgnoreCase("Оцените рецепт")) {
                            everything += w.get(i).text() + "\n";
                        } else {
                            break;
                        }
                    }
                    everything = everything.substring(0, everything.length() - 1);
                    recipes.addRecipe(name, url, everything, NewRecipeType.COMPOTE);
                }
                return recipes;
            } catch (IOException e) {
                ConsoleHelper.writeException(e);
            }
            return null;
        }

    }

    static Date updateTime = new Date();

    public static void main(String[] args) throws IOException, InterruptedException {
        ConsoleHelper.init();
        JSoupPropsLoader.loadMoonDay();
        JSoupPropsLoader.loadPresent();
        JSoupPropsLoader.loadOmens();
        JSoupPropsLoader.loadRecipe();
        JSoupPropsLoader.loadChurchPresent();
        JSoupPropsLoader.loadGoroscope();
        JSoupPropsLoader.loadNames();
        JSoupPropsLoader.loadHumans();
        loadWeekendsCombinations();
        ConsoleHelper.writeString("Программа готова работать!");
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Date now = new Date();
                        if (now.getDay() != updateTime.getDay()) {
                            updateTime = now;
                            JSoupPropsLoader.loadMoonDay();
                            JSoupPropsLoader.loadOmens();
                            JSoupPropsLoader.loadPresent();
                            JSoupPropsLoader.loadChurchPresent();
                            JSoupPropsLoader.loadGoroscope();
                            JSoupPropsLoader.loadNames();
                            JSoupPropsLoader.loadHumans();
                            JSoupPropsLoader.loadRecipe();
                        }
                        Thread.sleep(300000);
                    } catch (InterruptedException e) {
                        ConsoleHelper.writeException(e);
                    }
                }

            }
        }).start();
        int port = 4123; // 843 flash policy port
        YourDayServer s = new YourDayServer(port);
        s.start();
        InetAddress inetAddress = InetAddress.getLocalHost();
        System.out.println("IP Address:- " + inetAddress.getHostAddress());

        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String res = r.readLine();
            if (res.equals("exit")) {
                System.exit(0);
            } else if (res.equals("options")) {
                ConsoleHelper.hold();
                onEnter(r);
                ConsoleHelper.release();
            } else if (res.equals("help")) {
                System.out.println("Type: ");
                System.out.println("exit - exit from server;");
                System.out.println("options - options for server;");
            } else {
                System.out.println("Unknown command. Type 'help' for available commands.");
            }
        }
    }

    public static void onEnter(BufferedReader r) throws IOException {
        System.out.println("************************************************");
        System.out.println("You have entered options menu. You can choose: ");
        System.out.println("1 - modify/get version settings;");
        System.out.println("2 - update/get information;");
        System.out.println("3 - update/get time;");
        System.out.println("4 - for help;");
        System.out.println("5 - for exit;");
        while (true) {
            String answer = r.readLine();
            if (answer == null) {
                System.out.println("Unknown command");
            } else if (answer.equals("1")) {
                onVersionChosed(r);
                System.out.println("************************************************");
                System.out.println("You have entered options menu. You can choose: ");
                System.out.println("1 - modify/get version settings;");
                System.out.println("2 - update/get information;");
                System.out.println("3 - update/get time;");
                System.out.println("4 - for help;");
                System.out.println("5 - for exit;");
            } else if (answer.equals("2")) {
                onInfoChosed(r);
                System.out.println("************************************************");
                System.out.println("You have entered options menu. You can choose: ");
                System.out.println("1 - modify/get version settings;");
                System.out.println("2 - update/get information;");
                System.out.println("3 - update/get time;");
                System.out.println("4 - for help;");
                System.out.println("5 - for exit;");
            } else if (answer.equals("3")) {
                onTimeChosed(r);
                System.out.println("************************************************");
                System.out.println("You have entered options menu. You can choose: ");
                System.out.println("1 - modify/get version settings;");
                System.out.println("2 - update/get information;");
                System.out.println("3 - update/get time;");
                System.out.println("4 - for help;");
                System.out.println("5 - for exit;");
            } else if (answer.equals("4")) {
                System.out.println("************************************************");
                System.out.println("You have entered options menu. You can choose: ");
                System.out.println("1 - modify/get version settings;");
                System.out.println("2 - update/get information;");
                System.out.println("3 - update/get time;");
                System.out.println("4 - for help;");
                System.out.println("5 - for exit;");
            } else if (answer.equals("5")) {
                break;
            } else {
                System.out.println("Unknown command. Type '4' for available commands.");
            }
        }
    }

    private static void onTimeChosed(BufferedReader r) throws IOException {
        System.out.println("************************************************");
        System.out.println("You have entered date options.");
        System.out.println("1 - get date used for server;");
        System.out.println("2 - update date for server;");
        System.out.println("3 - for help;");
        System.out.println("4 - for exit;");
        String answer;
        while (true) {
            answer = r.readLine();
            if (answer == null) {
                System.out.println("Unknown command. Type '3' for help.");
            } else if (answer.equals("1")) {
                System.out.println(new SimpleDateFormat("dd MMMM yyyy").format(updateTime));
            } else if (answer.equals("2")) {
                System.out.println("************************************************");
                updateTime = new Date();
                System.out.println("Time was updated. Reloading information...");
                JSoupPropsLoader.loadPresent();
                JSoupPropsLoader.loadChurchPresent();
                JSoupPropsLoader.loadGoroscope();
                JSoupPropsLoader.loadNames();
                JSoupPropsLoader.loadHumans();
                System.out.println("Information was updated.");
            } else if (answer.equals("3")) {
                System.out.println("************************************************");
                System.out.println("You have entered date options.");
                System.out.println("1 - get date used for server;");
                System.out.println("2 - update date for server;");
                System.out.println("3 - for help;");
                System.out.println("4 - for exit;");
            } else if (answer.equals("4")) {
                return;
            } else {
                System.out.println("Unknown command. Type '3' for available commands.");
            }
        }

    }

    private static void onInfoChosed(BufferedReader r) throws IOException {
        System.out.println("************************************************");
        System.out.println("You have entered information options.");
        System.out.println("1 - get presents;");
        System.out.println("2 - get church presents;");
        System.out.println("3 - get goroscopes;");
        System.out.println("4 - get Day Of Angels;");
        System.out.println("5 - get famous peoples birthdays;");
        System.out.println("6 - update presents;");
        System.out.println("7 - update church presents;");
        System.out.println("8 - update goroscopes;");
        System.out.println("9 - update Day Of Angels;");
        System.out.println("10 - update famous peoples birthdays;");
        System.out.println("11 - for help;");
        System.out.println("12 - for exit;");
        while (true) {
            String answer = r.readLine();
            if (answer == null) {
                System.out.println("Unknown command. Type '11' for available commands.");
            } else if (answer.equals("1")) {
                System.out.println("************************************************");
                /*for (String a : presents) {
                    System.out.println(a);
                }*/
            } else if (answer.equals("2")) {
                System.out.println("************************************************");
                for (String a : churchPresents) {
                    System.out.println(a);
                }
            } else if (answer.equals("3")) {
                System.out.println("************************************************");
                for (Map.Entry<String, String> a : gorosope.entrySet()) {
                    System.out.println(a.getKey() + " - " + a.getValue());
                }
            } else if (answer.equals("4")) {
                System.out.println("************************************************");
                for (String a : names) {
                    System.out.println(a);
                }
            } else if (answer.equals("5")) {
                System.out.println("************************************************");
                for (Human a : humans) {
                    System.out.println(a);
                }
            } else if (answer.equals("6")) {
                System.out.println("************************************************");
                JSoupPropsLoader.loadPresent();
                System.out.println("Presents was updated.");
            } else if (answer.equals("7")) {
                System.out.println("************************************************");
                JSoupPropsLoader.loadChurchPresent();
                System.out.println("Church presents was updated.");
            } else if (answer.equals("8")) {
                System.out.println("************************************************");
                JSoupPropsLoader.loadGoroscope();
                System.out.println("Goroscope was updated.");
            } else if (answer.equals("9")) {
                System.out.println("************************************************");
                JSoupPropsLoader.loadNames();
                System.out.println("Names was updated.");
            } else if (answer.equals("10")) {
                System.out.println("************************************************");
                JSoupPropsLoader.loadHumans();
                System.out.println("Humans was updated.");
            } else if (answer.equals("11")) {
                System.out.println("************************************************");
                System.out.println("1 - get presents;");
                System.out.println("2 - get church presents;");
                System.out.println("3 - get goroscopes;");
                System.out.println("4 - get Day Of Angels;");
                System.out.println("5 - get famous peoples birthdays;");
                System.out.println("6 - update presents;");
                System.out.println("7 - update church presents;");
                System.out.println("8 - update goroscopes;");
                System.out.println("9 - update Day Of Angels;");
                System.out.println("10 - update famous peoples birthdays;");
                System.out.println("11 - for help;");
                System.out.println("12 - for exit;");
            } else if (answer.equals("12")) {
                return;
            } else {
                System.out.println("Unknown command. Type '11' for available commands.");
            }
        }
    }

    private static void onVersionChosed(BufferedReader r) throws IOException {
        System.out.println("************************************************");
        System.out.println("1 - get version;");
        System.out.println("2 - get update string;");
        System.out.println("3 - set version;");
        System.out.println("4 - set update string(s);");
        System.out.println("5 - for help;");
        System.out.println("6 - for exit;");
        while (true) {
            String answer = r.readLine();
            if (answer == null) {
                System.out.println("Unknown command. Type '5' for available commands.");
            } else if (answer.equals("1")) {
                System.out.println(version);
                System.out.println("************************************************");
            } else if (answer.equals("2")) {
                System.out.println(update);
                System.out.println("************************************************");
            } else if (answer.equals("3")) {
                while (true) {
                    System.out.print("Enter version name: ");
                    String a = r.readLine();
                    if (a == null) {
                        System.out.println("Invalid version code");
                    } else {
                        version = a;
                        System.out.println("Version setted succesfully!");
                        System.out.println("************************************************");
                        break;
                    }
                }
            } else if (answer.equals("4")) {
                System.out.println("Enter update string(s);");
                System.out.println("Type: 'exit' for finishing.");
                String res = "\n";
                while (true) {
                    String a = r.readLine();

                    if (a == null) {
                        System.out.println("Invalid string code");
                    } else if (!a.equals("exit")) {
                        res += "*" + a + "\n";
                    } else {
                        break;
                    }
                }
                update = res;
                System.out.println("Updates setted succesfully!");
                System.out.println("************************************************");
            } else if (answer.equals("5")) {
                System.out.println("************************************************");
                System.out.println("1 - get version;");
                System.out.println("2 - get update string;");
                System.out.println("3 - set version;");
                System.out.println("4 - set update string(s);");
                System.out.println("5 - for help;");
                System.out.println("6 - for exit;");
            } else if (answer.equals("6")) {
                return;
            } else {
                System.out.println("Unknown command. Type '5' for available commands.");
            }
        }

    }


    private static void loadWeekendsCombinations() {
        weekendCombinations.add("1 1");
        weekendCombinations.add("2 1");
        weekendCombinations.add("3 1");
        weekendCombinations.add("6 1");
        weekendCombinations.add("7 1");
        weekendCombinations.add("8 1");
        weekendCombinations.add("24 2");
        weekendCombinations.add("9 3");
        weekendCombinations.add("1 5");
        weekendCombinations.add("4 5");
        weekendCombinations.add("5 5");
        weekendCombinations.add("11 5");
        weekendCombinations.add("12 6");
        weekendCombinations.add("4 11");
    }

    public YourDayServer(int port) throws UnknownHostException {
        super(new InetSocketAddress(port));
    }

    public YourDayServer(InetSocketAddress address) {
        super(address);
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        ConsoleHelper.writeString("Подключен новый пользователь!");
        JSONObject o = new JSONObject();
        o.put("type", "version");
        o.put("ver", version);
        o.put("desc", update);
        webSocket.send(o.toString());
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {

    }

    @Override
    public void onMessage(WebSocket webSocket, String s) {
        //TODO:This is server
        JSONObject o = new JSONObject(s);
        ConsoleHelper.writeString("Принял сообщение: " + s);
        if (o.get("type").equals("day")) {
            JSONObject otvet = new JSONObject();
            Day finalAnswer = new Day(updateTime, isWeekend(updateTime), presents.get((int) (Math.random() * presents.size())), churchPresents.get((int) (Math.random() * churchPresents.size())));
            otvet = finalAnswer.createJSON();
            otvet.put("type", "dayRecieve");
            webSocket.send(otvet.toString());
            ConsoleHelper.writeString("Сегодняшний день отправлен: " + otvet.toString());
        } else if (o.get("type").equals("goroscope")) {
            JSONObject otvet;
            String p = o.getString("sign");
            Goroscope answer = new Goroscope(p, gorosope.get(p));
            otvet = answer.createJSON();
            otvet.put("type", "goroscopeRecieve");
            webSocket.send(otvet.toString());
            ConsoleHelper.writeString("Сегодняшний гороскоп отправлен: " + otvet.toString());
        } else if (o.get("type").equals("allPresents")) {
            JSONObject answer = new JSONObject();
            answer.put("type", "allPresentsAnswer");
            JSONArray array = new JSONArray();
            for (Present a : presents) {
                array.put(a.name);
            }
            for (String a : churchPresents) {
                array.put("Церковный праздник: " + a);
            }
            answer.put("presents", array);
            webSocket.send(answer.toString());
            ConsoleHelper.writeString("Все праздники отправлены: " + answer.toString());
        } else if (o.get("type").equals("allNewPresents")) {
            JSONObject answer = new JSONObject();
            answer.put("type", "allPresentsAnswer");
            JSONArray array = new JSONArray();
            for (Present a : presents) {
                array.put(a.createJSON());
            }
            for (String a : churchPresents) {
                array.put("Церковный праздник: " + a);
            }
            answer.put("presents", array);
            webSocket.send(answer.toString());
            ConsoleHelper.writeString("Все праздники отправлены: " + answer.toString());
        } else if (o.get("type").equals("namesBirthdays")) {
            Names n = new Names(names);
            JSONArray a = n.createJSON();
            JSONObject root = new JSONObject();
            root.put("type", "namesBirthdaysAnswer");
            root.put("names", a);
            webSocket.send(root.toString());
            ConsoleHelper.writeString("Именины отправлены: " + root.toString());
        } else if (o.get("type").equals("famousPeoples")) {
            Human j = humans.get((int) (humans.size() * Math.random()));
            JSONObject answer = j.createJSON();
            answer.put("type", "famousPeoplesAnswer");
            webSocket.send(answer.toString());
            ConsoleHelper.writeString("День рождения знаменитости отправлен: " + answer.toString());
        } else if (o.get("type").equals("allFamousPeoplePresents")) {
            JSONObject root = new JSONObject();
            root.put("type", "allFamousPeoplePresentsAnswer");
            JSONArray humans = new JSONArray();
            for (Human h : this.humans) {
                humans.put(h.createJSON());
            }
            root.put("humans", humans);
            webSocket.send(root.toString());
            ConsoleHelper.writeString("Все сегодняшние дни рождения знаменитости отправлен: " + root.toString());
        } else if (o.get("type").equals("quote")) {
            Quote quote = JSoupPropsLoader.getQuote();
            while (quote == null) {
                quote = JSoupPropsLoader.getQuote();
            }
            JSONObject answer = quote.createJSON();
            answer.put("type", "quoteAnswer");
            webSocket.send(answer.toString());
            ConsoleHelper.writeString("Цитата отправлена: " + answer.toString());
        } else if (o.get("type").equals("fact")) {
            Fact fact = JSoupPropsLoader.getFact();
            while (fact == null) {
                fact = JSoupPropsLoader.getFact();
            }
            JSONObject root = fact.createJSON();
            root.put("type", "factAnswer");
            webSocket.send(root.toString());
            ConsoleHelper.writeString("Интересный факт отправлен: " + root.toString());
        } else if (o.get("type").equals("recipe")) {
            JSONObject answer = recipe.createJSON();
            answer.put("type", "recipeAnswer");
            webSocket.send(answer.toString());
            ConsoleHelper.writeString("Рецепт отправлен: " + answer.toString());
        } else if (o.get("type").equals("newRecipe")) {
            NewRecipes recipes = JSoupPropsLoader.getRecipes();
            JSONObject res = recipes.createJSON();
            res.put("type", "newRecipeAnswer");
            webSocket.send(res.toString());
        } else if (o.get("type").equals("joke")) {
            Joke joke = JSoupPropsLoader.getJoke();
            JSONObject answer = joke.createJSON();
            answer.put("type", "jokeAnswer");
            webSocket.send(answer.toString());
        } else if (o.get("type").equals("omen")) {
            JSONObject object = omen.createJSON();
            object.put("type", "omenAnswer");
            webSocket.send(object.toString());
        } else if (o.get("type").equals("moonDay")) {
            JSONObject object = moonDay.createJSON();
            object.put("type", "moonDayAnswer");
            webSocket.send(object.toString());
        }
    }

    private boolean isWeekend(Date updateTime) {
        String dayOfTheWeek = new SimpleDateFormat("EEEE", new Locale("ru")).format(updateTime);
        if (dayOfTheWeek.equalsIgnoreCase("суббота") || dayOfTheWeek.equalsIgnoreCase("воскресенье")) {
            return true;
        } else {
            DateFormat a = new SimpleDateFormat("d M");
            String answer = a.format(updateTime);
            if (weekendCombinations.contains(answer)) {
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {
        ConsoleHelper.writeException(e);
    }

    @Override
    public void onStart() {

    }
}
