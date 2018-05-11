/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MusicMall.core;

/**
 *
 * @author Diana
 */
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import MusicMall.net.ftp;
import MusicMall.tools.date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;


public class log implements Runnable {
     private static List<String> logList = new ArrayList<String>();
    private static String logPath;
    private static String ClientID;

    public log(String logPath, String ClientID) {
        log.logPath = logPath;
        log.ClientID = ClientID;
        new File(logPath).mkdirs();
    }

    public static void writeLog(String log) {
        System.out.println("Log: " + date.LogTime() + "  " + log);
        logList.add(String.valueOf(date.LogTime()) + "  " + log);
    }


    public static void synchronizeLog() {
        try {
            if(logPath != null && (new File(logPath)).exists() && ClientID != null) {
                File f = new File(logPath + "//Log_" + date.ddmmyyyy() + " .txt");
                String str;
                if(f.exists()) {
                     {
                        Throwable var37 = null;
                        str = null;

                        try {
                            FileWriter fw = new FileWriter(f, true);

                            try {
                                BufferedWriter bw = new BufferedWriter(fw);

                                try {
                                    PrintWriter out = new PrintWriter(bw);

                                    try {
                                        for(int i = 0; i < logList.size(); ++i) {
                                            if(logList.get(i) != null) {
                                                out.println((String)logList.get(i));
                                            }
                                        }
                                    } finally {
                                        if(out != null) {
                                            out.close();
                                        }

                                    }
                                } catch (Throwable var32) {
                                    if(var37 == null) {
                                        var37 = var32;
                                    } else if(var37 != var32) {
                                        var37.addSuppressed(var32);
                                    }

                                    if(bw != null) {
                                        bw.close();
                                    }

                                    throw var37;
                                }

                                if(bw != null) {
                                    bw.close();
                                }
                            } catch (Throwable var33) {
                                if(var37 == null) {
                                    var37 = var33;
                                } else if(var37 != var33) {
                                    var37.addSuppressed(var33);
                                }

                                if(fw != null) {
                                    fw.close();
                                }

                                throw var37;
                            }

                            if(fw != null) {
                                fw.close();
                            }
                        } catch (Throwable var34) {
                            if(var37 == null) {
                                var37 = var34;
                            } else if(var37 != var34) {
                                var37.addSuppressed(var34);
                            }

                            //throw var37;
                        }
                    //} catch (IOException var35) {
                        System.out.println("Log write exception");
                    }
                } else {
                    FileWriter w = new FileWriter(f);
                    Iterator var3 = log.logList.iterator();

                    while(var3.hasNext()) {
                        str = (String)var3.next();
                        w.write(str);
                        w.write("\r\n");
                    }

                    w.close();
                }

                log.logList.clear();
            } else {
                System.out.println("Log hasnt been initialized yet");
            }
        } catch (IOException var36) {
            System.out.println("Error saving log");
            var36.printStackTrace();
        }

    }

    public static void synchronizeLogwithServer() {
        System.out.println("Synchronyzing log");
        File f = new File(log.logPath + "//Log_" + date.ddmmyyyy() + " .txt");
        if (!f.exists()) {
            System.out.println("No log to synchronize");
        } else if (ftp.uploadFile(f, "/log/" + ClientID)) {
            System.out.println("Log Synchronized");
        } else {
            System.out.println("Error synchronyzing log");
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                synchronizeLog();
                synchronizeLogwithServer();
                Thread.sleep(1500000); //выгрузка каждые 25 мин
            } catch (InterruptedException e) {
                log.writeLog("Unknown exception:  " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
   
}