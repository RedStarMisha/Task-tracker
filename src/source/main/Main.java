
public class Main {

    public static void main(String[] args) {
        try {
            new KVServer().start();
            HttpTaskServer httpTaskServer = new HttpTaskServer("http://localhost:8080");
            httpTaskServer.createServer();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
