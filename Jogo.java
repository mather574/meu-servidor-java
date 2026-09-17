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
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Aim Trainer CS2</title>

                    <style>
                        body {
                            margin: 0;
                            background: #111;
                            color: white;
                            font-family: Arial, sans-serif;
                            text-align: center;
                        }

                        h1 {
                            margin-top: 25px;
                        }

                        #placar {
                            font-size: 22px;
                            margin: 15px;
                        }

                        button {
                            padding: 12px 25px;
                            font-size: 18px;
                            border: none;
                            border-radius: 8px;
                            cursor: pointer;
                        }

                        #comecar {
                            background: #00aaff;
                            color: white;
                        }

                        #arena {
                            position: relative;
                            width: 90%;
                            max-width: 800px;
                            height: 450px;
                            margin: 20px auto;
                            background: #222;
                            border: 2px solid #555;
                            overflow: hidden;
                        }

                        #alvo {
                            position: absolute;
                            width: 55px;
                            height: 55px;
                            border-radius: 50%;
                            background: red;
                            border: 4px solid white;
                            display: none;
                            cursor: crosshair;
                        }

                        #mensagem {
                            margin-top: 150px;
                            font-size: 25px;
                        }
                    </style>
                </head>

                <body>

                    <h1>🎯 AIM TRAINER CS2</h1>

                    <div id="placar">
                        Pontos: <span id="pontos">0</span>
                        |
                        Acertos: <span id="acertos">0</span>
                        |
                        Tempo: <span id="tempo">30</span>
                    </div>

                    <button id="comecar">COMEÇAR</button>

                    <div id="arena">

                        <div id="mensagem">
                            Clique em COMEÇAR para jogar!
                        </div>

                        <button id="alvo"></button>

                    </div>

                    <script>

                        let pontos = 0;
                        let acertos = 0;
                        let tempo = 30;
                        let jogando = false;
                        let intervalo;

                        const pontosTexto =
                            document.getElementById("pontos");

                        const acertosTexto =
                            document.getElementById("acertos");

                        const tempoTexto =
                            document.getElementById("tempo");

                        const alvo =
                            document.getElementById("alvo");

                        const arena =
                            document.getElementById("arena");

                        const mensagem =
                            document.getElementById("mensagem");

                        const comecar =
                            document.getElementById("comecar");


                        function atualizar() {

                            pontosTexto.textContent = pontos;
                            acertosTexto.textContent = acertos;
                            tempoTexto.textContent = tempo;

                        }


                        function criarAlvo() {

                            let x =
                                Math.random() *
                                (arena.clientWidth - 70);

                            let y =
                                Math.random() *
                                (arena.clientHeight - 70);

                            alvo.style.left = x + "px";
                            alvo.style.top = y + "px";

                            alvo.style.display = "block";

                        }


                        function terminar() {

                            jogando = false;

                            clearInterval(intervalo);

                            alvo.style.display = "none";

                            mensagem.style.display = "block";

                            mensagem.textContent =
                                "FIM! Você fez " +
                                pontos +
                                " pontos!";

                            comecar.textContent =
                                "JOGAR NOVAMENTE";

                        }


                        comecar.addEventListener(
                            "click",
                            function() {

                                pontos = 0;
                                acertos = 0;
                                tempo = 30;

                                jogando = true;

                                mensagem.style.display =
                                    "none";

                                comecar.textContent =
                                    "JOGANDO...";

                                atualizar();

                                criarAlvo();

                                clearInterval(intervalo);

                                intervalo =
                                    setInterval(
                                        function() {

                                            tempo--;

                                            atualizar();

                                            if (tempo <= 0) {

                                                terminar();

                                            }

                                        },
                                        1000
                                    );

                            }
                        );


                        alvo.addEventListener(
                            "click",
                            function(evento) {

                                evento.stopPropagation();

                                if (!jogando) {
                                    return;
                                }

                                acertos++;

                                pontos += 10;

                                atualizar();

                                criarAlvo();

                            }
                        );


                        arena.addEventListener(
                            "click",
                            function() {

                                if (jogando) {

                                    pontos =
                                        Math.max(
                                            0,
                                            pontos - 2
                                        );

                                    atualizar();

                                }

                            }
                        );

                    </script>

                </body>
                </html>
                """;

            byte[] dados =
                html.getBytes(StandardCharsets.UTF_8);

            troca.getResponseHeaders().set(
                "Content-Type",
                "text/html; charset=UTF-8"
            );

            troca.sendResponseHeaders(
                200,
                dados.length
            );

            OutputStream resposta =
                troca.getResponseBody();

            resposta.write(dados);

            resposta.close();

        });

        servidor.start();

        System.out.println(
            "Servidor ligado!"
        );

        System.out.println(
            "Porta: " + porta
        );
    }
}
