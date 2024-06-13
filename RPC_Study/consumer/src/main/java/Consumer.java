import com.lz.HelloService;
import com.lz.proxy.ProxyFactory;

public class Consumer {
    public static void main(String[] args) {
        HelloService helloService = ProxyFactory.getProxy(HelloService.class);
        String result = helloService.saveHell("lz");
        System.out.println(result);
    }
}
