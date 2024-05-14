package com.example.experiencethefutureofsmartermobility.model;

import java.util.Arrays;

public class HotplaceImage {
    private String lastBuildDate;
    private int total;
    private int start;
    private int display;
    private Item[] items;

    public static class Item {
        private String title;
        private String link;
        private String thumbnail;
        private int sizeheight;
        private int sizewidth;

        // Getters and Setters
        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public int getSizeheight() {
            return sizeheight;
        }

        public void setSizeheight(int sizeheight) {
            this.sizeheight = sizeheight;
        }

        public int getSizewidth() {
            return sizewidth;
        }

        public void setSizewidth(int sizewidth) {
            this.sizewidth = sizewidth;
        }

        @Override
        public String toString() {
            return "Item{" +
                    "title='" + title + '\'' +
                    ", link='" + link + '\'' +
                    ", thumbnail='" + thumbnail + '\'' +
                    ", sizeheight=" + sizeheight +
                    ", sizewidth=" + sizewidth +
                    '}';
        }
    }

    // Getters and Setters
    public String getLastBuildDate() {
        return lastBuildDate;
    }

    public void setLastBuildDate(String lastBuildDate) {
        this.lastBuildDate = lastBuildDate;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getDisplay() {
        return display;
    }

    public void setDisplay(int display) {
        this.display = display;
    }

    public Item[] getItems() {
        return items;
    }

    @Override
    public String toString() {
        return "HotplaceImage{" +
                "lastBuildDate='" + lastBuildDate + '\'' +
                ", total=" + total +
                ", start=" + start +
                ", display=" + display +
                ", items=" + Arrays.toString(items)+
                '}';
    }
}
