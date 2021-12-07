package com.example.weather.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VilageFcst {

    @Expose
    @SerializedName("response")
    private Response response;

    public Response getResponse() {
        return response;
    }

    public static class Response {

        @Expose
        @SerializedName("header")
        private Header header;

        @Expose
        @SerializedName("body")
        private Body body;

        public Header getHeader() {
            return header;
        }
        public Body getBody() {
            return body;
        }

        @Override
        public String toString() {
            return "Response{" +
                    "header=" + header +
                    ", body=" + body +
                    '}';
        }
    }

    public static class Header {

        @Expose
        @SerializedName("resultCode")
        private String resultCode;

        @Expose
        @SerializedName("resultMsg")
        private String resultMsg;

        public String getResultCode() {
            return resultCode;
        }
        public String getResultMsg() {
            return resultMsg;
        }

        @Override
        public String toString() {
            return "Header{" +
                    "resultCode='" + resultCode + '\'' +
                    ", resultMsg='" + resultMsg + '\'' +
                    '}';
        }
    }

    public static class Body {

        @Expose
        @SerializedName("dataType")
        private String dataType;

        @Expose
        @SerializedName("items")
        private Items items;

        @Expose
        @SerializedName("pageNo")
        private String pageNo;

        @Expose
        @SerializedName("numOfRows")
        private String numOfRows;

        @Expose
        @SerializedName("totalCount")
        private String totalCount;

        public String getDataType() {
            return dataType;
        }
        public Items getItems() {
            return items;
        }
        public String getPageNo() {
            return pageNo;
        }
        public String getNumOfRows() {
            return numOfRows;
        }
        public String getTotalCount() {
            return totalCount;
        }

        @Override
        public String toString() {
            return "Body{" +
                    "dataType='" + dataType + '\'' +
                    ", items=" + items +
                    ", pageNo='" + pageNo + '\'' +
                    ", numOfRows='" + numOfRows + '\'' +
                    ", totalCount='" + totalCount + '\'' +
                    '}';
        }
    }

    public static class Items {

        @Expose
        @SerializedName("item")
        private List<Item> item;

        public List<Item> getItem() {
            return item;
        }

        @Override
        public String toString() {
            return "Items{" +
                    "item=" + item +
                    '}';
        }
    }

    public static class Item {

        @Expose
        @SerializedName("baseDate")
        private String baseDate;

        @Expose
        @SerializedName("baseTime")
        private String baseTime;

        @Expose
        @SerializedName("category")
        private String category;

        @Expose
        @SerializedName("fcstDate")
        private String fcstDate;

        @Expose
        @SerializedName("fcstTime")
        private String fcstTime;

        @Expose
        @SerializedName("fcstValue")
        private String fcstValue;

        @Expose
        @SerializedName("nx")
        private String nx;

        @Expose
        @SerializedName("ny")
        private String ny;

        public String getBaseDate() {
            return baseDate;
        }
        public String getBaseTime() {
            return baseTime;
        }
        public String getCategory() {
            return category;
        }
        public String getFcstDate() {
            return fcstDate;
        }
        public String getFcstTime() {
            return fcstTime;
        }
        public String getFcstValue() {
            return fcstValue;
        }
        public String getNx() {
            return nx;
        }
        public String getNy() {
            return ny;
        }

        @Override
        public String toString() {
            return "Item{" +
                    "baseDate='" + baseDate + '\'' +
                    ", baseTime='" + baseTime + '\'' +
                    ", category='" + category + '\'' +
                    ", fcstDate='" + fcstDate + '\'' +
                    ", fcstTime='" + fcstTime + '\'' +
                    ", fcstValue='" + fcstValue + '\'' +
                    ", nx='" + nx + '\'' +
                    ", ny='" + ny + '\'' +
                    '}';
        }
    }
}