import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class NhanhCan {
    private Map<String, List<String>> graph;
    private Map<String, String> parent;
    private Map<String, Integer> h;
    private Map<String, String> k;
    private Map<String, Integer> g;
    private Map<String, Integer> f;
    private String start;
    private String end;

    public void nhap(String fname) throws IOException {
        File file = new File(fname);
        Scanner scanner = new Scanner(file);
        graph = new HashMap<>();
        parent = new HashMap<>();
        h = new HashMap<>();
        k = new HashMap<>();
        g = new HashMap<>();
        f = new HashMap<>();

        String line = scanner.nextLine().trim();
        String[] parts = line.split(" ");
        start = parts[0].trim();
        end = parts[1].trim();

        line = scanner.nextLine().trim();
        String[] weights = line.split(",");
        for (String weight : weights) {
            String[] i = weight.split("-");
            h.put(i[0].trim(), Integer.parseInt(i[1].trim()));
        }

        while (scanner.hasNextLine()) {
            line = scanner.nextLine().trim();
            parts = line.split(":");
            if (parts.length == 2) {
                String node = parts[0].trim();
                graph.put(node, new ArrayList<>());
                parent.put(node, node);
                String[] neighbors = parts[1].trim().split(" ");
                for (String neighbor : neighbors) {
                    String[] uv = neighbor.split("-");
                    k.put(node + "-" + uv[0].trim(), uv[1].trim());
                    graph.get(node).add(uv[0].trim());
                }
            }
        }
        scanner.close();
    }

    private String toString(List<String> L) {
        List<String> formattedList = new ArrayList<>();
        for (String item : L) {
            String[] parts = item.split("-");
            formattedList.add(parts[0] + parts[1]);
        }
        return String.join(", ", formattedList);
    }

    public void NhanhvaCan(String outputFilename) throws IOException {
        List<String> L = new ArrayList<>();
        L.add(start + "-0");
        g.put(start, 0);
        int cost = 99999;
        int dem = 0;

        FileWriter writer = new FileWriter(outputFilename);
        writer.write(String.format("%-7s| %-13s| %-10s| %-10s| %-10s| %-10s| %-20s| %-20s\n", "TTPT", "TTK", "k(u,v)", "h(v)", "g(v)", "f(v)", "DS L1", "DS L"));

        while (true) {
            List<String> L1 = new ArrayList<>();
            String[] uParts = L.get(0).split("-");
            String u = uParts[0];
            L.remove(0);

            if (L.isEmpty() && dem != 0) {
                writer.write("Không tìm thấy đường đi\n");
                break;
            }

            if (u.equals(end)) {
                if (g.get(u) <= cost) {
                    cost = g.get(u);
                }
                writer.write(String.format("%-7s| %-13s| %-10s| %-10s| %-10s| %-10s| %-20s| %-20s\n", u, "TTKT, tìm được đường đi tạm thời, độ dài " + cost, "", "", "", "", toString(L1), toString(L)));
                inDuongDi(writer);
            } else {
                boolean kt = true;
                if (u.equals(start)) {
                    kt = false;
                } else {
                    for (String t : L) {
                        String[] tParts = t.split("-");
                        if (Integer.parseInt(tParts[1]) <= cost) {
                            kt = false;
                        }
                    }
                }

                if (kt) {
                    break;
                }

                writer.write(String.format("%-7s| ", u));

                List<String> neighbors = graph.get(u);
                for (int index = 0; index < neighbors.size(); index++) {
                    String v = neighbors.get(index);
                    parent.put(v, u);
                    int kuv = Integer.parseInt(k.get(u + "-" + v));
                    g.put(v, g.get(u) + kuv);
                    f.put(v, g.get(v) + h.get(v));
                    L1.add(v + "-" + f.get(v));
                    Collections.sort(L1, (s1, s2) -> Integer.compare(Integer.parseInt(s1.split("-")[1]), Integer.parseInt(s2.split("-")[1])));

                    writer.write(String.format("%-13s| %-10s| %-10s| %-10s| %-10s| %-20s| %-20s\n", v, kuv, h.get(v), g.get(v), f.get(v), toString(L1), toString(L)));
                }

                Collections.sort(L1, (s1, s2) -> Integer.compare(Integer.parseInt(s1.split("-")[1]), Integer.parseInt(s2.split("-")[1])));
                L.addAll(0, L1);
            }
            dem++;
        }

        writer.close();
    }
    private void inDuongDi(FileWriter writer) throws IOException {
        List<String> duongDi = new ArrayList<>();
        String current = end;
        while (!current.equals(start)) {
            duongDi.add(current);
            current = parent.get(current);
        }
        duongDi.add(start);
        Collections.reverse(duongDi);
        writer.write("Đường đi: " + String.join(" -> ", duongDi) + "\n");
    }
    public static void main(String[] args) {
        NhanhCan nhanhCan = new NhanhCan();
        try {
            nhanhCan.nhap("C:\\Users\\PHUONG\\Videos\\input.txt");
            nhanhCan.NhanhvaCan("C:\\Users\\PHUONG\\Videos\\output2.txt");

        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

}
