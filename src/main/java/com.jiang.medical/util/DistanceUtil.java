package com.jiang.medical.util;

/**
 * 2点经纬度计算距离
 * @author foxmen
 *
 */
public class DistanceUtil
 {
 
     private static final double EARTH_RADIUS = 6378137;
     private static double rad(double d)
     {
        return d * Math.PI / 180.0;
     }
     
     /**
      * 根据两点间经纬度坐标（double值），计算两点间距离，单位为米
      * @param lng1
      * @param lat1
      * @param lng2
      * @param lat2
      * @return
      */
     public static double getDistance(double lng1, double lat1, double lng2, double lat2)
     {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) + 
         Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
     }
     
     /**
      * 计算地球上任意两点(经纬度)距离
      *
      * @param long1 第一点经度
      * @param lat1  第一点纬度
      * @param long2 第二点经度
      * @param lat2  第二点纬度
      * @return 返回距离 单位：米
      */
     public static double distanceByLongNLat(double long1, double lat1, double long2, double lat2) {
         double a, b, R;
         R = 6378137;//地球半径
         lat1 = lat1 * Math.PI / 180.0;
         lat2 = lat2 * Math.PI / 180.0;
         a = lat1 - lat2;
         b = (long1 - long2) * Math.PI / 180.0;
         double d;
         double sa2, sb2;
         sa2 = Math.sin(a / 2.0);
         sb2 = Math.sin(b / 2.0);
         d = 2 * R * Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(lat1) * Math.cos(lat2) * sb2 * sb2));
         return d;
     }
     
     /**
      * 根据经纬度和半径计算经纬度范围
      *
      * @param raidus 单位米
      * @return minLat, minLng, maxLat, maxLng
      */
     public static double[] getAround(double lat, double lon, int raidus) {
         Double latitude = lat;
         Double longitude = lon;

         Double degree = (24901 * 1609) / 360.0;
         double raidusMile = raidus;

         Double dpmLat = 1 / degree;
         Double radiusLat = dpmLat * raidusMile;
         Double minLat = latitude - radiusLat;
         Double maxLat = latitude + radiusLat;

         Double mpdLng = degree * Math.cos(latitude * (Math.PI / 180));
         Double dpmLng = 1 / mpdLng;
         Double radiusLng = dpmLng * raidusMile;
         Double minLng = longitude - radiusLng;
         Double maxLng = longitude + radiusLng;
         return new double[]{minLat, minLng, maxLat, maxLng};
     }
     
     /**
      * @param args
      */
     public static void main(String[] args)
     {
     // TODO 自动生成方法存根
         double distance = getDistance(131.491909,41.233234,121.411994,11.206134);
         System.out.println("Distance is:"+distance);
     }
 
 }