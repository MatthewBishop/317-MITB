package com.jagex.cache.setting;

import com.jagex.cache.Archive;
import com.jagex.io.Buffer;

public final class  VariableParameter {

    private static VariableParameter parameters[];

    public static void unpackConfig(Archive archive)
    {
        Buffer buffer = new Buffer(archive.getEntry("varp.dat"));
        int cacheSize = buffer.readUShort();
        System.out.println(cacheSize);
        if(parameters == null)
            parameters = new VariableParameter[cacheSize];
        for(int j = 0; j < cacheSize; j++)
        {
            if(parameters[j] == null)
                parameters[j] = new VariableParameter();
            parameters[j].readValues(buffer, j);
        }
        if(buffer.position != buffer.payload.length)
            System.out.println("varptype load mismatch");
    }

    public static int get(int index) {
    	return parameters[index].parameter;
    }
   
	public static void destroy() {
    	parameters = null;
    }
	
    public int parameter;
    
    private void readValues(Buffer buffer, int i) {
		do {
			int j = buffer.readUByte();
			if (j == 0)
				return;
			if (j == 1 || j == 2)
				buffer.readUByte();
			else if (j == 5)
				parameter = buffer.readUShort();
			else if (j == 7 || j == 12)
				buffer.readInt();
			else if (j == 10)
				buffer.readString();
			else if (j != 3 || j != 4 || j != 6 || j != 8 || j != 11 || j != 13)
				System.out.println("Error unrecognised config code: " + j);
		} while (true);
	}
}
