/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backupgui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author SARJIT
 */
public class Operations {
    
    private HashMap<String,String> folders;
    private File f;
    
    ObjectInputStream readFolders;
    ObjectOutputStream writeFolders;
    
    public Operations()
    {
        folders = new HashMap<>();
        
        
        
        f = new File("D:\\YourFolderbackup.sarjit");
        
        if(!f.exists())
        {
            try
            {
                f.createNewFile();
                System.out.println("File Created");
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            try 
            {
                if(f.length() > 0){
                    readFolders = new ObjectInputStream(new FileInputStream(f));
                    folders = (HashMap<String,String>) readFolders.readObject();
                }
            }
            catch (FileNotFoundException ex) 
            {
                
            }
            catch (IOException | ClassNotFoundException ex) {
                
            }
        }
    }
    
    public void addFolder(String src,String dest)
    {
        if(folders.containsKey(src))
        {
            folders.remove(src);
        }
        
        folders.put(src, dest);
    }
    
    
    
    public String[] getAllSrc()
    {
        Set<String> allSrc = folders.keySet();
        
        String allSrcArr[] = new String[allSrc.size()];
        int i = 0;
        for (Iterator<String> iterator = allSrc.iterator(); iterator.hasNext();) {
                allSrcArr[i++] = iterator.next();
                
        }
        
        return allSrcArr;
    }
    
    public String getDest(String src)
    {
        return folders.get(src);
    }
    
    public void removeSrc(String src)
    {
        folders.remove(src);
    }
    
    public String checkSource(String src)
    {
        File check = new File(src);
        if(!check.exists())
        {
            return "Selected Path doesnot exist.";
        }
        else if(!check.isDirectory())
        {
            return "Selected Source should be a Directory.";
        }
        else
        {
            return "True";
        }
    }
    
    public boolean isEmpty()
    {
        return folders.isEmpty();
    }
    
    public void saveChanges()
    {
        try 
        {
            writeFolders = new ObjectOutputStream( new FileOutputStream(f));
            writeFolders.writeObject(folders);
            writeFolders.flush();
            writeFolders.close();
        }
        catch (IOException ex) {

        }
    }
    
    public void changeDest(String key,String dest)
    {
        removeSrc(key);
        addFolder(key, dest);
    }
    
    public void changeSrc(String old,String newSrc)
    {
        String dest = folders.get(old);
        removeSrc(old);
        addFolder(newSrc, dest);
    }
}
