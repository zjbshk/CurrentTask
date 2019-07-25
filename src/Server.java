import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Server {

    static List<String> ipList = new ArrayList<>();
    static List<IpItem> ipItemList = new ArrayList<>();


    public static void main(String[] args) {
        parse(args);
        deal();
        sout();
    }

    private static void parse(String[] args) {
        ipList.add(args[0]);
    }

    private static void deal() {
        if (ipList.size() == 0)return;
        Runtime runtime = Runtime.getRuntime();
        ExecutorService executorService = Executors.newFixedThreadPool(50);
        for (String ip : ipList) {
            final String tmpIp = ip;
            executorService.execute(() -> {
                try {
                    Process process = runtime.exec(String.format("ping %s", tmpIp));
                    String information = getInformation(process);
                    parseMsg(information);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        executorService.shutdown();
    }

    static String pattern = "[\\w\\s]+\\n{2}([0-9.]+).*\\s+.*已发送 = (\\d+)，已接收 = (\\d+)[\\w\\s]+";
    static Pattern compile = Pattern.compile(pattern);


    private static void parseMsg(String information) {
        System.out.println(information);
        if (information != null) {
            Matcher matcher = compile.matcher(information);
            if (matcher.matches()) {
                for (int i = 0; i < matcher.groupCount(); i++) {
                    System.out.println(matcher.group(i));
                }
            }else{
                System.out.println("<<<     无法解析        >>>");
            }
        }
    }

    private static String getInformation(Process process) throws IOException {
        InputStream inputStream = process.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "gbk"));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }
        br.close();
        inputStream.close();
        return sb.toString();
    }

    private static void sout() {
        if (ipItemList.size() != 0) {
            for (IpItem ipItem : ipItemList) {
                System.out.println(ipItem);
            }
        } else {
            System.out.println("<<<     无结果     >>>");
        }
    }
}
