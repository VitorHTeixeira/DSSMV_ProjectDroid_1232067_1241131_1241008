package com.lvhm.covertocover.datamodels;

import android.os.Bundle;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class BookResponse{
    @SerializedName("items")
    private ArrayList<Item> items;
    public ArrayList<Item> getItems() {
        return items;
    }
    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public static class Item {
        @SerializedName("volumeInfo")
        private VolumeInfo volumeInfo;
        public VolumeInfo getVolumeInfo() {
            return volumeInfo;
        }
        public void setVolumeInfo(VolumeInfo volumeInfo) {
            this.volumeInfo = volumeInfo;
        }

        public static class VolumeInfo {
            @SerializedName("title")
            private String title;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            @SerializedName("subtitle")
            private String subtitle;

            public String getSubtitle() {
                return subtitle;
            }

            public void setSubtitle(String subtitle) {
                this.subtitle = subtitle;
            }

            @SerializedName("authors")
            private ArrayList<String> authors;

            public ArrayList<String> getAuthors() {
                return authors;
            }

            public void setAuthors(ArrayList<String> authors) {
                this.authors = authors;
            }

            @SerializedName("publishedDate")
            private String publishedDate;

            public String getPublishedDate() {
                return publishedDate;
            }

            public void setPublishedDate(String publishedDate) {
                this.publishedDate = publishedDate;
            }

            @SerializedName("pageCount")
            private String pageCount;

            public String getPageCount() {
                return pageCount;
            }

            public void setPageCount(String pageCount) {
                this.pageCount = pageCount;
            }

            @SerializedName("categories")
            private ArrayList<String> categories;

            public ArrayList<String> getCategories() {
                return categories;
            }

            public void setCategories(ArrayList<String> categories) {
                this.categories = categories;
            }

            @SerializedName("imageLinks")
            private ImageLinks imageLinks;

            public ImageLinks getImageLinks() {
                return imageLinks;
            }

            public void setImageLinks(ImageLinks imageLinks) {
                this.imageLinks = imageLinks;
            }

            public Bundle getBundle() {
                Bundle bundle = new Bundle();
                if (title != null) {
                    bundle.putString("title", title);
                }
                if (subtitle != null) {
                    bundle.putString("subtitle", subtitle);
                }
                if (authors != null) {
                    bundle.putStringArrayList("authors", authors);
                }
                if (publishedDate != null) {
                    bundle.putString("publishedDate", publishedDate);
                }
                if (pageCount != null) {
                    bundle.putString("pageCount", pageCount);
                }
                if (categories != null) {
                    bundle.putStringArrayList("categories", categories);
                }
                if (imageLinks != null) {
                    bundle.putString("thumbnail", imageLinks.getThumbnail());
                }
                return bundle;
            }

            public static class ImageLinks {
                @SerializedName("thumbnail")
                private String thumbnail;

                public String getThumbnail() {
                    return thumbnail;
                }

                public void setThumbnail(String thumbnail) {
                    this.thumbnail = thumbnail;
                }
            }
        }
    }
}
