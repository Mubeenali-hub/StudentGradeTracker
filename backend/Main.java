package backend;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Main {
    static class Student {
        String id;
        String name;
        double quiz;
        double mid;
        double finalExam;

        Student(String id, String name, double quiz, double mid, double finalExam) {
            this.id = id;
            this.name = name;
            this.quiz = quiz;
            this.mid = mid;
            this.finalExam = finalExam;
        }

        double getTotalPercentage() {
            return (quiz * 0.20) + (mid * 0.30) + (finalExam * 0.50);
        }

        String getGrade() {
            double total = getTotalPercentage();
            if (total >= 85) return "A";
            if (total >= 70) return "B";
            if (total >= 50) return "C";
            return "F";
        }

        double getGPA() {
            double total = getTotalPercentage();
            if (total >= 85) return 4.0;
            if (total >= 70) return 3.0;
            if (total >= 50) return 2.0;
            return 0.0;
        }

        String getStatus() {
            return getTotalPercentage() >= 50 ? "PASSED" : "FAILED";
        }

        String toJson() {
            return String.format(
                "{\"id\":\"%s\",\"name\":\"%s\",\"quiz\":%.2f,\"mid\":%.2f,\"finalExam\":%.2f,\"total\":%.2f,\"grade\":\"%s\",\"gpa\":%.2f,\"status\":\"%s\"}",
                id, name, quiz, mid, finalExam, getTotalPercentage(), getGrade(), getGPA(), getStatus()
            );
        }
    }

    private static final List<Student> students = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        students.add(new Student("CS-101", "Mubeen Ali", 88.0, 78.5, 91.0));
        students.add(new Student("CS-102", "Sarah Khan", 65.0, 70.0, 68.0));

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        
        // Static Frontend Handler
        server.createContext("/", new StaticHandler());
        
        // API Handler
        server.createContext("/api/students", new StudentsHandler());

        server.setExecutor(null);
        System.out.println("Fullstack Server running on port 8080...");
        server.start();
    }

    static class StaticHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            File file = new File("frontend/index.html");
            if (!file.exists()) {
                exchange.sendResponseHeaders(404, -1);
                return;
            }
            byte[] bytes = Files.readAllBytes(file.toPath());
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, bytes.length);
            OutputStream os = exchange.getResponseBody();
            os.write(bytes);
            os.close();
        }
    }

    static class StudentsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                StringBuilder json = new StringBuilder("[");
                for (int i = 0; i < students.size(); i++) {
                    json.append(students.get(i).toJson());
                    if (i < students.size() - 1) json.append(",");
                }
                json.append("]");

                byte[] responseBytes = json.toString().getBytes();
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, responseBytes.length);
                OutputStream os = exchange.getResponseBody();
                os.write(responseBytes);
                os.close();
            } else if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
                StringBuilder body = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) body.append(line);

                String payload = body.toString().replaceAll("[{}\"]", "");
                String[] fields = payload.split(",");
                String id = "", name = "";
                double quiz = 0, mid = 0, finalExam = 0;

                for (String field : fields) {
                    String[] kv = field.split(":");
                    if (kv.length == 2) {
                        String key = kv[0].trim();
                        String val = kv[1].trim();
                        if (key.equals("id")) id = val;
                        else if (key.equals("name")) name = val;
                        else if (key.equals("quiz")) quiz = Double.parseDouble(val);
                        else if (key.equals("mid")) mid = Double.parseDouble(val);
                        else if (key.equals("finalExam")) finalExam = Double.parseDouble(val);
                    }
                }

                students.add(new Student(id, name, quiz, mid, finalExam));
                String res = "{\"message\":\"Student added successfully\"}";
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(201, res.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(res.getBytes());
                os.close();
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        }
    }
}