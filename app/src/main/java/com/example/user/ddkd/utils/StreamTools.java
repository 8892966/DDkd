package com.example.user.ddkd.utils;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamTools {
	private StreamTools(){
		
	}
	public static String readFromStream (InputStream inputstream) throws IOException{
		ByteArrayOutputStream stream =new ByteArrayOutputStream();
		byte[] buffer =new byte[1024];
		int i=0;
		while((i=inputstream.read(buffer))!=-1){
			stream.write(buffer, 0, i);
		}
		inputstream.close();
		String result =stream.toString("gbk");
		stream.close();
		return result;
	}
}
