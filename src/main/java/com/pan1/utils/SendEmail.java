package com.pan1.utils;

import java.io.*;
import java.net.Socket;
import java.util.Base64;


public class SendEmail {

    public static void send(String receiver, String mail, String title)
    {
        String sender="zhangyifan_fly@163.com";
        String password="13910019511";
    	
    	try {
            
            Socket socket=new Socket("smtp.163.com", 25);
            InputStream inputStream=socket.getInputStream();
            InputStreamReader isr=new InputStreamReader(inputStream);
            BufferedReader br=new BufferedReader(isr);

            String in;

            OutputStream outputStream=socket.getOutputStream();
            PrintWriter pw=new PrintWriter(outputStream, true);

            in=br.readLine();
            //System.out.println(in);
            
            //helo
            pw.println("helo exicg18");
            in=br.readLine();
            //System.out.println(in);
            

            //auth login
            pw.println("auth login");
            in=br.readLine();
            //System.out.println(in);
           
            //pw.println(UserBase64);
            pw.println(coding.log(sender));
            in=br.readLine();
            //System.out.println(in);
            
            //pw.println(PasswordBase64);
            pw.println(coding.log(password));
            in=br.readLine();
            //System.out.println(in);
           

            //Set "mail from" and  "rect to"
            pw.println("mail from:<"+sender+">");//senduser
            in=br.readLine();
            //System.out.println(in);
           
            pw.println("rcpt to:<"+receiver+">");//recieveuser
            in=br.readLine();
            //System.out.println(in);
           

            //Set "data"
            pw.println("data");
            in=br.readLine();
            //System.out.println(in);
            

            //邮件正文
            pw.println("subject:"+title);
            pw.println("from:"+sender);//senduser
            pw.println("to:"+receiver);//recieveuser
            pw.println("Content-Type: text/plain;charset=\"gb2312\"");
            pw.println();
            pw.println(mail);
            pw.println(".");
            pw.print("");
            in=br.readLine();
            //System.out.println(in);
            
            
            pw.println("rset");
            in=br.readLine();
            //System.out.println(in);
            
            pw.println("quit");
            in=br.readLine();
            //System.out.println(in);
           
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}


class coding {

    static public String Basee64encode(byte[] bin){
        int i = bin.length%3;
        int g = bin.length - i;
        StringBuffer s = new StringBuffer();
        String fix = new String();
        if( i==1 ){
            int b = (0x3f & (bin[g]<<4));
            fix += map64(0x3F & bin[g]>>2)+""+map64(b)+"==";
        }else if( i==2 ){
            int b = (0x03 & bin[g])<<4 | (0xf0 & bin[g+1])>>4;
            int c = (0x0f & bin[g+1])<<2; // !!! 鏈�鍚庡洓bits绉诲埌鏈�楂樹綅
            fix += map64(0x3f & bin[g]>>2) +""+ map64(b) +""+ map64(c)+'=';
        }
        for (i=0; i<g; i+=3 ) {
            int a = (0xff & bin[i]  )>>2; // bigger first
            int b = (0x03 & bin[i]  )<<4 | (0xF0 & bin[i+1])>>4;
            int c = (0x0f & bin[i+1])<<2 | (0xC0 & bin[i+2])>>6;
            int d = (0x3f & bin[i+2]);
            s.append(map64(a));
            s.append(map64(b));
            s.append(map64(c));
            s.append(map64(d));
        }
        return s.toString()+fix;
    }

    static public byte[] Basee64decode(String msg){
        byte[] bytes = msg.getBytes();
        byte[] res = new byte[bytes.length*3/4];
        for(int i=0; i<bytes.length; i+=4){
            byte a = unmap64(bytes[i]);
            byte b = unmap64(bytes[i+1]);
            byte c = unmap64(bytes[i+2]);
            byte d = unmap64(bytes[i+3]);
            res[i*3/4+0] = (byte)(a<<2 | b>>4);
            res[i*3/4+1] = (byte)(b<<4 | c>>2);
            res[i*3/4+2] = (byte)(c<<6 | d);
        }
        int l = bytes.length;
        int pad = bytes[l-2]=='='? 2:bytes[l-1]=='='? 1:0;
        if( pad>0 ){
            byte[] ret = new byte[res.length-pad];
            System.arraycopy(res, 0, ret, 0, res.length-pad);
            return ret;
        }
        return res;
    }
    static public char map64(int i){
        // +:43 /:47 0:48 =:61 A:65 a:97
        if( i>63 ) return '=';
        byte code = (byte)(i==62?'+':i==63?'/':i<26?'A'+i:i<52?'a'+i-26:'0'+i-52);
        return (char)code;
    }
    static public byte unmap64(byte i){
        // +:43 /:47 0:48 =:61 A:65 a:97
        if( i=='=' ) return 0;
        byte index = (byte)(i=='+'?62:i=='/'?63:i<'A'?i-'0'+52:i<'a'?i-'A':i-'a'+26);
        return (byte)index;
    }

    static public String log(String t){
        String b = Base64.getEncoder().encodeToString(t.getBytes());
        return b;
    }

}

