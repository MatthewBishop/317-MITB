// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)
// Source File Name:   signlink.java

package com.jagex.sign;

import java.io.*;
import java.net.*;

import com.jagex.Constants;

public final class signlink
    implements Runnable
{

    public static void startpriv()
    {
        signlink.threadliveid = (int)(Math.random() * 99999999D);
        if(signlink.active)
        {
            try
            {
                Thread.sleep(500L);
            }
            catch(Exception _ex) { }
            signlink.active = false;
        }
        signlink.dnsreq = null;
        signlink.savereq = null;
        Thread thread = new Thread(new signlink());
        thread.setDaemon(true);
        thread.start();
        while(!signlink.active)
            try
            {
                Thread.sleep(50L);
            }
            catch(Exception _ex) { }
    }

    @Override
	public void run()
    {
        signlink.active = true;
        String s = Constants.findcachedir();
        signlink.uid = signlink.getuid(s);
        try
        {
            File file = new File(s + "main_file_cache.dat");
            if(file.exists() && file.length() > 0x3200000L)
                file.delete();
            signlink.cache_dat = new RandomAccessFile(s + "main_file_cache.dat", "rw");
            for(int j = 0; j < 5; j++)
                signlink.cache_idx[j] = new RandomAccessFile(s + "main_file_cache.idx" + j, "rw");

        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        while(true)
        {
            if(signlink.dnsreq != null)
            {
                try
                {
                    signlink.dns = InetAddress.getByName(signlink.dnsreq).getHostName();
                }
                catch(Exception _ex)
                {
                    signlink.dns = "unknown";
                }
                signlink.dnsreq = null;
            } else
            if(signlink.savereq != null)
            {
                if(signlink.savebuf != null)
                    try
                    {
                        FileOutputStream fileoutputstream = new FileOutputStream(s + signlink.savereq);
                        fileoutputstream.write(signlink.savebuf, 0, signlink.savelen);
                        fileoutputstream.close();
                    }
                    catch(Exception _ex) { }
                if(signlink.waveplay)
                {
                    String wave = s + signlink.savereq;
                    signlink.waveplay = false;
                }
                if(signlink.midiplay)
                {
                    signlink.midi = s + signlink.savereq;
                    signlink.midiplay = false;
                }
                signlink.savereq = null;
            } 
            try
            {
                Thread.sleep(50L);
            }
            catch(Exception _ex) { }
        }

    }

    private static int getuid(String s)
    {
        try
        {
            File file = new File(s + "uid.dat");
            if(!file.exists() || file.length() < 4L)
            {
                DataOutputStream dataoutputstream = new DataOutputStream(new FileOutputStream(s + "uid.dat"));
                dataoutputstream.writeInt((int)(Math.random() * 99999999D));
                dataoutputstream.close();
            }
        }
        catch(Exception _ex) { }
        try
        {
            DataInputStream datainputstream = new DataInputStream(new FileInputStream(s + "uid.dat"));
            int i = datainputstream.readInt();
            datainputstream.close();
            return i + 1;
        }
        catch(Exception _ex)
        {
            return 0;
        }
    }

    public static synchronized void dnslookup(String s)
    {
        signlink.dns = s;
        signlink.dnsreq = s;
    }

    public static synchronized boolean wavesave(byte abyte0[], int i)
    {
        if(i > 0x1e8480)
            return false;
        if(signlink.savereq != null)
        {
            return false;
        } else
        {
            signlink.wavepos = (signlink.wavepos + 1) % 5;
            signlink.savelen = i;
            signlink.savebuf = abyte0;
            signlink.waveplay = true;
            signlink.savereq = "sound" + signlink.wavepos + ".wav";
            return true;
        }
    }

    public static synchronized boolean wavereplay()
    {
        if(signlink.savereq != null)
        {
            return false;
        } else
        {
            signlink.savebuf = null;
            signlink.waveplay = true;
            signlink.savereq = "sound" + signlink.wavepos + ".wav";
            return true;
        }
    }

    public static synchronized void midisave(byte abyte0[], int i)
    {
        if(i > 0x1e8480)
            return;
        if(signlink.savereq != null)
        {
        } else
        {
            signlink.midipos = (signlink.midipos + 1) % 5;
            signlink.savelen = i;
            signlink.savebuf = abyte0;
            signlink.midiplay = true;
            signlink.savereq = "jingle" + signlink.midipos + ".mid";
        }
    }

    private signlink()
    {
    }

    public static int uid;
    public static RandomAccessFile cache_dat = null;
    public static final RandomAccessFile[] cache_idx = new RandomAccessFile[5];
    public static boolean sunjava;
    private static boolean active;
    private static int threadliveid;
    private static String dnsreq = null;
    public static String dns = null;
    private static int savelen;
    private static String savereq = null;
    private static byte[] savebuf = null;
    private static boolean midiplay;
    private static int midipos;
    public static String midi = null;
    public static int midivol;
    public static int midifade;
    private static boolean waveplay;
    private static int wavepos;
    public static int wavevol;
    public static boolean reporterror = true;
    public static String errorname = "";

}
