package com.zucchettigroup.httpsessionfilter;

import java.io.File;
import java.net.URISyntaxException;

public class Utils 
{
    public static File getRootFolder() 
    {
        try 
        {
            File root;
            String runningJarPath = 
            		Utils.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath().replaceAll("\\\\", "/");
            int lastIndexOf = runningJarPath.lastIndexOf("/target/");
            if (lastIndexOf < 0) 
            {
                root = new File("");
            }
            else 
            {
                root = new File(runningJarPath.substring(0, lastIndexOf));
            }
            System.out.println("application resolved root folder: " + root.getAbsolutePath());
            return root;
        }
        catch (URISyntaxException ex) 
        {
            throw new RuntimeException(ex);
        }
    }
}
