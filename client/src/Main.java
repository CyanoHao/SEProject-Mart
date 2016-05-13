import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class Main {

    static CloseableHttpClient http = HttpClients.createDefault();

    static public void main(String []args) {
        Login login = new Login();
    }
}
