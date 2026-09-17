import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class Servidor {

    public static void main(String[] args) throws IOException {

        int porta = Integer.parseInt(
            System.getenv().getOrDefault("PORT", "8080")
        );

        HttpServer servidor = HttpServer.create(
            new InetSocketAddress("0.0.0.0", porta), 0
        );

        servidor.createContext("/", troca -> {

            String html = """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <title>Bora Jogar</title>
                </head>

                <body>
                    <h1>Bora jogar CS2</h1>
                    <p>FPS: 120</p>
                    <p>Headshot!</p>
                </body>
                </html>
                """;

            byte[] dados = html.getBytes(StandardCharsets.UTF_8);

            troca.getResponseHeaders().set(
                "Content-Type", "text/html; charset=UTF-8"
            );

            troca.sendResponseHeaders(200, dados.length);

            OutputStream resposta = troca.getResponseBody();
            resposta.write(dados);
            resposta.close();
        });

        servidor.start();

        System.out.println("Servidor ligado!");
        System.out.println("Porta: " + porta);
    }
}