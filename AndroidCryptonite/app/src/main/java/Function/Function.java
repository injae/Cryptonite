package Function;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Function
{
    public static void frontInsertByte(int start, byte[] from, byte[] to)
    {
        for(int i = 0 ; i < from.length; i++)
        {
            to[i + start] = from[i];
        }
    }

    public static byte[] cuttingByte(int start, byte[] array)
    {
        byte[] temp = new byte[array.length - start];
        for(int i = 0; i < temp.length; i++)
        {
            temp[i] = array[i+start];
        }

        return temp;
    }

    public static void backInsertByte(int start, byte[] from, byte[] to)
    {
        for(int i = 0 ; i < to.length; i++)
        {
            to[i] = from[i+2];
        }
    }

    //May be it cause Endian Probelm
    public static byte[] intToByteArray(final int integer) {
        ByteBuffer buff = ByteBuffer.allocate(Integer.SIZE / 8);
        buff.putInt(integer);
        buff.order(ByteOrder.BIG_ENDIAN);
        //buff.order(ByteOrder.LITTLE_ENDIAN);
        return buff.array();
    }

    public static int byteArrayToInt(byte[] bytes) {
        final int size = Integer.SIZE / 8;
        ByteBuffer buff = ByteBuffer.allocate(size);
        final byte[] newBytes = new byte[size];
        for (int i = 0; i < size; i++) {
            if (i + bytes.length < size) {
                newBytes[i] = (byte) 0x00;
            } else {
                newBytes[i] = bytes[i + bytes.length - size];
            }
        }
        buff = ByteBuffer.wrap(newBytes);
        buff.order(ByteOrder.BIG_ENDIAN); // Set matching Endian
        return buff.getInt();
    }

    public static byte[] doubleToByteArray(double value) {
        byte[] bytes = new byte[8];
        ByteBuffer.wrap(bytes).putDouble(value);
        return bytes;
    }

    public static double byteArrayToDouble(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getDouble();
    }


    public static double distance(double lat1, double lng1, double lat2, double lng2)
    {
        double earthRadius = 3958.75;
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double dist = earthRadius * c;
        double meterConversion = 1609.00;
        return dist * meterConversion;
    }
}

