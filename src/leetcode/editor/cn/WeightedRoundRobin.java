package leetcode.editor.cn;

import java.util.*;

public class WeightedRoundRobin {

    private List<Server> servers;
    private int currentIndex = 0;
    private int currentWeight = 0;

    public WeightedRoundRobin(List<Server> servers) {
        this.servers = servers;
    }

    public Server getNextServer() {
        if (servers.isEmpty()) {
            return null; // or throw an exception
        }
        //使用 currentIndex 从服务器列表中获取当前服务器。currentIndex 是一个类成员变量，它跟踪上次选择的服务器索引。
        Server currentServer = servers.get(currentIndex);
        int weight = currentServer.getWeight();
        //将当前服务器的权重加到 currentWeight 上。currentWeight 是一个类成员变量，用于跟踪迄今为止累积的权重
        currentWeight += weight;
        //循环直到找到合适的服务器,
        // 如果 currentWeight 大于或等于服务器列表的大小（即服务器的总数），
        while (currentWeight >= servers.size()) {
            // 则从 currentWeight 中减去服务器列表的大小，
            currentWeight -= servers.size();
            // 并将 currentIndex 递增（如果到达列表末尾，则回绕到开始处）。
            currentIndex = (currentIndex + 1) % servers.size();
        }

        return currentServer;
    }

    public static void main(String[] args) {
        List<Server> servers = Arrays.asList(
                new Server("Server1", 1),
                new Server("Server2", 2),
                new Server("Server3", 3)
        );

        WeightedRoundRobin wrr = new WeightedRoundRobin(servers);
        for (int i = 0; i < 18; i++) {
            System.out.println(wrr.getNextServer().getName());
        }
    }
}

class Server {
    private String name;
    private int weight;

    public Server(String name, int weight) {
        this.name = name;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public int getWeight() {
        return weight;
    }
}