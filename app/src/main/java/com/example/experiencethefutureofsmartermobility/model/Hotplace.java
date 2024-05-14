package com.example.experiencethefutureofsmartermobility.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Arrays;

public class Hotplace implements Serializable {

    @SerializedName("lastBuildDate")
    private String lastBuildDate;
    private int total;
    private int start;
    private int display;
    private Item[] items;

    public String getLastBuildDate() {
        return lastBuildDate;
    }

    public int getTotal() {
        return total;
    }

    public int getStart() {
        return start;
    }

    public int getDisplay() {
        return display;
    }

    public Item[] getItems() {
        return items;
    }

    public static class Item implements Serializable{
        private String title;
        private String link;
        private String category;
        private String description;
        private String telephone;
        private String address;
        @SerializedName("roadAddress")
        private String roadAddress;
        private String mapx;
        private String mapy;

        public String getTitle() {
            return title;
        }

        public String getLink() {
            return link;
        }

        public String getCategory() {
            return category;
        }

        public String getDescription() {
            return description;
        }

        public String getTelephone() {
            return telephone;
        }

        public String getAddress() {
            return address;
        }

        public String getRoadAddress() {
            return roadAddress;
        }

        public String getMapx() {
            return mapx;
        }

        public String getMapy() {
            return mapy;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public void setRoadAddress(String roadAddress) {
            this.roadAddress = roadAddress;
        }

        public void setMapx(String mapx) {
            this.mapx = mapx;
        }

        public void setMapy(String mapy) {
            this.mapy = mapy;
        }

        @Override
        public String toString() {
            return "Item{" +
                    "title='" + title + '\'' +
                    ", link='" + link + '\'' +
                    ", category='" + category + '\'' +
                    ", description='" + description + '\'' +
                    ", telephone='" + telephone + '\'' +
                    ", address='" + address + '\'' +
                    ", roadAddress='" + roadAddress + '\'' +
                    ", mapx='" + mapx + '\'' +
                    ", mapy='" + mapy + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Local{" +
                "lastBuildDate='" + lastBuildDate + '\'' +
                ", total=" + total +
                ", start=" + start +
                ", display=" + display +
                ", items=" + Arrays.toString(items) +
                '}';
    }
}
