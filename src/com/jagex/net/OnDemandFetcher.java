package com.jagex.net;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

import java.io.*;
import java.net.Socket;
import java.util.zip.CRC32;
import java.util.zip.GZIPInputStream;

import com.jagex.Client;
import com.jagex.Utils;
import com.jagex.cache.Archive;
import com.jagex.io.Buffer;
import com.jagex.link.Deque;
import com.jagex.link.Queue;
import com.jagex.window.RSBase;

public final class OnDemandFetcher extends OnDemandFetcherParent
        implements Runnable
{

    private boolean crcMatches(int i, int j, byte abyte0[])
    {
        if(abyte0 == null || abyte0.length < 2)
            return false;
        int k = abyte0.length - 2;
        int l = ((abyte0[k] & 0xff) << 8) + (abyte0[k + 1] & 0xff);
        this.crc32.reset();
        this.crc32.update(abyte0, 0, k);
        int i1 = (int) this.crc32.getValue();
        return l == i && i1 == j;
    }

    private void readData()
    {
        try
        {
            int j = this.inputStream.available();
            if(this.expectedSize == 0 && j >= 6)
            {
                this.waiting = true;
                for(int k = 0; k < 6; k += this.inputStream.read(this.ioBuffer, k, 6 - k));
                int l = this.ioBuffer[0] & 0xff;
                int j1 = ((this.ioBuffer[1] & 0xff) << 8) + (this.ioBuffer[2] & 0xff);
                int l1 = ((this.ioBuffer[3] & 0xff) << 8) + (this.ioBuffer[4] & 0xff);
                int i2 = this.ioBuffer[5] & 0xff;
                this.current = null;
                for(OnDemandData onDemandData = (OnDemandData) this.requested.getFront(); onDemandData != null; onDemandData = (OnDemandData) this.requested.getNext())
                {
                    if(onDemandData.dataType == l && onDemandData.ID == j1)
                        this.current = onDemandData;
                    if(this.current != null)
                        onDemandData.loopCycle = 0;
                }

                if(this.current != null)
                {
                    this.loopCycle = 0;
                    if(l1 == 0)
                    {
                        Utils.reporterror("Rej: " + l + "," + j1);
                        this.current.buffer = null;
                        if(this.current.incomplete)
                            synchronized(this.aClass19_1358)
                            {
                                this.aClass19_1358.pushBack(this.current);
                            }
                        else
                            this.current.unlink();
                        this.current = null;
                    } else
                    {
                        if(this.current.buffer == null && i2 == 0)
                            this.current.buffer = new byte[l1];
                        if(this.current.buffer == null && i2 != 0)
                            throw new IOException("missing start of file");
                    }
                }
                this.completedSize = i2 * 500;
                this.expectedSize = 500;
                if(this.expectedSize > l1 - i2 * 500)
                    this.expectedSize = l1 - i2 * 500;
            }
            if(this.expectedSize > 0 && j >= this.expectedSize)
            {
                this.waiting = true;
                byte abyte0[] = this.ioBuffer;
                int i1 = 0;
                if(this.current != null)
                {
                    abyte0 = this.current.buffer;
                    i1 = this.completedSize;
                }
                for(int k1 = 0; k1 < this.expectedSize; k1 += this.inputStream.read(abyte0, k1 + i1, this.expectedSize - k1));
                if(this.expectedSize + this.completedSize >= abyte0.length && this.current != null)
                {
                    if(this.clientInstance.indexs[0] != null)
                        this.clientInstance.indexs[this.current.dataType + 1].put(abyte0.length, abyte0, this.current.ID);
                    if(!this.current.incomplete && this.current.dataType == 3)
                    {
                        this.current.incomplete = true;
                        this.current.dataType = 93;
                    }
                    if(this.current.incomplete)
                        synchronized(this.aClass19_1358)
                        {
                            this.aClass19_1358.pushBack(this.current);
                        }
                    else
                        this.current.unlink();
                }
                this.expectedSize = 0;
            }
        }
        catch(IOException ioexception)
        {
            try
            {
                this.socket.close();
            }
            catch(Exception _ex) { }
            this.socket = null;
            this.inputStream = null;
            this.outputStream = null;
            this.expectedSize = 0;
        }
    }

    public void start(Archive archive, Client client1)
    {
        String as[] = {
            "model_version", "anim_version", "midi_version", "map_version"
        };
        for(int i = 0; i < 4; i++)
        {
            byte abyte0[] = archive.getEntry(as[i]);
            int j = abyte0.length / 2;
            Buffer buffer = new Buffer(abyte0);
            this.versions[i] = new int[j];
            this.fileStatus[i] = new byte[j];
            for(int l = 0; l < j; l++)
                this.versions[i][l] = buffer.readUShort();

        }

        String as1[] = {
            "model_crc", "anim_crc", "midi_crc", "map_crc"
        };
        for(int k = 0; k < 4; k++)
        {
            byte abyte1[] = archive.getEntry(as1[k]);
            int i1 = abyte1.length / 4;
            Buffer buffer_1 = new Buffer(abyte1);
            this.crcs[k] = new int[i1];
            for(int l1 = 0; l1 < i1; l1++)
                this.crcs[k][l1] = buffer_1.readInt();

        }

        byte abyte2[] = archive.getEntry("model_index");
        int j1 = this.versions[0].length;
        this.modelIndices = new byte[j1];
        for(int k1 = 0; k1 < j1; k1++)
            if(k1 < abyte2.length)
                this.modelIndices[k1] = abyte2[k1];
            else
                this.modelIndices[k1] = 0;

        abyte2 = archive.getEntry("map_index");
        Buffer buffer2 = new Buffer(abyte2);
        j1 = abyte2.length / 7;
        this.mapIndices1 = new int[j1];
        this.mapIndices2 = new int[j1];
        this.mapIndices3 = new int[j1];
        this.mapIndices4 = new int[j1];
        for(int i2 = 0; i2 < j1; i2++)
        {
            this.mapIndices1[i2] = buffer2.readUShort();
            this.mapIndices2[i2] = buffer2.readUShort();
            this.mapIndices3[i2] = buffer2.readUShort();
            this.mapIndices4[i2] = buffer2.readUByte();
        }

        abyte2 = archive.getEntry("anim_index");
        buffer2 = new Buffer(abyte2);
        j1 = abyte2.length / 2;
        this.anIntArray1360 = new int[j1];
        for(int j2 = 0; j2 < j1; j2++)
            this.anIntArray1360[j2] = buffer2.readUShort();

        abyte2 = archive.getEntry("midi_index");
        buffer2 = new Buffer(abyte2);
        j1 = abyte2.length;
        this.anIntArray1348 = new int[j1];
        for(int k2 = 0; k2 < j1; k2++)
            this.anIntArray1348[k2] = buffer2.readUByte();

        this.clientInstance = client1;
        this.running = true;
        Utils.startRunnable(this, 2);
    }

    public int getNodeCount()
    {
        synchronized(this.queue)
        {
            return this.queue.size();
        }
    }

    public void disable()
    {
        this.running = false;
    }

    public void method554(boolean flag)
    {
        int j = this.mapIndices1.length;
        for(int k = 0; k < j; k++)
            if(flag || this.mapIndices4[k] != 0)
            {
                this.method563((byte)2, 3, this.mapIndices3[k]);
                this.method563((byte)2, 3, this.mapIndices2[k]);
            }

    }

    public int getVersionCount(int j)
    {
        return this.versions[j].length;
    }

    private void closeRequest(OnDemandData onDemandData)
    {
        try
        {
            if(this.socket == null)
            {
                long l = System.currentTimeMillis();
                if(l - this.openSocketTime < 4000L)
                    return;
                this.openSocketTime = l;
                this.socket = this.clientInstance.openSocket(43594 + RSBase.portOff);
                this.inputStream = this.socket.getInputStream();
                this.outputStream = this.socket.getOutputStream();
                this.outputStream.write(15);
                for(int j = 0; j < 8; j++)
                    this.inputStream.read();

                this.loopCycle = 0;
            }
            this.ioBuffer[0] = (byte)onDemandData.dataType;
            this.ioBuffer[1] = (byte)(onDemandData.ID >> 8);
            this.ioBuffer[2] = (byte)onDemandData.ID;
            if(onDemandData.incomplete)
                this.ioBuffer[3] = 2;
            else
            if(!this.clientInstance.loggedIn)
                this.ioBuffer[3] = 1;
            else
                this.ioBuffer[3] = 0;
            this.outputStream.write(this.ioBuffer, 0, 4);
            this.writeLoopCycle = 0;
            this.anInt1349 = -10000;
            return;
        }
        catch(IOException ioexception) { }
        try
        {
            this.socket.close();
        }
        catch(Exception _ex) { }
        this.socket = null;
        this.inputStream = null;
        this.outputStream = null;
        this.expectedSize = 0;
        this.anInt1349++;
    }

    public int getAnimCount()
    {
        return this.anIntArray1360.length;
    }

    public void method558(int i, int j)
    {
        if(i < 0 || i > this.versions.length || j < 0 || j > this.versions[i].length)
            return;
        if(this.versions[i][j] == 0)
            return;
        synchronized(this.queue)
        {
            for(OnDemandData onDemandData = (OnDemandData) this.queue.peek(); onDemandData != null; onDemandData = (OnDemandData) this.queue.getNext())
                if(onDemandData.dataType == i && onDemandData.ID == j)
                    return;

            OnDemandData onDemandData_1 = new OnDemandData();
            onDemandData_1.dataType = i;
            onDemandData_1.ID = j;
            onDemandData_1.incomplete = true;
            synchronized(this.aClass19_1370)
            {
                this.aClass19_1370.pushBack(onDemandData_1);
            }
            this.queue.push(onDemandData_1);
        }
    }

    public int getModelIndex(int i)
    {
        return this.modelIndices[i] & 0xff;
    }

    @Override
	public void run()
    {
        try
        {
            while(this.running)
            {
                this.onDemandCycle++;
                int i = 20;
                if(this.anInt1332 == 0 && this.clientInstance.indexs[0] != null)
                    i = 50;
                try
                {
                    Thread.sleep(i);
                }
                catch(Exception _ex) { }
                this.waiting = true;
                for(int j = 0; j < 100; j++)
                {
                    if(!this.waiting)
                        break;
                    this.waiting = false;
                    this.checkReceived();
                    this.handleFailed();
                    if(this.uncompletedCount == 0 && j >= 5)
                        break;
                    this.method568();
                    if(this.inputStream != null)
                        this.readData();
                }

                boolean flag = false;
                for(OnDemandData onDemandData = (OnDemandData) this.requested.getFront(); onDemandData != null; onDemandData = (OnDemandData) this.requested.getNext())
                    if(onDemandData.incomplete)
                    {
                        flag = true;
                        onDemandData.loopCycle++;
                        if(onDemandData.loopCycle > 50)
                        {
                            onDemandData.loopCycle = 0;
                            this.closeRequest(onDemandData);
                        }
                    }

                if(!flag)
                {
                    for(OnDemandData onDemandData_1 = (OnDemandData) this.requested.getFront(); onDemandData_1 != null; onDemandData_1 = (OnDemandData) this.requested.getNext())
                    {
                        flag = true;
                        onDemandData_1.loopCycle++;
                        if(onDemandData_1.loopCycle > 50)
                        {
                            onDemandData_1.loopCycle = 0;
                            this.closeRequest(onDemandData_1);
                        }
                    }

                }
                if(flag)
                {
                    this.loopCycle++;
                    if(this.loopCycle > 750)
                    {
                        try
                        {
                            this.socket.close();
                        }
                        catch(Exception _ex) { }
                        this.socket = null;
                        this.inputStream = null;
                        this.outputStream = null;
                        this.expectedSize = 0;
                    }
                } else
                {
                    this.loopCycle = 0;
                    this.statusString = "";
                }
                if(this.clientInstance.loggedIn && this.socket != null && this.outputStream != null && (this.anInt1332 > 0 || this.clientInstance.indexs[0] == null))
                {
                    this.writeLoopCycle++;
                    if(this.writeLoopCycle > 500)
                    {
                        this.writeLoopCycle = 0;
                        this.ioBuffer[0] = 0;
                        this.ioBuffer[1] = 0;
                        this.ioBuffer[2] = 0;
                        this.ioBuffer[3] = 10;
                        try
                        {
                            this.outputStream.write(this.ioBuffer, 0, 4);
                        }
                        catch(IOException _ex)
                        {
                            this.loopCycle = 5000;
                        }
                    }
                }
            }
        }
        catch(Exception exception)
        {
            Utils.reporterror("od_ex " + exception.getMessage());
        }
    }

    public void method560(int i, int j)
    {
        if(this.clientInstance.indexs[0] == null)
            return;
        if(this.versions[j][i] == 0)
            return;
        if(this.fileStatus[j][i] == 0)
            return;
        if(this.anInt1332 == 0)
            return;
        OnDemandData onDemandData = new OnDemandData();
        onDemandData.dataType = j;
        onDemandData.ID = i;
        onDemandData.incomplete = false;
        synchronized(this.aClass19_1344)
        {
            this.aClass19_1344.pushBack(onDemandData);
        }
    }

    public OnDemandData getNextNode()
    {
        OnDemandData onDemandData;
        synchronized(this.aClass19_1358)
        {
            onDemandData = (OnDemandData)this.aClass19_1358.popFront();
        }
        if(onDemandData == null)
            return null;
        synchronized(this.queue)
        {
            onDemandData.unlinkCacheable();
        }
        if(onDemandData.buffer == null)
            return onDemandData;
        int i = 0;
        try
        {
            GZIPInputStream gzipinputstream = new GZIPInputStream(new ByteArrayInputStream(onDemandData.buffer));
            do
            {
                if(i == this.gzipInputBuffer.length)
                    throw new RuntimeException("buffer overflow!");
                int k = gzipinputstream.read(this.gzipInputBuffer, i, this.gzipInputBuffer.length - i);
                if(k == -1)
                    break;
                i += k;
            } while(true);
        }
        catch(IOException _ex)
        {
            throw new RuntimeException("error unzipping");
        }
        onDemandData.buffer = new byte[i];
        System.arraycopy(this.gzipInputBuffer, 0, onDemandData.buffer, 0, i);

        return onDemandData;
    }

    public int method562(int i, int k, int l)
    {
        int i1 = (l << 8) + k;
        for(int j1 = 0; j1 < this.mapIndices1.length; j1++)
            if(this.mapIndices1[j1] == i1)
                if(i == 0)
                    return this.mapIndices2[j1];
                else
                    return this.mapIndices3[j1];
        return -1;
    }

    @Override
	public void method548(int i)
    {
        this.method558(0, i);
    }

    public void method563(byte byte0, int i, int j)
    {
        if(this.clientInstance.indexs[0] == null)
            return;
        if(this.versions[i][j] == 0)
            return;
        byte abyte0[] = this.clientInstance.indexs[i + 1].get(j);
        if(this.crcMatches(this.versions[i][j], this.crcs[i][j], abyte0))
            return;
        this.fileStatus[i][j] = byte0;
        if(byte0 > this.anInt1332)
            this.anInt1332 = byte0;
        this.totalFiles++;
    }

    public boolean method564(int i)
    {
        for(int k = 0; k < this.mapIndices1.length; k++)
            if(this.mapIndices3[k] == i)
                return true;
        return false;
    }

    private void handleFailed()
    {
        this.uncompletedCount = 0;
        this.completedCount = 0;
        for(OnDemandData onDemandData = (OnDemandData) this.requested.getFront(); onDemandData != null; onDemandData = (OnDemandData) this.requested.getNext())
            if(onDemandData.incomplete)
                this.uncompletedCount++;
            else
                this.completedCount++;

        while(this.uncompletedCount < 10)
        {
            OnDemandData onDemandData_1 = (OnDemandData)this.aClass19_1368.popFront();
            if(onDemandData_1 == null)
                break;
            if(this.fileStatus[onDemandData_1.dataType][onDemandData_1.ID] != 0)
                this.filesLoaded++;
            this.fileStatus[onDemandData_1.dataType][onDemandData_1.ID] = 0;
            this.requested.pushBack(onDemandData_1);
            this.uncompletedCount++;
            this.closeRequest(onDemandData_1);
            this.waiting = true;
        }
    }

    public void method566()
    {
        synchronized(this.aClass19_1344)
        {
            this.aClass19_1344.clear();
        }
    }

    private void checkReceived()
    {
        OnDemandData onDemandData;
        synchronized(this.aClass19_1370)
        {
            onDemandData = (OnDemandData)this.aClass19_1370.popFront();
        }
        while(onDemandData != null)
        {
            this.waiting = true;
            byte abyte0[] = null;
            if(this.clientInstance.indexs[0] != null)
                abyte0 = this.clientInstance.indexs[onDemandData.dataType + 1].get(onDemandData.ID);
            if(!this.crcMatches(this.versions[onDemandData.dataType][onDemandData.ID], this.crcs[onDemandData.dataType][onDemandData.ID], abyte0))
                abyte0 = null;
            synchronized(this.aClass19_1370)
            {
                if(abyte0 == null)
                {
                    this.aClass19_1368.pushBack(onDemandData);
                } else
                {
                    onDemandData.buffer = abyte0;
                    synchronized(this.aClass19_1358)
                    {
                        this.aClass19_1358.pushBack(onDemandData);
                    }
                }
                onDemandData = (OnDemandData)this.aClass19_1370.popFront();
            }
        }
    }

    private void method568()
    {
        while(this.uncompletedCount == 0 && this.completedCount < 10)
        {
            if(this.anInt1332 == 0)
                break;
            OnDemandData onDemandData;
            synchronized(this.aClass19_1344)
            {
                onDemandData = (OnDemandData)this.aClass19_1344.popFront();
            }
            while(onDemandData != null)
            {
                if(this.fileStatus[onDemandData.dataType][onDemandData.ID] != 0)
                {
                    this.fileStatus[onDemandData.dataType][onDemandData.ID] = 0;
                    this.requested.pushBack(onDemandData);
                    this.closeRequest(onDemandData);
                    this.waiting = true;
                    if(this.filesLoaded < this.totalFiles)
                        this.filesLoaded++;
                    this.statusString = "Loading extra files - " + (this.filesLoaded * 100) / this.totalFiles + "%";
                    this.completedCount++;
                    if(this.completedCount == 10)
                        return;
                }
                synchronized(this.aClass19_1344)
                {
                    onDemandData = (OnDemandData)this.aClass19_1344.popFront();
                }
            }
            for(int j = 0; j < 4; j++)
            {
                byte abyte0[] = this.fileStatus[j];
                int k = abyte0.length;
                for(int l = 0; l < k; l++)
                    if(abyte0[l] == this.anInt1332)
                    {
                        abyte0[l] = 0;
                        OnDemandData onDemandData_1 = new OnDemandData();
                        onDemandData_1.dataType = j;
                        onDemandData_1.ID = l;
                        onDemandData_1.incomplete = false;
                        this.requested.pushBack(onDemandData_1);
                        this.closeRequest(onDemandData_1);
                        this.waiting = true;
                        if(this.filesLoaded < this.totalFiles)
                            this.filesLoaded++;
                        this.statusString = "Loading extra files - " + (this.filesLoaded * 100) / this.totalFiles + "%";
                        this.completedCount++;
                        if(this.completedCount == 10)
                            return;
                    }

            }

            this.anInt1332--;
        }
    }

    public boolean method569(int i)
    {
        return this.anIntArray1348[i] == 1;
    }

    public OnDemandFetcher()
    {
        this.requested = new Deque();
        this.statusString = "";
        this.crc32 = new CRC32();
        this.ioBuffer = new byte[500];
        this.fileStatus = new byte[4][];
        this.aClass19_1344 = new Deque();
        this.running = true;
        this.waiting = false;
        this.aClass19_1358 = new Deque();
        this.gzipInputBuffer = new byte[65000];
        this.queue = new Queue();
        this.versions = new int[4][];
        this.crcs = new int[4][];
        this.aClass19_1368 = new Deque();
        this.aClass19_1370 = new Deque();
    }

    private int totalFiles;
    private final Deque requested;
    private int anInt1332;
    public String statusString;
    private int writeLoopCycle;
    private long openSocketTime;
    private int[] mapIndices3;
    private final CRC32 crc32;
    private final byte[] ioBuffer;
    public int onDemandCycle;
    private final byte[][] fileStatus;
    private Client clientInstance;
    private final Deque aClass19_1344;
    private int completedSize;
    private int expectedSize;
    private int[] anIntArray1348;
    public int anInt1349;
    private int[] mapIndices2;
    private int filesLoaded;
    private boolean running;
    private OutputStream outputStream;
    private int[] mapIndices4;
    private boolean waiting;
    private final Deque aClass19_1358;
    private final byte[] gzipInputBuffer;
    private int[] anIntArray1360;
    private final Queue queue;
    private InputStream inputStream;
    private Socket socket;
    private final int[][] versions;
    private final int[][] crcs;
    private int uncompletedCount;
    private int completedCount;
    private final Deque aClass19_1368;
    private OnDemandData current;
    private final Deque aClass19_1370;
    private int[] mapIndices1;
    private byte[] modelIndices;
    private int loopCycle;
}
