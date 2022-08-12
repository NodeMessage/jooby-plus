package com.nodemessage.jooby.web;


import java.io.IOException;
import java.io.InputStream;

/**
 * @author wjsmc
 * @date 2022/8/11 15:31
 * @description
 **/
public class Banner {
    public void setting() {
        InputStream banner = Thread.currentThread().getContextClassLoader().getResourceAsStream("banner");
        try {
            int iAvail = banner.available();
            byte[] bytes = new byte[iAvail];
            banner.read(bytes);
            System.out.println();
            System.out.println(new String(bytes));
            System.out.print("  ===   " + "\033[0;0;31m" + "节点科讯(NodeMessage)" + "\033[0m");
            System.out.print("                 ");
            System.out.println("http://nodemessage.openpool.cn/");
            System.out.println("  ===   " + "\033[0;0;32m" + "Author Wjsmc" + "\033[0m");
            System.out.println("  ===   " + "\033[0;0;33m" + "v1.1.0" + "\033[0m");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
