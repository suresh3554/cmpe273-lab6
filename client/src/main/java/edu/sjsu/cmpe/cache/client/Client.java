package edu.sjsu.cmpe.cache.client;

import java.util.ArrayList;

import com.google.common.hash.Hashing;

import java.util.List;

public class Client {

    public static void main(String[] args) throws Exception {
        System.out.println("Starting Cache Client...");
        
        List<CacheServiceInterface> servers = new ArrayList<CacheServiceInterface>();
        servers.add(new DistributedCacheService("http://localhost:3000"));
        servers.add(new DistributedCacheService("http://localhost:3001"));
        servers.add(new DistributedCacheService("http://localhost:3002"));
        
        List<String> values = new ArrayList<String>();
        values.add("a");
        values.add("b");
        values.add("c");
        values.add("d");
        values.add("e");
        values.add("f");
        values.add("g");
        values.add("h");
        values.add("i");
        values.add("j");
        
        // Sharding on to servers
        System.out.println("Sharding on to servers");
        for (int i = 1; i <= 10; i++)
        {
            int bucket = Hashing.consistentHash(Hashing.md5().hashString(Integer.toString(i)), servers.size());
            
            CacheServiceInterface cache = servers.get(bucket);
            
            cache.put(i, values.get(i-1));
            System.out.println("key => value -->  " + i +"=>"+ values.get(i-1) + "  is sharded over http://localhost:300"+bucket);
        }
        
        // Fetching from servers
        System.out.println("Fetching from servers");
        for (int i = 1; i <= 10; i++)
        {
            int bucket = Hashing.consistentHash(Hashing.md5().hashString(Integer.toString(i)), servers.size());
            
            CacheServiceInterface cache = servers.get(bucket);
            
            String value = cache.get(i);
            System.out.println("key => value -->  " + i +"=>"+ value + "  is fetched from http://localhost:300"+bucket);
        }
    }
}
