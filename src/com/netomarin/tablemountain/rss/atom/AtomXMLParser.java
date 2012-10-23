
package com.netomarin.tablemountain.rss.atom;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class AtomXMLParser {

    public Feed parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    private Feed readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        Feed feed = new Feed();
        ArrayList<String> categories = new ArrayList<String>();
        ArrayList<Entry> entries = new ArrayList<Entry>();

        parser.require(XmlPullParser.START_TAG, null, "feed");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("entry")) {
                entries.add(readEntry(parser));
            } else if (name.equals("id")) {
                feed.setId(readText(name, parser));
            } else if (name.equals("updated")) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                try {
                    feed.setUpdated(formatter.parse(readText(name, parser)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else if (name.equals("category")) {
                categories.add(readCategory(parser));
            } else if (name.equals("title")) {
                feed.setTitle(readText(name, parser));
            } else if (name.equals("subtitle")) {
                feed.setSubtitle(readText(name, parser));
            } else if (name.equals("author")) {
                feed.setAuthor(readAuthor(parser));
            } else if (name.equals("openSearch:totalResults")) {
                feed.setTotalResults(Integer.parseInt(readText(name, parser)));
            } else if (name.equals("openSearch:startIndex")) {
                feed.setStartIndex(Integer.parseInt(readText(name, parser)));
            } else if (name.equals("openSearch:itemsPerPage")) {
                feed.setItemsPerPage(Integer.parseInt(readText(name, parser)));
            } else if (name.equals("link")) {
                parser.require(XmlPullParser.START_TAG, null, "link");
                String link = parser.getAttributeValue(null, "href");
                String relType = parser.getAttributeValue(null, "rel");
                if (relType.equals("alternate")) {
                    feed.setAlternateLink(link);
                } else if (relType.equals("next")) {
                    feed.setNextLink(link);
                }
                parser.nextTag();
                parser.require(XmlPullParser.END_TAG, null, "link");
            } else {
                skip(parser);
            }
        }

        feed.setCategories(categories);
        feed.setEntries(entries);

        return feed;
    }

    private Entry readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        Entry entry = new Entry();
        // 2012-09-26T10:43:00.000-07:00
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        parser.require(XmlPullParser.START_TAG, null, "entry");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("id")) {
                entry.setId(readText(name, parser));
            } else if (name.equals("published")) {
                String published = readText(name, parser);
                try {
                    entry.setPublished(formatter.parse(published));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else if (name.equals("updated")) {
                String updated = readText(name, parser);
                try {
                    entry.setUpdated(formatter.parse(updated));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else if (name.equals("app:edited")) {
                String edited = readText(name, parser);
                try {
                    entry.setEdited(formatter.parse(edited));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else if (name.equals("title")) {
                entry.setTitle(readText(name, parser));
            } else if (name.equals("category")) {
                entry.addCategory(readCategory(parser));
            } else if (name.equals("content")) {
                entry.setContent(readText(name, parser));
            } else if (name.equals("author")) {
                entry.setAuthor(readAuthor(parser));
            } else if (name.equals("link")) {
                parser.require(XmlPullParser.START_TAG, null, "link");
                String link = parser.getAttributeValue(null, "href");
                String relType = parser.getAttributeValue(null, "rel");
                if (relType.equals("alternate")) {
                    entry.setAlternateLink(link);
                } else if (relType.equals("self")) {
                    entry.setSelfLink(link);
                }
                parser.nextTag();
                parser.require(XmlPullParser.END_TAG, null, "link");
            } else {
                skip(parser);
            }
        }

        return entry;
    }

    private String readCategory(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, "category");
        String text = "";
        text = parser.getAttributeValue(null, "term");
        parser.nextTag();
        parser.require(XmlPullParser.END_TAG, null, "category");

        return text;
    }

    private Author readAuthor(XmlPullParser parser) throws XmlPullParserException, IOException {
        Author author = new Author();
        parser.require(XmlPullParser.START_TAG, null, "author");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("name")) {
                author.setName(readText(name, parser));
            } else if (name.equals("email")) {
                author.setEmail(readText(name, parser));
            } else if (name.equals("gd:image")) {
                parser.require(XmlPullParser.START_TAG, null, name);
                author.setImage(parser.getAttributeValue(null, "src"));
                parser.nextTag();
                parser.require(XmlPullParser.END_TAG, null, name);
            } else {
                skip(parser);
            }
        }
        return author;
    }

    private String readText(String tagName, XmlPullParser parser) throws XmlPullParserException,
            IOException {
        parser.require(XmlPullParser.START_TAG, null, tagName);
        String text = "";
        if (parser.next() == XmlPullParser.TEXT) {
            text = parser.getText();
            parser.nextTag();
        }
        parser.require(XmlPullParser.END_TAG, null, tagName);

        return text;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
